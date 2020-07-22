package com.lbz.googlearchitecture.ui.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.data.project.ProjectRepository
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:36
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ProjectViewModelFactory @Inject constructor(private val repository: ProjectRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}