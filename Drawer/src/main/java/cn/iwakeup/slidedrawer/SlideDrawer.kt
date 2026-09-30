package cn.iwakeup.slidedrawer

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlin.math.abs

class SlideDrawer(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs, 0),
    GesturableSlideDrawer {


    private val scroller = Scroller(context)

    init {
        setBackgroundColor(Color.CYAN)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    fun setDrawerContent(drawerContent: View) {
        if (drawerContent.parent == null) {
            removeAllViews()
            addView(drawerContent)
        }
    }

    private fun setDrawerToInitialPosition(duration: Float = 100f) {
        animate().translationX(-measuredWidth.toFloat())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(duration.toLong())
            .start()


    }


    private fun setDrawerToExpandedPosition(duration: Float = 100f) {
        animate().translationX(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(duration.toLong())
            .start()


    }


    override fun onScrollDrawer(distanceX: Float) {
        val destinationX = (translationX + distanceX)

        if (destinationX >= 0) {
            this.translationX = 0f
        } else {
            this.translationX = destinationX
        }
    }

    override fun onFlingDrawer(
        velocityX: Float,
        direction: GesturableSlideDrawer.SlideDirection
    ) {

        // LEFT_TO_RIGHT destinationX is the position drawer is expanded
        // otherwise,the position drawer is hidden
        val destinationX =
            if (direction == GesturableSlideDrawer.SlideDirection.LEFT_TO_RIGHT) 0 else -measuredWidth

        // dx = destinationX - currentX
        val dx = destinationX - translationX
        scroller.startScroll(translationX.toInt(), 0, dx.toInt(), 0)
        invalidate()
    }

    override fun onGestureFinished() {
        val currentTranslation = translationX
        val middleTranslationX = -(measuredWidth / 2)

        if (currentTranslation < middleTranslationX) {
            setDrawerToInitialPosition()
        } else {
            setDrawerToExpandedPosition()
        }
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            translationX = scroller.currX.toFloat()
            // Keep animating until the scroller stops
            postInvalidateOnAnimation()
        }
    }


}