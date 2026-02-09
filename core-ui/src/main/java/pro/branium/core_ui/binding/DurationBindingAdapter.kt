package pro.branium.core_ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.Locale

@BindingAdapter("formattedDuration")
fun setFormattedDuration(textView: TextView, duration: Long) {
    textView.text = formatDuration(duration)
}

private fun formatDuration(duration: Long): String {
    val minutes = duration / 60000
    val seconds = (duration / 1000) % 60
    return if (duration < 0 || duration > Int.MAX_VALUE) "00:00"
    else String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
}