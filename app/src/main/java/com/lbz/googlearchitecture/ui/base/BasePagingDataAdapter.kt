package com.lbz.googlearchitecture.ui.base

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.internal.ViewHelper

/**
 * @author: laibinzhi
 * @date: 2020-07-21 16:08
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
//参考 https://github.com/wasabeef/recyclerview-animators/blob/master/animators/src/main/java/jp/wasabeef/recyclerview/adapters/AnimationAdapter.java
enum class SlideType { SCALEIN, SCALELEFT, SCALERIGHT }

/**
 * 有滑动动画的PagingDataAdapter
 */
abstract class BasePagingDataAdapter<T : Any, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, VH>(diffCallback) {

    private var isFirstOnly = true
    private var mLastPosition = -1
    private var mDuration: Int = 300
    private val mFrom = 0f
    private var mInterpolator: Interpolator = LinearInterpolator()
    private var mSlideType = SlideType.SCALEIN

    override fun onBindViewHolder(holder: VH, position: Int) {
        val adapterPosition = holder.bindingAdapterPosition
        if (!isFirstOnly || adapterPosition > mLastPosition) {
            for (anim in getAnimators(holder.itemView)) {
                anim.setDuration(mDuration.toLong()).start()
                anim.interpolator = mInterpolator
            }
            mLastPosition = adapterPosition
        } else {
            ViewHelper.clear(holder.itemView)
        }
        funBindViewHolder(holder, position)
    }

    abstract fun funBindViewHolder(holder: VH, position: Int)

    protected open fun getAnimators(view: View): Array<ObjectAnimator> {
        return when (mSlideType) {
            SlideType.SCALEIN -> {
                val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)
                val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)
                arrayOf(scaleX, scaleY)
            }
            SlideType.SCALELEFT -> {
                arrayOf(
                    ObjectAnimator.ofFloat(
                        view,
                        "translationX",
                        -view.rootView.width.toFloat(),
                        0f
                    )
                )
            }
            SlideType.SCALERIGHT -> {
                arrayOf(
                    ObjectAnimator.ofFloat(
                        view,
                        "translationX",
                        view.rootView.width.toFloat(),
                        0f
                    )
                )
            }
        }


    }

    open fun setDuration(duration: Int) {
        mDuration = duration
    }

    open fun setInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
    }

    open fun setFirstOnly(firstOnly: Boolean) {
        isFirstOnly = firstOnly
    }

    open fun setSlideType(type: SlideType) {
        mSlideType = type
    }


}