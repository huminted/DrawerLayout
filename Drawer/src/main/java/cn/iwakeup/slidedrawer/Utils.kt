package cn.iwakeup.slidedrawer

import android.content.Context
import android.util.TypedValue


fun toPx(context: Context, dp: Int): Int {

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}