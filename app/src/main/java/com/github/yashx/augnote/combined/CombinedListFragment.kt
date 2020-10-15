package com.github.yashx.augnote.combined

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yashx.augnote.*
import com.github.yashx.augnote.databinding.FragmentCombinedListBinding
import com.github.yashx.augnote.helper.FileOpenerHelper
import com.mikepenz.aboutlibraries.LibsBuilder
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CombinedListFragment : BaseFragment(), CombinedListAdapter.OnItemClickListener, FileOpenerHelper.Listener {

    private val queries: AugnoteQueries by inject()
    private lateinit var binding: FragmentCombinedListBinding
    private val args: CombinedListFragmentArgs by navArgs()
    private val fileOpenerHelper: FileOpenerHelper by lazy { FileOpenerHelper(requireContext(), this) }
    private lateinit var combinedListAdapter: CombinedListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentCombinedListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        combinedListAdapter = CombinedListAdapter(this@CombinedListFragment).apply {
            setHasStableIds(true)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch {
            queries.itemsInFolder(args.folderId).asFlow().mapToList().collect {
                (binding.recyclerView).apply {
                    if (adapter == null) adapter = combinedListAdapter
                }
                combinedListAdapter.changeData(it)
            }
        }

    }

    override fun shouldShowBackButton() = args.folderId != 0L

    override fun fragmentTitle() = queries.getFolder(args.folderId).executeAsOne().name

    override fun onFail(reason: FileOpenerHelper.FailureReason) {
        Toast.makeText(requireContext(), R.string.cant_access_file, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: ItemsInFolder) {
        when (item.type) {
            "Folder" -> findNavController().navigate(CombinedListFragmentDirections.actionCombinedListFragmentSelf(item.id))
            "Tag" -> {
                val uri = Uri.parse(item.data)
                fileOpenerHelper.openUri(uri)
            }
        }
    }

    override fun onItemLongClick(item: ItemsInFolder) {
        when (item.type) {
            "Folder" -> {
                queries.deleteFolder(item.id); combinedListAdapter.notifyDataSetChanged()
            }
            "Tag" -> {
                queries.deleteTag(item.id); combinedListAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list_fragment, menu)
        if (args.folderId == 0L) {
            menu.findItem(R.id.addTagToList).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.addFolderToList -> {
                findNavController().navigate(CombinedListFragmentDirections.actionCombinedListFragmentToAddFolderDialogFragment(args.folderId))
                true
            }
            R.id.addTagToList -> {
                findNavController().navigate(CombinedListFragmentDirections.actionCombinedListFragmentToTagDialogFragment(args.folderId))
                true
            }
            R.id.searchList -> {
                findNavController().navigate(CombinedListFragmentDirections.actionCombinedListFragmentToSearchListFragment())
                true
            }
            R.id.barcodeScan -> {
                // change
                findNavController().navigate(CombinedListFragmentDirections.actionCombinedListFragmentToAboutFragment(LibsBuilder()))
                true
            }
            else -> false
        }
}