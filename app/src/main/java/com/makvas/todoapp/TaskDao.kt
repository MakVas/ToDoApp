package com.makvas.todoapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun getTasksOrderedByTitle(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY date ASC")
    fun getTasksOrderedByDate(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY time ASC")
    fun getTasksOrderedByTime(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 0")
    fun getUncompletedTasks(): Flow<List<Task>>
}