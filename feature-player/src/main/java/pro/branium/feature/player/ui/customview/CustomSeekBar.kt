package pro.branium.feature.player.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withTranslation
import pro.branium.player.R
import kotlin.math.max
import kotlin.math.min

class CustomSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var max = 100
        set(value) {
            field = maxOf(1, value)
            progress = progress.coerceIn(0, field)
            invalidate()
        }

    var progress = 0
        get() = field
        set(value) {
            val valid = min(max, max(0, value))
            if (field != valid) {
                field = valid
                invalidate()
                onSeekBarChangeListener?.onProgressChanged(this, field, false)
            }
        }

    var progressColor: Int = "#6200EE".toColorInt()
        set(value) {
            field = value
            paintProgress.color = field
            invalidate()
        }

    var trackColor: Int = "#AAAAAA".toColorInt()
        set(value) {
            field = value
            paintTrack.color = field
            invalidate()
        }

    var thumbColor: Int = "#6200EE".toColorInt()
        set(value) {
            field = value
            paintThumb.color = field
            invalidate()
        }

    private val progressHeightPx = dpToPx(4f)
    private var thumbRadiusPx = dpToPx(12f) // 24dp diameter

    private val paintTrack = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = trackColor
    }
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = progressColor
    }
    private val paintThumb = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = thumbColor
        setShadowLayer(dpToPx(2f), 0f, 0f, 0x33000000)
    }

    private var isTracking = false
    private var thumbDrawable: Drawable? = null
    private var thumbOffset: Float = 0f
    private val currentThumbRadiusPx: Float
        get() = thumbDrawable?.let { max(it.intrinsicWidth, it.intrinsicHeight) / 2f } ?: thumbRadiusPx


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomSeekBar,
            0, 0
        ).apply {
            try {
                progressColor = getColor(R.styleable.CustomSeekBar_progressTint, progressColor)
                trackColor = getColor(R.styleable.CustomSeekBar_trackColor, trackColor)
                thumbDrawable = getDrawable(R.styleable.CustomSeekBar_thumb)
                thumbOffset = getDimension(R.styleable.CustomSeekBar_thumbOffset, 0f)
            } finally {
                recycle()
            }
        }

        thumbDrawable?.let { drawable ->
            if (drawable.intrinsicWidth > 0 && drawable.intrinsicHeight > 0) {
                // Ưu tiên size từ drawable nếu có
                thumbRadiusPx = max(drawable.intrinsicWidth, drawable.intrinsicHeight) / 2f
            }
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
    }


    interface OnSeekBarChangeListener {
        fun onProgressChanged(seekBar: CustomSeekBar?, progress: Int, fromUser: Boolean)
        fun onStartTrackingTouch(seekBar: CustomSeekBar?)
        fun onStopTrackingTouch(seekBar: CustomSeekBar?)
    }

    private var onSeekBarChangeListener: OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(listener: OnSeekBarChangeListener) {
        onSeekBarChangeListener = listener
    }

    private fun dpToPx(dp: Float): Float = dp * resources.displayMetrics.density

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)

        // Lấy chiều cao thumb (nếu có) hoặc mặc định 24dp
        val thumbHeight = thumbDrawable?.intrinsicHeight?.toFloat() ?: (thumbRadiusPx * 2)
        // Lấy chiều cao progress bar
        val progressBarHeight = progressHeightPx

        // Chiều cao mong muốn tối thiểu
        val desiredHeight = maxOf(thumbHeight, progressBarHeight, dpToPx(24f)).toInt()

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize // match_parent, 0dp (ConstraintLayout tính toán), hoặc số cụ thể
            MeasureSpec.AT_MOST -> minOf(desiredHeight, heightSize) // wrap_content nhưng bị hạn chế bởi parent
            MeasureSpec.UNSPECIFIED -> desiredHeight // wrap_content, không giới hạn
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cy = height / 2f
        val startX = paddingLeft + currentThumbRadiusPx
        val endX = width - paddingRight - currentThumbRadiusPx

        // Draw track (background)
        val trackTop = cy - progressHeightPx / 2f
        val trackBottom = cy + progressHeightPx / 2f
        canvas.drawRoundRect(
            startX,
            trackTop,
            endX,
            trackBottom,
            progressHeightPx / 2,
            progressHeightPx / 2,
            paintTrack
        )

        // Draw progress
        val percent = progress.toFloat() / max
        val progressX = startX + percent * (endX - startX)
        canvas.drawRoundRect(
            startX,
            trackTop,
            progressX,
            trackBottom,
            progressHeightPx / 2,
            progressHeightPx / 2,
            paintProgress
        )

        // Draw thumb
        if (thumbDrawable != null) {
            val cx = progressX + thumbOffset
            val cy = height / 2f
            canvas.withTranslation(
                cx - thumbDrawable!!.intrinsicWidth / 2f,
                cy - thumbDrawable!!.intrinsicHeight / 2f
            ) {
                thumbDrawable!!.draw(this)
            }
        } else {
            canvas.drawCircle(progressX + thumbOffset, height / 2f, thumbRadiusPx, paintThumb)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val startX = paddingLeft + thumbRadiusPx
        val endX = width - paddingRight - thumbRadiusPx
        val x = event.x.coerceIn(startX, endX)
        val percent = (x - startX) / (endX - startX)
        val newProgress = (percent * max).toInt().coerceIn(0, max)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTracking = true
                parent.requestDisallowInterceptTouchEvent(true)
                onSeekBarChangeListener?.onStartTrackingTouch(this)
                progress = newProgress
                onSeekBarChangeListener?.onProgressChanged(this, progress, true)
                performClick()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                progress = newProgress
                onSeekBarChangeListener?.onProgressChanged(this, progress, true)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isTracking) {
                    progress = newProgress
                    onSeekBarChangeListener?.onProgressChanged(this, progress, true)
                    onSeekBarChangeListener?.onStopTrackingTouch(this)
                    isTracking = false
                }
                parent.requestDisallowInterceptTouchEvent(false)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}