package hu.bme.aut.android.mattermostremindus.data

import androidx.room.*

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todoitem")
    fun getAll(): List<TodoItem>

    @Insert
    fun insert(shoppingItems: TodoItem): Long

    @Update
    fun update(shoppingItem: TodoItem)

    @Delete
    fun deleteItem(shoppingItem: TodoItem)

    @Query("DELETE FROM TodoItem")
    fun deleteAll()
}
