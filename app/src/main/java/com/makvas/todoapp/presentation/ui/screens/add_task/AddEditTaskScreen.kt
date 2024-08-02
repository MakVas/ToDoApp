package com.makvas.todoapp.presentation.ui.screens.add_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.makvas.todoapp.presentation.ui.screens.add_task.components.CustomDatePicker
import com.makvas.todoapp.presentation.ui.screens.add_task.components.CustomTimePicker
import com.makvas.todoapp.presentation.util.UiEvent

@Composable
fun AddEditTaskScreen(
    popBackStack: () -> Unit,
    viewModel: AddEditTaskScreenViewModel = hiltViewModel()
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {

                UiEvent.PopBackStack -> popBackStack()

                is UiEvent.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionText
                    )
                }

                else -> Unit
            }
        }
    }

    AddEditTaskScreenScaffold(
        viewModel = viewModel,
        snackBarHostState = snackBarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditTaskScreenScaffold(
    snackBarHostState: SnackbarHostState,
    viewModel: AddEditTaskScreenViewModel
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(AddEditTaskScreenEvent.OnBackPressed)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(AddEditTaskScreenEvent.OnSaveEditTaskClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
                        )
                    }
                },
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add Task",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        AddEditTaskScreenBody(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
private fun AddEditTaskScreenBody(
    viewModel: AddEditTaskScreenViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.title,
            onValueChange = { viewModel.onEvent(AddEditTaskScreenEvent.OnTitleChange(it)) },
            placeholder = {
                Text("Title")
            },
            singleLine = true
        )


        Text(
            text = "isImportant: ${viewModel.isImportant}"
        )

        Text(
            text = "Selected Date: ${viewModel.date}"
        )

        Text(
            text = "Selected Time: ${viewModel.time}"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            Row {
                Checkbox(
                    checked = viewModel.isImportant,
                    onCheckedChange = {
                        viewModel.onEvent(
                            AddEditTaskScreenEvent.OnIsImportantChange(
                                it
                            )
                        )
                    },
                )

                CustomDatePicker(
                    onDateSelected = { viewModel.onEvent(AddEditTaskScreenEvent.OnDateChange(it)) }
                )

                CustomTimePicker(
                    onValueChange = { viewModel.onEvent(AddEditTaskScreenEvent.OnTimeChange(it)) }
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.description,
            onValueChange = { viewModel.onEvent(AddEditTaskScreenEvent.OnDescriptionChange(it)) },
            placeholder = {
                Text("Description")
            },
            singleLine = false,
            maxLines = 5
        )
    }
}