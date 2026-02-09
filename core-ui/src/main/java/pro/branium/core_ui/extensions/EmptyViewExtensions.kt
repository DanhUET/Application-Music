package pro.branium.core_ui.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import pro.branium.core_ui.databinding.LayoutEmptyListBinding

fun ViewGroup.setupEmptyView(
    iconRes: Int,
    message: String
): LayoutEmptyListBinding {
    val binding = LayoutEmptyListBinding
        .inflate(LayoutInflater.from(context), this, true)
    binding.iconRes = iconRes
    binding.message = message
    return binding
}
