package zykov.andrii.org.doonce

import android.content.Context
import androidx.annotation.NonNull
import java.util.concurrent.Executor

interface IDoOnce {
    /**
     * Task is going to be execute once per version.
     * @param ctx application/context/activity
     * @param tag String identifier, should be unique
     * @param runnable task to execute
     */

    fun perVersion(ctx: Context, @NonNull tag: String, runnable: Runnable)

    /**
     * Task is going to be execute once per version.
     * @param ctx application/context/activity
     * @param tag String identifier, should be unique
     * @param executor to run tesk on different threads, can be null, then current thread will be used
     * @param runnable task to execute
     */

    fun perVersion(ctx: Context, @NonNull tag: String, executor: Executor?, runnable: Runnable)

    /**
     * Task is going to be execute once per version.
     * @param ctx application/context/activity
     * @param tag String identifier, should be unique
     * @param function task to execute
     */

    fun perVersion(ctx: Context, @NonNull tag: String, function: (Unit) -> Unit)

    /**
     * Task is going to be execute once per app run.
     * @param tag String identifier, should be unique
     * @param runnable task to execute
     */
    
    fun perAppRun(@NonNull tag: String, runnable: Runnable)

    /**
     * Task is going to be execute once per app run.
     * @param tag String identifier, should be unique
     * @param executor to run tesk on different threads, can be null, then current thread will be used
     * @param runnable task to execute
     */
    
    fun perAppRun(@NonNull tag: String, executor: Executor?, runnable: Runnable)

    /**
     * Task is going to be execute once per app run.
     * @param tag String identifier, should be unique
     * @param function task to execute
     */
    
    fun perAppRun(@NonNull tag: String, function: (Unit) -> Unit)

    /**
     * Task is going to be execute once per app run.
     * @param tag String identifier, should be unique
     * @param currentTime current system time to process time interval
     * @param runnable task to execute
     */
    
    fun perAppRun(@NonNull tag: String, currentTime: Long, runnable: Runnable)

    /**
     * Task is going to be execute once per app run.
     * @param tag String identifier, should be unique
     * @param currentTime current system time to process time interval
     * @param executor to run task on different threads, can be null, then current thread will be used
     * @param runnable task to execute
     */
    
    fun perAppRun(@NonNull tag: String, currentTime: Long, executor: Executor?, runnable: Runnable)

    /**
     * Task is going to be execute once per app run.
     * @param tag String identifier, should be unique
     * @param currentTime current system time to process time interval
     * @param function task to execute
     */
    
    fun perAppRun(@NonNull tag: String, currentTime: Long, function: (Unit) -> Unit)

    /**
     * Task is going to be execute once interval
     * @param tag String identifier, should be unique
     * @param interval time interval
     * @param runnable task to execute
     */
    
    fun perTimeInterval(@NonNull tag: String, interval: Long, runnable: Runnable)

    /**
     * Task is going to be execute once interval
     * @param tag String identifier, should be unique
     * @param interval time interval
     * @param executor to run task on different threads, can be null, then current thread will be used
     * @param runnable task to execute
     */

    fun perTimeInterval(@NonNull tag: String, interval: Long, executor: Executor?, runnable: Runnable)

    /**
     * Task is going to be execute once interval
     * @param tag String identifier, should be unique
     * @param interval time interval
     * @param function task to execute
     */

    fun perTimeInterval(@NonNull tag: String, interval: Long, function: (Unit) -> Unit)

    /**
     * Task is going to be execute once interval
     * @param tag String identifier, should be unique
     * @param currentTime current system time to process time interval
     * @param interval time interval
     * @param runnable task to execute
     */

    fun perTimeInterval(@NonNull tag: String, currentTime: Long, interval: Long, runnable: Runnable)

    /**
     * Task is going to be execute once interval
     * @param tag String identifier, should be unique
     * @param currentTime current system time to process time interval
     * @param interval time interval
     * @param executor to run task on different threads, can be null, then current thread will be used
     * @param runnable task to execute
     */

    fun perTimeInterval(
        @NonNull tag: String, currentTime: Long,
        interval: Long,
        executor: Executor?,
        runnable: Runnable
    )

    /**
     * Task is going to be execute once interval
     * @param tag String identifier, should be unique
     * @param currentTime current system time to process time interval
     * @param interval time interval
     * @param function task to execute
     */

    fun perTimeInterval(@NonNull tag: String, currentTime: Long, interval: Long, function: (Unit) -> Unit)
}