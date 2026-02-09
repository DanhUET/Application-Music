package pro.branium.feature_playlist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.feature_playlist.databinding.ItemPlaylistBinding
import pro.branium.feature_playlist.ui.model.PlaylistItemModel

class PlaylistAdapter(
    private val listener: OnPlaylistClickListener,
    private val showOptionMenu: Boolean = true
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    private val _playlists = mutableListOf<PlaylistItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaylistBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return _playlists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_playlists[position], showOptionMenu)
    }

    fun updatePlaylists(newPlaylists: List<PlaylistItemModel>) {
        val diffCallback = PlaylistDiffCallback(_playlists, newPlaylists)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _playlists.clear()
        _playlists.addAll(newPlaylists)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private val binding: ItemPlaylistBinding,
        private val listener: OnPlaylistClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: PlaylistItemModel, showOptionMenu: Boolean) {
            binding.playlist = playlist
            binding.root.setOnClickListener {
                playlist.let { playlist ->
                    listener.onPlaylistClick(playlist.playlistId)
                }
            }
            if (showOptionMenu) {
                binding.btnItemPlaylistOption.visibility = View.VISIBLE
                binding.btnItemPlaylistOption.setOnClickListener {
                    playlist.let {
                        listener.onPlaylistMenuOptionClick(it.playlistId)
                    }
                }
            } else {
                binding.btnItemPlaylistOption.visibility = View.GONE
            }
            binding.root.setOnLongClickListener {
                playlist.let {
                    listener.onPlaylistMenuOptionClick(it.playlistId)
                }
                true
            }
        }
    }

    class PlaylistDiffCallback(
        private val oldList: List<PlaylistItemModel>,
        private val newList: List<PlaylistItemModel>
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
            val oldPlaylistId = oldList[oldItemPosition].playlistId
            val newPlaylistId = newList[newItemPosition].playlistId
            return oldPlaylistId == newPlaylistId
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface OnPlaylistClickListener {
        fun onPlaylistClick(playlistId: Long)
        fun onPlaylistMenuOptionClick(playlistId: Long)
    }
}