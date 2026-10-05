package cn.iwakeup.slidedrawer.gesture

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import cn.iwakeup.slidedrawer.DrawerLayout

class DrawerTriggerBehavior(
    context: Context,
    val drawerGestureDetector: DrawerGestureDetector,
    val overlayGestureDetector: OverlayGestureDetector,
    val onTouchEventStart: (ev: MotionEvent) -> Unit = {},
    val onTouchEventEnd: (ev: MotionEvent) -> Unit = {}
) :
    CoordinatorLayout.Behavior<FrameLayout>(context, null) {

    private var gesturableSlideDrawer: GesturableSlideDrawer? = null
    private var nestedViewCanScrollToRight = false


    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: FrameLayout,
        ev: MotionEvent
    ): Boolean {

        var state = GesturableSlideDrawer.DrawerState.HIDDEN
        var drawerWidth = 0

        if (parent is DrawerLayout) {
            gesturableSlideDrawer = parent.getDrawer()
            state = gesturableSlideDrawer?.getDrawerState() ?: state
            drawerWidth = parent.getDrawer().getDrawerWidth()
        }

        // if it is a clicking overlay gesture, no need to check if it is drawer gesture
        if (state == GesturableSlideDrawer.DrawerState.EXPANDED && drawerWidth > 0) {
            if (overlayGestureDetector.onTouchEvent(ev, drawerWidth)) return true

        }

        val intercepted = drawerGestureDetector.onInterceptTouchEvent(
            ev,
            nestedViewCanScrollToRight,
            state
        )

        return intercepted
    }


    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: FrameLayout,
        ev: MotionEvent
    ): Boolean {
        onTouchEventStart(ev)
        val result = drawerGestureDetector.onTouchEvent(ev, gesturableSlideDrawer)
        onTouchEventEnd(ev)
        return result
    }


    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FrameLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        val isHorizontal = axes == ViewCompat.SCROLL_AXIS_HORIZONTAL
        if (!isHorizontal) {
            return false
        }
        nestedViewCanScrollToRight = target.canScrollHorizontally(-1)

        return super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }


}