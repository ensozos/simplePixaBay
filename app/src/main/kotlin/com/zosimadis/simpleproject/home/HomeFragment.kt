package com.zosimadis.simpleproject.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zosimadis.simpleproject.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = ImageListAdapter(
        onItemClick = { image ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    imageId = image.localId,
                ),
            )
        },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeImages()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter.withLoadStateFooter(
                footer = LoadStateAdapter { this@HomeFragment.adapter.retry() },
            )
        }
    }

    private fun observeImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    // Show loading or error states
                    when (loadStates.refresh) {
                        is LoadState.Loading -> {
                        }
                        is LoadState.Error -> {
                        }
                        is LoadState.NotLoading -> {
                            if (loadStates.append.endOfPaginationReached && adapter.itemCount < 1) {
                                binding.recyclerView.scrollToPosition(0)
                            }
                        }
                    }
                }
            }

            launch {
                viewModel.imagesFlow.collectLatest { pagingData ->
                    adapter.submitData(lifecycle, pagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
