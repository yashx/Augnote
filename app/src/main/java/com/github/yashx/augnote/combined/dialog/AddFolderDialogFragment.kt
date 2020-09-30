package com.github.yashx.augnote.combined.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.github.yashx.augnote.AugnoteQueries
import com.github.yashx.augnote.R
import com.github.yashx.augnote.databinding.DialogFragmentAddFolderBinding
import org.koin.android.ext.android.inject

class AddFolderDialogFragment : DialogFragment() {

    private val queries: AugnoteQueries by inject()
    private val args: AddFolderDialogFragmentArgs by navArgs()
    private lateinit var binding: DialogFragmentAddFolderBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogFragmentAddFolderBinding.inflate(layoutInflater).apply {
            root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            input.setText(requireContext().getString(R.string.new_folder))
        }

        return with(AlertDialog.Builder(context, R.style.AlertDialogStyle)) {
            setTitle(R.string.folder_name)
            setView(binding.root)
            setPositiveButton(R.string.create, null)
            setNegativeButton(R.string.cancel) { _, _ ->
                this@AddFolderDialogFragment.dismiss()
            }
            create()
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog doesn't dismiss automatically when positive button is clicked
        (dialog as? AlertDialog)?.apply {
            getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
                if (binding.input.text.toString().isBlank()) {
                    Toast.makeText(context, R.string.no_name_given, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                queries.insertFolder(binding.input.text.toString(), args.parentFolderId)
                this@AddFolderDialogFragment.dismiss()
            }
        }
    }
}