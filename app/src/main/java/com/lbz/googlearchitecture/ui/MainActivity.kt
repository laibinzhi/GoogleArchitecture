package com.lbz.googlearchitecture.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.idescout.sql.SqlScoutServer
import com.lbz.googlearchitecture.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SqlScoutServer.create(this, packageName);
    }

}