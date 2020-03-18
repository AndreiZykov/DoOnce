package zykov.andrii.org.doonce

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import zykov.andrii.org.doonce.DoOnceImpl.Companion.SHARED_PREFERENCE_NAME_STRING
import java.util.concurrent.Executors


class DoOnceTest {

    @Test
    fun testPerformOncePerApplication_withRunnable_shouldIncrementIndexJustOnce() {
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(10) {
            doOnce.perAppRun(ONCE_PER_APP_1_TAG, CURRENT_TIME, Runnable {
                index++
            })
        }
        assertEquals(1, index)
    }

    @Test
    fun testPerformOncePerApplication_10TimesCallDuringSingleInterval_shouldIncrementIndexJustOnce() {
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(10) { doOnce.perAppRun(ONCE_PER_APP_2_TAG, CURRENT_TIME) { index++ } }
        assertEquals(1, index)
    }

    @Test
    fun testPerformOncePerApplication_10TimesCallDuringSingleIntervalWithBackgroundExecutor_shouldIncrementIndexJustOnce() {
        var index = 0
        val doOnce = DoOnceImpl.get()
        val executor = Executors.newSingleThreadExecutor()
        repeat(10) {
            doOnce.perAppRun(ONCE_PER_APP_2_TAG, CURRENT_TIME, executor,
                Runnable { index++; if (it > 9) assertEquals(1, index) })
        }
    }

    @Test
    fun testPerformOncePerTimeInterval_shouldCall5TimesInRowViolatingInterval_shouldOnlyBeCalledOnce() {
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(5) { doOnce.perTimeInterval(ONCE_PER_5_SECONDS_1_TAG, CURRENT_TIME, INTERVAL_5_SECONDS) { index++ } }
        assertEquals(1, index)
    }

    @Test
    fun testPerformOncePerTimeInterval_shouldCall5TimesInRowViolatingIntervalAnd1ExtraThatRightAfterInterval_shouldBeCalledTwice() {
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(5) { doOnce.perTimeInterval(ONCE_PER_5_SECONDS_2_TAG, CURRENT_TIME, INTERVAL_5_SECONDS) { index++ } }
        doOnce.perTimeInterval(ONCE_PER_5_SECONDS_2_TAG, INTERVAL_5_SECONDS, INTERVAL_5_SECONDS) { index++ }
        assertEquals(2, index)
    }

    @Test
    fun perVersion_called2timesPer1versionName_shouldRunOnlyOnce() {
        // before
        val packageName = "com.package.test"
        val versionName = "version-name"
        val identifier = "$versionName:$ONCE_PER_APP_1_VERSION"
        var index = 0
        val ctx = Mockito.mock(Context::class.java)
        val manager = Mockito.mock(PackageManager::class.java)
        val info = PackageInfo().apply { this.versionName = versionName }
        val sharedPref = Mockito.mock(SharedPreferences::class.java)
        val editor = Mockito.mock(SharedPreferences.Editor::class.java)
        Mockito.`when`(ctx.packageName).thenReturn(packageName)
        Mockito.`when`(ctx.packageManager).thenReturn(manager)
        Mockito.`when`(manager.getPackageInfo(packageName, 0)).thenReturn(info)
        Mockito.`when`(ctx.getSharedPreferences(SHARED_PREFERENCE_NAME_STRING, Context.MODE_PRIVATE))
            .thenReturn(sharedPref)
        Mockito.`when`(sharedPref.getBoolean(identifier, false)).thenReturn(false)
        Mockito.`when`(sharedPref.edit()).thenReturn(editor)
        Mockito.`when`(editor.putBoolean(identifier, true)).thenReturn(editor)
        // when
        DoOnceImpl.get().perVersion(ctx, ONCE_PER_APP_1_VERSION) { index++ }
        Mockito.`when`(sharedPref.getBoolean(identifier, false)).thenReturn(true)
        DoOnceImpl.get().perVersion(ctx, ONCE_PER_APP_1_VERSION) { index++ }
        // then
        assertEquals(1, index)
    }

    companion object {
        private const val ONCE_PER_APP_1_TAG = "ONCE_PER_APP_1_TAG"
        private const val ONCE_PER_APP_2_TAG = "ONCE_PER_APP_2_TAG"
        private const val ONCE_PER_5_SECONDS_1_TAG = "ONCE_PER_5_SECONDS_1_TAG"
        private const val ONCE_PER_5_SECONDS_2_TAG = "ONCE_PER_5_SECONDS_2_TAG"
        private const val ONCE_PER_APP_1_VERSION = "ONCE_PER_APP_1_VERSION"
        private const val INTERVAL_5_SECONDS = 5000L
        private const val CURRENT_TIME = 0L
    }

}