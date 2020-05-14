package com.example.task1

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task1.BoundService.MyBinder


class MainActivity : AppCompatActivity() {
    var boundService: BoundService? = null
    var isBound = false
    val text = "Service is connected"
    val duration = Toast.LENGTH_SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(boundServiceConnection)
            isBound = false
        }
    }

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binderBridge = service as MyBinder
            boundService = binderBridge.service
            isBound = true
            Toast.makeText(applicationContext,text, duration).show()
        }
        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            boundService = null
        }
    }

    fun stopService(view: View) {
        val serviceIntent = Intent(this, BoundService::class.java)
        stopService(serviceIntent)
    }

    fun startService(view: View) {
        val serviceIntent = Intent(this, BoundService::class.java)
        startService(serviceIntent)
        bindService(intent, boundServiceConnection, Context.BIND_AUTO_CREATE)
    }
}
