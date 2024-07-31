package com.makvas.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val date: String,
    val time: String,
    val isCompleted: Boolean,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
