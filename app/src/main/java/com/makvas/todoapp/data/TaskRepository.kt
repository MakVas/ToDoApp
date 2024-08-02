package com.makvas.todoapp.data

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun getTaskById(id: Int): Task?
    fun getTasksOrderedByTitle(): Flow<List<Task>>
    fun getTasksOrderedByDate(): Flow<List<Task>>
    fun getImportantTasks(): Flow<List<Task>>
    fun getCompletedTasks(): Flow<List<Task>>
    fun getUncompletedTasks(): Flow<List<Task>>
    fun getAllTasks(): Flow<List<Task>>
}