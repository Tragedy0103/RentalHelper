package com.example.RentHelper.Controller

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color.valueOf
import android.view.View

object AnimatorController {
    interface OnAnimationAction {
        fun onAnimationAction(animation: Animator?)
    }

    class AnimatorListenerAdaptor(
        val repeatAction: OnAnimationAction?
        , val endAction: OnAnimationAction?
        , val cancelAction: OnAnimationAction?
        , val startAction: OnAnimationAction?
    ) : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            repeatAction?.onAnimationAction(animation)
        }

        override fun onAnimationEnd(animation: Animator?) {
            endAction?.onAnimationAction(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
            cancelAction?.onAnimationAction(animation)
        }

        override fun onAnimationStart(animation: Animator?) {
            startAction?.onAnimationAction(animation)
        }
    }

    class AnimatorMiddle(private val animator: Animator) {
        private var repeatAction: OnAnimationAction? = null
        private var endAction: OnAnimationAction? = null
        private var cancelAction: OnAnimationAction? = null
        private var startAction: OnAnimationAction? = null
        fun setRepeatAction(repeatAction: OnAnimationAction): AnimatorMiddle {
            this.repeatAction = repeatAction
            return this
        }

        fun setEndAction(repeatAction: OnAnimationAction): AnimatorMiddle {
            this.endAction = repeatAction
            return this
        }

        fun setCancelAction(repeatAction: OnAnimationAction): AnimatorMiddle {
            this.cancelAction = repeatAction
            return this
        }

        fun setStartAction(repeatAction: OnAnimationAction): AnimatorMiddle {
            this.startAction = repeatAction
            return this
        }

        fun build(): Animator {
            animator.addListener(
                AnimatorListenerAdaptor(
                    repeatAction,
                    endAction,
                    cancelAction,
                    startAction
                )
            )
            return animator;
        }
    }

    fun verticalMove(view: View, time: Long, first: Float, end: Float): AnimatorMiddle {
        val animator: ValueAnimator = ValueAnimator.ofFloat(first, end)
        animator.duration = time
        animator.addUpdateListener { animation ->
            val move: Float = animation.animatedValue as Float
            view.layout(view.left, move.toInt(), view.right, move.toInt() + view.height)
        }
        return AnimatorMiddle(animator)
    }

    fun horizonMove(view: View, time: Long, first: Float, end: Float): AnimatorMiddle {
        val animator: ValueAnimator = ValueAnimator.ofFloat(first, end)
        animator.duration = time
        animator.addUpdateListener { animation ->
            val move: Float = animation.animatedValue as Float
            view.layout(move.toInt(), view.bottom, move.toInt() + view.width, view.top)
        }
        return AnimatorMiddle(animator)
    }

    fun verticalMove(view: View, time: Long, end: Float): AnimatorMiddle {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(view, "translationY", end)
        animator.duration = time
        return AnimatorMiddle(animator)
    }

    fun horizonMove(view: View, time: Long, end: Float): AnimatorMiddle {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(view, "translationX", end)
        animator.duration = time
        return AnimatorMiddle(animator)
    }

    fun despir(view: View, time: Long, start: Float, end: Float): AnimatorMiddle {
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(view, "alpha", start, end)
        animator.duration = time
        return AnimatorMiddle(animator)
    }

    fun transColor(view: View, time: Long, start: Int, end: Int): AnimatorMiddle {
        val animator = ValueAnimator.ofInt(valueOf(start).toArgb(), valueOf(end).toArgb())
        animator.setEvaluator(ArgbEvaluator())
        animator.duration = time
        animator.addUpdateListener { animation ->
            val color = animation.animatedValue as Int
//            view.background.setTint(color)
            view.setBackgroundColor(color)
        }
        return AnimatorMiddle(animator)
    }
}