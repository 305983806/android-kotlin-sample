package com.example.miniprogram.activity

// activity: Class<out BaseActivity>
// 等价于 Java 的 Class<? extneds BaseActivity>
class TaskStackInfo(processName: String, activityClass: Class<out BaseActivity>) {

    lateinit var processName: String
    lateinit var activityClass: Class<out BaseActivity>

    init {
        this.processName = processName
        this.activityClass = activityClass
    }

}