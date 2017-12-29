package com.kolevmobile.toolboxkotlin

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import com.wonderkiln.camerakit.CameraView

class LevelActivity : Activity(), SensorEventListener {

    internal lateinit var levelView: LevelView
    internal lateinit var rotateView: TextView
    internal lateinit var horizontalTextView: TextView
    internal lateinit var horizontalTextViewName: TextView
    internal lateinit var verticalTextView: TextView
    internal lateinit var verticalTextViewName: TextView
    // Gravity rotational data
    private var gravity: FloatArray? = null
    // Magnetic rotational data
    private var magnetic: FloatArray? = null
    private var accels: FloatArray? = FloatArray(3)
    private var mags: FloatArray? = FloatArray(3)
    private val values = FloatArray(3)

    internal lateinit var sManager: SensorManager


    internal lateinit var cameraView: com.wonderkiln.camerakit.CameraView


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        this.levelView = findViewById(R.id.levelView)
        this.rotateView = findViewById(R.id.rotateTextView)
        this.horizontalTextView = findViewById(R.id.horizontalTextView)
        this.horizontalTextViewName = findViewById(R.id.horizontalTextViewName)
        this.verticalTextView = findViewById(R.id.verticalTextView)
        this.verticalTextViewName = findViewById(R.id.verticalTextViewName)

        cameraView = findViewById(R.id.camera)

        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // I do not care about it at this moment.
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent) {

        when (event.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD -> mags = event.values.clone()
            Sensor.TYPE_ACCELEROMETER -> accels = event.values.clone()
        }

        if (mags != null && accels != null) {
            gravity = FloatArray(9)
            magnetic = FloatArray(9)
            SensorManager.getRotationMatrix(gravity, magnetic, accels, mags)
            val outGravity = FloatArray(9)
            SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X, SensorManager.AXIS_Z, outGravity)
            SensorManager.getOrientation(outGravity, values)
            mags = null
            accels = null


            //            float azimuth = (float) (values[0] * 180 / Math.PI);
            val pitch = (values[1] * 180 / Math.PI).toFloat()
            val roll = (-(values[2] * 57.2957795f) + 720) % 360

            if (pitch > 40 && pitch < 140) {     // check for horizontal device
                rotateView.text = ""
                val horizontalDegrees = ((90 - pitch) * Math.sin(Math.PI * roll / 180)).toInt()
                val verticalDegrees = ((90 - pitch) * Math.cos(Math.PI * roll / 180)).toInt()
                levelView.setDegreesHoriontalView(horizontalDegrees.toDouble(), verticalDegrees.toDouble())

                horizontalTextView.text = horizontalDegrees.toString() + 0x00B0.toChar()
                horizontalTextViewName.setText(R.string.string_horizontal)
                verticalTextView.text = verticalDegrees.toString() + 0x00B0.toChar()
                verticalTextViewName.setText(R.string.string_vertical)
            } else {
                rotateView.text = roll.toInt().toString() + "" + 0x00B0.toChar()
                rotateView.rotation = roll

                levelView.setDegreesVerticalView(roll)
                horizontalTextView.text = ""
                horizontalTextViewName.text = ""
                verticalTextView.text = ""
                verticalTextViewName.text = ""
            }
        }
    }


}

