package com.example.notidemo

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

class CustomResultReceiver(val callback :ResultCallback,handler: Handler): ResultReceiver(handler) {
    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {

        super.onReceiveResult(resultCode, resultData)
    }
}
public interface ResultCallback{
    fun onDataReceive()
}