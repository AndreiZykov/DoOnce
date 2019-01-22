package zykov.andrii.org.doonce

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.VisibleForTesting
import java.util.*


class DoOnceImpl : DoOnce {

    companion object {
        private const val SHARED_PREFERENCE_NAME_STRING = "org.do.once.preference"
        private const val PER_VERSION_NAME_STRING_IDENTIFIER = "PER_VERSION_NAME_STRING_IDENTIFIER"
        private var instance: DoOnceImpl? = null
        internal fun get(): DoOnce {
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

    private val mStateHash = HashMap<String, DoOnceState>()

    override fun perVersion(ctx: Context, runnable: Runnable) {
        ctx.packageManager.getPackageInfo(ctx.packageName, 0)?.versionName?.let { verionName ->
            val identifier = "$PER_VERSION_NAME_STRING_IDENTIFIER:$verionName"
            val performedPerThisVersion = ctx.getSharedPreferences(
                SHARED_PREFERENCE_NAME_STRING,
                Context.MODE_PRIVATE
            ).getBoolean(identifier, false)
            if (!performedPerThisVersion) {
                val pref = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME_STRING, Context.MODE_PRIVATE)
                pref.edit().putBoolean(identifier, true).apply()
                runnable.run()
            }
        }
    }

    override fun perVersion(ctx: Context, function: (Unit) -> Unit) {
        ctx.packageManager.getPackageInfo(ctx.packageName, 0)?.versionName?.let { verionName ->
            val identifier = "$PER_VERSION_NAME_STRING_IDENTIFIER:$verionName"
            val performedPerThisVersion = ctx.getSharedPreferences(
                SHARED_PREFERENCE_NAME_STRING,
                Context.MODE_PRIVATE
            ).getBoolean(identifier, false)
            if (!performedPerThisVersion) {
                val pref = ctx.getSharedPreferences(SHARED_PREFERENCE_NAME_STRING, Context.MODE_PRIVATE)
                pref.edit().putBoolean(identifier, true).apply()
                function(Unit)
            }
        }
    }

    override fun perAppRunning(@NonNull tag: String, runnable: Runnable) {
        perAppRunning(tag, System.currentTimeMillis(), runnable)
    }

    override fun perAppRunning(tag: String, currentTime: Long, runnable: Runnable) {
        var run = true
        synchronized(mStateHash) {
            if (!mStateHash.containsKey(tag))
                mStateHash[tag] = DoOnceState(currentTime, 0, false)
            else
                run = false
        }
        run.takeIf { run }?.run { runnable.run() }
    }

    override fun perAppRunning(tag: String, function: (Unit) -> Unit) {
        perAppRunning(tag, System.currentTimeMillis(), function)
    }

    override fun perAppRunning(tag: String, currentTime: Long, function: (Unit) -> Unit) {
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

    override fun perTimeInterval(tag: String, currentTime: Long, interval: Long, runnable: Runnable) {
        mStateHash[tag]?.let {
            if (it.repeat && (currentTime - it.timestamp) >= it.interval) {
                it.timestamp = currentTime; runnable.run()
            }
        } ?: run { runnable.run(); mStateHash[tag] = DoOnceState(currentTime, interval, true) }
    }

    override fun perTimeInterval(@NonNull tag: String, interval: Long, function: (Unit) -> Unit) {
        perTimeInterval(tag, System.currentTimeMillis(), interval, function)
    }

    override fun perTimeInterval(tag: String, currentTime: Long, interval: Long, function: (Unit) -> Unit) {
        mStateHash[tag]?.let {
            if (it.repeat && (currentTime - it.timestamp) >= it.interval) {
                it.timestamp = currentTime; function(Unit)
            }
        } ?: run { function(Unit); mStateHash[tag] = DoOnceState(currentTime, interval, true) }
    }

    @VisibleForTesting
    private class DoOnceState(var timestamp: Long, val interval: Long, val repeat: Boolean)
}