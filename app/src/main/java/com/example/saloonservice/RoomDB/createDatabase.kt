package com.example.saloonservice.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartList::class],version = 1)
abstract class CreateDatabase: RoomDatabase() {

    abstract  fun roomDao() : RoomDao?

    companion object{
        @Volatile
        private var INSTANCE : CreateDatabase? = null

        fun getAppData(context: Context): CreateDatabase?{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder<CreateDatabase>(
                    context.applicationContext, CreateDatabase::class.java,"SALON"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}