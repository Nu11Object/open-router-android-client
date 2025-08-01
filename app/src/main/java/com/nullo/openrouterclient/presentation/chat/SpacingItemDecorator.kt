package com.nullo.openrouterclient.presentation.chat

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(
    private val topSpacing: Int = 18,
    private val bottomSpacing: Int = 18,
    private val itemSpacing: Int = 24
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        val density = view.context.resources.displayMetrics.density
        val topSpacingPx = (topSpacing * density).toInt()
        val bottomSpacingPx = (bottomSpacing * density).toInt()
        val itemSpacingPx = (itemSpacing * density).toInt()

        when (position) {
            0 -> {
                outRect.top = topSpacingPx
                if (itemCount > 1) {
                    outRect.bottom = itemSpacingPx / 2
                } else {
                    outRect.bottom = bottomSpacingPx
                }
            }

            itemCount - 1 -> {
                outRect.top = itemSpacingPx / 2
                outRect.bottom = bottomSpacingPx
            }

            else -> {
                outRect.top = itemSpacingPx / 2
                outRect.bottom = itemSpacingPx / 2
            }
        }
    }
}
