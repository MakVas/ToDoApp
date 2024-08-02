package com.makvas.todoapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makvas.todoapp.R
import com.makvas.todoapp.presentation.util.SortType
import com.makvas.todoapp.domain.repository.TaskEvent
import com.makvas.todoapp.domain.model.TaskState
import com.makvas.todoapp.presentation.ui.components.AddTaskDialog
import com.makvas.todoapp.presentation.ui.components.SwipeToDeleteContainer
import com.makvas.todoapp.presentation.ui.components.TaskItem
import com.makvas.todoapp.presentation.util.StatsType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.checklist),
                            contentDescription = "logo"
                        )

                        Text(
                            text = "ToDoApp",
                            fontWeight = FontWeight.Bold,
                        )
                    }
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

        MainScreen(
            innerPadding = innerPadding,
            state = state,
            onEvent = onEvent
        )
    }
}

@Composable
private fun MainScreen(
    innerPadding: PaddingValues,
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
) {
    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier
            .fillMaxSize(),
    ) {

        item {
            ToDoStats()
        }

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

@Composable
private fun SortByRow(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit
) {
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
            TextButton(
                onClick = {
                    onEvent(TaskEvent.ShowDropMenu)
                },
            ) {
                Text(
                    text = state.sortType.name,
                    color = colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                )
            }

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

@Composable
private fun ToDoStats() {
    Surface(
        color = colorScheme.tertiaryContainer,
        contentColor = colorScheme.onTertiaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatsType.entries.forEachIndexed { index, statType ->

                StatsItem(statType.name)

                if (index != StatsType.entries.size - 1) {
                    VerticalDivider(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(horizontal = 8.dp),
                        color = colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun StatsItem(sortType: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = sortType,
            style = typography.titleMedium
        )
        Text(
            text = "5",
            style = typography.displaySmall
        )
    }
}