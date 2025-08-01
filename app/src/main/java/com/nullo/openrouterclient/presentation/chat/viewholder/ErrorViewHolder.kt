package com.nullo.openrouterclient.presentation.chat.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.nullo.openrouterclient.databinding.ItemErrorBinding
import com.nullo.openrouterclient.domain.entities.Message.Error

class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(error: Error) {
        with(binding) {
            tvHeader.text = error.header
            tvText.text = error.text
        }
    }
}
