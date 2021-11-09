package hu.bme.aut.android.mattermostremindus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.mattermostremindus.data.TodoItem
import hu.bme.aut.android.mattermostremindus.databinding.ItemTodoListBinding

class TodoAdapter(private val listener: TodoItemClickListener) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val items = mutableListOf<TodoItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TodoViewHolder(
        ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = items[position]

        holder.binding.tvName.text = todoItem.name
        holder.binding.ibRemove.setOnClickListener { deleteItem(todoItem) }
    }

    override fun getItemCount(): Int = items.size

    interface TodoItemClickListener {
        fun onItemChanged(item: TodoItem)
        fun onItemDeleted(item: TodoItem)
        fun onItemEdited(item: TodoItem)
    }

    fun addItem(item: TodoItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<TodoItem>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }

    fun editItem(shoppingItem: TodoItem) {
        val id = items.indexOf(items.find { item -> item.id == shoppingItem.id })
        items[id] = shoppingItem
        notifyItemChanged(id)
    }

    fun deleteItem(item: TodoItem) {
        val id = items.indexOf(item)
        items.removeAt(id)
        notifyItemRemoved(id)
    }

    fun deleteAll() {
        items.clear()
        notifyDataSetChanged()
    }


    inner class TodoViewHolder(val binding: ItemTodoListBinding) :
        RecyclerView.ViewHolder(binding.root)
}
