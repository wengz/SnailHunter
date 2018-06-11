# SnailHunter

Android工程中运行在`主线程`中阻塞(耗时较长)函数的检查工具。


## 配置运行
* 运行snailHunter子项目下的uploadArchives任务生成jar包(默认路径为整体项目目录下的repo文件夹)
* 在整体项目的build.gradle中添加依赖配置
```
...
buildscript {
    
    repositories {
        google()
        jcenter()
        
        //新增配置
        maven{
            url uri('/repo')
        }
    }
    
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.0'
				
        //新增配置
        classpath 'pers.wengzc:snailHunter:0.0.1'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
...
```

* 在app项目的build.gradle中应用插件
```
...
apply plugin: 'com.android.application'

//需配置在Android插件之后
apply plugin: 'pers.wengzc.snailHunter'

//配置时间标准，在UI线程中运行的超过该时间的函数将会被打印提示（毫秒）
snailHunter.timeCriterion = 500
...
```

## 示例输出
```
06-03 16:56:22.660 13561-13561/pers.wengzc.snailhunter I/System.out: 类[pers/wengzc/snailhunter/MainActivity].函数[sleepAWhile]在UI线程中耗时较长,执行时间为(毫秒):501
    类[pers/wengzc/snailhunter/MainActivity].函数[access$000]在UI线程中耗时较长,执行时间为(毫秒):502
    类[pers/wengzc/snailhunter/MainActivity$1].函数[onClick]在UI线程中耗时较长,执行时间为(毫秒):502
```

