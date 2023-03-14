package com.example.miniprogram.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.os.Process
import android.text.TextUtils
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread

class TaskStackManager {

    private val TAG = "TaskStackManager"
    private val MAX_PROCESS = 3

    private val taskInfo = HashMap<String, TaskStackInfo>()
    private lateinit var mainHandler: Handler

//    init{} 等价于 Java 的 构造方法
    init {
        taskInfo[FirstActivity::class.java.name] = TaskStackInfo(
            "process1", FirstActivity::class.java
        )
        taskInfo[SecondActivity::class.java.name] = TaskStackInfo(
            "process2", SecondActivity::class.java
        )
        taskInfo[ThirdActivity::class.java.name] = TaskStackInfo(
            "process3", ThirdActivity::class.java
        )
        taskInfo[FourthActivity::class.java.name] = TaskStackInfo(
            "process4", FourthActivity::class.java
        )
    }

//    单例——静态内部类式，类似于以下Java代码
//    private static class InstanceHelper {
//        static TaskStackManager instance = new TaskStackManager()
//    }
//    public static TaskStackManager getInstance() {
//        return InstanceHelper.instance;
//    }
// **************************************************************************
    object InstanceHelper {
        val instance = TaskStackManager()
    }

    companion object {
        fun getInstance() = InstanceHelper.instance
    }
// **************************************************************************

    @UiThread
    fun init() {
        mainHandler = Handler(Looper.getMainLooper())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startActivity(context: Context, url: String) {
        if (context == null) {
            return
        }

        var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var currentProcesses: List<String> = getCurrentProcesses(manager)

        doStartActivity(context, currentProcesses, url)

        // Handler 的 post 系列方法用于执行一个线程
        // 在 1000 毫秒后，执行线程 ProcessHandler()
        mainHandler?.postDelayed(Runnable {
            clearProcess(context)
        }, 1000)

    }

    private fun doStartActivity(context: Context, currentProcess: List<String>, url: String) {
        for (info in taskInfo.values) {
            var exist = false
            for (currentName in currentProcess) {
                if (TextUtils.equals(info.activityClass.name, currentName)) {
                    exist = true
                    break
                }
            }
            if (!exist) {
                var intent = Intent(context, info.activityClass)
                intent.putExtra("url", url)
                context.startActivity(intent)
                break
            }
        }
    }

    /**
     * 如果已启动了 MAX_PROCESS 个进程，start时会启动第MAX_PROCESS+1个，并清理第一个。
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun clearProcess(context: Context) {
        var manager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        var currentProcess = getCurrentProcesses(manager)

        if (currentProcess.size > MAX_PROCESS) {
            doClearProcess(manager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun doClearProcess(manager: ActivityManager) {
        var needClearTask = ArrayList<Pair<ActivityManager.AppTask, String>>()
        try {
            var appTasks = manager.appTasks
            for (appTask in appTasks) {
                if (appTask == null) {
                    continue
                }
                var componentName = appTask.taskInfo.baseActivity
                if (componentName != null && taskInfo.containsKey(componentName.className)) {
                    Log.d(TAG, "clearOldestProcess componentName:" + componentName.className);
                    var task = taskInfo[componentName.className] ?: return
                    needClearTask.add(Pair(appTask, task.processName))
                }
            }

            for (i in MAX_PROCESS until needClearTask.size) {
                var item = needClearTask[i].first
                var processName = needClearTask[i].second
                item.finishAndRemoveTask()
                killProcess(manager, processName)
            }
        } catch (e: Exception) {
            Log.e(TAG, "cleanLastTask" + e.message);
        }
    }

    private fun killProcess(manager: ActivityManager, processName: String) {
        var info = getProcessInfo(manager, processName)
        if (info != null) {
            Process.killProcess(info.pid)
        }
    }

    private fun getProcessInfo(manager: ActivityManager, processName: String): ActivityManager.RunningAppProcessInfo? {
        var runningAppProcessInfo = manager.runningAppProcesses
        try {
            for (processInfo in runningAppProcessInfo) {
                Log.d(TAG, "getProcessInfo process name: " + processInfo.processName)
                var childProcessFlag = processInfo.processName.lastIndexOf(":")
                if (childProcessFlag >= 0 && childProcessFlag < processInfo.processName.length) {
                    var process = processInfo.processName.substring(childProcessFlag, processInfo.processName.length)
                    if (process == processName) {
                        return processInfo
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getProcessInfo " + e.message)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getCurrentProcesses(manager: ActivityManager): List<String> {
        if (manager == null) {
//            return null
            //TODO
        }
        var currentProcesses = ArrayList<String>()

        try {
            var appTasks = manager.appTasks
            if (appTasks != null && appTasks.size > 0) {
                for (appTask in appTasks) {
                    if (appTasks == null) {
                        continue
                    }
                    var componentName = appTask.taskInfo.baseActivity
                    if (componentName != null && taskInfo.containsKey(componentName.className)) {
                        Log.d(TAG, "getCurrentProcessList componentName:" + componentName.className)
                        currentProcesses.add(componentName.className)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCurrentSize error:" + e.message)
        }

        return currentProcesses
    }
}