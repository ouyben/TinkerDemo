# TinkerDemo
tinker热修复demo

#### 执行步骤
1. 将app.gradle中的==def bakPath =file("${buildDir}/bakApk/app-debug-0531-15-41-35")== 的bakapk/后面一串字符串删除掉
2. 点击as右侧gradle按钮, 点击app中的build中的assembleDebug, 发布debug版本
3. 在项目中的app\build\bakApk中发现发布的app, 复制名称
4. 将复制好的名称粘贴到==def bakPath = file("${buildDir}/bakApk/app-debug-0531-15-41-35")== bakApk/后面,然后修复app.
5. 再次点击as右侧的tinker中的tinkerPatchDebug按钮, 发布debug补丁
6. 补丁在项目app\build\outputs\tinkerPatch中.
7. 然后按照debug基准app, 把Patch复制到手机根目录下, 打开app点击加载app, 当加载成功后,重启app即可完成操作
