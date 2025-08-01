package com.nullo.openrouterclient.presentation.aimodels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nullo.openrouterclient.databinding.FragmentSelectModelBinding
import com.nullo.openrouterclient.di.ViewModelFactory
import com.nullo.openrouterclient.presentation.MainViewModel
import com.nullo.openrouterclient.presentation.OpenRouterClientApp
import javax.inject.Inject

class SelectModelFragment : BottomSheetDialogFragment() {

    private val component by lazy {
        (requireActivity().application as OpenRouterClientApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    private var _binding: FragmentSelectModelBinding? = null
    private val binding
        get() = _binding ?: throw NullPointerException(
            "FragmentSelectModelBinding is null."
        )

    private lateinit var pinnedAiModelsAdapter: AiModelsAdapter
    private lateinit var cloudAiModelsAdapter: AiModelsAdapter

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
        with(viewModel) {
            pinnedAiModels.observe(viewLifecycleOwner) {
                pinnedAiModelsAdapter.submitList(it)
            }

            filteredCloudAiModels.observe(viewLifecycleOwner) {
                cloudAiModelsAdapter.submitList(it)
                if (it.isNotEmpty()) {
                    binding.pbCloudLoading.visibility = View.GONE
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBrowseModels.setOnClickListener {
            hideCloudModelsPlaceholders()
            showCloudModelsSection()
            viewModel.browseCloudModels()
        }
    }

    private fun hideCloudModelsPlaceholders() {
        with(binding) {
            tvAvailableModelsDescription.visibility = View.GONE
            btnBrowseModels.visibility = View.GONE
        }
    }

    private fun showCloudModelsSection() {
        with(binding) {
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
