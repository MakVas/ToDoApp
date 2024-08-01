package com.makvas.todoapp.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makvas.todoapp.presentation.util.SortType
import com.makvas.todoapp.data.local.entities.Task
import com.makvas.todoapp.data.local.dao.TaskDao
import com.makvas.todoapp.domain.repository.TaskEvent
import com.makvas.todoapp.domain.model.TaskState
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
                SortType.IMPORTANCE -> dao.getImportantTasks()
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
                val description = _state.value.description
                val isImportant = _state.value.isImportant
                val isCompleted = false

                if (title.isBlank() || date.isBlank() || time.isBlank() || description.isBlank()) {
                    return
                }

                val task = Task(
                    title = title,
                    date = date,
                    time = time,
                    isCompleted = isCompleted,
                    description = description,
                    isImportant = isImportant
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
                        description = "",
                        isImportant = false
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

            is TaskEvent.SetIsImportant -> {
                _state.update {
                    it.copy(
                        isImportant = event.isImportant
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