package com.github.yashx.augnote.combined.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.yashx.augnote.AugnoteQueries
import com.github.yashx.augnote.NavGraphDirections
import com.github.yashx.augnote.R
import com.github.yashx.augnote.databinding.DialogFragmentListItemOptionsBinding
import org.koin.android.ext.android.inject

class ItemOptionsDialogFragment : DialogFragment() {

    private val queries: AugnoteQueries by inject()
    private val args: ItemOptionsDialogFragmentArgs by navArgs()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogFragmentListItemOptionsBinding.inflate(layoutInflater).apply {
            root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        binding.deleteItemOption.setOnClickListener {
            when (args.type) {
                "Folder" -> {
                    queries.deleteFolder(args.id)
                }
                "Tag" -> {
                    queries.deleteTag(args.id)
                }
            }
            this@ItemOptionsDialogFragment.dismiss()
        }

        binding.editNameOption.setOnClickListener {
            findNavController().navigate(NavGraphDirections.actionGlobalEditNameDialogFragment(args.id, args.type))
        }

        return with(AlertDialog.Builder(context, R.style.AlertDialogStyle)) {
            setTitle(R.string.options)
            setView(binding.root)
            create()
        }
    }
}