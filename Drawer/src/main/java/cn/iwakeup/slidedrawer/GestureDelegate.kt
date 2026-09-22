package cn.iwakeup.slidedrawer

import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import kotlin.math.abs


class GestureDelegate(
    val velocityTracker: VelocityTracker,
    val scrollSlop: Int,
    var enabled: Boolean = true
) {


    private var touchDownX = 0f
    private var touchDownY = 0f
    private var lastX = 0f
    private var lastY = 0f


    fun onInterceptTouchEvent(
        event: MotionEvent,
        childCanScrollToRight: Boolean,
        drawerState: GesturableSlideDrawer.DrawerState
    ): Boolean {

        // when disabled, never intercept touch event, onTouchEvent will never been called as well
        if (!enabled) {
            return false
        }


        var intercepted = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {

                touchDownX = event.x
                touchDownY = event.y
                intercepted = false
            }

            MotionEvent.ACTION_MOVE -> {
                val currentX = event.x
                val currentY = event.y

                val isScrollingGesture = abs(touchDownX - currentX) > scrollSlop
                val isHorizontalScrolling = isHorizontalScrolling(
                    touchDownX,
                    touchDownY,
                    currentX,
                    currentY
                ) && isScrollingGesture

                if (isHorizontalScrolling) {
                    lastX = currentX
                    lastY = currentY


                    val isScrollingRight = (currentX - touchDownX) > 0

                    // when gesture is from left to right, only child is unable to scroll to right
                    // intercepted scrolling to trigger drawer
                    if (!childCanScrollToRight && isScrollingRight) {
                        intercepted = true
                    }

                    // when drawer is expanded,intercepted scrolling to trigger drawer
                    if (drawerState == GesturableSlideDrawer.DrawerState.EXPANDED) {
                        return true
                    }
                }
                lastX = currentX
                lastY = currentY
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                intercepted = false
            }

        }


        return intercepted
    }


    fun onTouchEvent(event: MotionEvent, gesturableSlideDrawer: GesturableSlideDrawer?): Boolean {
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val currentX = event.x
                val currentY = event.y
                val distanceX = currentX - lastX


                if (isHorizontalScrolling(lastX, lastY, currentX, currentY)) {

                    gesturableSlideDrawer?.onScrollDrawer(distanceX)

                    lastX = currentX
                    lastY = currentY

                    return true
                }

                lastX = currentX
                lastY = currentY
                return false
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                velocityTracker.computeCurrentVelocity(1000)
                val xVelocity = velocityTracker.xVelocity


                val isFling = abs(xVelocity) > 1000

                Log.d("MinDebug", "GestureDelegate onTouchEvent: ${xVelocity}")

                if (isFling) {
                    val direction =
                        if (xVelocity > 0) GesturableSlideDrawer.SlideDirection.LEFT_TO_RIGHT
                        else GesturableSlideDrawer.SlideDirection.RIGHT_TO_LEFT
                    gesturableSlideDrawer?.onFlingDrawer(xVelocity, direction)
                } else {
                    gesturableSlideDrawer?.onScrollFinished()
                }
                velocityTracker.clear()

                return false
            }
        }

        return false
    }

    private fun isHorizontalScrolling(oldX: Float, oldY: Float, x: Float, y: Float): Boolean {
        val dx = abs(oldX - x)
        val dy = abs(oldY - y)
        return dx > dy
    }

    fun canScrollHorizontally(view: View): Boolean {
        return view.canScrollHorizontally(1) || view.canScrollHorizontally(-1)
    }

    fun release() {
        velocityTracker.recycle()
    }
}