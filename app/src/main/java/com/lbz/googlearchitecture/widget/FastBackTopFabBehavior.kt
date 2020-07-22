package com.lbz.googlearchitecture.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * @author: laibinzhi
 * @date: 2020-07-22 09:30
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val TAG = "FastBackTopFabBehavior"

class FastBackTopFabBehavior(context: Context, attrs: AttributeSet):FloatingActionButton.Behavior(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        Log.d(
            TAG,
            "onStartNestedScroll() called with: coordinatorLayout = $coordinatorLayout, child = $child, directTargetChild = $directTargetChild, target = $target, nestedScrollAxes = $nestedScrollAxes"
        )
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(
                    coordinatorLayout,
                    child,
                    directTargetChild,
                    target,
                    nestedScrollAxes
                )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.visibility = View.INVISIBLE
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
            child.visibility = View.VISIBLE
        }
        Log.d(
            TAG,
            "onNestedScroll() called with: coordinatorLayout = $coordinatorLayout, child = $child, target = $target, dxConsumed = $dxConsumed, dyConsumed = $dyConsumed, dxUnconsumed = $dxUnconsumed, dyUnconsumed = $dyUnconsumed, type = $type, consumed = $consumed"
        )
    }

}
