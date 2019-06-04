# XLogger
基于微信Mars：https://github.com/Tencent/mars.git
## Features
 - 低内存、低CPU：性能优势大，不占内存CPU
 - 功能丰富：与原生Log使用几乎一致，但增加了写入文件功能，同时自带加密

   
## Installation
### Gradle Dependency

#####   Add the library to your project build.gradle

```gradle
compile 'com.joybar.xlog:library:1.0.6'

```

## Sample Usage

### 使用


#### 1. 在Application中初始化
```java
 
L.init(LogUtils.LOG_CACHE_PATH, LogUtils.LOG_PATH, LogUtils.logFileName(), LogUtils.PUB_KEY, LogUtils.CONSOLE_LOG_OPEN);

```

#### 2. 使用
```java
 
L.i(TAG, "writeLog_info: " + i);
L.d(TAG, "writeLog_debug: " + i);
L.w(TAG, "writeLog_warning: " + i);
L.e(TAG, "writeLog_error: " + i);
```

#### 3. 在app退出时或者后台调用
```java
L.appenderClose();

```

#### 4. 混淆


```java

-keep class com.tencent.mars.** {
  public protected private *;
}

```

或者

```java

-keep class com.tencent.mars.xlog.** { *; }
-keep class com.tencent.mars.comm.* { *; }
-keep class com.tencent.mars.app.* { *; }
-keep class com.tencent.mars.stn.* {*;}
```


#### 5. 其它使用方法详见demo注释

## License

    Copyright 2018 MyJoybar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.   