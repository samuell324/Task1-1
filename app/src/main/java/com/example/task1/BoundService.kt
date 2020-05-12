package com.example.task1

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder


class BoundService : Service() {
    private val localBinder: IBinder = MyBinder()
    override fun onBind(intent: Intent): IBinder? {
        return localBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    inner class MyBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }
}
