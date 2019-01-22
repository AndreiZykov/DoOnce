# DoOnce

under development

open source android library that helps to run chunks of code one per application session, per time interval, version, etc

```
var index = 0
val doOnce = DoOnceImpl.get()
repeat(10) { doOnce.perAppRunning(ONCE_PER_APP_2_TAG, CURRENT_TIME) { index++ } }
assertEquals(1, index)
```

```
var index = 0
val doOnce = DoOnceImpl.get()
repeat(5) { doOnce.perTimeInterval(ONCE_PER_5_SECONDS_2_TAG, CURRENT_TIME, FIVE_SECONDS) { index++ } }
doOnce.perTimeInterval(ONCE_PER_5_SECONDS_2_TAG, FIVE_SECONDS, FIVE_SECONDS) { index++ }
assertEquals(2, index)
```

``` 
var index = 0
val doOnce = DoOnceImpl.get()
repeat(5){ doOnce.perVersion(context){ index++ } }
assertEquals(1, index)
```
