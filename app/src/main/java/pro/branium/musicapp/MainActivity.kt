package pro.branium.musicapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pro.branium.core_model.event.UiEvent
import pro.branium.core_ui.base.SnackBarAnchorProvider
import pro.branium.core_ui.event.UiEventBus
import pro.branium.core_ui.model.PlayerUiState
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature.home.viewmodel.HomeViewModel
import pro.branium.feature.player.viewmodel.PlaybackInitializerViewModel
import pro.branium.feature.player.viewmodel.PlaybackStateViewModel
import pro.branium.feature.player.viewmodel.PlayerViewModel
import pro.branium.infrastructure.media.PlaybackService
import pro.branium.musicapp.databinding.ActivityMainBinding
import pro.branium.musicapp.ui.SnackbarManager
import pro.branium.presentation_common.viewmodel.PostNotificationPermissionViewModel
import pro.branium.presentation_common.viewmodel.VoiceSearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SnackBarAnchorProvider {
    @Inject
    lateinit var uiEventBus: UiEventBus
    private lateinit var binding: ActivityMainBinding
    private val voiceSearchViewModel: VoiceSearchViewModel by viewModels()
    private val postNotificationViewModel: PostNotificationPermissionViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()
    private val playbackStateViewModel: PlaybackStateViewModel by viewModels()
    private val playbackInitViewModel: PlaybackInitializerViewModel by viewModels()

    private val homeViewModel: HomeViewModel by viewModels()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                val snackBar = Snackbar.make(
                    binding.root.rootView,
                    getString(R.string.permission_denied),
                    Snackbar.LENGTH_LONG
                )
                snackBar.setAnchorView(R.id.nav_view)
                snackBar.show()
            } else {
                postNotificationViewModel.grantPermission()
            }
        }

    private val recordAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                voiceSearchViewModel.setRecordAudioPermissionGranted(true)
                startVoiceSearchActivity()
            } else {
                val snackBar = Snackbar.make(
                    binding.root.rootView,
                    getString(R.string.record_audio_permission_denied),
                    Snackbar.LENGTH_LONG
                )
                snackBar.setAnchorView(R.id.nav_view)
                snackBar.show()
            }
        }

    private val voiceSearchActivityLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val query = matches?.get(0) ?: ""
                voiceSearchViewModel.setVoiceSearchQuery(query)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        setupMiniPlayer()
        setupBottomNav()
        setupNavigationRail()
        setupComponents()
        checkPostNotificationPermissionStatus()
        checkRecordAudioPermissionStatus()
        observeSnackBarEvent()
        observeData()
        if (savedInstanceState == null) {
            // đọc dữ liệu phiên trước đó khi lần đầu mở app
            loadPreviousSessionSong()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            playbackInitViewModel.nowPlayingSong.collect { song ->
                if (song != null) {
                    playbackStateViewModel.updatePlayingSong(song)
                    playerViewModel.onSongStarted()
                }
            }
        }
    }

    private fun setupMiniPlayer() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerViewModel.playerUiState.collect { state ->
                    when (state) {
                        PlayerUiState.HIDDEN -> {
                            binding.fcvMiniPlayer.visibility = View.GONE
                        }

                        PlayerUiState.MINI_VISIBLE -> {
                            binding.fcvMiniPlayer.visibility = View.VISIBLE
                        }

                        PlayerUiState.FULL_VISIBLE -> {
                            binding.fcvMiniPlayer.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setupBottomNav() {
        val navView: BottomNavigationView? = binding.navView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView?.setupWithNavController(navController)
    }

    private fun setupNavigationRail() {
        val navRail = binding.navRailView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navRail?.setupWithNavController(navController)
    }

    private fun setupComponents() {
        MusicAppUtils.DENSITY =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                windowManager.currentWindowMetrics.density
            } else {
                resources.displayMetrics.density
            }
    }

    private fun checkPostNotificationPermissionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val isPermissionGranted = ActivityCompat
                .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            if (isPermissionGranted) {
                postNotificationViewModel.grantPermission()
            }
        } else {
            postNotificationViewModel.grantPermission()
        }
    }

    private fun checkRecordAudioPermissionStatus() {
        val permission = Manifest.permission.RECORD_AUDIO
        val isGranted =
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            voiceSearchViewModel.setRecordAudioPermissionGranted(true)
        }
    }

    private fun observeSnackBarEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiEventBus.events.collect { event ->
                    when (event) {
                        is UiEvent.ShowMessage -> {
                            SnackbarManager.showMessage(this@MainActivity, event.message)
                        }
                    }
                }
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                postNotificationViewModel.state.collect { state ->
                    if (state.asked && state.userTriggered) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val permission = Manifest.permission.POST_NOTIFICATIONS
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this@MainActivity,
                                    permission
                                )
                            ) {
                                val snackBar = Snackbar.make(
                                    binding.root.rootView,
                                    getString(R.string.permission_denied),
                                    Snackbar.LENGTH_LONG
                                )
                                snackBar.setAction(R.string.action_agree) {
                                    permissionLauncher.launch(permission)
                                }
                                snackBar.setAnchorView(R.id.nav_view)
                                snackBar.show()
                            } else {
                                permissionLauncher.launch(permission)
                            }
                        }
                        postNotificationViewModel.revokeTrigger()
                    }
                }
            }
        }
        voiceSearchViewModel.voiceSearchTrigger.observe(this) { triggered ->
            val isPermissionGranted =
                voiceSearchViewModel.isRecordAudioPermissionGranted.value == true
            val isUserTriggered = voiceSearchViewModel.isUserTriggered.value == true
            if (isUserTriggered && triggered && isPermissionGranted) {
                startVoiceSearchActivity()
            } else if (isUserTriggered && !isPermissionGranted) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    val snackBar = Snackbar.make(
                        binding.root.rootView,
                        getString(R.string.record_audio_permission_rationale),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar.setAction(R.string.action_agree) {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                    snackBar.setAnchorView(R.id.nav_view)
                    snackBar.show()
                } else {
                    recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
            voiceSearchViewModel.disableUserTriggered()
        }
    }

    private fun startVoiceSearchActivity() {
        val voiceSearchIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voiceSearchIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        val prompt = getString(R.string.prompt_speech_to_search)
        voiceSearchIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
        voiceSearchActivityLauncher.launch(voiceSearchIntent)
    }

    private fun loadPreviousSessionSong() {
        val playbackState = homeViewModel.getPlaybackState()
        playbackInitViewModel.loadPreviousSessionSong(playbackState)
    }

    override fun getSnackBarAnchorView(): View? {
        val navView = findViewById<View>(R.id.nav_view)
        if (navView.isShown) return navView
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(PlaybackService.Companion.ACTION_STOP_SERVICE)
        sendBroadcast(intent)
    }
}