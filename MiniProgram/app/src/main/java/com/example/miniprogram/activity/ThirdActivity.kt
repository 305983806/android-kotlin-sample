package com.example.miniprogram.activity

import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.miniprogram.R
import com.example.miniprogram.ui.theme.MiniProgramTheme

class ThirdActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniProgramTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "这是第三页")
                        Button(onClick = {
                            ActivityCollector.finishAll()
//                            android.os.Process.killProcess(android.os.Process.myPid())
                        }) {
                            Text(text = "关闭小程序")
                        }
                    }
                }
            }
        }
        setCustomTaskDescription()
    }

    private fun setCustomTaskDescription() {
        val taskDescription = ActivityManager.TaskDescription(
            "ThirdActivity",
            BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
        )
        setTaskDescription(taskDescription)
    }
}