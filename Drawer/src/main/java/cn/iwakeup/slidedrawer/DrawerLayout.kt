package cn.iwakeup.slidedrawer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.view.updateLayoutParams


fun prepareDrawerContainer(context: Context): SlideDrawer {
    return SlideDrawer(context, null).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }
}

fun prepareMainContainer(context: Context, gestureDelegate: GestureDelegate): ViewGroup {
    return FrameLayout(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        ).apply {
            behavior = DrawerTriggerBehavior(context, gestureDelegate)
        }
    }
}

fun prepareDimmingView(context: Context): View {
    return View(context, null).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(Color.BLACK)
        alpha = 0f

    }
}

class DrawerLayout(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout(context, attrs), SlideDrawer.Listener, View.OnClickListener {

    companion object {
        const val DEFAULT_DRAWER_WIDTH = 300
    }

    private val velocityTracker: VelocityTracker = VelocityTracker.obtain()
    private val scrollSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val gestureDelegate = GestureDelegate(velocityTracker, scrollSlop)
    private var drawerWidth = toPx(context, DEFAULT_DRAWER_WIDTH)
    private val drawer = prepareDrawerContainer(context)
    private val mainContainer = prepareMainContainer(context, gestureDelegate)
    private val dimmingView = prepareDimmingView(context)

    private var drawerProgressListener: SlideDrawer.Listener? = null


    init {
        addView(mainContainer)
        addView(dimmingView)
        addView(drawer)
        drawer.listener = this
        setDimmingViewClickable(true)
    }


    fun openDrawer() {
        drawer.setDrawerExpanded()
    }

    fun closeDrawer() {
        drawer.setDrawerHidden()
    }

    fun setDrawerSwipeable(swipeable: Boolean) {
        gestureDelegate.enabled = swipeable
    }

    fun setDimmingViewClickable(clickable: Boolean) {
        if (clickable) {
            dimmingView.setOnClickListener(this)
        } else {
            dimmingView.setOnClickListener(null)
        }
    }


    fun addDrawerListener(listener: SlideDrawer.Listener) {
        drawerProgressListener = listener
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


    fun setDrawerContent(drawerContent: View) {
        drawer.removeAllViews()
        drawer.setDrawerContent(drawerContent)
    }

    fun setMainContent(mainContent: View) {
        mainContainer.removeAllViews()
        mainContainer.addView(
            mainContent,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    fun getDrawer(): SlideDrawer {
        return drawer
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        gestureDelegate.release()
        drawerProgressListener = null
        drawer.listener = null
    }

    override fun onProgress(progress: Float) {
        drawerProgressListener?.onProgress(progress)
        dimmingView.alpha = progress * 0.3f
        dimmingView.isClickable = progress == 1f

    }

    override fun onClick(v: View) {
        if (v == dimmingView) {
            closeDrawer()
        }
    }


}

