package com.nullo.openrouterclient.presentation.chat.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.nullo.openrouterclient.databinding.ItemQueryBinding
import com.nullo.openrouterclient.domain.entities.Message

class UserQueryViewHolder(
    private val binding: ItemQueryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: Message) {
        binding.tvText.text = message.text
    }
}
