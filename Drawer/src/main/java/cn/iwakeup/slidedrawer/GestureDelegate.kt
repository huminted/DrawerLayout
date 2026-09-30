package cn.iwakeup.slidedrawer

import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import kotlin.math.abs


class GestureDelegate {

    private var touchDownX = 0f
    private var touchDownY = 0f
    private var lastX = 0f
    private var lastY = 0f
    private val velocityTracker: VelocityTracker = VelocityTracker.obtain()


    fun onInterceptTouchEvent(event: MotionEvent): Boolean {
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

                if (isHorizontalScrolling(touchDownX, touchDownY, currentX, currentY)) {
                    lastX = currentX
                    lastY = currentY
                    intercepted = true
                }
                lastX = currentX
                lastY = currentY
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
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                velocityTracker.computeCurrentVelocity(1000)
                val xVelocity = velocityTracker.xVelocity


                val isFling = abs(xVelocity) > 10

                if (isFling) {
                    val direction =
                        if (xVelocity > 0) GesturableSlideDrawer.SlideDirection.LEFT_TO_RIGHT
                        else GesturableSlideDrawer.SlideDirection.RIGHT_TO_LEFT
                    gesturableSlideDrawer?.onFlingDrawer(xVelocity, direction)
                } else {
                    gesturableSlideDrawer?.onGestureFinished()
                }
                velocityTracker.clear()

                return true
            }
        }

        return false
    }

    private fun isHorizontalScrolling(oldX: Float, oldY: Float, x: Float, y: Float): Boolean {
        return abs(oldX - x) > abs(oldY - y)
    }

    fun canScrollHorizontally(view: View): Boolean {
        return view.canScrollHorizontally(1) || view.canScrollHorizontally(-1)
    }

    fun release() {
        velocityTracker.recycle()
    }
}