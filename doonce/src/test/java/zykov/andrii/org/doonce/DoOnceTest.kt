package zykov.andrii.org.doonce

import junit.framework.Assert.assertEquals
import org.junit.Test

class DoOnceTest {

    companion object {
        private const val ONCE_PER_APP_1_TAG = "ONCE_PER_APP_1_TAG"
        private const val ONCE_PER_APP_2_TAG = "ONCE_PER_APP_2_TAG"
        private const val ONCE_PER_5_SECONDS_1_TAG = "ONCE_PER_5_SECONDS_1_TAG"
        private const val ONCE_PER_5_SECONDS_2_TAG = "ONCE_PER_5_SECONDS_2_TAG"
        private const val INTERVAL_5_SECONDS = 5000L
        private const val CURRENT_TIME = 0L
    }

    @Test
    fun performOncePerApplication_withRunnable_shouldIncrementIndexJustOnce() {
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(10) {
            doOnce.perAppRunning(ONCE_PER_APP_1_TAG, CURRENT_TIME, Runnable {
                index++
            })
        }
        assertEquals(1, index)
    }

    @Test
    fun performOncePerApplication_inlineFunction_shouldIncrementIndexJustOnce() {
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(10) { doOnce.perAppRunning(ONCE_PER_APP_2_TAG, CURRENT_TIME) { index++ } }
        assertEquals(1, index)
    }

    @Test
    fun performOncePerTimeInterval_shouldCall5TimesInRowViolatingInterval_shouldOnlyBeCalledOnce(){
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(5) { doOnce.perTimeInterval(ONCE_PER_5_SECONDS_1_TAG, CURRENT_TIME, INTERVAL_5_SECONDS) { index++ } }
        assertEquals(1, index)
    }

    @Test
    fun performOncePerTimeInterval_shouldCall5TimesInRowViolatingIntervalAnd1ExtraThatRightAfterInterval_shouldBeCalledTwice(){
        var index = 0
        val doOnce = DoOnceImpl.get()
        repeat(5) { doOnce.perTimeInterval(ONCE_PER_5_SECONDS_2_TAG, CURRENT_TIME, INTERVAL_5_SECONDS) { index++ } }
        doOnce.perTimeInterval(ONCE_PER_5_SECONDS_2_TAG, INTERVAL_5_SECONDS, INTERVAL_5_SECONDS) { index++ }
        assertEquals(2, index)
    }

}