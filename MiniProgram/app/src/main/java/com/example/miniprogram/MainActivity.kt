package com.example.miniprogram

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.miniprogram.activity.FirstActivity
import com.example.miniprogram.activity.SecondActivity
import com.example.miniprogram.activity.ThirdActivity
import com.example.miniprogram.ui.theme.MiniProgramTheme

class MainActivity : ComponentActivity() {
    val activities = ArrayList<ComponentActivity>(3)

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

    }
}

@Composable
fun Greeting(name: String) {
    val mContext = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            val data = "Hello world."
            val intent = Intent(mContext, FirstActivity::class.java)
            intent.putExtra("extra_data", data)
            mContext.startActivity(intent)
        }) {
            Text(text = "启动第一行代码")
        }
        Button(onClick = {
            mContext.startActivity(Intent(mContext, SecondActivity::class.java))
        }) {
            Text(text = "启动第二行代码")
        }
        Button(onClick = {
            mContext.startActivity(Intent(mContext, ThirdActivity::class.java))
        }) {
            Text(text = "启动第三行代码")
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MiniProgramTheme {
        Greeting("Android")
    }
}