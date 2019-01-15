# Xlogger
### 1. 下载源码：git clone https://github.com/Tencent/mars.git
### 2. 构建本地cmake version 3.13.2和android-ndk-r16b本地环境
### 3. 编译 进入源码目录 python build_android.py
### 4. 混淆

```java

-keep class com.tencent.mars.** {
  public protected private *;
}

```

### 解密python2.7环境
    1. 文件未做加密，执行：python decode_mars_nocrypt_log_file.py
    2. 文件做了加密（初始化时pubKey传入了值，值需要和decode_mars_crypt_log_file.py中PUB_KEY值相同）
       执行：python decode_mars_crypt_log_file.py
### 优势
   1. 低内存、低CPU：性能优势大，不占内存CPU
   2. 功能丰富：与原生Log使用几乎一致，但增加了写入文件功能，同时自带加密
   3. https://mp.weixin.qq.com/s/cnhuEodJGIbdodh0IxNeXQ
### 方案:
   1. 使用流式压缩方式对单行日志进行压缩，压缩加密后写进作为 log 中间 buffer的 mmap 中，
      当 mmap 中的数据到达一定大小后再写进磁盘文件中