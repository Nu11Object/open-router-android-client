package com.nullo.openrouterclient.presentation.aimodels.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.databinding.ItemAiModelBinding
import com.nullo.openrouterclient.domain.entities.AiModel

class AiModelViewHolder(
    private val binding: ItemAiModelBinding,
    private val onModelSelected: (model: AiModel) -> Unit,
    private val onModelPinned: (model: AiModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: AiModel) {
        val context = binding.root.context

        val reasoningStringRes =
            if (model.supportsReasoning) R.string.supports_reasoning else R.string.no_reasoning_support
        val reasoningColorRes = if (model.supportsReasoning) R.color.light_purple else R.color.white
        val isFreeStringRes = if (model.freeToUse) R.string.free else R.string.paid

        val isFreeString = ContextCompat.getString(context, isFreeStringRes)
        val isSupportsReasoning = context.getString(reasoningStringRes).lowercase()
        val description = "$isFreeString, $isSupportsReasoning"

        binding.apply {
            tvModelName.text = model.name
            tvModelDescription.apply {
                text = description
                setTextColor(ContextCompat.getColor(context, reasoningColorRes))
            }
            btnSelectModel.setOnClickListener {
                onModelSelected(model)
            }
            btnPinModel.setOnClickListener {
                onModelPinned(model)
            }
        }
    }
}
