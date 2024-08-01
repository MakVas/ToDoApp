package com.makvas.todoapp.domain.repository

import com.makvas.todoapp.data.local.entities.Task
import com.makvas.todoapp.presentation.util.SortType

sealed interface TaskEvent {
    data object SaveTask: TaskEvent
    data class SetTitle(val title: String): TaskEvent
    data class SetDate(val date: String): TaskEvent
    data class SetTime(val time: String): TaskEvent
    data class SetDescription(val description: String): TaskEvent
    data class SetIsImportant(val isImportant: Boolean): TaskEvent
    data class UpdateCompleted(val task: Task, val isCompleted: Boolean): TaskEvent
    data object ShowDropMenu: TaskEvent
    data object HideDropMenu: TaskEvent
    data object ShowDialog: TaskEvent
    data object HideDialog: TaskEvent
    data class SortTasks(val sortType: SortType): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent
}