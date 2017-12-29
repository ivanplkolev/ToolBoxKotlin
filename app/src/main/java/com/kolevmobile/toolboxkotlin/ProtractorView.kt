package com.kolevmobile.toolboxkotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent

import java.lang.Math.PI
import java.lang.Math.abs
import java.lang.Math.atan
import java.lang.Math.cos
import java.lang.Math.round
import java.lang.Math.sin
import java.lang.Math.toDegrees

class ProtractorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : MeasuresAbstractView(context, attrs!!) {

    private var centerPoint: Point? = null
    private var r: Int = 0
    private var rectF: RectF? = null

    private var lastX = 0
    private var lastX2 = 0
    private var offsetX = 0
    private val offsetX2 = 0

    protected var line1Point = Point()
    protected var line2Point = Point()
    protected var primaryLine = Point()
    protected var secondaryLine = Point()

    override fun init() {
        super.init()
        line1Point.y = (0.25 * height).toFloat()
        line2Point.y = (0.75 * height).toFloat()
        startPoint.y = if (height > 2 * width) height - 2 * width else 0
        startPoint.x = if (height > 2 * width) 0 else (width - height / 2) / 2
        endPoint.y = height - startPoint.y
        endPoint.x = width - startPoint.x
        r = endPoint.x - startPoint.x
        centerPoint = Point(startPoint.x.toFloat(), (startPoint.y + r).toFloat())
        rectF = RectF((startPoint.x - r).toFloat(), startPoint.y.toFloat(), endPoint.x.toFloat(), endPoint.y.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dpi == 0f) init()

        canvas.drawArc(rectF!!, 0f, -90f, true, contentBackgroundPaint)
        canvas.drawArc(rectF!!, 0f, 90f, true, contentBackgroundPaint)

        run {
            var i = 0
            while (i <= 180) {
                val iRad = Math.toRadians(i.toDouble())
                canvas.drawLine(startPoint.x + sin(iRad).toFloat() * r, centerPoint!!.y - cos(iRad).toFloat() * r, startPoint.x + sin(iRad).toFloat() * (r - bigLine), centerPoint!!.y - cos(iRad).toFloat() * (r - bigLine), santimetersPaint)
                var j = 2
                while (j < 10 && i < 180) {
                    val jRad = Math.toRadians((i + j).toDouble())
                    canvas.drawLine(startPoint.x + sin(jRad).toFloat() * r, centerPoint!!.y - cos(jRad).toFloat() * r, startPoint.x + sin(jRad).toFloat() * (r - smallLine), centerPoint!!.y - cos(jRad).toFloat() * (r - smallLine), santimetersPaint)
                    j += 2
                }
                i += 10
            }
        }
        canvas.save()
        var i = 0
        while (i <= 180) {
            canvas.drawText(i.toString(), startPoint.x.toFloat(), (startPoint.y + bigLine * 2).toFloat(), santimetersPaint)
            canvas.rotate(20f, startPoint.x.toFloat(), centerPoint!!.y)
            i += 20
        }
        canvas.restore()

        val angle1 = line1Point.y.toFloat() / (2 * r) * 180
        val angle2 = line2Point.y.toFloat() / (2 * r) * 180
        val radians1 = (angle1 / 180 * PI).toFloat()
        val radians2 = (angle2 / 180 * PI).toFloat()


        val line1EndPoint = Point(startPoint.x + sin(radians1.toDouble()).toFloat() * r, centerPoint!!.y - cos(radians1.toDouble()).toFloat() * r)
        val line1MidPoint = Point((centerPoint!!.x + line1EndPoint.x) / 2, (centerPoint!!.y + line1EndPoint.y) / 2)
        val line2EndPoint = Point(startPoint.x + sin(radians2.toDouble()).toFloat() * r, centerPoint!!.y - cos(radians2.toDouble()).toFloat() * r)
        val line2MidPoint = Point((centerPoint!!.x + line2EndPoint.x) / 2, (centerPoint!!.y + line2EndPoint.y) / 2)

        canvas.drawLine(centerPoint!!.x, centerPoint!!.y, line1EndPoint.x, line1EndPoint.y, cursorLinePaint)
        canvas.drawLine(centerPoint!!.x, centerPoint!!.y, line2EndPoint.x, line2EndPoint.y, cursorLinePaint)
        canvas.drawOval(line1MidPoint.x - radius, line1MidPoint.y - radius, line1MidPoint.x + radius, line1MidPoint.y + radius, pointPaint)
        canvas.drawOval(line2MidPoint.x - radius, line2MidPoint.y - radius, line2MidPoint.x + radius, line2MidPoint.y + radius, pointPaint)

        canvas.rotate((angle1 + angle2) / 2, startPoint.x.toFloat(), centerPoint!!.y)
        canvas.drawText(round(abs(angle1 - angle2)).toString() + "" + 0x00B0.toChar(), startPoint.x.toFloat(), (startPoint.y + bigLine * 3).toFloat(), valuesTextPaint)
    }

    protected class Point {
        internal var x: Float = 0.toFloat()
        internal var y: Float = 0.toFloat()

        internal constructor() {}
        internal constructor(x: Float, y: Float) {
            this.x = x
            this.y = y
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.getX(0).toInt()
        val y = event.getY(0).toInt()
        var y2 = 0
        var x2 = 0
        if (event.pointerCount > 1) {
            x2 = event.getX(1).toInt()
            y2 = event.getY(1).toInt()
        }
        when (event.actionMasked) {
        //register the offsets
            MotionEvent.ACTION_DOWN -> if (y > startPoint.y && y < endPoint.y) {
                //determine which line to move
                val touchAngle = (180 + toDegrees(atan((x / (centerPoint!!.y - y)).toDouble()))).toFloat() % 180
                val angle1 = line1Point.y / (2 * r) * 180
                val angle2 = line2Point.y / (2 * r) * 180
                if (abs(angle1 - touchAngle) < abs(angle2 - touchAngle)) {
                    movingLine = 1
                    offsetY = (line1Point.y - y).toInt()
                    offsetX = lastX - x
                    lastX = x
                } else {
                    movingLine = 2
                    offsetY = (line2Point.y - y).toInt()
                    offsetX = lastX - x
                    lastX2 = x
                }
            } else {
                movingLine = 0
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (y2 > startPoint.y && y2 < endPoint.y) {
                    if (movingLine == 2) {
                        offsetY2 = (line1Point.y - y2).toInt()
                    } else {
                        offsetY2 = (line2Point.y - y2).toInt()
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {

                if (event.pointerCount <= 2) {

                    if (y + offsetY > startPoint.y && y + offsetY < endPoint.y) {
                        if (movingLine == 1) {
                            line1Point.y = (y + offsetY).toFloat()
                        } else if (movingLine == 2) {
                            line2Point.y = (y + offsetY).toFloat()
                        }
                    } else if (y + offsetY < startPoint.y) {
                        if (movingLine == 1) {
                            line1Point.y = startPoint.y.toFloat()
                        } else if (movingLine == 2) {
                            line2Point.y = startPoint.y.toFloat()
                        }
                    } else if (y + offsetY > endPoint.y) {
                        if (movingLine == 1) {
                            line1Point.y = endPoint.y.toFloat()
                        } else if (movingLine == 2) {
                            line2Point.y = endPoint.y.toFloat()
                        }
                    }

                    if (y2 != 0) {
                        if (y2 + offsetY2 > startPoint.y && y2 + offsetY2 < endPoint.y) {
                            if (movingLine == 2) {
                                line1Point.y = (y2 + offsetY2).toFloat()
                            } else if (movingLine == 1) {
                                line2Point.y = (y2 + offsetY2).toFloat()
                            }
                        } else if (y2 + offsetY2 < startPoint.y) {
                            if (movingLine == 2) {
                                line1Point.y = startPoint.y.toFloat()
                            } else if (movingLine == 1) {
                                line2Point.y = startPoint.y.toFloat()
                            }
                        } else if (y2 + offsetY2 > endPoint.x) {
                            if (movingLine == 2) {
                                line1Point.y = endPoint.y.toFloat()
                            } else if (movingLine == 1) {
                                line2Point.y = endPoint.y.toFloat()
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                movingLine = 0
                offsetY = 0
                offsetY2 = 0
                lastY = 0
                lastY2 = 0
            }
        }
        invalidate()

        return true
    }


}
