package hu.bme.aut.android.mattermostremindus.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.mattermostremindus.databinding.DialogDeleteAllBinding


class DeleteAllDialogFragment() : DialogFragment() {
    interface DeleteAllDialogListener {
        fun onDeleteAll()
    }

    private lateinit var listener: DeleteAllDialogListener

    private lateinit var binding: DialogDeleteAllBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? DeleteAllDialogListener
            ?: throw RuntimeException("Activity must implement the DeleteAllDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogDeleteAllBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setView(binding.root)
            .setPositiveButton("Ok") { dialogInterface, i ->
                listener.onDeleteAll()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        const val TAG = "DeleteAllDialogFragment"
    }
}