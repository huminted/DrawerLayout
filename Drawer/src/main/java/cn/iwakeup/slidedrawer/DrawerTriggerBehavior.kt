package cn.iwakeup.slidedrawer

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat


class DrawerTriggerBehavior(context: Context, val gestureDelegate: GestureDelegate) :
    CoordinatorLayout.Behavior<FrameLayout>(context, null) {

    private var nestedChildNeedsHorizontalScroll = false

    private var gesturableSlideDrawer: GesturableSlideDrawer? = null

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: FrameLayout,
        ev: MotionEvent
    ): Boolean {

        if (parent is DrawerLayout) {
            gesturableSlideDrawer = parent.getDrawer()
        }

        val intercepted =
            gestureDelegate.onInterceptTouchEvent(ev) && !nestedChildNeedsHorizontalScroll

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

        nestedChildNeedsHorizontalScroll =
            isHorizontal && gestureDelegate.canScrollHorizontally(target)

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