package pro.branium.feature_playlist.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_ui.base.PlaylistOptionMenuActionListener
import pro.branium.feature_playlist.databinding.FragmentPlaylistOptionMenuDialogBinding
import pro.branium.feature_playlist.databinding.ItemPlaylistOptionMenuBinding
import pro.branium.feature_playlist.ui.menu.PlaylistOptionMenu
import pro.branium.feature_playlist.ui.menu.PlaylistOptionMenuItem
import pro.branium.feature_playlist.viewmodel.PlaylistOptionMenuViewModel

@AndroidEntryPoint
class PlaylistOptionMenuDialogFragment(
    private val listener: PlaylistOptionMenuActionListener
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentPlaylistOptionMenuDialogBinding
    private lateinit var adapter: PlaylistOptionMenuItemAdapter
    private val viewModel: PlaylistOptionMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistOptionMenuDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        adapter = PlaylistOptionMenuItemAdapter(
            listener = object : OnOptionMenuItemClickListener {
                override fun onClick(item: PlaylistOptionMenuItem) {
                    onMenuItemClick(item)
                }
            }
        )
        binding.rvPlaylistOptionMenu.adapter = adapter
    }

    private fun onMenuItemClick(item: PlaylistOptionMenuItem) {
        when (item.option) {
            PlaylistOptionMenu.VIEW_DETAIL -> viewDetail()
            PlaylistOptionMenu.UPDATE -> update()
            PlaylistOptionMenu.DELETE -> delete()
            PlaylistOptionMenu.SYNCHRONIZE -> synchronize()
        }
    }

    private fun viewDetail() {
        listener.onViewDetail()
        dismiss()
    }

    private fun update() {
        listener.onUpdate()
        dismiss()
    }

    private fun delete() {
        listener.onDelete()
        dismiss()
    }

    private fun synchronize() {
        listener.onSynchronize()
        dismiss()
    }

    private fun setupObserver() {
        viewModel.menuItems.observe(viewLifecycleOwner) { items ->
            adapter.updateMenuItems(items)
        }
    }

    class PlaylistOptionMenuItemAdapter(
        private val playlistOptionMenuItems: MutableList<PlaylistOptionMenuItem> = mutableListOf(),
        private val listener: OnOptionMenuItemClickListener
    ) : RecyclerView.Adapter<PlaylistOptionMenuItemAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPlaylistOptionMenuBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding, listener)
        }

        override fun getItemCount(): Int {
            return playlistOptionMenuItems.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(playlistOptionMenuItems[position])
        }

        fun updateMenuItems(items: List<PlaylistOptionMenuItem>) {
            playlistOptionMenuItems.addAll(items)
            notifyItemRangeChanged(0, playlistOptionMenuItems.size)
        }

        class ViewHolder(
            private val binding: ItemPlaylistOptionMenuBinding,
            private val listener: OnOptionMenuItemClickListener
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: PlaylistOptionMenuItem) {
                val title = binding.root.context.getString(item.menuItemTitle)
                binding.tvItemMenuTitle.text = title
                Glide.with(binding.root)
                    .load(item.iconId)
                    .into(binding.imgItemMenuIcon)
                binding.root.setOnClickListener {
                    listener.onClick(item)
                }
            }
        }
    }

    interface OnOptionMenuItemClickListener {
        fun onClick(item: PlaylistOptionMenuItem)
    }

    companion object {
        const val TAG = "PlaylistOptionMenuDialogFragment"
    }
}