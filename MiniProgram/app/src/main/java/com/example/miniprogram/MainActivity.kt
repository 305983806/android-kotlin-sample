package com.example.miniprogram

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.miniprogram.activity.SecondActivity
import com.example.miniprogram.activity.TaskStackManager
import com.example.miniprogram.activity.ThirdActivity
import com.example.miniprogram.ui.theme.MiniProgramTheme

class MainActivity : ComponentActivity() {
    val activities = ArrayList<ComponentActivity>(3)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiniProgramTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }

        TaskStackManager.getInstance().init()
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun Greeting(name: String) {
    val mContext = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
//            val data = "Hello world."
//            val intent = Intent(mContext, FirstActivity::class.java)
//            intent.putExtra("extra_data", data)
//            mContext.startActivity(intent)
            TaskStackManager.getInstance().startActivity(context = mContext, "https://jd.com")
        }) {
            Text(text = "京东")
        }
        Button(onClick = {
//            mContext.startActivity(Intent(mContext, SecondActivity::class.java))
            TaskStackManager.getInstance().startActivity(context = mContext, "https://tmall.com")
        }) {
            Text(text = "天猫")
        }
        Button(onClick = {
//            mContext.startActivity(Intent(mContext, ThirdActivity::class.java))
            TaskStackManager.getInstance().startActivity(context = mContext, "https://taobao.com")
        }) {
            Text(text = "淘宝")
        }
        Button(onClick = {
            TaskStackManager.getInstance().startActivity(context = mContext, "https://www.kaola.com/")
        }) {
            Text(text = "考拉海购")
        }
    }
}