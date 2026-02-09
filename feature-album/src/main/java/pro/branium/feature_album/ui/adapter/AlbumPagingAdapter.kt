package pro.branium.feature_album.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.feature_album.databinding.ItemAlbumBinding
import pro.branium.feature_album.domain.model.AlbumSummaryModel

class AlbumPagingAdapter(
    private val listener: (albumModel: AlbumSummaryModel) -> Unit
) : PagingDataAdapter<AlbumSummaryModel, AlbumPagingAdapter.AlbumViewHolder>(ALBUM_COMPARATOR) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAlbumBinding.inflate(layoutInflater, parent, false)
        return AlbumViewHolder(binding, listener)
    }

    override fun onBindViewHolder(
        holder: AlbumViewHolder,
        position: Int
    ) {
        val album = getItem(position)
        if (album != null) {
            holder.bind(album)
        }
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

    interface OnAlbumClickListener {
        fun onAlbumClick(albumModel: AlbumSummaryModel)
    }

    companion object {
        val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<AlbumSummaryModel>() {
            override fun areItemsTheSame(
                oldItem: AlbumSummaryModel,
                newItem: AlbumSummaryModel
            ): Boolean {
                return oldItem.albumId == newItem.albumId
            }

            override fun areContentsTheSame(
                oldItem: AlbumSummaryModel,
                newItem: AlbumSummaryModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}