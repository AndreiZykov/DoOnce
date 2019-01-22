package zykov.andrii.org.doonce

import android.content.Context
import android.support.annotation.NonNull

interface DoOnce {
    fun perVersion(ctx: Context, runnable: Runnable)
    fun perVersion(ctx: Context, function: (Unit) -> Unit)
    fun perAppRunning(@NonNull tag: String, runnable: Runnable)
    fun perAppRunning(tag: String, function: (Unit) -> Unit)
    fun perAppRunning(@NonNull tag: String, currentTime: Long, runnable: Runnable)
    fun perAppRunning(tag: String, currentTime: Long, function: (Unit) -> Unit)
    fun perTimeInterval(@NonNull tag: String, interval: Long, runnable: Runnable)
    fun perTimeInterval(@NonNull tag: String, interval: Long, function: (Unit) -> Unit)
    fun perTimeInterval(@NonNull tag: String, currentTime: Long, interval: Long, runnable: Runnable)
    fun perTimeInterval(tag: String, currentTime: Long, interval: Long, function: (Unit) -> Unit)
}