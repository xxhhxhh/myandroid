# 本模块实现流式布局（横向）
## 功能
### 自定义创建（代码内创建）
可自定义最大宽高，两种方式：px、dp（可按模式转换）
调用forCustom（）
### 布局中创建或使用原始构造方法
调用forDefault()
## 注意
基于kotlin实现
Android版本 >= 5.0
gradle依赖:implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
SDK>=21
