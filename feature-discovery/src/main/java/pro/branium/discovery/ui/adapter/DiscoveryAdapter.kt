package pro.branium.discovery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pro.branium.discovery.databinding.ItemDiscoveryArtistBinding
import pro.branium.discovery.databinding.ItemDiscoveryForYouBinding
import pro.branium.discovery.databinding.ItemDiscoveryMostHeardBinding

class DiscoveryAdapter(
    private val sections: List<DiscoverySection>
) : RecyclerView.Adapter<DiscoveryAdapter.SectionViewHolder>() {
    sealed class DiscoverySection {
        object Artist : DiscoverySection()
        object MostHeard : DiscoverySection()
        object ForYou : DiscoverySection()
    }

    sealed class SectionViewHolder(
        bindingRoot: View
    ) : RecyclerView.ViewHolder(bindingRoot) {
        class ArtistViewHolder(
            binding: ItemDiscoveryArtistBinding
        ) : SectionViewHolder(binding.root)

        class MostHeardViewHolder(
            binding: ItemDiscoveryMostHeardBinding
        ) : SectionViewHolder(binding.root)

        class ForYouViewHolder(
            binding: ItemDiscoveryForYouBinding
        ) : SectionViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int): Int {
        return when (sections[position]) {
            is DiscoverySection.Artist -> 0
            is DiscoverySection.MostHeard -> 1
            is DiscoverySection.ForYou -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = ItemDiscoveryArtistBinding.inflate(layoutInflater, parent, false)
                SectionViewHolder.ArtistViewHolder(binding)
            }

            1 -> {
                val binding = ItemDiscoveryMostHeardBinding.inflate(layoutInflater, parent, false)
                SectionViewHolder.MostHeardViewHolder(binding)
            }

            else -> {
                val binding = ItemDiscoveryForYouBinding.inflate(layoutInflater, parent, false)
                SectionViewHolder.ForYouViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        // CustomViews (ArtistView, MostHeardView, ForYouView) tự lo data
        when (holder) {
            is SectionViewHolder.ArtistViewHolder -> {
                // holder.binding.artistView nếu cần
            }

            is SectionViewHolder.MostHeardViewHolder -> {
                // holder.binding.mostHeardView nếu cần
            }

            is SectionViewHolder.ForYouViewHolder -> {
                // holder.binding.forYouView nếu cần
            }
        }
    }

    override fun getItemCount(): Int = sections.size
}
