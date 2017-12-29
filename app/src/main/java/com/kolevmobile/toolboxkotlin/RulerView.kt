package com.kolevmobile.toolboxkotlin

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent

class RulerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : MeasuresAbstractView(context, attrs!!) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dpi == 0f) init()

        canvas.drawRect(startPoint.x.toFloat(), startPoint.y.toFloat(), endPoint.x.toFloat(), endPoint.y.toFloat(), contentBackgroundPaint)
        //inches
        run {
            var i = 0
            while (startPoint.y + i * dpi < endPoint.y) {
                canvas.drawLine(startPoint.x.toFloat(), i * dpi + startPoint.y, (startPoint.x + bigLine).toFloat(), i * dpi + startPoint.y, santimetersPaint)
                var j = 1
                while (j < 5 && i * dpi + (height / 30).toFloat() + j * (dpi / 5) < endPoint.y) {
                    canvas.drawLine(startPoint.x.toFloat(), i * dpi + startPoint.y.toFloat() + j * (dpi / 5), (startPoint.x + smallLine).toFloat(), i * dpi + startPoint.y.toFloat() + j * (dpi / 5), milimetersPaint)
                    j++
                }
                i++
            }
        }
        //cm
        run {
            var i = 0
            while (startPoint.y + i * dpc < endPoint.y) {
                canvas.drawLine((endPoint.x - bigLine).toFloat(), i * dpc + startPoint.y, endPoint.x.toFloat(), i * dpc + startPoint.y, santimetersPaint)
                var j = 1
                while (j < 5 && i * dpc + startPoint.y.toFloat() + j * (dpc / 5) < endPoint.y) {
                    canvas.drawLine((endPoint.x - smallLine).toFloat(), i * dpc + startPoint.y.toFloat() + j * (dpc / 5), endPoint.x.toFloat(), i * dpc + startPoint.y.toFloat() + j * (dpc / 5), milimetersPaint)
                    j++
                }
                i++
            }
        }

        canvas.drawLine(0f, line1.toFloat(), width.toFloat(), line1.toFloat(), cursorLinePaint)
        canvas.drawLine(0f, line2.toFloat(), width.toFloat(), line2.toFloat(), cursorLinePaint)
        canvas.drawOval((width / 2 - radius).toFloat(), (line1 - radius).toFloat(), (width / 2 + radius).toFloat(), (line1 + radius).toFloat(), pointPaint)
        canvas.drawOval((width / 2 - radius).toFloat(), (line2 - radius).toFloat(), (width / 2 + radius).toFloat(), (line2 + radius).toFloat(), pointPaint)

        canvas.save()
        canvas.rotate(90f, (width / 2).toFloat(), (height / 2).toFloat())

        run {
            var i = 0
            while (startPoint.y + i * dpi < endPoint.y) {
                canvas.drawText(i.toString(), i * dpi + startPointRotated.y, (endPointRotated.x.toDouble() - bigLine.toDouble() - 0.3 * textHeight).toFloat(), santimetersPaint)
                i++
            }
        }
        canvas.drawText(String.format("%.2f inch", Math.abs(line1 - line2) / dpi), (startPointRotated.y + (line1 + line2) / 2).toFloat(), (endPointRotated.x + 1.2 * textHeight).toFloat(), valuesTextPaint)

        var i = 0
        while (startPoint.y + i * dpc < endPoint.y) {
            canvas.drawText(i.toString(), i * dpc + startPointRotated.y, (startPointRotated.x.toDouble() + bigLine.toDouble() + 1.3 * textHeight).toFloat(), santimetersPaint)
            i++
        }
        canvas.drawText(String.format("%.2f cm", Math.abs(line1 - line2) / dpc), (startPointRotated.y + (line1 + line2) / 2).toFloat(), (startPointRotated.x - 0.5 * textHeight).toFloat(), valuesTextPaint)

        //        canvas.restore();
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.getY(0).toInt()
        var x2 = 0
        if (event.pointerCount > 1)
            x2 = event.getY(1).toInt()

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> if (x > startPoint.y && x < endPoint.y) {
                if (Math.abs(line1 - x) < Math.abs(line2 - x)) {
                    movingLine = 1
                    offsetY = line1 - x
                } else {
                    movingLine = 2
                    offsetY = line2 - x
                }
            } else {
                movingLine = 0
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (x2 > startPoint.y && x2 < endPoint.y) {
                    if (movingLine == 2) {
                        offsetY2 = line1 - x2
                    } else {
                        offsetY2 = line2 - x2
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {

                if (x2 == 0 && lastY != 0 && Math.abs(x - lastY) > Math.abs(x - lastY2)) {
                    lastY = 0
                    lastY2 = 0
                    if (movingLine == 1) {
                        movingLine = 2
                    } else {
                        movingLine = 1
                    }
                }

                if (event.pointerCount <= 2) {

                    if (x + offsetY > startPoint.y && x + offsetY < endPoint.y) {
                        if (movingLine == 1) {
                            line1 = x + offsetY
                        } else if (movingLine == 2) {
                            line2 = x + offsetY
                        }
                    } else if (x + offsetY < startPoint.y) {
                        if (movingLine == 1) {
                            line1 = startPoint.y
                        } else if (movingLine == 2) {
                            line2 = startPoint.y
                        }
                    } else if (x + offsetY > endPoint.y) {
                        if (movingLine == 1) {
                            line1 = endPoint.y
                        } else if (movingLine == 2) {
                            line2 = endPoint.y
                        }
                    }
                    if (x2 != 0) {
                        if (x2 + offsetY2 > startPoint.y && x2 + offsetY2 < endPoint.y) {
                            if (movingLine == 2) {
                                line1 = x2 + offsetY2
                            } else if (movingLine == 1) {
                                line2 = x2 + offsetY2
                            }
                        } else if (x2 + offsetY2 < startPoint.y) {
                            if (movingLine == 2) {
                                line1 = startPoint.y
                            } else if (movingLine == 1) {
                                line2 = startPoint.y
                            }
                        } else if (x2 + offsetY2 > endPoint.x) {
                            if (movingLine == 2) {
                                line1 = endPoint.y
                            } else if (movingLine == 1) {
                                line2 = endPoint.y
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
