package com.example.task1

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var boundService: BoundService? = null
    var isBound = false
    private val duration = Toast.LENGTH_SHORT
    var mMessenger: Messenger? = null
    var sMessenger: Messenger? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            isBound = true
            mMessenger = Messenger(service)
            sMessenger = Messenger(service)

        }
        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            boundService = null
            mMessenger = null
            sMessenger = null
        }
    }

    private fun doBindService() {
        bindService(
            Intent(this, BoundService::class.java),
            boundServiceConnection,
            Context.BIND_AUTO_CREATE
        )
        isBound = true
        Toast.makeText(applicationContext,"Service is connected", duration).show()
    }

    private fun doUnbindService(){
        if (isBound) {
            unbindService(boundServiceConnection)
            isBound = false
        }
    }

    fun stopService(view: View) {
        val serviceIntent = Intent(this, BoundService::class.java)
        serviceIntent.action = BoundService.ACTION_STOP_FOREGROUND_SERVICE
        stopService(serviceIntent)
        doUnbindService()
    }

    fun startService(view: View) {
        val serviceIntent = Intent(this, BoundService::class.java)
        serviceIntent.action = BoundService.ACTION_START_FOREGROUND_SERVICE
        startService(serviceIntent)
        doBindService()
    }

    fun connectService(view: View) {
        if (isBound) {
            val message: Message = Message.obtain(null, BoundService.ConnectionState.CONNECTED.key1)
            mMessenger?.send(message)
            Log.d("StateActivity", "${BoundService.ConnectionState.CONNECTED.key1}")
        }
        else {
            Toast.makeText(applicationContext, "Bind service first", Toast.LENGTH_LONG).show()
        }
    }

    fun disconnectService(view: View) {
        if (isBound) {
            val message: Message = Message.obtain(null, BoundService.ConnectionState.DISCONNECTED.key1)
            mMessenger?.send(message)
            Log.d("StateActivity", "${BoundService.ConnectionState.DISCONNECTED.key1}")
        }
        else {
            Toast.makeText(applicationContext, "Bind service first", Toast.LENGTH_LONG).show()
        }
    }

    fun doWork(view: View) {
        if (isBound) {
            val message: Message = Message.obtain(null, BoundService.ServiceState.BUSY.key2)
            sMessenger?.send(message)
            Log.d("StateActivity", "${BoundService.ServiceState.BUSY.key2}")
        }
        else {
            Toast.makeText(applicationContext, "Bind service first", Toast.LENGTH_LONG).show()
        }
    }

    fun stopWork(view: View) {
        if (isBound) {
            val message: Message = Message.obtain(null, BoundService.ServiceState.IDLE.key2)
            sMessenger?.send(message)
            Log.d("StateActivity", "${BoundService.ServiceState.IDLE.key2}")
        }
        else {
            Toast.makeText(applicationContext, "Bind service first", Toast.LENGTH_LONG).show()
        }
    }
}
