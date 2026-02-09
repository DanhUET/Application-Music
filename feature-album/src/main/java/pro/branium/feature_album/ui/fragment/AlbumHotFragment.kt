package pro.branium.feature_album.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_navigation.home.AlbumDetailNavigator
import pro.branium.core_navigation.home.AlbumNavigator
import pro.branium.feature_album.databinding.FragmentAlbumHotBinding
import pro.branium.feature_album.ui.adapter.AlbumAdapter
import pro.branium.feature_album.viewmodel.AlbumHotViewModel
import pro.branium.presentation_common.base.NetworkBaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class AlbumHotFragment : NetworkBaseFragment() {
    @Inject
    lateinit var navigator: AlbumNavigator
    @Inject
    lateinit var albumDetailNavigator: AlbumDetailNavigator

    private lateinit var binding: FragmentAlbumHotBinding
    private lateinit var adapter: AlbumAdapter

    private val albumViewModel: AlbumHotViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumHotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
    }

    private fun setupView() {
        adapter = AlbumAdapter(
            listener = { albumModel ->
                navigateToDetailAlbum(albumModel.albumId)
            }
        )
        binding.rvAlbumHot.adapter = adapter
        binding.layoutMoreAlbum.setOnClickListener {
            navigateToMoreAlbum()
        }
        binding.progressBarAlbum.isVisible = true
    }

    private fun observeData() {
        networkViewModel.isNetworkAvailable.observe(viewLifecycleOwner) { networkAvailable ->
            if (networkAvailable) {
                if (adapter.itemCount == 0) {
                    albumViewModel.loadTopAlbums()
                }
            } else {
                showNetworkError(binding.root)
            }
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                adapter.loadStateFlow.collect { loadStates ->
//                    binding.progressBarAlbum.isVisible =
//                        loadStates.source.refresh is LoadState.Loading
//                    val isNotLoading = loadStates.source.refresh is LoadState.NotLoading
//                    val hasData = adapter.itemCount > 0
//                    if (isNotLoading) {
//                        if (hasData) {
//                            binding.albumContentLayout.visibility = View.VISIBLE
//                            binding.albumEmptyLayout.visibility = View.GONE
//                        } else {
//                            binding.albumEmptyLayout.visibility = View.VISIBLE
//                            binding.albumContentLayout.visibility = View.GONE
//                            binding.includeEmptyContent.tvEmptyMessage.text =
//                                getString(R.string.no_album_available)
//                        }
//                    }
//                }
//            }
//        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                albumViewModel.topAlbums.collect { albums ->
                    binding.progressBarAlbum.visibility = View.GONE
                    adapter.update(albums)
                }
            }
        }
    }

    private fun navigateToDetailAlbum(albumId: Int) {
        albumDetailNavigator.openAlbumDetail(this, albumId)
    }

    private fun navigateToMoreAlbum() {
        navigator.openMoreAlbum(this)
    }
}