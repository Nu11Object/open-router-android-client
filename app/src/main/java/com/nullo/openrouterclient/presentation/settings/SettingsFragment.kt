package com.nullo.openrouterclient.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nullo.openrouterclient.databinding.FragmentSettingsBinding
import com.nullo.openrouterclient.di.ViewModelFactory
import com.nullo.openrouterclient.presentation.OpenRouterClientApp
import com.nullo.openrouterclient.presentation.MainViewModel
import javax.inject.Inject

class SettingsFragment : BottomSheetDialogFragment() {

    private val component by lazy {
        (requireActivity().application as OpenRouterClientApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by activityViewModels {
        viewModelFactory
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding ?: throw NullPointerException(
            "FragmentSettingsBinding is null."
        )

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setupClickListeners()
    }

    private fun observeViewModel() {
        viewModel.apiKey.observe(viewLifecycleOwner) {
            binding.etApiKey.apply {
                setText(it)
                requestFocus()
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            binding.btnClearChat.isEnabled = it.isNotEmpty()
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnSaveKey.setOnClickListener {
                val apiKey = etApiKey.text.toString().trim()
                viewModel.setApiKey(apiKey)
            }

            btnClearChat.setOnClickListener {
                viewModel.clearChat()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "SettingsBottomSheet"

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
