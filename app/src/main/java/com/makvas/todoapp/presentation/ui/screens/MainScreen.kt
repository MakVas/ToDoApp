package com.makvas.todoapp.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makvas.todoapp.presentation.util.SortType
import com.makvas.todoapp.domain.repository.TaskEvent
import com.makvas.todoapp.domain.model.TaskState
import com.makvas.todoapp.presentation.ui.components.AddTaskDialog
import com.makvas.todoapp.presentation.ui.components.SwipeToDeleteContainer
import com.makvas.todoapp.presentation.ui.components.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ToDoApp",
                        fontWeight = FontWeight.Bold,
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = colorScheme.primaryContainer,
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
                .fillMaxSize(),
        ) {

            item {
                SortByRow(state = state, onEvent = onEvent)
            }

            items(
                items = state.tasks,
                key = { task -> task.id }
            ) { task ->
                Box {
                    SwipeToDeleteContainer(
                        item = task,
                        onDelete = { onEvent(TaskEvent.DeleteTask(task)) },
                    ) {
                        TaskItem(
                            onEvent = onEvent,
                            task = task
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 50.dp)
                            .align(Alignment.BottomCenter),
                        thickness = 0.25.dp,
                        color = colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun SortByRow(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Sort by:"
        )

        Column {
            Text(
                text = state.sortType.name,
                color = colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        onEvent(TaskEvent.ShowDropMenu)
                    }
            )

            DropdownMenu(
                expanded = state.isDropMenuVisible,
                onDismissRequest = { onEvent(TaskEvent.HideDropMenu) }
            ) {
                SortType.entries.forEach { sortType ->
                    DropdownMenuItem(
                        text = {
                            Text(text = sortType.name)
                        },
                        onClick = {
                            onEvent(TaskEvent.SortTasks(sortType))
                            onEvent(TaskEvent.HideDropMenu)
                        }
                    )
                }
            }
        }
    }
}
