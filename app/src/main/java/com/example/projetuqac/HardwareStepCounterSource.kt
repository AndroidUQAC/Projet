package com.example.projetuqac

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class HardwareStepCounterSource(private val context: Context, params: WorkerParameters) : SensorEventListener,
    CoroutineWorker(context, params) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var numberOfStep = 0
    private var baseStepNumber : Int? = null


    override fun onSensorChanged(p0: SensorEvent?) {
        p0?.also {
            if (baseStepNumber == null) {
                baseStepNumber = it.values[0].toInt()
            }

            numberOfStep += (it.values[0].toInt() - baseStepNumber!!)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override suspend fun doWork(): Result {
        if (stepCounterSensor == null) {
            return Result.failure()
        }

        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)

        setForeground(getForegroundInfo())

        update()

        return Result.success()
    }

    private suspend fun update() {
        if (!isStopped) {
            delay(5000)
            setForeground(createForegroundInfo())
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo()
    }

    private fun createForegroundInfo() : ForegroundInfo {
        val title = "Steps"
        val channelId = "com.example.projetuqac.STEP_CHANNEL_ID"


        var notificationBuilder : NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId)
            notificationBuilder = NotificationCompat.Builder(context, channelId)
        } else {
            notificationBuilder = NotificationCompat.Builder(context)
        }

        val notification = notificationBuilder
            .setContentTitle(title)
            .setTicker(title)
            .setContentText("$numberOfStep steps")
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        return ForegroundInfo(STEP_NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)
    }

    private fun createChannel(channelId: String) {
        val name = context.getString(R.string.step_channel_name)
        val descriptionText = context.getString(R.string.step_channel_description)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, name, importance)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        channel.description = descriptionText
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val STEP_NOTIFICATION_ID = 0
    }
}