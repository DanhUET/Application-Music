package pro.branium.feature.player.ui.fragment

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.Player
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_playback.MediaPlaybackController
import pro.branium.core_domain.repository.RepeatMode
import pro.branium.core_domain.manager.FavoriteManager
import pro.branium.core_model.SongModel
import pro.branium.core_navigation.navigator.SongOptionMenuNavigator
import pro.branium.core_resources.R
import pro.branium.core_ui.extensions.showToast
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.feature.player.ui.customview.CustomSeekBar
import pro.branium.feature.player.viewmodel.NowPlayingViewModel
import pro.branium.feature.player.viewmodel.PlaybackStateViewModel
import pro.branium.feature.player.viewmodel.PlayerViewModel
import pro.branium.player.databinding.FragmentNowPlayingBinding
import pro.branium.presentation_common.viewmodel.NetworkStateViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NowPlayingFragment : BottomSheetDialogFragment(), View.OnClickListener {
    @Inject
    lateinit var songOptionMenuNavigator: SongOptionMenuNavigator

    @Inject
    lateinit var playbackController: MediaPlaybackController

    @Inject
    lateinit var favoriteManager: FavoriteManager

    private lateinit var binding: FragmentNowPlayingBinding
    private lateinit var pressedAnimator: Animator
    private lateinit var seekBarHandler: Handler
    private lateinit var seekbarCallback: Runnable
    private lateinit var rotationAnimator: ObjectAnimator
    private val nowPlayingViewModel: NowPlayingViewModel by activityViewModels()
    private val playbackStateViewModel: PlaybackStateViewModel by activityViewModels()
    private val networkViewModel: NetworkStateViewModel by activityViewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupAnimator()
        setupObserver()
        observePlayingSong()
        observeFavoriteStatus()
        observeCurrentAngle()
        observePlayingStatus()
        observeShuffleState()
        observePlaybackStateChange()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet?.requestLayout()
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        playerViewModel.onCollapsePlayer()
                        dismiss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // todo
                }
            })
        }
    }

    override fun onClick(v: View) {
        pressedAnimator.setTarget(v)
        pressedAnimator.start()
        when (v) {
            binding.btnPlayPauseNowPlaying -> setupPlayPauseAction()
            binding.btnShuffle -> setupShuffleAction()
            binding.btnSkipPrevNowPlaying -> setupSkipPrevious()
            binding.btnSkipNextNowPlaying -> setupSkipNext()
            binding.btnRepeat -> setupRepeatAction()
            binding.btnShareNowPlaying -> share()
            binding.btnFavoriteNowPlaying -> setupFavoriteAction()
            else -> {}
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        playerViewModel.onCollapsePlayer()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    private fun setupPlayPauseAction() {
        networkViewModel.isNetworkAvailable.observe(this) { isNetworkAvailable ->
            if (isNetworkAvailable == true) {
                playbackStateViewModel.playPause()
            } else {
                playbackStateViewModel.pause()
                showNetworkError(binding.root)
            }
        }
    }

    private fun setupShuffleAction() {
        playbackStateViewModel.toggleShuffleMode()
    }

    private fun setupSkipPrevious() {
        val isNetworkAvailable = networkViewModel.isNetworkAvailable.value
        if (isNetworkAvailable == true) {
            if (!playbackStateViewModel.hasPreviousMediaItem()) {
                showSnackBar(pro.branium.player.R.string.no_previous_song)
            } else {
                playbackStateViewModel.seekToPrevious()
                rotationAnimator.end()
            }
        } else {
            showNetworkError(binding.root)
        }
    }

    private fun setupSkipNext() {
        val isNetworkAvailable = networkViewModel.isNetworkAvailable.value
        if (isNetworkAvailable == true) {
            if (!playbackStateViewModel.hasNextMediaItem()) {
                showSnackBar(pro.branium.player.R.string.no_next_song)
            } else {
                playbackStateViewModel.seekToNext()
                rotationAnimator.end()
            }
        } else {
            showNetworkError(binding.root)
        }
    }

    private fun showSnackBar(messageId: Int) {
        val snackbar = Snackbar.make(binding.root, messageId, Snackbar.LENGTH_SHORT)
        snackbar.anchorView = binding.btnPlayPauseNowPlaying
        snackbar.show()
    }

    private fun setupRepeatAction() {
        playbackStateViewModel.changeRepeatMode()
    }

    private fun share() {
        requireContext().showToast(R.string.message_function_implementing)
    }

    private fun setupFavoriteAction() {
        lifecycleScope.launch {
            favoriteManager.toggleFavorite()
        }
    }

    private fun setupView() {
        binding.btnPlayPauseNowPlaying.setOnClickListener(this)
        binding.btnShuffle.setOnClickListener(this)
        binding.btnSkipPrevNowPlaying.setOnClickListener(this)
        binding.btnSkipNextNowPlaying.setOnClickListener(this)
        binding.btnRepeat.setOnClickListener(this)
        binding.btnShareNowPlaying.setOnClickListener(this)
        binding.btnFavoriteNowPlaying.setOnClickListener(this)
        binding.btnNowPlayingSongOption.setOnClickListener {
            playbackStateViewModel.nowPlayingSong.value?.song?.let {
                showOptionMenu(it)
            }
        }
        initToolbar()
        initSeekBar()
    }

    private fun showOptionMenu(song: SongModel) {
        songOptionMenuNavigator.openSongOptionMenu(parentFragmentManager, song)
    }

    private fun initToolbar() {
        binding.toolbarNowPlaying.setNavigationOnClickListener {
            rotationAnimator.pause()
            playerViewModel.onCollapsePlayer()
            val bottomSheet = dialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initSeekBar() {
        binding.seekBarNowPlaying.setOnSeekBarChangeListener(
            object : CustomSeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: CustomSeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        playbackController.seekTo(progress.toLong())
                    }
                    binding.currentPositionMs = progress.toLong()
                }

                override fun onStartTrackingTouch(seekBar: CustomSeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: CustomSeekBar?) {
                }
            })
    }

    private fun setupObserver() {
        networkViewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isNetworkAvailable ->
            if (!isNetworkAvailable) {
                showNetworkError(binding.root)
            }
        }
    }

    private fun setupSeekBar() {
        seekBarHandler = Looper.myLooper()?.let { Handler(it) }!!
        seekbarCallback = object : Runnable {
            override fun run() {
                val currentPosition = playbackStateViewModel.currentPosition.value
                binding.seekBarNowPlaying.progress = currentPosition.toInt()
                seekBarHandler.postDelayed(this, 1000)
            }
        }
        seekBarHandler.post(seekbarCallback)
    }

    private fun showSongInfo() {
        updateSeekBarMaxValue()
        updateDuration()
        observeRepeatMode()
    }

    private fun updateSeekBarMaxValue() {
        val currentPos = playbackStateViewModel.currentPosition.value
        binding.seekBarNowPlaying.progress = currentPos.toInt()
        val duration = playbackStateViewModel.nowPlayingState.value?.durationMs ?: 0
        binding.seekBarNowPlaying.max = nowPlayingViewModel.getDuration(duration)
    }

    private fun updateDuration() {
        val duration = playbackStateViewModel.nowPlayingState.value?.durationMs ?: 0
        binding.durationMs = duration
    }

    private fun observeRepeatMode() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.repeatMode.collect { repeatMode ->
                    val iconId = when (repeatMode) {
                        RepeatMode.NONE -> pro.branium.player.R.drawable.ic_repeat_off
                        RepeatMode.ALL -> pro.branium.player.R.drawable.ic_repeat_all
                        RepeatMode.ONE -> pro.branium.player.R.drawable.ic_repeat_one
                    }
                    binding.btnRepeat.setImageResource(iconId)
                }
            }
        }
    }

    private fun observeShuffleState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.shuffleMode.collect { isShuffle ->
                    val iconId = if (isShuffle) {
                        pro.branium.player.R.drawable.ic_shuffle_on
                    } else {
                        pro.branium.player.R.drawable.ic_shuffle
                    }
                    binding.btnShuffle.setImageResource(iconId)
                }
            }
        }
    }

    private fun showFavoriteState(isFavorite: Boolean) {
        val favoriteIcon = if (isFavorite) {
            pro.branium.player.R.drawable.ic_favorited
        } else {
            pro.branium.player.R.drawable.ic_favorite_off
        }
        binding.btnFavoriteNowPlaying.setImageResource(favoriteIcon)
    }

    private fun observePlaybackStateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.playbackStateChanged.collect { playbackState ->
                    if (playbackState == Player.STATE_READY) {
                        updateSeekBarMaxValue()
                        updateDuration()
                        binding.pbNowPlaying.visibility = View.GONE
                    } else if (playbackState == Player.STATE_BUFFERING) {
                        binding.pbNowPlaying.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupAnimator() {
        pressedAnimator = AnimatorInflater.loadAnimator(
            requireContext(),
            pro.branium.player.R.animator.button_pressed
        )
        rotationAnimator = ObjectAnimator
            .ofFloat(binding.imageArtworkNowPlaying, "rotation", 0f, 360f)
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.duration = 12000
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.repeatMode = ObjectAnimator.RESTART
    }

    private fun observePlayingSong() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.nowPlayingSong.collect { song ->
                    if (song == null) return@collect
                    val playingState =
                        playbackStateViewModel.nowPlayingState.value?.isPlaying ?: return@collect
                    if (!playingState) {
                        playbackController.play()
                    }
                    setupSongToPlay(song)
                }
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteManager.isFavorite.collect { isFavorite ->
                    showFavoriteState(isFavorite)
                }
            }
        }
    }

    private fun observePlayingStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.nowPlayingState.collect { playingState ->
                    updateUI(playingState?.isPlaying == true)
                    updatePlayPauseButtonIcon(playingState?.isPlaying == true)
                    updateRotationState(playingState?.isPlaying == true)
                }
            }
        }
    }

    private fun updateUI(isPlaying: Boolean = false) {
        if (isPlaying) {
            updateSeekBarMaxValue()
            updateDuration()
        }
    }

    private fun updatePlayPauseButtonIcon(isPlaying: Boolean) {
        val iconId = if (isPlaying) {
            pro.branium.player.R.drawable.ic_pause_circle
        } else {
            pro.branium.player.R.drawable.ic_play_circle
        }
        binding.btnPlayPauseNowPlaying.setImageResource(iconId)
    }

    private fun updateRotationState(isPlaying: Boolean) {
        if (isPlaying) {
            if (rotationAnimator.isPaused) {
                rotationAnimator.resume()
            } else {
                rotationAnimator.start()
            }
        } else {
            rotationAnimator.pause()
        }
    }

    private fun observeCurrentAngle() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackStateViewModel.currentAngle.collect { angle ->
                    rotationAnimator.setCurrentFraction(angle)
                }
            }
        }
    }

    private fun setupSongToPlay(displaySongModel: DisplaySongModel) {
        val isNetworkAvailable = networkViewModel.isNetworkAvailable.value
        if (isNetworkAvailable == true) {
            binding.songModel = displaySongModel
            setupSeekBar()
            showSongInfo()
            favoriteManager.setFavoriteSongId(displaySongModel.song.id)
        } else {
            showNetworkError(binding.root)
        }
    }

    private fun showNetworkError(view: View) {
        val message = getString(pro.branium.player.R.string.no_internet)
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }

    override fun onStop() {
        super.onStop()
        playbackStateViewModel.updateCurrentAngle(rotationAnimator.animatedFraction)
        if (this::seekBarHandler.isInitialized) {
            seekBarHandler.removeCallbacks(seekbarCallback)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rotationAnimator.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::seekBarHandler.isInitialized) {
            seekBarHandler.removeCallbacks(seekbarCallback)
        }
    }
}