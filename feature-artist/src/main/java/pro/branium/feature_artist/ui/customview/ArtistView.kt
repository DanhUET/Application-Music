package pro.branium.feature_artist.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_navigation.discovery.ArtistDetailNavigator
import pro.branium.core_navigation.discovery.ArtistNavigator
import pro.branium.core_resources.R
import pro.branium.core_ui.databinding.LayoutEmptyListBinding
import pro.branium.core_ui.extensions.showSnackBar
import pro.branium.core_ui.layout.MaxHeightConstraintLayout
import pro.branium.core_ui.model.DisplayArtistModel
import pro.branium.feature_artist.databinding.ViewArtistBinding
import pro.branium.feature_artist.ui.adapter.ArtistPagingAdapter
import pro.branium.feature_artist.viewmodel.ArtistViewModel
import pro.branium.presentation_common.viewmodel.NetworkStateViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ArtistView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaxHeightConstraintLayout(context, attrs, defStyleAttr) {
    @Inject
    lateinit var moreArtistNavigator: ArtistNavigator

    @Inject
    lateinit var artistDetailNavigator: ArtistDetailNavigator

    private val binding = ViewArtistBinding.inflate(LayoutInflater.from(context), this, true)

    // lấy ViewModel từ Activity scope (Fragment cũ dùng activityViewModels)
    private val viewModel: ArtistViewModel by lazy {
        val owner = findViewTreeViewModelStoreOwner()!!
        ViewModelProvider(owner)[ArtistViewModel::class.java]
    }

    private val adapter = ArtistPagingAdapter(object : ArtistPagingAdapter.ArtistListener {
        override fun onClick(artistModel: DisplayArtistModel) {
            val fragment = findFragment() ?: return
            artistDetailNavigator.openArtistDetail(fragment, artistModel.artist.id)
        }

        override fun onSubscribe(artistModel: DisplayArtistModel) {
            Toast.makeText(
                context,
                context.getString(R.string.message_function_implementing),
                Toast.LENGTH_SHORT
            ).show()
        }
    })

    private var isDataLoaded = false

    init {
        binding.rvArtist.adapter = adapter
        binding.layoutMoreArtist.setOnClickListener {
            val fragment = findFragment() ?: return@setOnClickListener
            moreArtistNavigator.openMoreArtist(fragment)
        }

        LayoutEmptyListBinding.inflate(
            LayoutInflater.from(context),
            binding.artistEmptyLayout,
            true
        ).apply {
            iconRes = pro.branium.feature_artist.R.drawable.ic_person_off
            message = context.getString(R.string.no_artist)
        }

        doOnAttach {
            observeNetworkState()
            loadData()
        }
    }

    private fun observeNetworkState() {
        val owner = findViewTreeLifecycleOwner() ?: return
        if (owner is Fragment) {
            owner.activityViewModels<NetworkStateViewModel>()
                .value
                .isNetworkAvailable
                .observe(owner.viewLifecycleOwner) { isNetworkAvailable ->
                    if (isNetworkAvailable && !isDataLoaded) {
                        adapter.refresh()
                    } else if (!isNetworkAvailable) {
                        owner.requireView()
                            .showSnackBar(
                                R.string.no_internet,
                                owner.requireView()
                            )
                    }
                }
        }
    }

    private fun loadData() {
        val owner = findViewTreeLifecycleOwner() ?: return

        owner.lifecycleScope.launch {
            owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.limitedArtistFlow.collect { page ->
                    adapter.submitData(page)
                }
            }
        }

        owner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                val isNotLoading = loadStates.source.refresh is LoadState.NotLoading
                val hasData = adapter.itemCount > 0

                if (isNotLoading) {
                    binding.pbArtist.visibility = GONE
                    if (hasData) {
                        isDataLoaded = true
                        binding.artistEmptyLayout.visibility = GONE
                        binding.rvArtist.visibility = VISIBLE
                    } else {
                        binding.artistEmptyLayout.visibility = VISIBLE
                        binding.rvArtist.visibility = GONE
                    }
                } else {
                    binding.pbArtist.visibility = VISIBLE
                }
            }
        }
    }

    private fun findFragment(): Fragment? {
        val fragment = try {
            findFragment<Fragment>()
        } catch (_: IllegalStateException) {
            null
        }
        return fragment
    }
}
