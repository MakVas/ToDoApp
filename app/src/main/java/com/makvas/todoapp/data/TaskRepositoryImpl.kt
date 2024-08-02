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

    override fun getTasksOrderedByTitle(): Flow<List<Task>> {
        return dao.getTasksOrderedByTitle()
    }

    override fun getTasksOrderedByDate(): Flow<List<Task>> {
        return dao.getTasksOrderedByDate()
    }

    override fun getImportantTasks(): Flow<List<Task>> {
        return dao.getImportantTasks()
    }

    override fun getCompletedTasks(): Flow<List<Task>> {
        return dao.getCompletedTasks()
    }

    override fun getUncompletedTasks(): Flow<List<Task>> {
        return dao.getUncompletedTasks()
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks()
    }

}