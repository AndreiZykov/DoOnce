package zykov.andrii.org.doonce

import android.content.Context
import android.support.annotation.NonNull
import java.util.concurrent.Executor

interface IDoOnce {
    fun perVersion(ctx: Context, @NonNull tag: String, runnable: Runnable)
    fun perVersion(ctx: Context, @NonNull tag: String, executor: Executor?, runnable: Runnable)
    fun perVersion(ctx: Context, @NonNull tag: String, function: (Unit) -> Unit)
    fun perAppRun(@NonNull tag: String, runnable: Runnable)
    fun perAppRun(@NonNull tag: String, executor: Executor?, runnable: Runnable)
    fun perAppRun(@NonNull tag: String, function: (Unit) -> Unit)
    fun perAppRun(@NonNull tag: String, currentTime: Long, runnable: Runnable)
    fun perAppRun(@NonNull tag: String, currentTime: Long, executor: Executor?, runnable: Runnable)
    fun perAppRun(@NonNull tag: String, currentTime: Long, function: (Unit) -> Unit)
    fun perTimeInterval(@NonNull tag: String, interval: Long, runnable: Runnable)
    fun perTimeInterval(@NonNull tag: String, interval: Long, executor: Executor?, runnable: Runnable)
    fun perTimeInterval(@NonNull tag: String, interval: Long, function: (Unit) -> Unit)
    fun perTimeInterval(@NonNull tag: String, currentTime: Long, interval: Long, runnable: Runnable)
    fun perTimeInterval(
        @NonNull tag: String, currentTime: Long,
        interval: Long,
        executor: Executor?,
        runnable: Runnable
    )

    fun perTimeInterval(@NonNull tag: String, currentTime: Long, interval: Long, function: (Unit) -> Unit)
}