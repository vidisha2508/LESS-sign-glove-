package com.example.glove.bluetooth

enum class BluetoothConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

data class BluetoothDeviceDomain(
    val name: String?,
    val macAddress: String
)
