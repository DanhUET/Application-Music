package pro.branium.core_ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pro.branium.core_resources.R

class ConfirmationDialogFragment(
    private val messageId: Int? = null,
    private val message: String = "",
    private val listener: OnDeleteConfirmListener
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val title = getString(R.string.title_confirmation)
        builder.setTitle(title)
        if (messageId == null) {
            builder.setMessage(message)
        } else {
            builder.setMessage(messageId)
        }
        builder.setIcon(R.drawable.ic_warning)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            listener.onConfirm(true)
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
            listener.onConfirm(false)
        }
        return builder.create()
    }

    interface OnDeleteConfirmListener {
        fun onConfirm(isConfirmed: Boolean)
    }

    companion object {
        const val TAG = "ConfirmationDialogFragment"
    }
}