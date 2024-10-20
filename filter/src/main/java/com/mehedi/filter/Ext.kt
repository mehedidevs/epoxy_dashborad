package com.mehedi.filter


import android.animation.ValueAnimator
import android.view.View
import androidx.core.animation.addListener


fun View.expand(duration: Long = 300) {
    // Ensure the view is laid out before starting the animation
    this.post {
        // Make the view visible before animating
        this.visibility = View.VISIBLE

        // Measure the view's height
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((this.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        this.measure(matchParentMeasureSpec, wrapContentMeasureSpec)

        val targetHeight = this.measuredHeight

        // Animate from height 0 to the measured height
        val animator = ValueAnimator.ofInt(0, targetHeight).apply {
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                this@expand.layoutParams.height = value
                this@expand.requestLayout()
            }
            this.duration = duration
        }
        animator.start()
    }
}


fun View.collapse(duration: Long = 300) {
    // Ensure the view is laid out before starting the animation
    this.post {
        val initialHeight = this.measuredHeight

        // Animate from the current height to 0
        val animator = ValueAnimator.ofInt(initialHeight, 0).apply {
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                this@collapse.layoutParams.height = value
                this@collapse.requestLayout()
            }
            this.duration = duration
        }

        animator.addListener(onEnd = {
            // Set the view to GONE after collapsing
            this@collapse.visibility = View.GONE
        })
        animator.start()
    }
}




