package hu.bme.aut.android.mattermostremindus.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.mattermostremindus.data.TodoItem
import hu.bme.aut.android.mattermostremindus.databinding.DialogNewTodoItemBinding


class NewTodoItemDialogFragment(private val olditem: TodoItem?) : DialogFragment() {
    interface NewTodoItemDialogListener {
        fun onTodoItemCreated(newItem: TodoItem)
        fun onTodoItemEdited(newItem: TodoItem)
    }

    private lateinit var listener: NewTodoItemDialogListener

    private lateinit var binding: DialogNewTodoItemBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewTodoItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewTodoItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewTodoItemBinding.inflate(LayoutInflater.from(context))
        if (olditem != null) {
            loadShoppingItem(olditem)
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Create new Todo Item")
            .setView(binding.root)
            .setPositiveButton("Create") { dialogInterface, i ->
                if (isValid()) {
                    if (olditem != null) {
                        val item = getShoppingItem()
                        item.id = olditem.id
                        listener.onTodoItemEdited(item)
                    } else {
                        listener.onTodoItemCreated(getShoppingItem())
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getShoppingItem() = TodoItem(
        name = binding.etName.text.toString()
    )

    private fun loadShoppingItem(item: TodoItem) {
        binding.etName.setText(item.name)
    }


    companion object {
        const val TAG = "NewTodoItemDialogFragment"
    }
}