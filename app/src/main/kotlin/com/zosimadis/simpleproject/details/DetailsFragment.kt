package com.zosimadis.simpleproject.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.zosimadis.simpleproject.R
import com.zosimadis.simpleproject.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        observeState()
        observeEvents()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun updateUI(state: DetailsUiState) {
        with(binding) {
            progressBar.isVisible = state.isLoading
            contentGroup.isVisible = !state.isLoading

            imageView.load(state.imageUrl) {
                crossfade(true)
                error(android.R.drawable.ic_menu_report_image)
                placeholder(android.R.drawable.ic_menu_gallery)
            }

            imageSize.text = getString(R.string.size_label, state.imageSize.toString())
            imageType.text = getString(R.string.type_label, state.imageType)
            userName.text = state.userName

            tagsChipGroup.removeAllViews()
            state.tags.split(",").forEach { tag ->
                val chip = com.google.android.material.chip.Chip(requireContext()).apply {
                    text = tag.trim()
                    isClickable = false
                    setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)
                }
                tagsChipGroup.addView(chip)
            }

            views.text = NumberFormat.getNumberInstance().format(state.views)
            likes.text = NumberFormat.getNumberInstance().format(state.likes)
            comments.text = NumberFormat.getNumberInstance().format(state.comments)
            downloads.text = NumberFormat.getNumberInstance().format(state.downloads)
        }
    }

    private fun handleEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.ShowError -> {
                Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
