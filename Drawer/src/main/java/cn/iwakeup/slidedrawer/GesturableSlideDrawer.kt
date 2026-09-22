package cn.iwakeup.slidedrawer

interface GesturableSlideDrawer {
    enum class SlideDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT;
    }

    enum class DrawerState {
        EXPANDED, HIDDEN, DRAGGLING;
    }

    fun onScrollDrawer(distanceX: Float)

    fun onScrollFinished()

    // direction left->right 1
    // direction right -> left -1
    fun onFlingDrawer(velocityX: Float, direction: SlideDirection)

    fun getDrawerState(): DrawerState
}