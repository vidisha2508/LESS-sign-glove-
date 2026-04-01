package com.example.glove.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@SuppressLint("MissingPermission")
class GloveBluetoothManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        context.getSystemService(BluetoothManager::class.java)?.adapter
    }

    private val SPP_UUID: UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val _connectionState = MutableStateFlow(BluetoothConnectionState.DISCONNECTED)
    val connectionState: StateFlow<BluetoothConnectionState> = _connectionState

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>> = _scannedDevices

    private val _incomingData = MutableSharedFlow<String>()
    val incomingData: SharedFlow<String> = _incomingData

    private var socket: BluetoothSocket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    @Volatile
    private var isConnected = false

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    fun getPairedDevices() {
        bluetoothAdapter?.bondedDevices?.let { devices ->
            _scannedDevices.value = devices.map {
                BluetoothDeviceDomain(it.name ?: "Unknown", it.address)
            }
        }
    }

    suspend fun connectToDevice(mac: String) {
        if (!isBluetoothEnabled()) return

        _connectionState.value = BluetoothConnectionState.CONNECTING

        withContext(Dispatchers.IO) {
            try {
                val device = bluetoothAdapter!!.getRemoteDevice(mac)

                bluetoothAdapter!!.cancelDiscovery()

                // ✅ Primary + fallback socket
                socket = try {
                    device.createRfcommSocketToServiceRecord(SPP_UUID)
                } catch (e: Exception) {
                    val method = device.javaClass.getMethod(
                        "createRfcommSocket",
                        Int::class.java
                    )
                    method.invoke(device, 1) as BluetoothSocket
                }

                socket?.connect()

                // Small delay improves stability
                Thread.sleep(300)

                input = socket?.inputStream
                output = socket?.outputStream

                isConnected = true
                _connectionState.value = BluetoothConnectionState.CONNECTED

                listen()

            } catch (e: Exception) {
                e.printStackTrace()
                closeConnection()
                _connectionState.value = BluetoothConnectionState.ERROR
            }
        }
    }

    private suspend fun listen() {
        withContext(Dispatchers.IO) {

            val buffer = ByteArray(1024)
            val builder = StringBuilder()

            while (isConnected) {
                try {
                    val bytes = input?.read(buffer) ?: -1

                    if (bytes > 0) {
                        val data = String(buffer, 0, bytes)

                        // ✅ Emit raw (for phones/laptops)
                        _incomingData.emit(data.trim())

                        // ✅ Also handle newline-based messages
                        for (c in data) {
                            if (c == '\n' || c == '\r') {
                                if (builder.isNotEmpty()) {
                                    _incomingData.emit(builder.toString().trim())
                                    builder.clear()
                                }
                            } else {
                                builder.append(c)
                            }
                        }
                    }

                } catch (e: IOException) {
                    isConnected = false
                    _connectionState.value = BluetoothConnectionState.DISCONNECTED
                    break
                }
            }
        }
    }

    suspend fun sendCommand(msg: String) {
        withContext(Dispatchers.IO) {
            try {
                // ✅ newline for compatibility
                output?.write((msg + "\n").toByteArray())
                output?.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun closeConnection() {
        try {
            isConnected = false
            input?.close()
            output?.close()
            socket?.close()
            _connectionState.value = BluetoothConnectionState.DISCONNECTED
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}