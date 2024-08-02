package com.makvas.todoapp.presentation.util

sealed class UiEvent {
    data object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(
        val message: String,
        val actionText: String? = null,
    ) : UiEvent()
}