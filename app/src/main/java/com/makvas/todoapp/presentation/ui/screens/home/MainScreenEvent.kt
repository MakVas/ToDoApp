package com.makvas.todoapp.presentation.ui.screens.home

import com.makvas.todoapp.data.Task
import com.makvas.todoapp.presentation.util.SortType

sealed class MainScreenEvent {
    data class OnDeleteClick(val task: Task) : MainScreenEvent()
    data object OnUndoDeleteClick : MainScreenEvent()
    data class OnCompletedClick(val task: Task, val isCompleted: Boolean) : MainScreenEvent()
    data class OnSortTasksClick(val sortType: SortType) : MainScreenEvent()
    data class OnTaskClick(val task: Task) : MainScreenEvent()
    data object OnAddTaskClick : MainScreenEvent()
    data object ShowDropMenu : MainScreenEvent()
    data object HideDropMenu : MainScreenEvent()
}