package pro.branium.feature_album.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.feature_album.databinding.ItemAlbumBinding
import pro.branium.feature_album.domain.model.AlbumSummaryModel

class AlbumAdapter(
    private val listener: (albumModel: AlbumSummaryModel) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    private val albums = mutableListOf<AlbumSummaryModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlbumBinding.inflate(inflater, parent, false)
        return AlbumViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int
    ) {
        val album = albums[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun update(newAlbums: List<AlbumSummaryModel>) {
        val diffCallback = AlbumDiffCallback(albums, newAlbums)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        albums.clear()
        albums.addAll(newAlbums)
        diffResult.dispatchUpdatesTo(this)
    }

    class AlbumViewHolder(
        private val binding: ItemAlbumBinding,
        private val listener: (albumModel: AlbumSummaryModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(albumModel: AlbumSummaryModel) {
            binding.album = albumModel
            binding.root.setOnClickListener {
                listener(albumModel)
            }
        }
    }

    class AlbumDiffCallback(
        private val oldList: List<AlbumSummaryModel>,
        private val newList: List<AlbumSummaryModel>
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
            return oldList[oldItemPosition].albumId == newList[newItemPosition].albumId
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}