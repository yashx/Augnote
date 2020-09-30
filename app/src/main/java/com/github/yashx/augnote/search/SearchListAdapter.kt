package com.github.yashx.augnote.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.yashx.augnote.R
import com.github.yashx.augnote.Tag
import com.github.yashx.augnote.databinding.ListItemBinding

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
            holder.binding.icon.setImageResource(R.drawable.ic_link)
            holder.itemView.setOnClickListener { listener.onItemClick(this) }
        }
    }

    override fun getItemCount() = listItems.size

    override fun getItemId(position: Int) = listItems[position].hashCode().toLong()
}