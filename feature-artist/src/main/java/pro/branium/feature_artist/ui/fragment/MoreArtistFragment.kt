package pro.branium.feature_artist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_navigation.discovery.ArtistDetailNavigator
import pro.branium.core_ui.extensions.showToast
import pro.branium.core_ui.model.DisplayArtistModel
import pro.branium.feature_artist.R
import pro.branium.feature_artist.databinding.FragmentMoreArtistBinding
import pro.branium.feature_artist.ui.adapter.ArtistPagingAdapter
import pro.branium.feature_artist.viewmodel.ArtistViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MoreArtistFragment : Fragment() {
    @Inject
    lateinit var artistDetailNavigator: ArtistDetailNavigator
    private lateinit var binding: FragmentMoreArtistBinding
    private lateinit var adapter: ArtistPagingAdapter

    private val artistViewModel: ArtistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        binding.toolbarMoreArtist.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        adapter = ArtistPagingAdapter(
            object : ArtistPagingAdapter.ArtistListener {
                override fun onClick(artistModel: DisplayArtistModel) {
                    navigateToArtistDetail(artistModel.artist.id)
                }

                override fun onSubscribe(artistModel: DisplayArtistModel) {
                    requireContext()
                        .showToast(
                            pro.branium.core_resources.R.string.message_function_implementing
                        )
                }
            }
        )
        binding.includeArtist.rvArtist.adapter = adapter
    }

    private fun navigateToArtistDetail(artistId: Int) {
        artistDetailNavigator.openArtistDetail(this, artistId)
    }

    private fun observeData() {
        lifecycleScope.launch {
            artistViewModel.artistFlow.collect { page ->
                adapter.submitData(page)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                val isNotLoading =
                    loadStates.source.refresh is LoadState.NotLoading
                val hasData = adapter.itemCount > 0
                if (isNotLoading) {
                    if (hasData) {
                        binding.artistContentLayout.visibility = View.VISIBLE
                        binding.artistEmptyLayout.visibility = View.GONE
                    } else {
                        binding.artistEmptyLayout.visibility = View.VISIBLE
                        binding.artistContentLayout.visibility = View.GONE
                        binding.includeEmptyContent.tvEmptyMessage.text =
                            getString(R.string.no_artist)
                    }
                }
            }
        }
    }
}