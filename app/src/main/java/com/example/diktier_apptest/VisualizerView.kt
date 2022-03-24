package com.example.diktier_apptest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * TODO: document your custom view class.
 */
class VisualizerView : View {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private val MAX_AMPLITUDE = 32767

    private lateinit var amplitudes: FloatArray
    private lateinit var vectors: FloatArray
    private var insertIdx = 0
    private var pointPaint: Paint? = null
    private var linePaint: Paint? = null

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        linePaint = Paint()
        linePaint!!.color = Color.GREEN
        linePaint!!.strokeWidth = 1f
        pointPaint = Paint()
        pointPaint!!.color = Color.BLUE
        pointPaint!!.strokeWidth = 1f
    }

    override fun onSizeChanged(width: Int, h: Int, oldw: Int, oldh: Int) {
        amplitudes = FloatArray(this.width * 2) // xy for each point across the width
        vectors = FloatArray(this.width * 4) // xxyy for each line across the width
    }

    /**
     * modifies draw arrays. cycles back to zero when amplitude samples reach max screen size
     */
    fun addAmplitude(amplitude: Int) {
        invalidate()
        val scaledHeight = amplitude.toFloat() / MAX_AMPLITUDE * (height - 1)
        var ampIdx = insertIdx * 2
        amplitudes[ampIdx++] = insertIdx.toFloat() // x
        amplitudes[ampIdx] = scaledHeight // y
        var vectorIdx = insertIdx * 4
        vectors[vectorIdx++] = insertIdx.toFloat() // x0
        vectors[vectorIdx++] = 0f // y0
        vectors[vectorIdx++] = insertIdx.toFloat() // x1
        vectors[vectorIdx] = scaledHeight // y1
        // insert index must be shorter than screen width
        insertIdx = if (++insertIdx >= width) 0 else insertIdx
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLines(vectors, linePaint!!)
        canvas.drawPoints(amplitudes, pointPaint!!)
    }
}