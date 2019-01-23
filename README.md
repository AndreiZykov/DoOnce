# DoOnce

Small Android library (api 14+) that helps to run chunks of code once per application run, time interval or version

## Installation

You'll need to have jcenter() in your list of repositories

```
implementation 'org.andriizykov.doonce:doonce:0.1.01'
```

## Usage

To perform task once per time interval
```
DoOnce.get().perTimeInterval(UNIQUE_STRING_IDENTIFIER, INTERVAL_IN_MILLISECONDS) {                  
  // do something
}
```

To perform task once per time interval, on a specific thread 
```
val executor = Executors.newSingleThreadExecutor()
DoOnce.get().perTimeInterval(UNIQUE_STRING_IDENTIFIER, INTERVAL_IN_MILLISECONDS, executor, Runnable {                  
  // do something
})
```

To perform task once per application run
```
DoOnce.get().perAppRun(UNIQUE_STRING_IDENTIFIER) { 
  // do something
}
```

To perform task once per application run, on a specific thread 
```
val executor = Executors.newSingleThreadExecutor()
DoOnce.get().perAppRun(UNIQUE_STRING_IDENTIFIER, executor, Runnable { 
  // do something
})
```

To perform task once per version (per update)
```
DoOnce.get().perVersion(context, UNIQUE_STRING_IDENTIFIER)  { 
  // do something
}
```

To perform task once per version (per update), on a specific thread 
```
val executor = Executors.newSingleThreadExecutor()
DoOnce.get().perVersion(context, UNIQUE_STRING_IDENTIFIER, executor, Runnable { 
  // do something
})
```


## License

```
Copyright 2019 Andrii Zykov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
