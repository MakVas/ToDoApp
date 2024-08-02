package com.makvas.todoapp.di

import android.app.Application
import androidx.room.Room
import com.makvas.todoapp.data.TaskDatabase
import com.makvas.todoapp.data.TaskRepository
import com.makvas.todoapp.data.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "tasks.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(database: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(database.taskDao)
    }

}