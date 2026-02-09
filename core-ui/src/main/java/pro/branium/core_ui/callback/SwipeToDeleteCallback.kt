package pro.branium.core_ui.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pro.branium.core_model.SongModel

class SwipeToDeleteCallback(
    private val helperAdapter: ItemTouchHelperAdapter,
    direction: Int = 0
) : ItemTouchHelper.SimpleCallback(
    direction,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        helperAdapter.onItemDismiss(position)
    }
}

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
}

interface OnUndoListener {
    fun onUndo()
}

interface OnItemDeleteListener {
    fun onItemDelete(position: Int, songModel: SongModel, callback: OnUndoListener)
}