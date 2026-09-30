package cn.iwakeup.slidedrawer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.view.updateLayoutParams


fun prepareDrawerContainer(context: Context): SlideDrawer {
    val drawer = SlideDrawer(context, null)
    return drawer
}

fun prepareMainContainer(context: Context, gestureDelegate: GestureDelegate): ViewGroup {
    val mainContainer = FrameLayout(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        ).apply {
            behavior = DrawerTriggerBehavior(context, gestureDelegate)
        }
    }
    return mainContainer
}

class DrawerLayout(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout(context, attrs) {

    companion object {
        const val DEFAULT_DRAWER_WIDTH = 300
    }

    private var drawerWidth = toPx(context, DEFAULT_DRAWER_WIDTH)
    private val gestureDelegate = GestureDelegate()

    private val drawer = prepareDrawerContainer(context)
    private val mainContainer = prepareMainContainer(context, gestureDelegate)


    init {
        addView(mainContainer)
        addView(drawer)
    }


    fun setDrawerContent(drawerContent: View) {


        drawer.setDrawerContent(drawerContent)
    }

    fun setDrawerWidth(drawerWidth: Int) {
        val drawerWidthInPx = toPx(context, drawerWidth)
        drawer.translationX = -drawerWidthInPx.toFloat()
        drawer.updateLayoutParams {
            width = drawerWidthInPx
        }
        drawer.requestLayout()
        this.drawerWidth = drawerWidthInPx
    }

    fun setMainContent(mainContent: View) {
        if (mainContent.parent == null) {
            mainContainer.removeAllViews()
            mainContainer.addView(mainContent)
        }
    }

    fun getDrawer(): SlideDrawer {
        return drawer
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        gestureDelegate.release()

    }


}

