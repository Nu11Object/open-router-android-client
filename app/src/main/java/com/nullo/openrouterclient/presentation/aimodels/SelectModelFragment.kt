package com.nullo.openrouterclient.presentation.aimodels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.databinding.FragmentSelectModelBinding
import com.nullo.openrouterclient.presentation.MainViewModel
import kotlinx.coroutines.launch

class SelectModelFragment : BottomSheetDialogFragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentSelectModelBinding? = null
    private val binding
        get() = _binding ?: throw NullPointerException(
            "FragmentSelectModelBinding is null."
        )

    private lateinit var pinnedAiModelsAdapter: AiModelsAdapter
    private lateinit var cloudAiModelsAdapter: AiModelsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectModelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerViews()
        setupClickListeners()
        setupCloudModelsSearchField()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        setupRcPinnedModels()
        setupRcCloudModels()
    }

    private fun setupRcPinnedModels() {
        pinnedAiModelsAdapter = AiModelsAdapter(
            onModelSelected = {
                viewModel.selectModel(it)
                dismiss()
            },
            onModelPinned = { viewModel.unpinModel(it) }
        )
        binding.rcPinnedModels.apply {
            adapter = pinnedAiModelsAdapter
        }
    }

    private fun setupRcCloudModels() {
        cloudAiModelsAdapter = AiModelsAdapter(
            onModelSelected = {
                viewModel.selectModel(it)
                dismiss()
            },
            onModelPinned = {
                viewModel.pinModel(it)
            }
        )
        binding.rcCloudModels.apply {
            adapter = cloudAiModelsAdapter
        }
    }

    private fun setupCloudModelsSearchField() {
        binding.etModelName.addTextChangedListener {
            viewModel.filterCloudModelsByName(it.toString())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectUiState()
            }
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            binding.pbCloudLoading.isVisible = state.loadingCloudAiModels
            pinnedAiModelsAdapter.submitList(state.pinnedAiModels)
            cloudAiModelsAdapter.submitList(state.cloudAiModels)
        }
    }

    private fun setupClickListeners() {
        binding.btnBrowseModels.setOnClickListener {
            enableCloudModelsSection()
            viewModel.browseCloudModels()
        }
    }

    private fun enableCloudModelsSection() {
        with(binding) {
            tvAvailableModelsDescription.text = ContextCompat.getString(
                requireContext(),
                R.string.available_models_modality_hint,
            )
            btnBrowseModels.visibility = View.GONE

            tilModelName.visibility = View.VISIBLE
            rcCloudModels.visibility = View.VISIBLE
            pbCloudLoading.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "SelectModelBottomSheet"

        fun newInstance(): SelectModelFragment {
            return SelectModelFragment()
        }
    }
}
