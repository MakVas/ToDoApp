package com.makvas.todoapp.presentation.ui.screens.add_task

sealed class AddEditTaskScreenEvent {
    data object OnSaveEditTaskClick : AddEditTaskScreenEvent()
    data class OnTitleChange(val title: String) : AddEditTaskScreenEvent()
    data class OnDateChange(val date: String) : AddEditTaskScreenEvent()
    data class OnTimeChange(val time: String) : AddEditTaskScreenEvent()
    data class OnDescriptionChange(val description: String) : AddEditTaskScreenEvent()
    data class OnIsImportantChange(val isImportant: Boolean) : AddEditTaskScreenEvent()
    data object OnBackPressed : AddEditTaskScreenEvent()
}