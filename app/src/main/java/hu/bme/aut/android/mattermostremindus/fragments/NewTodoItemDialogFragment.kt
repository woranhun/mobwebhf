package hu.bme.aut.android.mattermostremindus.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
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
        binding.spMultiplier.adapter =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    TodoItem.IntervalMultiplier.values()
                )
            }
        if (olditem != null) {
            loadTodoItem(olditem)
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Todo Item Editor")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                if (isValid()) {
                    val item = getTodoItem()
                    if (item.isOn) {
                        item.nextSendInMs = System.currentTimeMillis() + item.periodInMs
                    } else {
                        item.nextSendInMs = 0
                    }
                    if (olditem != null) {
                        item.id = olditem.id
                        listener.onTodoItemEdited(item)
                    } else {
                        listener.onTodoItemCreated(getTodoItem())
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun isValid() = binding.etSubject.text.isNotEmpty()

    private fun getTodoItem() = TodoItem(
        subject = binding.etSubject.text.toString(),
        message = binding.etMessage.text.toString(),
        sendTo = binding.etSendTo.text.toString(),
        periodInMs = binding.etPeriod.text.toString().toLong() * TodoItem.IntervalMultiplier.toLong(
            binding.spMultiplier.selectedItem as TodoItem.IntervalMultiplier
        ),
        nextSendInMs = 0,
        previousSendInMs = -1000,
        intervalMultiplier = binding.spMultiplier.selectedItem as TodoItem.IntervalMultiplier,
        isOn = binding.tsTodoIsOn.isChecked
    )

    private fun loadTodoItem(item: TodoItem) {
        binding.etSubject.setText(item.subject)
        binding.etMessage.setText(item.message)
        binding.etSendTo.setText(item.sendTo)
        binding.etPeriod.setText(
            (item.periodInMs / TodoItem.IntervalMultiplier.toLong(item.intervalMultiplier)).toString()
        )
        binding.tsTodoIsOn.isChecked = item.isOn
        binding.spMultiplier.post {
            binding.spMultiplier.setSelection(
                TodoItem.IntervalMultiplier.values().indexOf(item.intervalMultiplier)
            )
        }
    }


    companion object {
        const val TAG = "NewTodoItemDialogFragment"
    }
}