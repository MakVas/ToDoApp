package com.makvas.todoapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ToDoApp") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(TaskEvent.ShowDialog)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task"
                    )
                }
            )
        },
    ) { innerPadding ->

        if (state.isAddingTask) {
            AddTaskDialog(state = state, onEvent = onEvent)
        }

        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.entries.forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(TaskEvent.SortTasks(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(TaskEvent.SortTasks(sortType))
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            items(state.tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = {
                            onEvent(TaskEvent.UpdateCompleted(task, !task.isCompleted))
                        }
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = task.title,
                            fontSize = 20.sp
                        )

                        Text(
                            text = task.date + "  " + task.time,
                            fontSize = 14.sp
                        )
                    }
                    IconButton(
                        onClick = {
                            onEvent(TaskEvent.DeleteTask(task))
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete task"
                            )
                        },
                    )
                }
            }
        }
    }
}