package pro.branium.discovery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_navigation.navigator.SearchNavigator
import pro.branium.discovery.databinding.FragmentDiscoveryBinding
import pro.branium.discovery.ui.adapter.DiscoveryAdapter
import pro.branium.presentation_common.viewmodel.VoiceSearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DiscoveryFragment : Fragment() {
    private var _binding: FragmentDiscoveryBinding? = null
    private val binding: FragmentDiscoveryBinding
        get() = _binding!!
    private val voiceSearchViewModel: VoiceSearchViewModel by activityViewModels()

    @Inject
    lateinit var searchNavigator: SearchNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeVoiceSearch()
    }

    private fun setupRecyclerView() {
        val recycler = binding.recyclerDiscovery
        val sections = listOf(
            DiscoveryAdapter.DiscoverySection.Artist,
            DiscoveryAdapter.DiscoverySection.MostHeard,
            DiscoveryAdapter.DiscoverySection.ForYou
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = DiscoveryAdapter(sections)
    }

    private fun setupListeners() {
        binding.btnSearchDiscovery.setOnClickListener {
            searchNavigator.openSearch(null)
        }
        binding.btnSpeechToSearch.setOnClickListener {
            voiceSearchViewModel.triggerVoiceSearch(true)
        }
    }

    private fun observeVoiceSearch() {
        voiceSearchViewModel.voiceSearchQuery.observe(viewLifecycleOwner) { query ->
            if (query.isNotEmpty()) {
                val directions = searchNavigator.openSearch(query)
                findNavController().navigate(directions)
                voiceSearchViewModel.setVoiceSearchQuery("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}