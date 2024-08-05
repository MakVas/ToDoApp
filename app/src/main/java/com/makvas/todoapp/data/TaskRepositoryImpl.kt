package com.makvas.todoapp.data

import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override suspend fun getTaskById(id: Int): Task? {
        return dao.getTaskById(id)
    }

    override fun getCompletedTasks(): Flow<List<Task>> {
        return dao.getCompletedTasks()
    }

    override fun getCurrentTasks(currentTime: Long): Flow<List<Task>> {
        return dao.getUncompletedTasks(currentTime)
    }

    override fun getOverdueTasks(currentTime: Long): Flow<List<Task>> {
        return dao.getOverdueTasks(currentTime)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks()
    }
}