package com.makvas.todoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id: Int): Task?

    @Query("SELECT * FROM task WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 0 AND date >= :currentTime")
    fun getUncompletedTasks(currentTime: Long): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 0 AND date < :currentTime")
    fun getOverdueTasks(currentTime: Long): Flow<List<Task>>

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>
}