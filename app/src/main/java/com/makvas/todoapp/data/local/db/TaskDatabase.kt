package com.makvas.todoapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makvas.todoapp.data.local.entities.Task
import com.makvas.todoapp.data.local.dao.TaskDao

@Database(
    entities = [Task::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}