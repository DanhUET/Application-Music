package pro.branium.feature_song.action

import android.view.View
import pro.branium.core_model.action.SongAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongActionDispatcher @Inject constructor(
    private val handlers: Set<@JvmSuppressWildcards SongActionHandler>
) {
    suspend fun dispatch(action: SongAction, anchorView: View? = null) {
        handlers.forEach { handler ->
            handler.handle(action, anchorView)
        }
    }
}