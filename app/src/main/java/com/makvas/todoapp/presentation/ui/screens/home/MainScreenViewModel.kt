package com.makvas.todoapp.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makvas.todoapp.data.Task
import com.makvas.todoapp.presentation.util.OrderType
import com.makvas.todoapp.data.TaskRepository
import com.makvas.todoapp.presentation.util.Routes
import com.makvas.todoapp.presentation.util.StatusType
import com.makvas.todoapp.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _statusType = MutableStateFlow(StatusType.All)
    val statusType: StateFlow<StatusType> = _statusType

    private val _orderType = MutableStateFlow(OrderType.TITLE)
    val orderType: StateFlow<OrderType> = _orderType

    private val _tasks = _orderType
        .flatMapLatest { sortType ->
            when (sortType) {
                OrderType.TITLE -> repository.getTasksOrderedByTitle()
                OrderType.DATE -> repository.getTasksOrderedByDate()
                OrderType.IMPORTANCE -> repository.getImportantTasks()
            }
        }
    val tasks = _tasks

    private val _isDropMenuVisible = MutableStateFlow(false)
    val isDropMenuVisible: StateFlow<Boolean> = _isDropMenuVisible

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTask: Task? = null

    fun onEvent(event: MainScreenEvent) {
        when (event) {

            is MainScreenEvent.ShowDropMenu -> {
                _isDropMenuVisible.value = true
            }
            is MainScreenEvent.HideDropMenu -> {
                _isDropMenuVisible.value = false
            }
            is MainScreenEvent.OnOrderByClick -> {
                _orderType.value = event.orderType
            }

            is MainScreenEvent.OnStatusClick -> {
                _statusType.value = event.statusType
            }

            is MainScreenEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    deletedTask = event.task
                    repository.deleteTask(event.task)
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "Task deleted",
                            actionText = "Undo"
                        )
                    )
                }
            }

            MainScreenEvent.OnUndoDeleteClick -> {
                deletedTask?.let { task ->
                    viewModelScope.launch {
                        repository.insertTask(task)
                    }
                }
            }

            MainScreenEvent.OnAddTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK_SCREEN))
            }

            is MainScreenEvent.OnCompletedClick -> {
                viewModelScope.launch {
                    repository.insertTask(
                        event.task.copy(
                            isCompleted = event.isCompleted
                        )
                    )
                }
            }

            is MainScreenEvent.OnTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK_SCREEN + "?taskId=${event.task.id}"))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}