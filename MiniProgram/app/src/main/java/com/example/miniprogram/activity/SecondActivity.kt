package com.example.miniprogram.activity

import android.app.ActivityManager
import android.content.Intent
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

class SecondActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val intent = Intent(this, ThirdActivity::class.java)
//
//        setContent {
//            MiniProgramTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(text = "这是第二页")
//                        Button(onClick = {
//                            startActivity(intent)
//                        }) {
//                            Text(text = "跳转至第三页")
//                        }
//                    }
//                }
//            }
//        }
        setCustomTaskDescription()
    }

    private fun setCustomTaskDescription() {
        val taskDescription = ActivityManager.TaskDescription(
            "SecondActivity",
            BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
        )
        setTaskDescription(taskDescription)
    }
}