package zykov.andrii.org.doonce

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import java.util.*
import java.util.concurrent.Executor


internal class DoOnceImpl : IDoOnce {

    private val mStateHash = HashMap<String, DoOnceState>()

    override fun perVersion(ctx: Context, tag: String, runnable: Runnable) {
        perVersion(ctx, tag, null, runnable)
    }

    override fun perVersion(ctx: Context, tag: String, executor: Executor?, runnable: Runnable) {
        ctx.packageManager.getPackageInfo(ctx.packageName, 0)?.versionName?.let { versionName ->
            val identifier = "$versionName:$tag"
            val sharedPref = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME_STRING, Context.MODE_PRIVATE)
            val performedPerThisVersion = sharedPref.getBoolean(identifier, false)
            if (!performedPerThisVersion) {
                sharedPref.edit().putBoolean(identifier, true).apply()
                executor?.execute(runnable) ?: run { runnable.run() }
            }
        }
    }

    override fun perVersion(ctx: Context, tag: String, function: (Unit) -> Unit) {
        ctx.packageManager.getPackageInfo(ctx.packageName, 0)?.versionName?.let { versionName ->
            val identifier = "$versionName:$tag"
            val sharedPref = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME_STRING, Context.MODE_PRIVATE)
            val performedPerThisVersion = sharedPref.getBoolean(identifier, false)
            if (!performedPerThisVersion) {
                sharedPref.edit().putBoolean(identifier, true).apply()
                function(Unit)
            }
        }
    }

    override fun perAppRun(@NonNull tag: String, runnable: Runnable) {
        perAppRun(tag, System.currentTimeMillis(), runnable)
    }

    override fun perAppRun(tag: String, executor: Executor?, runnable: Runnable) {
        perAppRun(tag, System.currentTimeMillis(), null, runnable)
    }

    override fun perAppRun(tag: String, currentTime: Long, runnable: Runnable) {
        perAppRun(tag, currentTime, null, runnable)
    }

    override fun perAppRun(tag: String, currentTime: Long, executor: Executor?, runnable: Runnable) {
        var run = true
        synchronized(mStateHash) {
            if (!mStateHash.containsKey(tag))
                mStateHash[tag] = DoOnceState(currentTime, 0, false)
            else
                run = false
        }
        run.takeIf { run }?.run {
            executor?.execute(runnable) ?: run { runnable.run() }
        }
    }

    override fun perAppRun(tag: String, function: (Unit) -> Unit) {
        perAppRun(tag, System.currentTimeMillis(), function)
    }

    override fun perAppRun(tag: String, currentTime: Long, function: (Unit) -> Unit) {
        var run = true
        synchronized(mStateHash) {
            if (!mStateHash.containsKey(tag))
                mStateHash[tag] = DoOnceState(currentTime, 0, false)
            else
                run = false
        }
        run.takeIf { run }?.run { function(Unit) }
    }

    override fun perTimeInterval(@NonNull tag: String, interval: Long, runnable: Runnable) {
        perTimeInterval(tag, System.currentTimeMillis(), interval, runnable)
    }

    override fun perTimeInterval(@NonNull tag: String, interval: Long, function: (Unit) -> Unit) {
        perTimeInterval(tag, System.currentTimeMillis(), interval, function)
    }

    override fun perTimeInterval(tag: String, currentTime: Long, interval: Long, runnable: Runnable) {
        perTimeInterval(tag, currentTime, interval, null, runnable)
    }

    override fun perTimeInterval(tag: String, interval: Long, executor: Executor?, runnable: Runnable) {
        perTimeInterval(tag, System.currentTimeMillis(), interval, null, runnable)
    }

    override fun perTimeInterval(tag: String, currentTime: Long, interval: Long, executor: Executor?, runnable: Runnable) {
        mStateHash[tag]?.let {
            if (it.repeat && (currentTime - it.timestamp) >= it.interval) {
                it.timestamp = currentTime; executor?.execute(runnable) ?: run { runnable.run() }
            }
        } ?: run {
            mStateHash[tag] = DoOnceState(currentTime, interval, true)
            executor?.execute(runnable) ?: run { runnable.run() }
        }
    }

    override fun perTimeInterval(tag: String, currentTime: Long, interval: Long, function: (Unit) -> Unit) {
        mStateHash[tag]?.let {
            if (it.repeat && (currentTime - it.timestamp) >= it.interval) {
                it.timestamp = currentTime; function(Unit)
            }
        } ?: run { function(Unit); mStateHash[tag] = DoOnceState(currentTime, interval, true) }
    }


    private class DoOnceState(var timestamp: Long, val interval: Long, val repeat: Boolean)

    companion object {
        @VisibleForTesting
        const val SHARED_PREFERENCE_NAME_STRING = "org.do.once.preference"
        private var instance: DoOnceImpl? = null
        internal fun get(): IDoOnce {
            synchronized(DoOnceImpl) {
                if (instance == null) {
                    synchronized(DoOnceImpl) {
                        instance = DoOnceImpl()
                    }
                }
            }
            return instance!!
        }
    }

}