package com.example.task1

enum class ConnectionState {
    CONNECTED {
        fun isConnected(){}
    },
    DISCONNECTED {
        fun isDisconnected(){}
    },
}

enum class ServiceState {
    IDLE {
        fun isIdle(){}
    },
    BUSY {
        fun isBusy(){}
    };
}

