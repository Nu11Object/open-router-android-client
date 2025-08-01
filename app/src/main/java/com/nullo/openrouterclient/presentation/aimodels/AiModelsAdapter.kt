package com.nullo.openrouterclient.presentation.aimodels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.nullo.openrouterclient.databinding.ItemAiModelBinding
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.presentation.aimodels.viewholder.AiModelViewHolder

class AiModelsAdapter(
    private val onModelSelected: (model: AiModel) -> Unit,
    private val onModelPinned: (model: AiModel) -> Unit,
) : ListAdapter<AiModel, AiModelViewHolder>(AiModelDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AiModelViewHolder {
        val binding = ItemAiModelBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AiModelViewHolder(binding, onModelSelected, onModelPinned)
    }

    override fun onBindViewHolder(holder: AiModelViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }
}
