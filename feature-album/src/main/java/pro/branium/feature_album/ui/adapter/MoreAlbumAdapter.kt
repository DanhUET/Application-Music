package pro.branium.feature_album.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import pro.branium.feature_album.databinding.ItemMoreAlbumBinding
import pro.branium.feature_album.domain.model.AlbumSummaryModel

class MoreAlbumAdapter(
    private val listener: (albumModel: AlbumSummaryModel) -> Unit
) : PagingDataAdapter<AlbumSummaryModel, MoreAlbumAdapter.MoreAlbumViewHolder>(
    AlbumPagingAdapter.ALBUM_COMPARATOR
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMoreAlbumBinding.inflate(inflater, parent, false)
        return MoreAlbumViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MoreAlbumViewHolder, position: Int) {
        val album = getItem(position)
        if (album != null) {
            holder.bind(album)
        }
    }

    class MoreAlbumViewHolder(
        private val binding: ItemMoreAlbumBinding,
        private val listener: (albumModel: AlbumSummaryModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumSummaryModel) {
            binding.album = album
            binding.root.setOnClickListener {
                listener(album)
            }
        }
    }
}