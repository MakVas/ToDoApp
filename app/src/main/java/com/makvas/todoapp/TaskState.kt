package com.makvas.todoapp

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val isCompleted: Boolean = false,
    val description: String = "",
    val isImportant: Boolean = false,
    val isAddingTask: Boolean = false,
    val sortType: SortType = SortType.TITLE
)