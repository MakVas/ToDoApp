package com.makvas.todoapp.domain.model

import com.makvas.todoapp.data.local.entities.Task
import com.makvas.todoapp.presentation.util.SortType

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val isCompleted: Boolean = false,
    val description: String = "",
    val isImportant: Boolean = false,
    val isAddingTask: Boolean = false,
    val isDropMenuVisible: Boolean = false,
    val sortType: SortType = SortType.TITLE
)