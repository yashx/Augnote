package com.github.yashx.augnote.list

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yashx.augnote.AugnoteQueries
import com.github.yashx.augnote.FileOpenerHelper
import com.github.yashx.augnote.ItemsInFolder
import com.github.yashx.augnote.R
import com.github.yashx.augnote.databinding.FragmentListBinding
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ListFragment : Fragment(), ListRecyclerViewAdapter.OnItemClickListener, FileOpenerHelper.Listener {

    private val queries: AugnoteQueries by inject()
    private lateinit var binding: FragmentListBinding
    private val args: ListFragmentArgs by navArgs()
    private val fileOpenerHelper: FileOpenerHelper by lazy { FileOpenerHelper(requireContext(), this) }
    private lateinit var listRecyclerViewAdapter: ListRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listRecyclerViewAdapter = ListRecyclerViewAdapter(this@ListFragment).apply {
            setHasStableIds(true)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listRecyclerViewAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch {
            queries.itemsInFolder(args.folderId).asFlow().mapToList().collect {
                listRecyclerViewAdapter.changeData(it)
            }
        }

    }

    override fun onFail(reason: FileOpenerHelper.FailureReason) {
        Toast.makeText(requireContext(), reason.message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: ItemsInFolder) {
        when (item.type) {
            "Folder" -> findNavController().navigate(ListFragmentDirections.actionListFragmentSelf(item.id))
            "Tag" -> {
                val uri = Uri.parse(item.data)
                fileOpenerHelper.openUri(uri)
            }
        }
    }

    override fun onItemLongClick(item: ItemsInFolder) {
        when (item.type) {
            "Folder" -> {queries.deleteFolder(item.id); listRecyclerViewAdapter.notifyDataSetChanged()}
            "Tag" -> {queries.deleteTag(item.id); listRecyclerViewAdapter.notifyDataSetChanged()}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.addFolderToList -> {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToAddFolderDialogFragment(args.folderId))
                true
            }
            R.id.addRelationToList -> {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToTagDialogFragment(args.folderId))
                true
            }
            R.id.searchList, R.id.barcodeScan -> true
            else -> false
        }
}