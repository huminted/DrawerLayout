# DrawerLayout

一个轻量、易集成的 Android 侧滑抽屉组件，支持手势滑动、代码控制打开/关闭、抽屉宽度配置、抽屉滑动进度监听，以及自定义主内容和抽屉内容。

[![JitPack](https://jitpack.io/v/huminted/DrawerLayout.svg)](https://jitpack.io/#huminted/DrawerLayout)

## 功能特性

- 支持从左侧滑出 Drawer
- 支持手势拖拽打开和关闭
- 支持通过代码打开、关闭 Drawer
- 支持自定义 Drawer 宽度
- 支持自定义主内容和抽屉内容
- 支持禁用或启用手势滑动
- 支持监听 Drawer 打开进度，进度范围为 `0f..1f`
- 点击遮罩层可关闭 Drawer
- 最低支持 Android API 24

## 安装

### 1. 添加 JitPack 仓库

在项目的 `settings.gradle.kts` 中添加 JitPack 仓库：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

如果项目使用的是 Groovy DSL，则添加：

```groovy
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

### 2. 添加依赖

```kotlin
dependencies {
    implementation("com.github.huminted:drawerLayout:0.0.8")
}
```

> 版本号请以 [JitPack 发布版本](https://jitpack.io/#huminted/DrawerLayout) 为准。

## 快速开始

### XML 中声明 DrawerLayout

`DrawerLayout` 本身是一个容器，不需要在 XML 中直接放置主内容或抽屉内容，这些内容可以在 Kotlin 代码中动态设置：

```xml
<?xml version="1.0" encoding="utf-8"?>
<cn.iwakeup.slidedrawer.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### Kotlin 中配置内容

```kotlin
val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

drawer.setMainContent(mainContent)
drawer.setDrawerContent(getDrawerContent(this))
drawer.setDrawerWidth(300)
```

`setDrawerWidth(300)` 的单位是 dp，默认宽度也是 `300dp`。

### 打开和关闭 Drawer

```kotlin
// 打开
 drawer.openDrawer()

// 关闭
 drawer.closeDrawer()
```

## 常用 API

| API | 说明 |
| --- | --- |
| `setMainContent(view)` | 设置主内容视图 |
| `setDrawerContent(view)` | 设置 Drawer 内容视图 |
| `setDrawerWidth(widthDp)` | 设置 Drawer 宽度，单位为 dp |
| `openDrawer()` | 以动画打开 Drawer |
| `closeDrawer()` | 以动画关闭 Drawer |
| `setDrawerSwipeable(enabled)` | 启用或禁用手势滑动 |
| `setDimmingViewClickable(clickable)` | 设置点击遮罩层是否关闭 Drawer |
| `addDrawerListener(listener)` | 监听 Drawer 滑动进度 |
| `getDrawer()` | 获取底层 `SlideDrawer` 实例 |

## 监听滑动进度

`SlideDrawer.Listener#onProgress` 会在 Drawer 滑动时回调，进度范围为 `0f..1f`：

```kotlin
import cn.iwakeup.slidedrawer.SlideDrawer

drawer.addDrawerListener(object : SlideDrawer.Listener {
    override fun onProgress(progress: Float) {
        // 0f：完全关闭；1f：完全打开
        progressText.text = "Open Progress:\n$progress"
    }
})
```

## 禁用和启用手势

如果只希望通过按钮或业务逻辑控制 Drawer，可以禁用手势滑动：

```kotlin
// 禁用手势
 drawer.setDrawerSwipeable(false)

// 重新启用手势
 drawer.setDrawerSwipeable(true)
```

## 完整示例

下面的示例来自 `app` 模块中的 `MainActivity.kt`，展示了如何设置主内容、抽屉内容、抽屉宽度，以及控制 Drawer 的开关和手势：

```kotlin
package cn.iwakeup.slidedrawer.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.iwakeup.slidedrawer.DrawerLayout
import cn.iwakeup.slidedrawer.SlideDrawer

class MainActivity : AppCompatActivity() {
    private var drawerOpened = false
    private var drawerSwipeable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val mainContent = getBlankFrameLayout(this)
        val drawerContent = getDrawerContent(this)

        drawer.setMainContent(mainContent)
        drawer.setDrawerContent(drawerContent)
        drawer.setDrawerWidth(300)

        openOrCloseButton.setOnClickListener {
            if (drawerOpened) {
                drawer.closeDrawer()
            } else {
                drawer.openDrawer()
            }
            drawerOpened = !drawerOpened
        }

        enableOrDisableButton.setOnClickListener {
            drawerSwipeable = !drawerSwipeable
            drawer.setDrawerSwipeable(drawerSwipeable)
        }

        drawer.addDrawerListener(object : SlideDrawer.Listener {
            override fun onProgress(progress: Float) {
                // 根据 progress 更新 UI
            }
        })
    }
}
```

> 上面的 `openOrCloseButton`、`enableOrDisableButton`、`getBlankFrameLayout` 和 `getDrawerContent` 仅用于说明，实际项目中请替换为自己的 View 和内容创建逻辑。

## 在项目中运行示例

仓库包含一个 `app` 示例模块：

- 示例入口：`app/src/main/java/cn/iwakeup/slidedrawer/example/MainActivity.kt`
- 示例布局：`app/src/main/res/layout/app.xml`
- Library 模块：`Drawer`

使用 Android Studio 打开项目后，运行 `app` 模块即可查看完整效果。

## 注意事项

- `setDrawerWidth` 使用 dp 作为输入参数，组件内部会自动转换为 px。
- `setMainContent` 和 `setDrawerContent` 会替换容器中已有的对应内容。
- Drawer 默认通过点击遮罩层关闭；如需改变此行为，可调用 `setDimmingViewClickable(false)`。
- 如果主内容中包含横向可滑动控件，建议结合实际交互测试手势冲突场景。

## License

本项目暂未声明具体开源协议。使用或分发前，请先确认仓库维护者的授权范围。
