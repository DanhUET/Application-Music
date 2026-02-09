package pro.branium.feature.search.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_ui.model.SearchUiState
import pro.branium.feature.search.viewmodel.SearchingViewModel
import pro.branium.presentation_common.base.BaseFragment
import pro.branium.presentation_common.viewmodel.VoiceSearchViewModel
import pro.branium.search.R
import pro.branium.search.databinding.FragmentSearchingBinding

@AndroidEntryPoint
class SearchingFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchingBinding
    private val args: SearchingFragmentArgs by navArgs()
    private val voiceSearchViewModel: VoiceSearchViewModel by activityViewModels()

    private val viewModel: SearchingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupSearchView()
        setupObserver()
        setupVoiceSearchObserver()
    }

    private fun setupView() {
        binding.toolbarSearching.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSpeechToSearch.setOnClickListener {
            voiceSearchViewModel.triggerVoiceSearch(true)
        }
    }

    private fun setupSearchView() {
        val manager: SearchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchViewHome
        searchView.setSearchableInfo(manager.getSearchableInfo(requireActivity().componentName))
        searchView.isSubmitButtonEnabled = false
        searchView.isQueryRefinementEnabled = true
        searchView.isIconifiedByDefault = false
        searchView.postDelayed({
            searchView.requestFocus()
            val imm = requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
        }, 100)
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onSearchConfirmed(query)
                if (query.trim().isNotEmpty()) {
                    performSearch(query)
                    viewModel.insertSearchedKey(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.trim().isNotEmpty()) {
                    viewModel.onSearchQueryChanged(newText)
                    performSearch(newText)
                } else {
                    viewModel.onSearchQueryChanged(newText)
                }
                return true
            }
        })
        if (!args.query.isNullOrEmpty()) {
            searchView.setQuery(args.query, true)
        }
    }

    private fun performSearch(query: String) {
        viewModel.search(query)
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchUiState.collect { state ->
                    when (state) {
                        is SearchUiState.Idle -> showSearchHistoryView()
                        is SearchUiState.Typing -> showSearchResultView()
                        is SearchUiState.Results -> showSearchResultView()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shouldSaveSearchedKey.collect { shouldSave ->
                    if (shouldSave) {
                        val query = binding.searchViewHome.query.toString()
                        viewModel.insertSearchedKey(query)
                        viewModel.shouldSaveSearchedKey(false)
                    }
                }
            }
        }
        viewModel.searchedKey.observe(viewLifecycleOwner) { key ->
            binding.searchViewHome.setQuery(key, false)
        }
    }

    private fun showSearchHistoryView() {
        childFragmentManager.beginTransaction()
            .replace(R.id.content_container_layout, SearchHistoryFragment::class.java, null)
            .commit()
    }

    private fun showSearchResultView() {
        childFragmentManager.beginTransaction()
            .replace(R.id.content_container_layout, SearchResultFragment::class.java, null)
            .commit()
    }

    private fun setupVoiceSearchObserver() {
        voiceSearchViewModel.voiceSearchQuery.observe(viewLifecycleOwner) { query ->
            if (query.isNotEmpty()) {
                binding.searchViewHome.setQuery(query, true)
                voiceSearchViewModel.setVoiceSearchQuery("")
            }
        }
    }
}