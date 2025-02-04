package com.section11.mystock.framework.secretmenu

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

private const val SHAKE_THRESHOLD = 2.5f
private const val SHAKE_DURATION = 200L

class ShakeDetector(context: Context, onShake: () -> Unit) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val sensorEventListener = object : SensorEventListener {
        private var lastTime = System.currentTimeMillis()

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val shakeThreshold = SHAKE_THRESHOLD
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastTime > SHAKE_DURATION) {
                    val gX = it.values[0] / SensorManager.GRAVITY_EARTH
                    val gY = it.values[1] / SensorManager.GRAVITY_EARTH
                    val gZ = it.values[2] / SensorManager.GRAVITY_EARTH
                    val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

                    if (gForce > shakeThreshold) {
                        onShake()
                        lastTime = currentTime
                    }
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { /* no op */ }
    }

    fun start() {
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
