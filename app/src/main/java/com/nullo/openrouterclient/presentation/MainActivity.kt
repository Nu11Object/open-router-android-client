package com.nullo.openrouterclient.presentation

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.databinding.ActivityMainBinding
import com.nullo.openrouterclient.domain.entities.AiModel
import com.nullo.openrouterclient.domain.entities.Message
import com.nullo.openrouterclient.presentation.UiEvent.ShowError
import com.nullo.openrouterclient.presentation.UiEvent.ShowMessage
import com.nullo.openrouterclient.presentation.aimodels.SelectModelFragment
import com.nullo.openrouterclient.presentation.chat.MessagesAdapter
import com.nullo.openrouterclient.presentation.chat.SpacingItemDecorator
import com.nullo.openrouterclient.presentation.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var messagesAdapter: MessagesAdapter

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom + ime.bottom
            )
            insets
        }

        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        binding.rcChat.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                setStackFromEnd(true)
            }
            adapter = messagesAdapter
            addItemDecoration(SpacingItemDecorator())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { collectUiState() }
                launch { collectUiEvents() }
            }
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            updateMessages(state.messages)
            updateCurrentAiModel(state.currentAiModel)
            updateContextButton(state.contextEnabled)
            binding.btnSendQuery.isEnabled = !state.waitingForResponse
        }
    }

    private fun updateMessages(messages: List<Message>) {
        binding.groupAppHeadlines.isVisible = messages.isEmpty()
        messagesAdapter.submitList(messages) {
            if (messages.isNotEmpty()) {
                binding.rcChat.smoothScrollToPosition(messages.size - 1)
            }
        }
    }

    private fun updateCurrentAiModel(aiModel: AiModel?) {
        aiModel?.let {
            with(binding) {
                tvModelName.text = it.name
                displayReasoningSupport(it.supportsReasoning)
            }
        }
    }

    private suspend fun collectUiEvents() {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is ShowMessage -> showMessage(getString(event.messageStringRes))
                is ShowError -> showMessage(getString(event.error.stringRes))
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnToggleContext.setOnClickListener {
                viewModel.toggleContextEnabled()
            }
            btnSendQuery.setOnClickListener {
                viewModel.sendQuery(etUserInput.text.toString().trim())
                clearInput(etUserInput)
            }
            btnSelectModel.setOnClickListener {
                launchModelsMenu()
            }
            ivSettings.setOnClickListener {
                launchSettingsMenu()
            }
        }
    }

    private fun clearInput(editText: EditText) {
        editText.apply {
            setText("")
            clearFocus()
            val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    private fun launchModelsMenu() {
        val bottomSheetDialog = SelectModelFragment.Companion.newInstance()
        bottomSheetDialog.show(supportFragmentManager, SelectModelFragment.Companion.TAG)
    }

    private fun launchSettingsMenu() {
        val bottomSheetDialog = SettingsFragment.Companion.newInstance()
        bottomSheetDialog.show(supportFragmentManager, SettingsFragment.Companion.TAG)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun displayReasoningSupport(supportsReasoning: Boolean) {
        val messageRes =
            if (supportsReasoning) R.string.supports_reasoning else R.string.no_reasoning_support
        val colorRes = if (supportsReasoning) R.color.light_purple else R.color.white

        val color = ContextCompat.getColor(this, colorRes)
        val message = getString(messageRes)

        with(binding) {
            tvSupportsReasoning.apply {
                text = message
                setTextColor(color)
            }
            tvReasoningSupportIndicator.apply {
                setTextColor(color)
                setStrikeThrough(!supportsReasoning)
            }
            ivReasoningSupport.imageTintList = ColorStateList.valueOf(color)
        }
    }

    private fun TextView.setStrikeThrough(enabled: Boolean) {
        paintFlags = if (enabled) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private fun updateContextButton(contextEnabled: Boolean) {
        val (backgroundColorResId, foregroundColorResID) = if (contextEnabled) {
            R.color.dark_purple to R.color.light_purple
        } else {
            R.color.gray_button to R.color.white
        }
        val foregroundColor = ContextCompat.getColor(this@MainActivity, foregroundColorResID)
        binding.btnToggleContext.apply {
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, backgroundColorResId))
            setTextColor(foregroundColor)
            iconTint = ColorStateList.valueOf(foregroundColor)
        }
    }
}
