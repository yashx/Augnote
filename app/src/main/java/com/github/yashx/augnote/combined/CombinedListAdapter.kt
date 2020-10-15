package com.github.yashx.augnote.combined

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yashx.augnote.ItemsInFolder
import com.github.yashx.augnote.R
import com.github.yashx.augnote.databinding.ListItemBinding
import com.github.yashx.augnote.databinding.ViewEmptyBinding
import com.github.yashx.augnote.helper.TagType
import com.github.yashx.augnote.helper.tagType

class CombinedListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val EMPTY_LIST_ITEM = 0
        const val LIST_ITEM = 1
    }

    interface OnItemClickListener {
        fun onItemClick(item: ItemsInFolder)
        fun onItemLongClick(item: ItemsInFolder)
    }

    class ListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
    class ViewEmptyHolder(val binding: ViewEmptyBinding) : RecyclerView.ViewHolder(binding.root)

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
        if (viewType == LIST_ITEM)
            ListViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else
            ViewEmptyHolder(ViewEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LIST_ITEM) {
            holder as ListViewHolder
            with(listItems[position]) {
                holder.binding.title.text = name
                val drawableId = when (type) {
                    "Folder" -> R.drawable.ic_folder
                    else -> getTagDrawable(Uri.parse(data), holder.itemView.context)
                }
                holder.binding.icon.setImageResource(drawableId)
                holder.itemView.setOnClickListener { listener.onItemClick(this) }
                holder.itemView.setOnLongClickListener { listener.onItemLongClick(this); true }
            }
        }
    }

    private fun getTagDrawable(uri: Uri, context: Context) =
        when (uri.tagType(context)) {
            TagType.VIDEO -> R.drawable.ic_video
            TagType.IMAGE -> R.drawable.ic_image
            TagType.AUDIO -> R.drawable.ic_audio
            TagType.FILE -> R.drawable.ic_file
            TagType.WEB -> R.drawable.ic_web
        }

    override fun getItemCount() = if (listItems.size == 0) 1 else listItems.size

    override fun getItemId(position: Int) = if (listItems.size == 0) -1 else listItems[position].hashCode().toLong()

    override fun getItemViewType(position: Int) = if (listItems.size == 0) EMPTY_LIST_ITEM else LIST_ITEM
}