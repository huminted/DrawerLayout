package cn.iwakeup.slidedrawer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Scroller
import kotlin.math.abs

class SlideDrawer(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs, 0),
    GesturableSlideDrawer {


    interface Listener {
        fun onProgress(progress: Float)
    }

    private val scroller = Scroller(context)

    var listener: Listener? = null


    fun setDrawerContent(drawerContent: View) {
        if (drawerContent.parent == null) {
            addView(
                drawerContent,
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }
    }

    fun setDrawerExpanded(duration: Float = 100f) {
        fling(GesturableSlideDrawer.SlideDirection.LEFT_TO_RIGHT)
    }


    fun setDrawerHidden(duration: Float = 100f) {
        fling(GesturableSlideDrawer.SlideDirection.RIGHT_TO_LEFT)
    }


    private fun fling(direction: GesturableSlideDrawer.SlideDirection) {

        // LEFT_TO_RIGHT destinationX is the position drawer is expanded
        // otherwise,the position drawer is hidden
        val destinationX =
            if (direction == GesturableSlideDrawer.SlideDirection.LEFT_TO_RIGHT) 0 else -measuredWidth

        // dx = destinationX - currentX
        val dx = destinationX - translationX.toInt()
        scroller.startScroll(translationX.toInt(), 0, dx, 0)
        postInvalidateOnAnimation()
    }


    override fun onScrollDrawer(distanceX: Float) {
        val destinationX = (translationX + distanceX)

        if (destinationX >= 0) {
            this.translationX = 0f
        } else if (destinationX < -measuredWidth) {
            this.translationX = -measuredWidth.toFloat()
        } else {
            this.translationX = destinationX
        }

        notifyListener()
    }

    override fun onFlingDrawer(
        velocityX: Float,
        direction: GesturableSlideDrawer.SlideDirection
    ) {
        fling(direction)
    }

    override fun onScrollFinished() {
        val currentTranslation = translationX
        val middleTranslationX = -(measuredWidth / 2)

        if (currentTranslation < middleTranslationX) {
            setDrawerHidden()
        } else {
            setDrawerExpanded()
        }
    }

    override fun getDrawerState(): GesturableSlideDrawer.DrawerState {

        if (translationX == 0f) {
            return GesturableSlideDrawer.DrawerState.EXPANDED
        } else if (translationX == -measuredWidth.toFloat()) {
            return GesturableSlideDrawer.DrawerState.HIDDEN
        }

        return GesturableSlideDrawer.DrawerState.DRAGGLING
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            translationX = scroller.currX * 1f
            // Keep animating until the scroller stops
            postInvalidateOnAnimation()
        }
        notifyListener()
    }

    private fun notifyListener() {
        listener?.onProgress(getDrawerSlideProgress())
    }

    private fun getDrawerSlideProgress(): Float {

        val drawerWidth = measuredWidth.toFloat()


        // translationX is a range in (-measuredWidth,0)
        return ((drawerWidth + translationX.toInt()) / drawerWidth)
    }


}