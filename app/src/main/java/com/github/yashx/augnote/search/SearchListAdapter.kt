package com.github.yashx.augnote.search

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yashx.augnote.R
import com.github.yashx.augnote.Tag
import com.github.yashx.augnote.databinding.ListItemBinding
import com.github.yashx.augnote.helper.TagType
import com.github.yashx.augnote.helper.tagType

class SearchListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<SearchListAdapter.ListViewHolder>() {
    class ListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onItemClick(item: Tag)
    }

    private var listItems = mutableListOf<Tag>()

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
     * @param list Should be a list of objects [Tag]
     */
    fun changeData(list: List<Tag>) {
        listItems.clear()
        listItems.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(listItems[position]) {
            holder.binding.title.text = name
            holder.binding.icon.setImageResource(getTagDrawable(Uri.parse(linkTo), holder.itemView.context))
            holder.itemView.setOnClickListener { listener.onItemClick(this) }
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

    override fun getItemCount() = listItems.size

    override fun getItemId(position: Int) = listItems[position].hashCode().toLong()
}