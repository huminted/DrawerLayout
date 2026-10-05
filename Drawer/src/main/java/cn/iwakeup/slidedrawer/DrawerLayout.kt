package cn.iwakeup.slidedrawer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import cn.iwakeup.slidedrawer.gesture.DrawerGestureDetector
import cn.iwakeup.slidedrawer.gesture.DrawerTriggerBehavior
import cn.iwakeup.slidedrawer.gesture.OverlayGestureDetector


fun prepareDrawerContainer(context: Context): SlideDrawer {
    return SlideDrawer(context, null).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }
}

fun prepareMainContainer(
    context: Context,
    drawerGestureDetector: DrawerGestureDetector,
    overlayGestureDetector: OverlayGestureDetector,
    onTouchEventStart: (ev: MotionEvent) -> Unit,
    onTouchEventEnd: (ev: MotionEvent) -> Unit
): ViewGroup {
    return FrameLayout(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        ).apply {
            behavior = DrawerTriggerBehavior(
                context, drawerGestureDetector, overlayGestureDetector,
                onTouchEventStart, onTouchEventEnd
            )
        }
    }
}

class DrawerLayout(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout(context, attrs), SlideDrawer.Listener {
    interface DrawerListener : SlideDrawer.Listener {
        fun onStart(ev: MotionEvent)
        fun onEnd(ev: MotionEvent)

    }

    companion object {
        const val DEFAULT_DRAWER_WIDTH = 300
    }

    private var drawerProgressListener: DrawerListener? = null
    private val velocityTracker: VelocityTracker = VelocityTracker.obtain()
    private val scrollSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val drawerGestureDetector = DrawerGestureDetector(velocityTracker, scrollSlop)
    private val overlayGestureDetector = OverlayGestureDetector(context, {
        closeDrawer()
    })
    private var drawerWidth = toPx(context, DEFAULT_DRAWER_WIDTH)
    private val drawer = prepareDrawerContainer(context)
    private val mainContainer = prepareMainContainer(
        context,
        drawerGestureDetector,
        overlayGestureDetector,
        { ev ->
            drawerProgressListener?.onStart(ev)
        },
        { ev ->
            drawerProgressListener?.onEnd(ev)
        }
    )
    private val overlayDrawable = Color.BLACK.toDrawable().apply {
        alpha = 0
    }


    init {
        addView(mainContainer)
        addView(drawer)

        mainContainer.doOnLayout {
            overlayDrawable.setBounds(0, 0, mainContainer.width, mainContainer.height)
            mainContainer.overlay.add(overlayDrawable)
        }
    }


    fun openDrawer() {
        drawer.setDrawerExpanded()
    }

    fun closeDrawer() {
        drawer.setDrawerHidden()
    }

    fun setDrawerSwipeable(swipeable: Boolean) {
        drawerGestureDetector.enabled = swipeable
    }


    fun addDrawerListener(listener: DrawerListener) {
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


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        drawer.listener = this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        drawerGestureDetector.release()
        drawerProgressListener = null
        drawer.listener = null
    }


    override fun onProgress(progress: Float) {
        drawerProgressListener?.onProgress(progress)
        overlayDrawable.alpha = (255f * progress * 0.5).toInt()
    }

}

