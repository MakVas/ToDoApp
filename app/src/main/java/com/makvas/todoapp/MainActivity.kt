package com.makvas.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.makvas.todoapp.presentation.ui.screens.home.MainScreen
import com.makvas.todoapp.presentation.ui.screens.add_task.AddEditTaskScreen
import com.makvas.todoapp.presentation.ui.theme.AppTheme
import com.makvas.todoapp.presentation.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Routes.MAIN_SCREEN
                ) {

                    composable(Routes.MAIN_SCREEN) {
                        MainScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            }
                        )
                    }

                    composable(
                        route = Routes.ADD_EDIT_TASK_SCREEN + "?taskId={taskId}",
                        arguments = listOf(
                            navArgument(name = "taskId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditTaskScreen(
                            popBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}