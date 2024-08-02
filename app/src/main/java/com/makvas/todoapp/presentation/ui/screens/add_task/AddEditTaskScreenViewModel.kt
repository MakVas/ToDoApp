package com.makvas.todoapp.presentation.ui.screens.add_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makvas.todoapp.data.Task
import com.makvas.todoapp.data.TaskRepository
import com.makvas.todoapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskScreenViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var task by mutableStateOf<Task?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var date by mutableStateOf("")
        private set

    var time by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var isImportant by mutableStateOf(false)
        private set

    private val _uiEvent = Channel<UiEvent>()
    var uiEvent = _uiEvent.receiveAsFlow()

    init {
        val taskId = savedStateHandle.get<Int>("taskId")!!
        if (taskId != -1) {
            viewModelScope.launch {
                repository.getTaskById(taskId)?.let { task ->
                    title = task.title
                    date = task.date
                    time = task.time ?: ""
                    description = task.description ?: ""
                    isImportant = task.isImportant
                    this@AddEditTaskScreenViewModel.task = task
                }
            }
        }
    }

    fun onEvent(event: AddEditTaskScreenEvent) {
        when (event) {

            AddEditTaskScreenEvent.OnBackPressed -> {
                sendUiEvent(UiEvent.PopBackStack)
            }

            AddEditTaskScreenEvent.OnSaveEditTaskClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar("Title cannot be empty")
                        )
                        return@launch
                    } else if (date.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar("Date cannot be empty")
                        )
                        return@launch
                    } else {
                        repository.insertTask(
                            Task(
                                title = title,
                                date = date,
                                time = time,
                                description = description,
                                isImportant = isImportant,
                                isCompleted = task?.isCompleted ?: false
                            )
                        )
                        sendUiEvent(UiEvent.PopBackStack)
                    }
                }
            }

            is AddEditTaskScreenEvent.OnDateChange -> {
                date = event.date
            }

            is AddEditTaskScreenEvent.OnDescriptionChange -> {
                description = event.description
            }

            is AddEditTaskScreenEvent.OnIsImportantChange -> {
                isImportant = event.isImportant
            }

            is AddEditTaskScreenEvent.OnTimeChange -> {
                time = event.time
            }

            is AddEditTaskScreenEvent.OnTitleChange -> {
                title = event.title
            }

        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}