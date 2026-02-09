package pro.branium.infrastructure.media

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionError
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.branium.core_model.PlaybackStateData
import pro.branium.core_playback.usecase.SavePlaybackStateUseCase
import pro.branium.core_utils.MusicAppUtils
import pro.branium.core_utils.MusicAppUtils.ACTION_PLAY_PLAYLIST
import pro.branium.core_utils.MusicAppUtils.EXTRA_PLAYLIST_ID
import pro.branium.feature_recent.domain.usecase.SetRecentSongUseCase
import pro.branium.feature_song.data.SongApi
import pro.branium.feature_song.domain.usecase.UpdateSongCounterUseCase
import pro.branium.infrastructure.R
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    private lateinit var mediaSession: MediaSession
    private lateinit var listener: Player.Listener
    private lateinit var receiver: BroadcastReceiver
    private lateinit var networkStateReceiver: BroadcastReceiver
    private val customCommand = SessionCommand(ACTION_PLAY_PLAYLIST, Bundle.EMPTY)
    private var playbackState = -1
    private var isNetworkAvailable: Boolean = true
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var playlistId: Long = -1

    @Inject
    lateinit var singleTopActivity: PendingIntent

    @Inject
    lateinit var setRecentSongUseCase: SetRecentSongUseCase

    @Inject
    lateinit var savePlaybackStateUseCase: SavePlaybackStateUseCase

    @Inject
    lateinit var updateSongCounterUseCase: UpdateSongCounterUseCase

    @Inject
    lateinit var songApi: SongApi

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        initSessionAndPlayer()
        registerListener()
        setListener(MediaSessionServiceListener())
        registerBroadcast()
    }

    var lastMediaItemId: String? = null
    private fun registerListener() {
        listener = object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                lastMediaItemId = mediaItem?.mediaId
                saveDataToDB()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                this@PlaybackService.playbackState = playbackState
            }
        }
        mediaSession.player.addListener(listener)
    }

    private fun saveDataToDB() {
        serviceScope.launch {
            delay(DELAY_TIME_TO_CONFIRM)
            withContext(Dispatchers.Main) {
                if (mediaSession.player.isPlaying) {
                    val songId = mediaSession.player
                        .currentMediaItem
                        ?.mediaId
                    songId?.let {
                        withContext(Dispatchers.IO) {
                            try {
                                setRecentSongUseCase(it)
                                saveCounterAndReplay(it)
                                saveCounterToInternet(it)
                            } catch (_: Exception) {
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun saveCounterAndReplay(songId: String) {
        withContext(Dispatchers.IO) {
            updateSongCounterUseCase(songId)
        }
    }

    private suspend fun saveCounterToInternet(songId: String) {
        if (isNetworkAvailable) {
            try {
                songApi.updateCounter(songId = songId)
            } catch (_: IOException) {
            } catch (_: Exception) {
            }
        }
    }

    private fun registerBroadcast() {
        val serviceCallback = object : ServiceCallback {
            override fun stopService() {
                if (playbackState == Player.STATE_READY && !mediaSession.player.playWhenReady) {
                    val notificationManager =
                        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancelAll()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }
            }
        }
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                serviceCallback.stopService()
            }
        }
        val filter = IntentFilter(ACTION_STOP_SERVICE)
        ContextCompat.registerReceiver(
            this,
            receiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
        networkStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                isNetworkAvailable = intent?.getBooleanExtra(
                    MusicAppUtils.EXTRA_NETWORK_STATE,
                    false
                ) == true
            }
        }
        val networkFilter = IntentFilter(ACTION_NETWORK_STATE_CHANGED)
        ContextCompat.registerReceiver(
            this,
            networkStateReceiver,
            networkFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        saveCurrentSong()
    }

    override fun onDestroy() {
        if (this::mediaSession.isInitialized) {
            mediaSession.player.removeListener(listener)
            mediaSession.player.release()
            mediaSession.release()
        }
        serviceScope.cancel()
        unregisterReceiver(receiver)
        unregisterReceiver(networkStateReceiver)
        super.onDestroy()
    }

    private fun saveCurrentSong() {
        val currentMediaItem = mediaSession.player.currentMediaItem
        if (currentMediaItem != null) {
            val songId = currentMediaItem.mediaId
            val position = mediaSession.player.currentPosition
            val state = PlaybackStateData(songId, playlistId, position)
            savePlaybackStateUseCase(state)
        }
    }

    @Inject
    lateinit var backStackedActivity: PendingIntent

    private fun initSessionAndPlayer() {
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
        val intent = singleTopActivity
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MySessionCallback())
            .setSessionActivity(intent)
            .build()
    }

    private fun ensureNotificationChannel(managerCompat: NotificationManagerCompat) {
        if (managerCompat.getNotificationChannel(CHANNEL_ID) != null) {
            return
        }
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        managerCompat.createNotificationChannel(channel)
    }

    @UnstableApi
    private inner class MediaSessionServiceListener : Listener {
        override fun onForegroundServiceStartNotAllowedException() {
            if (Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val notificationManagerCompat =
                NotificationManagerCompat.from(this@PlaybackService)
            ensureNotificationChannel(notificationManagerCompat)
            val builder =
                NotificationCompat.Builder(this@PlaybackService, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_album)
                    .setContentTitle(getString(R.string.notification_channel_name))
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(getString(R.string.notification_content_text))
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
            val pendingIntent = backStackedActivity
            builder.setContentIntent(pendingIntent)
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build())
        }
    }

    inner class MySessionCallback : MediaSession.Callback {

        @OptIn(UnstableApi::class)
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS
                .buildUpon()
                .add(customCommand)
                .build()
            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }

        @OptIn(UnstableApi::class)
        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            if (customCommand.customAction == ACTION_PLAY_PLAYLIST) {
                playlistId = args.getLong(EXTRA_PLAYLIST_ID)
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
            return Futures.immediateFuture(SessionResult(SessionError.ERROR_BAD_VALUE))
        }
    }

    interface ServiceCallback {
        fun stopService()
    }

    companion object {
        private const val NOTIFICATION_ID = 9999
        private const val DELAY_TIME_TO_CONFIRM = 3000L
        private const val CHANNEL_ID = "media_playback_channel"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val ACTION_NETWORK_STATE_CHANGED = "ACTION_NETWORK_STATE_CHANGED"

    }
}