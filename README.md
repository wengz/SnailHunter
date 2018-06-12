# SnailHunter

Android工程中函数运行时间检测的Gradle插件

## 配置运行
* 运行hunterKit子项目下的uploadArchives任务生成jar包到maven`本地依赖目录`(默认为D:/local_repo)
* 运行snailHunter子项目下的uploadArchives任务生成jar包到maven`本地依赖目录`
* 在整体项目的build.gradle中添加依赖配置
```
...
buildscript {
    
    repositories {
        google()
        jcenter()
        
        //添加本地库
        maven{
            url uri('D:/local_repo')
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

allprojects {
    repositories {
        google()
        jcenter()
		
	//添加本地库
        maven{
            url uri('D:/local_repo')
        }
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

//配置时间标准，在UI线程中运行的超过该时间的函数将会被打印提示（毫秒），默认为300
snailHunter.timeCriterion = 500

//可选择关闭主线程中耗时函数的检查
//snailHunter.huntInMainThread = false

dependencies {
    //新增依赖
    implementation 'pers.wengzc:hunterKit:0.0.1'
}

...
```

* 在需要检查运行时间的函数上新增注解 @ExamineMethodRunTime,无论其运行时间的长短均会打印显示

## 示例输出

主线程中耗时函数检查到时的输出
```
06-03 16:56:22.660 13561-13561/pers.wengzc.snailhunter I/System.out: 类[pers/wengzc/snailhunter/MainActivity].函数[sleepAWhile]在UI线程中耗时较长,执行时间为(毫秒):501
    类[pers/wengzc/snailhunter/MainActivity].函数[access$000]在UI线程中耗时较长,执行时间为(毫秒):502
    类[pers/wengzc/snailhunter/MainActivity$1].函数[onClick]在UI线程中耗时较长,执行时间为(毫秒):502
```

被 @ExamineMethodRunTime 注解的函数的运行时间输出
```
06-12 15:01:34.886 21521-21521/wengzc.pers.usesnailhunter I/System.out: 类[wengzc/pers/usesnailhunter/MainActivity].函数[onCreate]执行时间为(毫秒):343
```
