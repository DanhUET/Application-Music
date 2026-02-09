package pro.branium.core_ui.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import pro.branium.core_resources.R

/**
 * Load an image from a URL into an ImageView using Glide.
 */
@BindingAdapter(
    value = ["imageUrl", "isCircleCrop"],
    requireAll = false
)
fun ImageView.loadImage(
    url: String?,
    isCircleCrop: Boolean = false
) {
    Glide.with(context)
        .load(if (url.isNullOrEmpty()) R.drawable.ic_music_note else url)
        .error(R.drawable.ic_music_note)
        .error(R.drawable.ic_music_note)
        .let { if (isCircleCrop) it.circleCrop() else it }
        .into(this)
}

/**
 * Set the play/pause icon based on the isPlaying parameter.
 */
@BindingAdapter("isPlaying")
fun ImageView.setPlayPauseIcon(isPlaying: Boolean) {
    val resId = if (isPlaying) {
        R.drawable.ic_pause
    } else {
        R.drawable.ic_play
    }
    setImageResource(resId)
}
