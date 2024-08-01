package com.makvas.todoapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.makvas.todoapp.presentation.ui.screens.MainScreen
import com.makvas.todoapp.presentation.ui.screens.MainScreenViewModel
import com.makvas.todoapp.data.local.db.TaskDatabase
import com.makvas.todoapp.presentation.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "tasks.db"
        ).build()
    }

    private val viewModel by viewModels<MainScreenViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainScreenViewModel(db.taskDao()) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val state = viewModel.state.collectAsState()
                MainScreen(
                    state = state.value,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}