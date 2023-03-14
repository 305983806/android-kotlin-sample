package com.example.miniprogram.activity

import android.app.ActivityManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.miniprogram.R

class FourthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCustomTaskDescription()
    }

    private fun setCustomTaskDescription() {
        val taskDescription = ActivityManager.TaskDescription(
            "FourthActivity",
            BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
        )
        setTaskDescription(taskDescription)
    }
}