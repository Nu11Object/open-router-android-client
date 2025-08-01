package com.nullo.openrouterclient.presentation.aimodels

import androidx.recyclerview.widget.DiffUtil
import com.nullo.openrouterclient.domain.entities.AiModel

class AiModelDiffUtilItemCallback : DiffUtil.ItemCallback<AiModel>() {

    override fun areItemsTheSame(oldItem: AiModel, newItem: AiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AiModel, newItem: AiModel): Boolean {
        return oldItem == newItem
    }
}
