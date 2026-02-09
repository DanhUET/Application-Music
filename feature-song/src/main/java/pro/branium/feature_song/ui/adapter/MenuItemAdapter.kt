package pro.branium.feature_song.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.feature_song.databinding.ItemOptionMenuBinding


class MenuItemAdapter(
    private val songMenuItems: MutableList<SongMenuItem> = mutableListOf(),
    private val optionMenuClick: (menuItem: SongMenuItem) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOptionMenuBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, optionMenuClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songMenuItems[position])
    }

    override fun getItemCount(): Int {
        return songMenuItems.size
    }

    fun updateMenuItems(newItems: List<SongMenuItem>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = songMenuItems.size

            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return songMenuItems[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return songMenuItems[oldItemPosition] == newItems[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        songMenuItems.clear()
        songMenuItems.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private val binding: ItemOptionMenuBinding,
        private val optionMenuClick: (menuItem: SongMenuItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SongMenuItem) {
            // text
            binding.textItemMenuTitle.text = binding.root.context.getString(item.textRes)
            // main icon
            binding.imageItemMenuIcon.setImageResource(item.icon)

            // state icon
            if (item.stateIcon != null) {
                binding.imgItemMenuState.visibility = View.VISIBLE
                binding.imgItemMenuState.setImageResource(item.stateIcon!!)
            } else {
                binding.imgItemMenuState.visibility = View.GONE
            }

            // click
            binding.root.setOnClickListener {
                optionMenuClick(item)
            }
        }
    }
}