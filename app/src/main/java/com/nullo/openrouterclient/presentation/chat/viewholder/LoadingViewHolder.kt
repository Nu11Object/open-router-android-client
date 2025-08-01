package com.nullo.openrouterclient.presentation.chat.viewholder

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.nullo.openrouterclient.databinding.ItemLoadingBinding

class LoadingViewHolder(
    private val binding: ItemLoadingBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val rotationAnimator = ObjectAnimator.ofFloat(
        binding.ivLogo,
        View.ROTATION,
        ANGLE_START,
        ANGLE_END
    ).apply {
        duration = DURATION
        interpolator = AccelerateDecelerateInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
    }

    fun bind() {
        if (!rotationAnimator.isStarted) {
            rotationAnimator.start()
        }
    }

    private companion object {

        const val ANGLE_START = 0f
        const val ANGLE_END = -360f
        const val DURATION = 1_000L
    }
}
