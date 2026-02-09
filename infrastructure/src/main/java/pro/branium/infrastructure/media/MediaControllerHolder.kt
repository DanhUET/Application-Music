package pro.branium.infrastructure.media

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import pro.branium.core_playback.MediaControllerProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaControllerHolder @Inject constructor(
    @ApplicationContext appContext: Context,
    token: SessionToken
) : MediaControllerProvider {
    private val _controller = MutableStateFlow<MediaController?>(null)
    override val controllerFlow: StateFlow<MediaController?> = _controller

    init {
        val future = MediaController.Builder(appContext, token).buildAsync()
        // Đưa kết quả về main thread
        future.addListener(
            {
                try {
                    _controller.value = future.get()
                } catch (_: Throwable) {
                }
            },
            ContextCompat.getMainExecutor(appContext)
        )
    }

    /** Chờ tới khi controller sẵn sàng rồi trả về */
    override suspend fun await() = controllerFlow.filterNotNull().first()
}