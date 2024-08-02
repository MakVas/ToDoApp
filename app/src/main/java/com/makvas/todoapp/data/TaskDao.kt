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

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun getTasksOrderedByTitle(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY date ASC")
    fun getTasksOrderedByDate(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE isImportant = 1")
    fun getImportantTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 0")
    fun getUncompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>
}