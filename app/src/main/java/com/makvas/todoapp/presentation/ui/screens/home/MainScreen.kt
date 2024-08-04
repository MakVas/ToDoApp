package com.makvas.todoapp.presentation.ui.screens.home

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.makvas.todoapp.R
import com.makvas.todoapp.data.Task
import com.makvas.todoapp.presentation.ui.screens.add_task.components.StatusItem
import com.makvas.todoapp.presentation.ui.screens.home.components.SwipeToDeleteContainer
import com.makvas.todoapp.presentation.ui.screens.home.components.TaskItem
import com.makvas.todoapp.presentation.util.OrderType
import com.makvas.todoapp.presentation.util.StatsType
import com.makvas.todoapp.presentation.util.StatusType
import com.makvas.todoapp.presentation.util.UiEvent

@Composable
fun MainScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val tasks = viewModel.tasks.collectAsState(initial = emptyList())
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {

                is UiEvent.Navigate -> onNavigate(event)

                is UiEvent.ShowSnackbar -> {
                    val result = snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionText,
                        duration = SnackbarDuration.Short
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(MainScreenEvent.OnUndoDeleteClick)
                    }
                }

                else -> Unit
            }
        }
    }

    MainScreenScaffold(
        tasks = tasks.value,
        snackBarHostState = snackBarHostState,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenScaffold(
    tasks: List<Task>,
    snackBarHostState: SnackbarHostState,
    viewModel: MainScreenViewModel
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
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
                    viewModel.onEvent(MainScreenEvent.OnAddTaskClick)
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

        MainScreenBody(
            innerPadding = innerPadding,
            tasks = tasks,
            viewModel = viewModel
        )
    }
}

@Composable
private fun MainScreenBody(
    innerPadding: PaddingValues,
    tasks: List<Task>,
    viewModel: MainScreenViewModel,
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
            SortByStatus(viewModel = viewModel)
        }

        item {
            OrderByRow(viewModel = viewModel)
        }

        items(
            items = tasks,
            key = { task -> task.id }
        ) { task ->
            Box {
                SwipeToDeleteContainer(
                    item = task,
                    onDelete = { viewModel.onEvent(MainScreenEvent.OnDeleteClick(task)) },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    TaskItem(
                        onEvent = viewModel::onEvent,
                        task = task,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.large)
                            .clickable {
                                viewModel.onEvent(MainScreenEvent.OnTaskClick(task))
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun SortByStatus(viewModel: MainScreenViewModel) {

    val statusType = viewModel.statusType.collectAsState(initial = StatusType.All)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        StatusType.entries.forEach { status ->
            StatusItem(
                selected = status == statusType.value,
                onClick = { viewModel.onEvent(MainScreenEvent.OnStatusClick(status)) },
                text = status.name
            )
        }
    }
}

@Composable
private fun OrderByRow(
    viewModel: MainScreenViewModel
) {
    val orderType = viewModel.orderType.collectAsState(initial = OrderType.TITLE)
    val isDropMenuVisible by viewModel.isDropMenuVisible.collectAsState(initial = false)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Order by:"
        )

        Column {
            TextButton(
                onClick = {
                    viewModel.onEvent(MainScreenEvent.ShowDropMenu)
                },
            ) {
                Text(
                    text = orderType.value.name,
                    fontWeight = FontWeight.Bold,
                )
            }

            DropdownMenu(
                expanded = isDropMenuVisible,
                onDismissRequest = { viewModel.onEvent(MainScreenEvent.HideDropMenu) }
            ) {
                OrderType.entries.forEach { sortType ->
                    DropdownMenuItem(
                        text = {
                            Text(text = sortType.name)
                        },
                        onClick = {
                            viewModel.onEvent(MainScreenEvent.OnOrderByClick(sortType))
                            viewModel.onEvent(MainScreenEvent.HideDropMenu)
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
            .padding(horizontal = 16.dp)
            .height(125.dp)
            .clip(MaterialTheme.shapes.extraLarge)
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
                            .height(60.dp),
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