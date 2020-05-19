package com.example.task1

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task1.BoundService.MyBinder


class MainActivity : AppCompatActivity() {
    var boundService: BoundService? = null
    var isBound = false
    private val duration = Toast.LENGTH_SHORT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binderBridge = service as MyBinder
            boundService = binderBridge.service
            isBound = true
        }
        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            boundService = null
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
        stopService(serviceIntent)
        doUnbindService()
    }

    fun startService(view: View) {
        val serviceIntent = Intent(this, BoundService::class.java)
        startService(serviceIntent)
        doBindService()
    }

    fun connectService(view: View) {
    }

}
