package pro.branium.core_ui.adapter

import androidx.recyclerview.widget.DiffUtil
import pro.branium.core_ui.model.DisplaySongModel

class SongDiffCallback(
    private val oldList: List<DisplaySongModel>,
    private val newList: List<DisplaySongModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition].song.id == newList[newItemPosition].song.id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem.isNowPlaying != newItem.isNowPlaying
            || oldItem.isPlaying != newItem.isPlaying) {
            PAYLOAD_PLAYING_STATE
        } else {
            null
        }
    }

    companion object {
        const val PAYLOAD_PLAYING_STATE = "PAYLOAD_PLAYING_STATE"
    }
}