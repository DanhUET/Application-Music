package pro.branium.feature_recommended.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.core_ui.databinding.ItemSongBinding
import pro.branium.core_ui.model.DisplaySongModel

class SongPagingAdapter(
    private val onSongClickListener: (songModel: DisplaySongModel, index: Int) -> Unit,
    private val onSongOptionMenuClickListener: (songModel: DisplaySongModel) -> Unit
) : PagingDataAdapter<DisplaySongModel, SongPagingAdapter.SongViewHolder>(
    SongDiffCallback()
) {

    override fun onBindViewHolder(holder: SongViewHolder, position: Int, payloads: List<Any?>) {
        val song = getItem(position)
        if (payloads.contains(SongDiffCallback.PAYLOAD_PLAYING_STATE)) {
            song?.let {
                holder.updatePlayingState(it)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        if (song != null) {
            holder.bind(song, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSongBinding.inflate(layoutInflater, parent, false)
        return SongViewHolder(binding, onSongClickListener, onSongOptionMenuClickListener)
    }

    class SongViewHolder(
        private val binding: ItemSongBinding,
        private val onSongClickListener: (songModel: DisplaySongModel, index: Int) -> Unit,
        private val onSongOptionMenuClickListener: (songModel: DisplaySongModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            songModel: DisplaySongModel,
            index: Int
        ) {
            binding.song = songModel
            binding.executePendingBindings()
            setupListener(songModel, index)
        }

        private fun setupListener(songModel: DisplaySongModel, index: Int) {
            binding.root.setOnClickListener {
                onSongClickListener(songModel, index)
            }
            binding.btnItemSongOption.setOnClickListener {
                onSongOptionMenuClickListener(songModel)
            }
            binding.root.setOnLongClickListener {
                onSongOptionMenuClickListener(songModel)
                true
            }
        }

        fun updatePlayingState(songModel: DisplaySongModel) {
            binding.song = songModel
            binding.executePendingBindings()
        }
    }

    class SongDiffCallback : DiffUtil.ItemCallback<DisplaySongModel>() {
        override fun areItemsTheSame(
            oldItem: DisplaySongModel,
            newItem: DisplaySongModel
        ): Boolean {
            return oldItem.song.id == newItem.song.id
        }

        override fun areContentsTheSame(
            oldItem: DisplaySongModel,
            newItem: DisplaySongModel
        ): Boolean {
            return oldItem == newItem &&
                    oldItem.isNowPlaying == newItem.isNowPlaying &&
                    oldItem.isPlaying == newItem.isPlaying
        }

        override fun getChangePayload(
            oldItem: DisplaySongModel,
            newItem: DisplaySongModel
        ): Any? {
            return if (oldItem.isNowPlaying != newItem.isNowPlaying ||
                oldItem.isPlaying != newItem.isPlaying
            ) {
                PAYLOAD_PLAYING_STATE
            } else {
                null
            }
        }

        companion object {
            const val PAYLOAD_PLAYING_STATE = "PAYLOAD_PLAYING_STATE"
        }
    }
}