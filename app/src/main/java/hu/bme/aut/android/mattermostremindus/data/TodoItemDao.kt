package hu.bme.aut.android.mattermostremindus.data

import androidx.room.*

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todoitem")
    fun getAll(): List<TodoItem>

    @Query("SELECT * FROM todoitem WHERE id = :itemID")
    fun getFromID(itemID: Long): TodoItem?

    @Insert
    fun insert(todoItem: TodoItem): Long

    @Update
    fun update(todoItem: TodoItem)

    @Delete
    fun deleteItem(todoItem: TodoItem)

    @Query("DELETE FROM TodoItem")
    fun deleteAll()

    @Query("SELECT * FROM todoitem WHERE isOn == 1 ORDER BY nextSendInMs  LIMIT 1")
    fun getNextSend(): TodoItem?
}
