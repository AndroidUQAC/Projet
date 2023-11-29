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
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.projetuqac.db.Result
import com.example.projetuqac.db.repository.ApiRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class HardwareStepCounterSource @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val apiRepository: ApiRepository) : SensorEventListener,
    CoroutineWorker(context, params) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var numberOfStep = 0
    private var baseStepNumber : Int? = null

    private var accelerometreX : Double = 0.0
    private var accelerometreY : Double = 0.0
    private var accelerometreZ : Double = 0.0


    override fun onSensorChanged(p0: SensorEvent?) {
        p0?.also {
            accelerometreX = it.values[0].toDouble()
            accelerometreY = it.values[1].toDouble()
            accelerometreZ = it.values[2].toDouble()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override suspend fun doWork(): Result {
        if (stepCounterSensor == null) {
            Log.d("debug","stepCounterSensor == null")
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
            Log.d("debug","accelerometreX : $accelerometreX")
            Log.d("debug","accelerometreY : $accelerometreY")
            Log.d("debug","accelerometreZ : $accelerometreZ")
            try {
                val result = apiRepository.refreshPosts()
                if (result is com.example.projetuqac.db.Result.Success) {
                    Log.d("debug", "Posts refreshed successfully")
                } else if (result is com.example.projetuqac.db.Result.Error) {
                    Log.e("debug", "Error refreshing posts: ${result.message}")
                }
            } catch (e: Exception) {
                Log.e("debug", "Error refreshing posts", e)
            }

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
            .setContentText("X: $accelerometreX, Y: $accelerometreY, Z: $accelerometreZ")
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