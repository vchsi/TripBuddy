/*
Vincent Hsiao - CIS 135 Final Project
EventDB.kt

Event Database which holds the Trips and Events tables. Currently version 5,
will remove data if updated. Allows for select statements to be ran on the main thread,
for simplicity.


 */

package com.hsiao.tripbuddy.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class, TripC::class], version = 5, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class  EventDB: RoomDatabase() {
    abstract fun EventDAO(): EventDAO
    companion object {
        @Volatile
        private var Instance: EventDB? = null

        fun getDatabase(context: Context): EventDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, EventDB::class.java, "item_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }



}