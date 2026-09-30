package cn.iwakeup.slidedrawer

interface GesturableSlideDrawer {
    enum class SlideDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT;
    }

    fun onScrollDrawer(distanceX: Float)


    // direction left->right 1
    // direction right -> left -1
    fun onFlingDrawer(velocityX: Float, direction: SlideDirection)

    fun onGestureFinished()
}