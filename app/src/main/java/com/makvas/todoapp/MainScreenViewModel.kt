package com.makvas.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModel(
    private val dao: TaskDao
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.TITLE)
    private val _tasks = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.TITLE -> dao.getTasksOrderedByTitle()
                SortType.DATE -> dao.getTasksOrderedByDate()
                SortType.TIME -> dao.getTasksOrderedByTime()
                SortType.COMPLETED -> dao.getCompletedTasks()
                SortType.UNCOMPLETED -> dao.getUncompletedTasks()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TaskState())
    val state = combine(_state, _sortType, _tasks) { state, sortType, tasks ->
        state.copy(
            tasks = tasks,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    dao.deleteTask(event.task)
                }
            }

            TaskEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingTask = false
                    )
                }
            }

            TaskEvent.SaveTask -> {
                val title = _state.value.title
                val date = _state.value.date
                val time = _state.value.time
                val isCompleted = false
                val description = _state.value.description

                if (title.isBlank() || date.isBlank() || time.isBlank() || description.isBlank()) {
                    return
                }

                val task = Task(
                    title = title,
                    date = date,
                    time = time,
                    isCompleted = isCompleted,
                    description = description
                )

                viewModelScope.launch {
                    dao.upsertTask(task)
                }

                _state.update {
                    it.copy(
                        isAddingTask = false,
                        title = "",
                        date = "",
                        time = "",
                        isCompleted = false,
                        description = ""
                    )
                }
            }

            is TaskEvent.SetDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is TaskEvent.SetDescription -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            is TaskEvent.SetTime -> {
                _state.update {
                    it.copy(
                        time = event.time
                    )
                }
            }

            is TaskEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            TaskEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingTask = true
                    )
                }
            }

            is TaskEvent.UpdateCompleted -> {
                val updatedTask = event.task.copy(isCompleted = event.isCompleted)
                viewModelScope.launch {
                    dao.upsertTask(updatedTask)
                }
            }

            is TaskEvent.SortTasks -> {
                _sortType.value = event.sortType
            }
        }
    }
}