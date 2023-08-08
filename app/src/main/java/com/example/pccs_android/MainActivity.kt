package com.example.pccs_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.io.DataOutputStream
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var sck: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread {
            try {
                sck = Socket("192.168.1.200", 6001)
            } catch (e: java.lang.Exception) {
                println(e)
            }
        }.start()
    }

    fun onButtonClick(view: View) {
        val tag = view.tag
        if (tag is String) {
            val buttonValue = tag.toInt() // 将标签转换为您需要的数据类型
            val cmd = byteArrayOf(0xFA.toByte(),0x00,0x00,buttonValue.toByte(),0x00,0x03,0x05,0xFD.toByte(),0x01,0x01,0x01,0x01,0x01)
            sendCmd(cmd)
        }
    }

    private fun sendCmd(data: ByteArray){
        if(::sck.isInitialized && sck.isConnected){
            Thread{
                try {
                    val outputStream = sck.getOutputStream()
                    val dataOutputStream = DataOutputStream(outputStream)
                    dataOutputStream.write(data)
                    dataOutputStream.flush()
                    outputStream.flush()
                }
                catch (e: java.lang.Exception){
                    println(e)
                }
            }.start()
        }else {
            // 在这里处理未初始化的情况
            println("sck未初始化或未连接")
        }

    }

}