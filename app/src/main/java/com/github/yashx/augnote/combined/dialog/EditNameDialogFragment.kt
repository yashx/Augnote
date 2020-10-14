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
import com.github.yashx.augnote.databinding.DialogFragmentEditTextBinding
import org.koin.android.ext.android.inject

class EditNameDialogFragment : DialogFragment() {
    private val queries: AugnoteQueries by inject()
    private val args: EditNameDialogFragmentArgs by navArgs()
    private lateinit var binding: DialogFragmentEditTextBinding
    private lateinit var name: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        name = when (args.itemType) {
            "Folder" -> queries.getFolder(args.itemId).executeAsOne().name
            else -> queries.getTag(args.itemId).executeAsOne().name

        }
        binding = DialogFragmentEditTextBinding.inflate(layoutInflater).apply {
            root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            input.setText(name)
        }

        return with(AlertDialog.Builder(context, R.style.AlertDialogStyle)) {
            setTitle(R.string.name)
            setView(binding.root)
            setPositiveButton(R.string.update, null)
            setNegativeButton(R.string.cancel) { _, _ ->
                this@EditNameDialogFragment.dismiss()
            }
            create()
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog doesn't dismiss automatically when positive button is clicked
        (dialog as? AlertDialog)?.apply {
            getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {

                with(binding.input.text.toString()) {
                    if (isBlank()) {
                        Toast.makeText(context, R.string.no_name_given, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (this != name) {
                        when (args.itemType) {
                            "Folder" -> queries.updateFolderName(this, args.itemId)
                            "Tag" -> queries.updateTagName(this, args.itemId)
                        }
                    }
                }
                this@EditNameDialogFragment.dismiss()
            }
        }
    }

}