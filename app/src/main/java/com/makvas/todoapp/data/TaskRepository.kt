package com.makvas.todoapp.data

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun getTaskById(id: Int): Task?
    fun getCompletedTasks(): Flow<List<Task>>
    fun getCurrentTasks(currentTime: Long): Flow<List<Task>>
    fun getOverdueTasks(currentTime: Long): Flow<List<Task>>
    fun getAllTasks(): Flow<List<Task>>
}