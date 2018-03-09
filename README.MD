# SimpleClass

[![Android version](https://img.shields.io/badge/Android-5.0%2B-brightgreen.svg)](https://github.com/Icear/SimpleClass/release/latest/) [![Travis](https://api.travis-ci.org/Icear/SimpleClass.svg?branch=master)](https://travis-ci.org/Icear/SimpleClass#) [![GitHub release](https://img.shields.io/github/release/Icear/SimpleClass.svg)](https://github.com/Icear/SimpleClass/releases/latest/)



> 能够将学校教务处的课程表等信息导入至手机日历中
>- 当前适配：
    - 东北林业大学

## 功能

- [x] 支持导入至指定日历或创建单独的日历并导入（创建单独日历时可以通过隐藏该日历）
- [x] 自动匹配对应教室的上课时间
- [x] 支持选择要加入的课程（其实就是删除）
- [x] 支持完全清除已导入的日历事件
- [ ] 在课程导入前进行修改
- [ ] 允许自定义导入的事件的格式
- [ ] 在课程变化之后支持更新事件
- [ ] 支持英语

>- 日历导入之后可能默认是不显示状态(需要去自己的日历中手动打开该日历的显示)
>- 可以将日历导入至Google Schedule之类支持同步的日历，能够实现多终端同步
>- 课程事件默认没有添加提醒，有的日历APP会自带提醒功能，为了避免重复没有加入
>- 不同的手机日历APP显示效果可能不一样
	
## 安装
- 在[release](https://github.com/Icear/SimpleClass/releases/latest/)中下载最新版本进行安装


## 使用

- 使用时输入教务处的身份验证信息（即账号&密码），**程序不会保存您的密码**，所以每一次使用时均需要手动输入
- 在课程卡片上右划以删去该课程
- 点击课程卡片可查看详细信息
- 上课时间表由项目维护，暂时无法自定义，如遇某处上课时间不准确或缺失，请与我们反馈（issue、邮件等方式）

## 参与到项目中

### 开发环境

- Android Studio 3.0.1+
- Gradle 4.6+

### 引入项目

- 在Android Studio中引入
    > 在Welcome界面或项目界面->VCS,选择"Check out project from Version Control",根据提示操作即可

- 通过 git bash 引入
    > - git clone https://github.com/Icear/SimpleClass/releases
    > - 在Android Studio中选择Open an existing Android Studio project 
    > - 根据提示操作

### 项目细节

- APP采用了MVP模式，单Activity多Fragment
- 将获取课程数据的部分使用接口分离,想要适配新的目标，只需实现`indi.github.icear.simpleclass.data.academicdata.AcademicDataHelper`及相关接口
- 时间表数据独立放置在timeSchedule.json中，采用json，格式参考项目中正在使用的timeSchedule.json文件
- 版权问题，上传的源代码中不包含林大的校徽图片，同时在fragment_login.xml中注释了对应引用，不影响代码逻辑

TAG：三角形轮子
