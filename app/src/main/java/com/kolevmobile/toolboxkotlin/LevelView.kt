package com.kolevmobile.toolboxkotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class LevelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var paint = Paint()
    private var dashedPaint: Paint? = null
    private var paintSector: Paint? = null
    private var myPaint: Paint? = null

    private var radius = 0
    private var smallRadius = 0
    private var arrow = 0

    private var horizontalDegrees: Double = 0.toDouble()
    private var verticalDegrees: Double = 0.toDouble()
    private var roll: Float = 0.toFloat()

    private var isHorizontal: Boolean = false

    private var v: Float = 0.toFloat()
    private var h: Float = 0.toFloat()
    private var minusV: Float = 0.toFloat()
    private var minusH: Float = 0.toFloat()

    fun setDegreesVerticalView(roll: Float) {
        this.roll = roll
        this.isHorizontal = false
        invalidate()
    }

    fun setDegreesHoriontalView(horizontalDegrees: Double, verticalDegrees: Double) {
        this.horizontalDegrees = horizontalDegrees
        this.verticalDegrees = verticalDegrees
        isHorizontal = true
        invalidate()
    }

    private fun setSizes() {
        if (radius == 0) {
            //nothing dependant
            paint = Paint()
            paint.strokeWidth = resources.getDimension(R.dimen.line_thickness_4)
            paint.color = resources.getColor(R.color.color_level_black)
            dashedPaint = Paint()
            dashedPaint!!.style = Paint.Style.STROKE
            dashedPaint!!.pathEffect = DashPathEffect(floatArrayOf(25.0f, 20.0f), 0f)
            dashedPaint!!.color = resources.getColor(R.color.color_level_white)
            dashedPaint!!.strokeWidth = resources.getDimension(R.dimen.line_thickness_2)
            paintSector = Paint()
            paintSector!!.color = resources.getColor(R.color.color_level_sectors)
            myPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            myPaint!!.style = Paint.Style.STROKE
            myPaint!!.strokeWidth = resources.getDimension(R.dimen.line_thickness_2)
            myPaint!!.color = resources.getColor(R.color.color_level_white)
        }
        //window size dependant
        radius = (Math.min(width, height).toDouble() * 0.5 * 0.7).toInt()
        smallRadius = (radius * 0.07).toInt()
        arrow = (radius * 1.3).toInt()
        //data dependant
        v = (height / 2 + arrow * Math.sin(Math.PI * roll / 180)).toFloat()
        h = (width / 2 + arrow * Math.cos(Math.PI * roll / 180)).toFloat()
        minusV = (height / 2 - arrow * Math.sin(Math.PI * roll / 180)).toFloat()
        minusH = (width / 2 - arrow * Math.cos(Math.PI * roll / 180)).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setSizes()

        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius.toFloat(), myPaint!!)

        if (isHorizontal) {
            val path = Path()
            path.moveTo((width / 2 + smallRadius * 1.5).toFloat(), (height / 2).toFloat())
            path.lineTo((width / 2 + radius).toFloat(), (height / 2).toFloat())
            path.moveTo((width / 2 - radius).toFloat(), (height / 2).toFloat())
            path.lineTo((width / 2 - smallRadius * 1.5).toFloat(), (height / 2).toFloat())
            path.moveTo((width / 2).toFloat(), (height / 2 - radius).toFloat())
            path.lineTo((width / 2).toFloat(), (height / 2 - smallRadius * 1.5).toFloat())
            path.moveTo((width / 2).toFloat(), (height / 2 + smallRadius * 1.5).toFloat())
            path.lineTo((width / 2).toFloat(), (height / 2 + radius).toFloat())
            canvas.drawPath(path, dashedPaint!!)
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (smallRadius * 1.5).toFloat(), dashedPaint!!)
            canvas.drawOval((width / 2 + (radius - smallRadius) * horizontalDegrees / 50).toFloat() - smallRadius, (height / 2 - (radius - smallRadius) * verticalDegrees / 50).toFloat() - smallRadius, (width / 2 + (radius - smallRadius) * horizontalDegrees / 50).toFloat() + smallRadius, (height / 2 - (radius - smallRadius) * verticalDegrees / 50).toFloat() + smallRadius, paintSector!!)
        } else {
            val path = Path()
            path.moveTo((width / 2 + radius).toFloat(), (height / 2).toFloat())
            path.lineTo((width / 2 - radius).toFloat(), (height / 2).toFloat())
            path.moveTo((width / 2).toFloat(), (height / 2 - radius).toFloat())
            path.lineTo((width / 2).toFloat(), (height / 2 + radius).toFloat())
            canvas.drawPath(path, dashedPaint!!)
            val rectF = RectF((width / 2 - radius).toFloat(), (height / 2 - radius).toFloat(), (width / 2 + radius).toFloat(), (height / 2 + radius).toFloat())
            if (roll < 45) {
                canvas.drawArc(rectF, 0f, roll, true, paintSector!!)
                canvas.drawArc(rectF, 180f, roll, true, paintSector!!)
            } else if (roll < 135) {
                canvas.drawArc(rectF, 90f, roll - 90, true, paintSector!!)
                canvas.drawArc(rectF, 270f, roll - 90, true, paintSector!!)
            } else if (roll < 225) {
                canvas.drawArc(rectF, 180f, roll - 180, true, paintSector!!)
                canvas.drawArc(rectF, 0f, roll - 180, true, paintSector!!)
            } else if (roll < 315) {
                canvas.drawArc(rectF, 270f, roll - 270, true, paintSector!!)
                canvas.drawArc(rectF, 90f, roll - 270, true, paintSector!!)
            } else {
                canvas.drawArc(rectF, 0f, roll - 360, true, paintSector!!)
                canvas.drawArc(rectF, 180f, roll - 360, true, paintSector!!)
            }

            canvas.drawLine(minusH, minusV, h, v, paint)
        }
    }

}
