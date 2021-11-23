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

        holder.binding.tvSubject.text = todoItem.subject
        holder.binding.tvSendTo.text = todoItem.sendTo
        holder.binding.tvPeriod.text = todoItem.periodInMs.toString()
        holder.binding.tvPeriodUnit.text = "miliseconds"
        holder.binding.tsTodoIsOn.isChecked = todoItem.isOn
        holder.binding.tsTodoIsOn.setOnCheckedChangeListener { _, state ->
            todoItem.isOn = state
            if (todoItem.isOn) {
                todoItem.nextSendInMs = System.currentTimeMillis() + todoItem.periodInMs
            } else {
                todoItem.nextSendInMs = 0
            }
            listener.onItemChanged(todoItem)
        }
        holder.binding.ibRemove.setOnClickListener { listener.onItemDeleted(todoItem) }
        holder.binding.ibEdit.setOnClickListener { listener.onItemEdited(todoItem) }
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

    fun editItem(todoItem: TodoItem) {
        val id = items.indexOf(todoItem.id?.let { getItemById(it) })
        items.removeAt(id)
        items.add(id, todoItem)
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

    fun getItemById(itemid: Long): TodoItem? {
        return items.find { item -> item.id == itemid }
    }

    inner class TodoViewHolder(val binding: ItemTodoListBinding) :
        RecyclerView.ViewHolder(binding.root)
}
