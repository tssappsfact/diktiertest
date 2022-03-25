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
class MyView : View {

    private val MAX_AMPLITUDE = 32767

    private lateinit var amplitudes: FloatArray
    private lateinit var vectors: FloatArray
    private var recPaint: Paint? = null
    private var linePaint: Paint? = null
    private var ovalPaint: Paint? = null
    var scaledHeight = 0f

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        recPaint = Paint()
        recPaint!!.color = Color.BLACK
        recPaint!!.strokeWidth = 20f
        linePaint = Paint()
        linePaint!!.color = Color.GREEN
        linePaint!!.strokeWidth = 300f
        ovalPaint = Paint().apply {
            color = Color.GREEN
            strokeWidth = 50f
        }
    }

    override fun onSizeChanged(width: Int, h: Int, oldw: Int, oldh: Int) {
        amplitudes = FloatArray(this.width * 2) // xy for each point across the width
        vectors = FloatArray(this.width * 4) // xyxy for each line across the width
    }

    /**
     * modifies draw arrays. cycles back to zero when amplitude samples reach max screen size
     */
    fun addAmplitude(amplitude: Int) {
        invalidate()
        scaledHeight = amplitude.toFloat() / MAX_AMPLITUDE * (height - 1)
        if(amplitude.toFloat() >= 0.2 * MAX_AMPLITUDE)
            ovalPaint?.color = Color.RED
        else
            ovalPaint?.color = Color.GREEN
        var vectorIdx = 0
        vectors[vectorIdx++] = 0f // x0
        vectors[vectorIdx++] = 0f // y0
        vectors[vectorIdx++] = 0f // x1
        vectors[vectorIdx] = scaledHeight // y1
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f,0f, width.toFloat(), height.toFloat(), recPaint!!)
        canvas.drawLines(vectors, linePaint!!)
        canvas.drawOval(500f-scaledHeight, 500f-scaledHeight, 500f+scaledHeight, 500f+scaledHeight,ovalPaint!!)
    }
}