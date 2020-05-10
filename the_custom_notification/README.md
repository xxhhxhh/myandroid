# 自定义通知服务
## 以服务为基础
通过绑定发送通知
## 以广播为基础
通过广播发送通知，动态注册广播
## 流程
创建NotificationBean对象，即通知
调用SendNotification.sendBroadCast以广播发送通知
<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;或<br>
调用SendNotification.sendWithService以NotificationBean对象为参数或者以id为参数发送通知，前提需要绑定服务并获取binder对象
## 注意
以广播启动要注册广播<br>
以服务启动要绑定服务并获取binder对象
只有服务启动可以通过id直接发送通知<br>
适用：Android5.0+
