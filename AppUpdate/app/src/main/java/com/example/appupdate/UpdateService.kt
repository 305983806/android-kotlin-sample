package com.example.appupdate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class UpdateService : Service() {

    private var downloadFile:String = ""

    private lateinit var player: MediaPlayer

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return super.onStartCommand(intent, flags, startId)
        Log.i("Service Test", "开启服务")
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        player.setLooping(true)
        player.start()
        return START_STICKY
    }

    private fun doDownload(url: String) {
        var connection: HttpsURLConnection? = null
        val url = URL(url)
        connection = url.openConnection() as HttpsURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        connection.requestMethod = "GET"
        val input = connection.inputStream
        val contentCount = connection.getContentLength()
        var byteArray = ByteArray(1024)

        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

        val file = File(storageDir?.absolutePath + "/test.apk")
        if (file.exists()) {
            file.delete()
        } else {
            file.createNewFile()
        }
        downloadFile = storageDir?.absolutePath + "/test.apk"
        val fileOutputStream = FileOutputStream(file)
        var downloadSize = 0

        while (true) {
            var readLength = input.read(byteArray)
            if (readLength == -1) {
                break
            } else {
                fileOutputStream.write(byteArray, 0, readLength)
                downloadSize = downloadSize + readLength
                var process = ((downloadSize.toDouble() / contentCount) * 100).toInt()
            }
        }

        if (contentCount == downloadSize) {

        } else {
            //TODO 下载失败，通过

        }
    }

    private fun sendNotification(msg:String){
        val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel=NotificationChannel("my_service","前台Service通知",NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val notification= NotificationCompat.Builder(this,"my_service")
            .setContentTitle("下载")
            .setContentText(msg)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
        startForeground(2,notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        Log.i("Service Test", "结束服务")
    }
}