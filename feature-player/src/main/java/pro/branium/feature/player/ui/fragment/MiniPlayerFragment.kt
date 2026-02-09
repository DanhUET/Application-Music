package pro.branium.feature.player.ui.fragment

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.Player
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_playback.MediaControllerProvider
import pro.branium.core_domain.manager.FavoriteManager
import pro.branium.core_navigation.navigator.PlayerNavigator
import pro.branium.feature.player.viewmodel.PlaybackStateViewModel
import pro.branium.feature.player.viewmodel.PlayerViewModel
import pro.branium.player.R
import pro.branium.player.databinding.FragmentMiniPlayerBinding
import pro.branium.presentation_common.base.NetworkBaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class MiniPlayerFragment : NetworkBaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentMiniPlayerBinding
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val playbackStateViewModel: PlaybackStateViewModel by activityViewModels()

    @Inject
    lateinit var navigator: PlayerNavigator

    @Inject
    lateinit var mediaControllerHolder: MediaControllerProvider

    @Inject
    lateinit var favoriteManger: FavoriteManager

//    private val favoriteSongViewModel: FavoriteSongViewModel by activityViewModels()

    private lateinit var pressedAnimator: Animator
    private lateinit var rotationAnimator: ObjectAnimator
    private var currentFraction: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupAnimator()
        observePlayingSong()
        observeNowPlayingState()
        observeFavoriteStatus()
        observePlaybackStateChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rotationAnimator.cancel()
    }

    override fun onClick(v: View) {
        pressedAnimator.setTarget(v)
        pressedAnimator.start()
        when (v) {
            binding.btnMiniPlayerPlayPause -> playPause()

            binding.btnMiniPlayerSkipNext -> skipNext()

            binding.btnMiniPlayerFavorite -> setupFavorite()
        }
    }

    private fun playPause() {
        val state = networkViewModel.isNetworkAvailable.value
        if (state == true) {
            playbackStateViewModel.playPause()
        } else {
            playbackStateViewModel.pause()
            showNetworkError(binding.root)
        }
    }

    private fun skipNext() {
        val networkState = networkViewModel.isNetworkAvailable.value
        if (networkState == true) {
            if (playbackStateViewModel.hasNextMediaItem()) {
                playbackStateViewModel.seekToNext()
                rotationAnimator.end()
            } else {
                val snackBar = Snackbar
                    .make(binding.root, R.string.no_next_song, Snackbar.LENGTH_SHORT)
                snackBar.setAnchorView(R.id.mini_player)
                snackBar.show()
            }
        } else {
            showNetworkError(binding.root)
        }
    }

    private fun setupFavorite() {
        lifecycleScope.launch {
            favoriteManger.toggleFavorite()
        }
    }

    private fun setupView() {
        binding.btnMiniPlayerFavorite.setOnClickListener(this)
        binding.btnMiniPlayerPlayPause.setOnClickListener(this)
        binding.btnMiniPlayerSkipNext.setOnClickListener(this)
        binding.textMiniPlayerTitle.maxLines = 1
        binding.textMiniPlayerTitle.ellipsize = TextUtils.TruncateAt.END
        binding.root.setOnClickListener {
            val networkState = networkViewModel.isNetworkAvailable.value
            if (networkState == true) {
                playbackStateViewModel.updateCurrentAngle(rotationAnimator.animatedFraction)
                playerViewModel.onExpandPlayer()
                navigateToNowPlaying()
            } else {
                showNetworkError(binding.root)
            }
        }
    }

    private fun setupAnimator() {
        pressedAnimator = AnimatorInflater.loadAnimator(requireContext(), R.animator.button_pressed)
        rotationAnimator = ObjectAnimator
            .ofFloat(binding.imageMiniPlayerArtwork, "rotation", 0f, 360f)
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.duration = 12000
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.repeatMode = ObjectAnimator.RESTART
    }

    private fun observePlaybackStateChanged() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.playbackStateChanged.collect { playbackState ->
                    if (playbackState == Player.STATE_BUFFERING) {
                        binding.pbMini.visibility = View.VISIBLE
                        binding.btnMiniPlayerPlayPause.visibility = View.INVISIBLE
                    } else {
                        binding.pbMini.visibility = View.GONE
                        binding.btnMiniPlayerPlayPause.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun observePlayingSong() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.nowPlayingSong.collect { song ->
                    if (song != null) {
                        binding.displaySong = song
                        favoriteManger.setFavoriteSongId(song.song.id)
                        playerViewModel.onSongStarted()
                    } else {
                        binding.displaySong = null
                    }
                }
            }
        }
    }

    private fun observeNowPlayingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.nowPlayingState.collect { nowPlaying ->
                    nowPlaying?.let {
                        playbackStateViewModel.getSongById(it.id)
                        if (it.isPlaying) {
                            binding.btnMiniPlayerPlayPause
                                .setImageResource(R.drawable.ic_pause_circle)
                            if (rotationAnimator.isPaused) {
                                if (currentFraction != 0f) {
                                    rotationAnimator.start()
                                    rotationAnimator.setCurrentFraction(currentFraction)
                                    currentFraction = 0f
                                } else {
                                    rotationAnimator.resume()
                                }
                            } else if (!rotationAnimator.isRunning) {
                                rotationAnimator.start()
                            }
                        } else {
                            binding.btnMiniPlayerPlayPause
                                .setImageResource(R.drawable.ic_play_circle)
                            rotationAnimator.pause()
                        }
                    }
                }
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteManger.isFavorite.collect { isFavorite ->
                    updateFavoriteStatus(isFavorite)
                }
            }
        }
    }

    private fun updateFavoriteStatus(isFavorite: Boolean) {
        val favoriteIcon = if (isFavorite) {
            R.drawable.ic_favorited
        } else {
            R.drawable.ic_mini_favorite_off
        }
        binding.btnMiniPlayerFavorite.setImageResource(favoriteIcon)
    }

    private fun navigateToNowPlaying() {
        navigator.navigateToNowPlaying()
    }
}