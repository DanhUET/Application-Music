package pro.branium.musicapp.ui

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar
import pro.branium.musicapp.R
import androidx.core.view.isVisible

object SnackbarManager {

    fun showMessage(activity: Activity, message: String) {
        val miniPlayer = activity.findViewById<View>(R.id.fcv_mini_player)
        val bottomNav = activity.findViewById<View>(R.id.nav_view)

        val anchor = if (miniPlayer.isVisible) miniPlayer else bottomNav

        Snackbar.make(
            activity.findViewById(R.id.container), // ConstraintLayout root
            message,
            Snackbar.LENGTH_SHORT
        ).setAnchorView(anchor).show()
    }
}