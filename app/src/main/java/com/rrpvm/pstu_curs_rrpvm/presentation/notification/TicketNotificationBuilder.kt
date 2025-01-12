package com.rrpvm.pstu_curs_rrpvm.presentation.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rrpvm.domain.model.BaseShortSessionModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketNotificationBuilder(private val context: Context) {
    fun buildNotification(sessionInfo: BaseShortSessionModel) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        createNotificationChannel()
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.rrpvm.profile.R.drawable.ic_tickets)
            .setContentTitle("Вы купили билет! - ${sessionInfo.sessionInfo}")
            .setContentText("Не забудьте, показ фильма начнется с ${getStringDate(sessionInfo.sessionDate)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setChannelId(CHANNEL_ID)
            .build()

        NotificationManagerCompat.from(context)
            .notify((System.currentTimeMillis() % (Int.MAX_VALUE - 1)).toInt(), builder)
    }
    fun buildAlarmNotification() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        createNotificationChannel()
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.rrpvm.profile.R.drawable.ic_tickets)
            .setContentTitle("Фильм вот-вот начнется!")
            .setContentText("У вас осталось меньше часа до начала, не забудьте)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setChannelId(CHANNEL_ID)
            .build()

        NotificationManagerCompat.from(context)
            .notify((System.currentTimeMillis() % (Int.MAX_VALUE - 1)).toInt(), builder)
    }

    private fun getStringDate(date: Date):String  {
        val str = dateFormat.format(date)
        return str
    }

    private fun createNotificationChannel() {
        val descriptionText = CHANNEL_DESC
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private val dateFormat = SimpleDateFormat("dd/MM, HH:mm", Locale("Ru"))
        private const val CHANNEL_NAME = "TicketsAlarm"
        private const val CHANNEL_ID = "TicketsAlarmID"
        private const val CHANNEL_DESC = "testw"
    }
}