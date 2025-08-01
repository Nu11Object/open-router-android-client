package com.nullo.openrouterclient.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nullo.openrouterclient.databinding.ItemErrorBinding
import com.nullo.openrouterclient.databinding.ItemLoadingBinding
import com.nullo.openrouterclient.databinding.ItemQueryBinding
import com.nullo.openrouterclient.databinding.ItemResponseBinding
import com.nullo.openrouterclient.domain.entities.Message
import com.nullo.openrouterclient.presentation.chat.viewholder.AiResponseViewHolder
import com.nullo.openrouterclient.presentation.chat.viewholder.ErrorViewHolder
import com.nullo.openrouterclient.presentation.chat.viewholder.LoadingViewHolder
import com.nullo.openrouterclient.presentation.chat.viewholder.UserQueryViewHolder
import io.noties.markwon.Markwon
import javax.inject.Inject

class MessagesAdapter @Inject constructor(
    private val markwon: Markwon
) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(
                ItemLoadingBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_QUERY -> UserQueryViewHolder(
                ItemQueryBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_RESPONSE -> AiResponseViewHolder(
                ItemResponseBinding.inflate(inflater, parent, false),
                markwon
            )

            VIEW_TYPE_ERROR -> ErrorViewHolder(ItemErrorBinding.inflate(inflater, parent, false))

            else -> throw IllegalArgumentException("Unknown item view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Message.Loading -> (holder as LoadingViewHolder).bind()
            is Message.Query -> (holder as UserQueryViewHolder).bind(item)
            is Message.AiResponse -> (holder as AiResponseViewHolder).bind(item)
            is Message.Error -> (holder as ErrorViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is Message.Loading -> VIEW_TYPE_LOADING
            is Message.Query -> VIEW_TYPE_QUERY
            is Message.AiResponse -> VIEW_TYPE_RESPONSE
            is Message.Error -> VIEW_TYPE_ERROR
        }
    }

    companion object {

        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_QUERY = 1
        const val VIEW_TYPE_RESPONSE = 2
        const val VIEW_TYPE_ERROR = 3
    }
}
