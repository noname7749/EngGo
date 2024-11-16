package com.example.enggo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.enggo.model.course.Course

@Database(entities = [Course::class], version = 1, exportSchema = false)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseSearchDao() : CourseSearchDao

    companion object {
        @Volatile
        private var Instance: CourseDatabase? = null

        fun getDatabase(context: Context): CourseDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CourseDatabase::class.java, "sqlite")
                    .createFromAsset("database/sqlite.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}