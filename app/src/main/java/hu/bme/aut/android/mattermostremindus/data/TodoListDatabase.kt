package hu.bme.aut.android.mattermostremindus.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoItem::class], version = 3)
abstract class TodoListDatabase : RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao

    companion object {
        fun getDatabase(applicationContext: Context): TodoListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                TodoListDatabase::class.java,
                "todo-list"
            ).fallbackToDestructiveMigration().build()
        }
    }
}
