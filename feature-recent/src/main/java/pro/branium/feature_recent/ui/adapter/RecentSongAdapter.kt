package pro.branium.feature_recent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pro.branium.core_ui.adapter.SongDiffCallback
import pro.branium.core_ui.databinding.ItemSongBinding
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_utils.MusicAppUtils

class RecentSongAdapter(
    private val songListener: OnSongClickListener,
    private val songOptionMenuListener: OnSongOptionMenuClickListener
) : RecyclerView.Adapter<RecentSongAdapter.ViewHolder>() {
    private val _songModels = mutableListOf<DisplaySongModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSongBinding.inflate(layoutInflater, parent, false)
        val layout = binding.root
        val vto = layout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = layout.measuredWidth
                val height = layout.measuredHeight
                val deltaX = (MusicAppUtils.DEFAULT_MARGIN_END * MusicAppUtils.DENSITY).toInt()
                binding.rootLayoutSongItem.layoutParams.width = width - deltaX
                binding.rootLayoutSongItem.layoutParams.height = height
            }
        })
        return ViewHolder(binding, songListener, songOptionMenuListener)
    }

    override fun getItemCount(): Int {
        return _songModels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_songModels[position], position)
    }

    fun updateSongs(newSongModels: List<DisplaySongModel>) {
        val diffCallback = SongDiffCallback(_songModels, newSongModels)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _songModels.clear()
        _songModels.addAll(newSongModels)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private val binding: ItemSongBinding,
        private val songListener: OnSongClickListener,
        private val songOptionMenuListener: OnSongOptionMenuClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(songModel: DisplaySongModel, index: Int) {
            setupView(songModel)
            setupListener(songModel, index)
        }

        private fun setupView(songModel: DisplaySongModel) {
            binding.song = songModel
        }

        private fun setupListener(songModel: DisplaySongModel, position: Int) {
            binding.root.setOnClickListener {
                songListener.onClick(songModel, position)
            }
            binding.btnItemSongOption.setOnClickListener {
                songOptionMenuListener.onClick(songModel)
            }
            binding.root.setOnLongClickListener {
                songOptionMenuListener.onClick(songModel)
                true
            }
        }
    }

    interface OnSongClickListener {
        fun onClick(songModel: DisplaySongModel, index: Int)
    }

    interface OnSongOptionMenuClickListener {
        fun onClick(songModel: DisplaySongModel)
    }
}