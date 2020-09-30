package com.github.yashx.augnote.combined

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yashx.augnote.ItemsInFolder
import com.github.yashx.augnote.R
import com.github.yashx.augnote.databinding.ListItemBinding

class CombinedListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<CombinedListAdapter.ListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: ItemsInFolder)
        fun onItemLongClick(item: ItemsInFolder)
    }

    class ListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var listItems = mutableListOf<ItemsInFolder>()

    /**
     * Clears data adapter is showing and calls [notifyDataSetChanged]
     *
     */
    fun clearData() {
        listItems.clear()
        notifyDataSetChanged()
    }


    /**
     * Changes data adapter is showing and calls [notifyDataSetChanged]
     *
     * @param list Should be a list of objects [ItemsInFolder]
     */
    fun changeData(list: List<ItemsInFolder>) {
        listItems.clear()
        listItems.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(listItems[position]) {
            holder.binding.title.text = name
            val drawableId = when (type) {
                "Folder" -> R.drawable.ic_folder
                else -> R.drawable.ic_link
            }
            holder.binding.icon.setImageResource(drawableId)
            holder.itemView.setOnClickListener { listener.onItemClick(this) }
            holder.itemView.setOnLongClickListener { listener.onItemLongClick(this); true }
        }
    }

    override fun getItemCount() = listItems.size

    override fun getItemId(position: Int): Long {
        return listItems[position].hashCode().toLong()
    }
}