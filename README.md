# DrawerLayout

<img src="docs/drawer-layout-logo.svg" alt="DrawerLayout Logo" width="180">

[![](https://jitpack.io/v/huminted/DrawerLayout.svg)](https://jitpack.io/#huminted/DrawerLayout)

A lightweight and easy-to-integrate Android drawer component for side-slide layouts. `DrawerLayout` provides gesture-driven navigation with simple APIs for custom content, programmatic control, swipe gestures, and customizable drawer behavior.

## Installation

### 1. Add JitPack to your project

In `settings.gradle.kts`:

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

For Groovy DSL:

```groovy
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

### 2. Add the dependency

```kotlin
dependencies {
    implementation("com.github.huminted:drawerLayout:0.0.8")
}
```

> Check the [JitPack release page](https://jitpack.io/#huminted/DrawerLayout) for the latest available version.

## Quick Start

### XML declaration

`DrawerLayout` is a container. Its main content and drawer content can be set dynamically from Kotlin:

```xml
<?xml version="1.0" encoding="utf-8"?>
<cn.iwakeup.slidedrawer.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### Configure the drawer in Kotlin

```kotlin
val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

drawer.setMainContent(mainContent)
drawer.setDrawerContent(getDrawerContent(this))
drawer.setDrawerWidth(300)
```

`setDrawerWidth(300)` uses dp as the input unit. The default drawer width is `300dp`.

### Open and close the drawer

```kotlin
// Open
drawer.openDrawer()

// Close
drawer.closeDrawer()
```

## Common API

| API | Description |
| --- | --- |
| `setMainContent(view)` | Sets the main content view. |
| `setDrawerContent(view)` | Sets the drawer content view. |
| `setDrawerWidth(widthDp)` | Sets the drawer width in dp. |
| `openDrawer()` | Opens the drawer with animation. |
| `closeDrawer()` | Closes the drawer with animation. |
| `setDrawerSwipeable(enabled)` | Enables or disables swipe gestures. |
| `setDimmingViewClickable(clickable)` | Enables or disables closing by tapping the dim background. |
| `addDrawerListener(listener)` | Registers a drawer progress listener. |
| `getDrawer()` | Returns the underlying `SlideDrawer` instance. |

## Listen to drawer progress

`SlideDrawer.Listener#onProgress` is called while the drawer is moving. The progress value ranges from `0f` to `1f`:

```kotlin
import cn.iwakeup.slidedrawer.SlideDrawer

drawer.addDrawerListener(object : SlideDrawer.Listener {
    override fun onProgress(progress: Float) {
        // 0f = fully closed, 1f = fully opened
        progressText.text = "Open Progress:\n$progress"
    }
})
```

## Enable or disable swipe gestures

```kotlin
// Disable swipe gestures
drawer.setDrawerSwipeable(false)

// Re-enable swipe gestures
drawer.setDrawerSwipeable(true)
```

## Example usage from the sample app

The repository contains a sample app under the `app` module. The example activity demonstrates how to configure the drawer, toggle its state, enable or disable swipe gestures, and observe the opening progress.

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
                // Update the UI based on progress
            }
        })
    }
}
```

This example is based on:

- `app/src/main/java/cn/iwakeup/slidedrawer/example/MainActivity.kt`
- `app/src/main/res/layout/app.xml`

> Replace `openOrCloseButton`, `enableOrDisableButton`, `getBlankFrameLayout`, and `getDrawerContent` with your own views and content creation logic.


## Notes

- `setDrawerWidth` accepts dp values and internally converts them to px.
- `setMainContent` and `setDrawerContent` replace the existing content in their respective containers.
- The drawer closes when the dimmed background is tapped by default.
- To disable this behavior, call `setDimmingViewClickable(false)`.
- If the main content contains horizontally scrollable views, test gesture interactions carefully.

## License
```
Copyright (c) 2026 huminted

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
