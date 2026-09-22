package cn.iwakeup.slidedrawer

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View

import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class DrawerTriggerBehavior(context: Context, val gestureDelegate: GestureDelegate) :
    CoordinatorLayout.Behavior<FrameLayout>(context, null) {

    private var gesturableSlideDrawer: GesturableSlideDrawer? = null
    private var nestedViewCanScrollToRight = false

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: FrameLayout,
        ev: MotionEvent
    ): Boolean {

        var state = GesturableSlideDrawer.DrawerState.HIDDEN
        if (parent is DrawerLayout) {
            gesturableSlideDrawer = parent.getDrawer()
            state =
                gesturableSlideDrawer?.getDrawerState() ?: GesturableSlideDrawer.DrawerState.HIDDEN
        }

        val intercepted =
            gestureDelegate.onInterceptTouchEvent(
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

        return gestureDelegate.onTouchEvent(ev, gesturableSlideDrawer)
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