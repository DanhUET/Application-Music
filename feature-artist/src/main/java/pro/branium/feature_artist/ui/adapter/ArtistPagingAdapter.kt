package pro.branium.feature_artist.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.core_ui.model.DisplayArtistModel
import pro.branium.feature_artist.databinding.ItemArtistBinding

class ArtistPagingAdapter(
    private val listener: ArtistListener
) : PagingDataAdapter<DisplayArtistModel, ArtistPagingAdapter.ArtistViewHolder>(
    ARTIST_COMPARATOR
) {
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        if (artist != null) {
            holder.bind(artist)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemArtistBinding.inflate(layoutInflater, parent, false)
        return ArtistViewHolder(binding, listener)
    }

    class ArtistViewHolder(
        private val binding: ItemArtistBinding,
        private val listener: ArtistListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artistModel: DisplayArtistModel) {
            binding.artist = artistModel
            binding.root.setOnClickListener {
                listener.onClick(artistModel)
            }
            binding.btnSubscribe.setOnClickListener {
                listener.onSubscribe(artistModel)
            }
        }
    }

    interface ArtistListener {
        fun onClick(artistModel: DisplayArtistModel)

        fun onSubscribe(artistModel: DisplayArtistModel)
    }

    companion object {
        private val ARTIST_COMPARATOR = object : DiffUtil.ItemCallback<DisplayArtistModel>() {
            override fun areItemsTheSame(
                oldItem: DisplayArtistModel,
                newItem: DisplayArtistModel
            ): Boolean {
                return oldItem.artist.id == newItem.artist.id
            }

            override fun areContentsTheSame(
                oldItem: DisplayArtistModel,
                newItem: DisplayArtistModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}