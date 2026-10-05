package cn.iwakeup.slidedrawer.gesture

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent

class OverlayGestureDetector(
    context: Context,
    onClick: () -> Unit
) {

    private var drawerWidth = 0
    val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val isClickedOnOverlay = e.x > drawerWidth
            if (isClickedOnOverlay) {
                onClick()
            }


            return isClickedOnOverlay
        }
    })


    fun onTouchEvent(ev: MotionEvent, drawerWidth: Int): Boolean {
        this.drawerWidth = drawerWidth
        return detector.onTouchEvent(ev)
    }
}