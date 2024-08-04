package com.makvas.todoapp.presentation.ui.screens.home

import com.makvas.todoapp.data.Task
import com.makvas.todoapp.presentation.util.OrderType
import com.makvas.todoapp.presentation.util.StatusType

sealed class MainScreenEvent {
    data class OnDeleteClick(val task: Task) : MainScreenEvent()
    data object OnUndoDeleteClick : MainScreenEvent()
    data class OnCompletedClick(val task: Task, val isCompleted: Boolean) : MainScreenEvent()
    data class OnOrderByClick(val orderType: OrderType) : MainScreenEvent()
    data class OnTaskClick(val task: Task) : MainScreenEvent()
    data object OnAddTaskClick : MainScreenEvent()
    data object ShowDropMenu : MainScreenEvent()
    data object HideDropMenu : MainScreenEvent()
    data class OnStatusClick(val statusType: StatusType) : MainScreenEvent()
}