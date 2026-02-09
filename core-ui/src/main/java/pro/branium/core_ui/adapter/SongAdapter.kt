package pro.branium.core_ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.core_model.SongModel
import pro.branium.core_ui.callback.ItemTouchHelperAdapter
import pro.branium.core_ui.callback.OnItemDeleteListener
import pro.branium.core_ui.callback.OnUndoListener
import pro.branium.core_ui.databinding.ItemSongBinding
import pro.branium.core_ui.model.DisplaySongModel

class SongAdapter(
    private val onSongClick: (song: SongModel, index: Int) -> Unit,
    private val onSongOptionMenuClick: (songModel: DisplaySongModel) -> Unit,
    private val onItemDeleteListener: OnItemDeleteListener? = null
) : RecyclerView.Adapter<SongAdapter.ViewHolder>(), ItemTouchHelperAdapter {
    private val songModels: MutableList<DisplaySongModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSongBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, onSongClick, onSongOptionMenuClick)
    }

    override fun getItemCount(): Int {
        return songModels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.contains(SongDiffCallback.Companion.PAYLOAD_PLAYING_STATE)) {
            holder.updateNowPlaying(songModels[position])
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songModels[position], position)
    }

    override fun onItemDismiss(position: Int) {
        val removedSong = songModels.removeAt(position)
        notifyItemRemoved(position)
        onItemDeleteListener?.onItemDelete(
            position,
            removedSong.song,
            object : OnUndoListener {
                override fun onUndo() {
                    songModels.add(position, removedSong)
                    notifyItemInserted(position)
                }
            }
        )
    }

    fun updateSongs(newSongModels: List<DisplaySongModel>?) {
        val diffCallback = SongDiffCallback(songModels, newSongModels ?: emptyList())
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        songModels.clear()
        songModels.addAll(newSongModels ?: emptyList())
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private val binding: ItemSongBinding,
        private val onSongClickListener: (song: SongModel, index: Int) -> Unit,
        private val onSongOptionMenuClickListener: (songModel: DisplaySongModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(songModel: DisplaySongModel, index: Int) {
            binding.song = songModel
            applyNowPlayingUI(songModel)
            setupListener(songModel, index)
            binding.executePendingBindings()
        }

        fun updateNowPlaying(songModel: DisplaySongModel) {
            applyNowPlayingUI(songModel)
            binding.executePendingBindings()
        }

        private fun applyNowPlayingUI(songModel: DisplaySongModel) {
            binding.root.isActivated = songModel.isNowPlaying
        }

        private fun setupListener(songModel: DisplaySongModel, index: Int) {
            binding.root.setOnClickListener {
                onSongClickListener(songModel.song, index)
            }
            binding.btnItemSongOption.setOnClickListener {
                onSongOptionMenuClickListener(songModel)
            }
            binding.root.setOnLongClickListener {
                onSongOptionMenuClickListener(songModel)
                true
            }
        }
    }
}
