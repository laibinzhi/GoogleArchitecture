package com.lbz.googlearchitecture.ui.project

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lbz.googlearchitecture.data.project.ProjectRepository
import com.lbz.googlearchitecture.model.ProjectData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

/**
 * @author: laibinzhi
 * @date: 2020-07-15 16:52
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
class ProjectViewModel @ViewModelInject constructor(private val repository: ProjectRepository) :
    ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getProjectTitles() {
        viewModelScope.launch {
            repository.getProjectTitles()
        }
    }

    val status = repository.status

    val projectTitles = repository.projectTitles


    fun getProjectData(cid: Int): Flow<PagingData<ProjectData>> =
        repository.getProjectDataStream(cid)
            .cachedIn(viewModelScope)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}