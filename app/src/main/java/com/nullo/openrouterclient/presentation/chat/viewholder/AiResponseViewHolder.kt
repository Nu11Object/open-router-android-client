package com.nullo.openrouterclient.presentation.chat.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.databinding.ItemResponseBinding
import com.nullo.openrouterclient.domain.entities.Message.AiResponse
import io.noties.markwon.Markwon

class AiResponseViewHolder(
    private val binding: ItemResponseBinding,
    private val markwon: Markwon
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: AiResponse) {
        val context = binding.root.context
        with(binding) {
            tvReasoning.text = message.reasoning ?: ContextCompat.getString(
                context,
                R.string.answer
            )
            markwon.setMarkdown(tvText, message.text)
        }
    }
}
