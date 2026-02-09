package pro.branium.feature_album.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_navigation.home.AlbumDetailNavigator
import pro.branium.feature_album.R
import pro.branium.feature_album.databinding.FragmentMoreAlbumBinding
import pro.branium.feature_album.ui.adapter.MoreAlbumAdapter
import pro.branium.feature_album.viewmodel.AlbumHotViewModel
import pro.branium.presentation_common.base.NetworkBaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class MoreAlbumFragment : NetworkBaseFragment() {
    @Inject
    lateinit var albumDetailNavigator: AlbumDetailNavigator
    private lateinit var binding: FragmentMoreAlbumBinding
    private lateinit var adapter: MoreAlbumAdapter

    private val albumViewModel: AlbumHotViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.toolbarMoreAlbum.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        adapter = MoreAlbumAdapter(
            listener = { albumModel ->
                navigateToDetailAlbum(albumModel.albumId)
            }
        )
        binding.rvMoreAlbum.adapter = adapter
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                albumViewModel.moreAlbumFlow.collect { data ->
                    adapter.submitData(data)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                val isNotLoading = loadStates.source.refresh is LoadState.NotLoading
                val hasData = adapter.itemCount > 0
                if (isNotLoading) {
                    if (hasData) {
                        binding.albumContentLayout.visibility = View.VISIBLE
                        binding.albumEmptyLayout.visibility = View.GONE
                    } else {
                        binding.albumEmptyLayout.visibility = View.VISIBLE
                        binding.albumContentLayout.visibility = View.GONE
                        binding.includeEmptyContent.tvEmptyMessage.text =
                            getString(R.string.no_album_available)
                    }
                }
            }
        }
    }

    private fun navigateToDetailAlbum(albumId: Int) {
        albumDetailNavigator.openAlbumDetail(this, albumId)
    }
}