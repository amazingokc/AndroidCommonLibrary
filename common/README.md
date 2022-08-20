# ConmmonLibrary

#### 介绍
通用库介绍：

1.该库基于MVVM框架搭建，具体使用方法：
  *  具体项目的ViewModel需要自己的一个BaseViewModel实现该库的BaseViewModel实现其toLogin（）
     方法跳转到登录界面
  *  具体项目的Model需要继承该库的BaseModel,或创建自己的BaseModel继承该库的BaseModel
  *  该库的BaseActivity和BaseFragment也必须被具体项目的View基类继承,这里说的View是指Activity或
     Fragment，View通过实现onApiSuccessCallBack（）、onApiErrorCallBack（）拿到响应结果，尽量
     View需要什么数据就返回什么数据，不再在View层处理数据。该库的View基类提供showDialog（）、
     dismissDialog（）用于显示或关闭loading，其他方法可到该类查看注释。
  *  view基类的layout状态页用于显示无数据，接口请求错误等提示；toolbar作为通用标题栏
    

2.封装了网络框架（Retrofi+Rxjava）：
  *  添加了网络拦截器
  *  统一处理了http网络异常和服务器相关错误处理
  *  服务器相应数据统一返回java对象（返回BaseResponse）
     
3.添加了用户实体类UserInfoBean,UserManager类用于获取用户存取用户对象

4.SharePreferenceUtil类用于存取基本数据、删除数据（文件名为pref-file，于具体项目原有的文件同名）

5.AppManager用于管理Activity栈；ApplicationContext类用户获取Application的context、getColor（）
  、getString（）、getDrawable（）等资源

6.CommonLibrarySDKManager类用于配置应用的第三方SDK（如友盟、LeakCanary等），在完善中。

7.添加Arouter框架

8.集成个推第三方推送

9.数据埋点相关功能

10.消息中心相关接口 + 拉取消息弹窗接口及窗口DialogFrament
