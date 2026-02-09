package pro.branium.core_ui.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(messageId: Int) {
    Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()
}

fun View.showSnackBar(messageId: Int, anchorView: View? = null) {
    val snackbar = Snackbar.make(this, messageId, Snackbar.LENGTH_SHORT)
    snackbar.anchorView = anchorView
    snackbar.show()
}

fun View.showSnackBar(message: String, anchorView: View? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackbar.anchorView = anchorView
    snackbar.show()
}
