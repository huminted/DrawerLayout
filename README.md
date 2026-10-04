# DrawerLayout

A lightweight and easy-to-integrate Android drawer component for side-slide layouts. It supports gesture drag, programmatic open/close control, adjustable drawer width, drawer progress callbacks, and custom main/drawer content.

[![](https://jitpack.io/v/huminted/DrawerLayout.svg)](https://jitpack.io/#huminted/DrawerLayout)

## Features

- Left-side drawer support
- Gesture-based drag to open and close
- Programmatic open/close control
- Custom drawer width
- Custom main content and drawer content
- Enable or disable swipe gestures
- Drawer progress listener
- Clickable dim background to close the drawer
- Minimum Android API level: 24

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

The repository contains a sample app under the `app` module. The example activity uses the following pattern:

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

## Run the sample app

Open the project in Android Studio and run the `app` module to view the drawer in action.

## Notes

- `setDrawerWidth` accepts dp values and internally converts them to px.
- `setMainContent` and `setDrawerContent` replace the existing content in their respective containers.
- The drawer closes when the dimmed background is tapped by default.
- To disable this behavior, call `setDimmingViewClickable(false)`.
- If the main content contains horizontally scrollable views, test gesture interactions carefully.

## License

This project does not currently declare a specific open-source license in the repository. Please confirm the usage terms with the project owner before distribution or commercial use.
