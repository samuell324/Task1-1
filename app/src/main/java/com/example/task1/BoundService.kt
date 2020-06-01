package com.example.task1
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.task1.App.Companion.channelID
import android.app.NotificationManager
import android.content.Context
import android.os.*
import android.util.Log
import kotlinx.android.parcel.Parcelize


class BoundService : Service() {

    var mMessenger: Messenger = Messenger(IncomingHandler())
    var mConnectionState = ConnectionState.DISCONNECTED
    var mServiceState = ServiceState.IDLE
    private val startId = 1

    companion object {
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
    }

    enum class ConnectionState(var key1: Int) {
        CONNECTED(1),
        DISCONNECTED(2),
    }
    enum class ServiceState(var key2: Int) {
        IDLE(3),
        BUSY(4)
    }

    override fun onBind(intent: Intent): IBinder? {
        Toast.makeText(applicationContext, "Service Binding...", Toast.LENGTH_LONG).show()
        sendGadget()
        return mMessenger.binder
    }

    inner class IncomingHandler(): Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ConnectionState.DISCONNECTED.key1-> mConnectionState = ConnectionState.DISCONNECTED
                ConnectionState.CONNECTED.key1 -> mConnectionState = ConnectionState.CONNECTED
            }
            when (msg.what) {
                ServiceState.IDLE.key2 -> mServiceState = ServiceState.IDLE
                ServiceState.BUSY.key2 -> mServiceState = ServiceState.BUSY
            }
            Log.d("State", "$mServiceState")
            super.handleMessage(msg)
            updateNotification()
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START_FOREGROUND_SERVICE -> startForegroundService()
            ACTION_STOP_FOREGROUND_SERVICE -> stopForegroundService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        startForeground(startId, getMyActivityNotification(smallIcon = R.drawable.ic_launcher_foreground))
    }

    private fun getMyActivityNotification(smallIcon: Int): Notification {
        val intent = Intent()
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent, 0)
        val b = NotificationCompat.Builder(this, channelID )
        b.setContentIntent(pendingIntent)
        b.setContentTitle("Title")
        b.setContentText("Simple text")
        b.setSmallIcon(smallIcon)
        START_NOT_STICKY
        return b.build()
    }

    private fun updateNotification() {
        val smallIcon: Int = when (mConnectionState) {
            ConnectionState.CONNECTED -> R.drawable.ic_thumb_up_black_24dp
            ConnectionState.DISCONNECTED -> R.drawable.ic_thumb_down_black_24dp}
        val notification: Notification = getMyActivityNotification(smallIcon)
        val mNotificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(startId, notification)
    }


    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    @Parcelize
    data class Gadget ( val value1: Int, val value2: Int, val value3: Int) :Parcelable

    private fun sendGadget () {
        val intent = Intent (this, MainActivity::class.java)
        intent.putExtra("extra_Gadget", Gadget(value1 = 1, value2 = 2, value3 = 3))
    }
}