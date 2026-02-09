package pro.branium.core_ui.layout

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import pro.branium.core_ui.R

open class MaxHeightConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var maxHeightPx: Int = 0

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MaxHeightConstraintLayout,
            0,
            0
        ).apply {
            try {
                maxHeightPx = getDimensionPixelSize(
                    R.styleable.MaxHeightConstraintLayout_maxHeight,
                    0
                )
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightSpec = heightMeasureSpec
        if (maxHeightPx > 0) {
            newHeightSpec = MeasureSpec.makeMeasureSpec(maxHeightPx, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }
}
