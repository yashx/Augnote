package com.github.yashx.augnote.search

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.yashx.augnote.AugnoteQueries
import com.github.yashx.augnote.BaseFragment
import com.github.yashx.augnote.R
import com.github.yashx.augnote.Tag
import com.github.yashx.augnote.databinding.FragmentSearchListBinding
import com.github.yashx.augnote.helper.FileOpenerHelper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class SearchListFragment : BaseFragment(), SearchListAdapter.OnItemClickListener, FileOpenerHelper.Listener {
    private val queries: AugnoteQueries by inject()
    private lateinit var binding: FragmentSearchListBinding
    private val fileOpenerHelper: FileOpenerHelper by lazy { FileOpenerHelper(requireContext(), this) }
    private lateinit var searchListAdapter: SearchListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun shouldShowBackButton() = true

    override fun fragmentTitle() = "Search"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchListAdapter = SearchListAdapter(this).apply {
            setHasStableIds(true)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        lifecycleScope.launch {
            queries.getTagsForQuery("").executeAsList().also {
                (binding.recyclerView).apply {
                    if (adapter == null) adapter = searchListAdapter
                }
                searchListAdapter.changeData(it)
            }
        }


        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 == null)
                    return false
                lifecycleScope.launch {
                    queries.getTagsForQuery(p0).asFlow().mapToList().collect {
                        (binding.recyclerView).apply {
                            if (adapter == null) adapter = searchListAdapter
                        }
                        searchListAdapter.changeData(it)
                    }
                }
                return true
            }

        })
    }

    override fun onItemClick(item: Tag) {
        val uri = Uri.parse(item.linkTo)
        fileOpenerHelper.openUri(uri)
    }

    override fun onFail(reason: FileOpenerHelper.FailureReason) {
        Toast.makeText(requireContext(), R.string.cant_access_file, Toast.LENGTH_SHORT).show()
    }
}