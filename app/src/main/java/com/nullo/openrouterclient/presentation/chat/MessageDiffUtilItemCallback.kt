package com.nullo.openrouterclient.presentation.chat

import androidx.recyclerview.widget.DiffUtil
import com.nullo.openrouterclient.domain.entities.Message

class MessageDiffUtilItemCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}
