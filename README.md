# DoOnce

Small Android library (api 14+) that helps to run chunks of code once per application run, time interval, version

## Usage

to perform task once per time interval
```
DoOnce.get().perTimeInterval(UNIQUE_STRING_IDENTIFIER, INTERVAL_IN_MILLISECONDS) {                  
  // do something
}
```
