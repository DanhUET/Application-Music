package pro.branium.core_ui.binding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("srcCompat")
fun setSrcCompat(view: ImageView, @DrawableRes resId: Int?) {
    resId?.let {
        view.setImageResource(it)
    }
}
