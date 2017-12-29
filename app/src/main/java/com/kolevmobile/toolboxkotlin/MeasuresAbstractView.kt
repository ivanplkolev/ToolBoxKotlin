package com.kolevmobile.toolboxkotlin

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View

abstract class MeasuresAbstractView(context: Context, attrs: AttributeSet) : View(context, attrs) {


    protected var context: Activity

    protected var valuesTextPaint = Paint()
    protected var milimetersPaint = Paint()
    protected var santimetersPaint = Paint()
    protected var contentBackgroundPaint = Paint()
    protected var pointPaint = Paint()
    protected var cursorLinePaint = Paint()

    protected var viewHeight: Int = 0
    protected var viewWidth: Int = 0
    protected var startPoint = Point()
    protected var endPoint = Point()
    protected var startPointRotated = Point()
    protected var endPointRotated = Point()


    protected var offsetY = 0
    protected var offsetY2 = 0
    protected var bigLine = 0
    protected var smallLine = 0
    protected var radius = 0
    protected var textHeight = 0

    protected var dpi: Float = 0.toFloat()
    protected var dpc: Float = 0.toFloat()

    protected var line1: Int = 0
    protected var line2: Int = 0
    protected var movingLine = 0
    protected var lastY: Int = 0
    protected var lastY2: Int = 0

    init {
        this.context = context as Activity
    }

    protected open fun init() {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        dpi = displayMetrics.ydpi
        dpc = (dpi / 2.54).toFloat()
        viewHeight = getHeight()
        viewWidth = getWidth()
        startPoint.y = viewHeight / 30
        startPoint.x = viewWidth / 10
        endPoint.y = viewHeight - startPoint.y
        endPoint.x = viewWidth - startPoint.x
        startPointRotated.y = (viewWidth - viewHeight) / 2 + startPoint.y
        startPointRotated.x = (viewHeight - viewWidth) / 2 + startPoint.x
        endPointRotated.y = (viewHeight - viewWidth) / 2 - startPoint.y
        endPointRotated.x = (viewWidth + viewHeight) / 2 - startPoint.x
        line1 = (0.25 * viewHeight).toInt()
        line2 = (0.75 * viewHeight).toInt()
        bigLine = (0.5 * dpc).toInt()
        smallLine = (0.25 * dpc).toInt()
        radius = (0.2 * dpc).toInt()
        santimetersPaint.strokeWidth = resources.getDimension(R.dimen.line_thickness_2)
        santimetersPaint.textSize = resources.getDimension(R.dimen.text_medium)
        santimetersPaint.textAlign = Paint.Align.CENTER
        valuesTextPaint.color = resources.getColor(R.color.color_ruler_white)
        valuesTextPaint.textSize = resources.getDimension(R.dimen.text_over_medium)
        valuesTextPaint.textAlign = Paint.Align.CENTER
        santimetersPaint.color = resources.getColor(R.color.color_ruler_light_gray)
        milimetersPaint.color = resources.getColor(R.color.color_ruler_gray)
        contentBackgroundPaint.color = resources.getColor(R.color.color_ruler_dark_gray)
        pointPaint.color = resources.getColor(R.color.color_ruler_red)
        cursorLinePaint.strokeWidth = resources.getDimension(R.dimen.line_thickness_1)
        cursorLinePaint.color = resources.getColor(R.color.color_ruler_light_red)
        textHeight = valuesTextPaint.textSize.toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }


}
