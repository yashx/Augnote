package com.github.yashx.augnote

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.github.yashx.augnote.databinding.DialogFragmentTagBinding
import org.koin.android.ext.android.inject

class TagDialogFragment : DialogFragment(), FileSelectorHelper.Listener {
    private val queries: AugnoteQueries by inject()
    private val args: TagDialogFragmentArgs by navArgs()
    private lateinit var binding: DialogFragmentTagBinding

    private var linkTo: String? = null

    private val fileSelectorHelper: FileSelectorHelper by lazy { FileSelectorHelper(this, this) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogFragmentTagBinding.inflate(layoutInflater).apply {
            root.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            relationName.setText(requireContext().getString(R.string.new_tag))
            selectFileButton.setOnClickListener {
                fileSelectorHelper.launchIntentToSelectFile()
            }
            chipGroupType.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.chip_type_file -> {
                        inputUrlUri.isVisible = false
                        selectFileButton.isVisible = true
                    }
                    R.id.chip_type_url_uri -> {
                        inputUrlUri.isVisible = true
                        selectFileButton.isVisible = false
                    }
                }
                selectFileButton.setText(R.string.select_file)
                inputUrlUri.text.clear()
                linkTo = null
            }
            inputUrlUri.addTextChangedListener { linkTo = it.toString() }
        }

        return with(AlertDialog.Builder(context, R.style.AlertDialogStyle)) {
            setView(binding.root)
            setPositiveButton(R.string.create, null)
            setNegativeButton(R.string.cancel, null)
            create()
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog doesn't dismiss automatically when positive button is clicked
        (dialog as? AlertDialog)?.apply {
            getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
                if (binding.relationName.text.toString().isBlank()) {
                    Toast.makeText(requireContext(), R.string.no_name_given, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (linkTo == null || linkTo?.isBlank() == true) {
                    Toast.makeText(requireContext(), R.string.no_select, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                queries.insertTag(binding.relationName.text.toString(), args.parentFolderId, linkTo!!)
                this@TagDialogFragment.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fileSelectorHelper.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess(uri: Uri) {
        linkTo = uri.toString()
        binding.selectFileButton.setText(R.string.file_selected)
    }

    override fun onFail(reason: FileSelectorHelper.FailureReason) {
        Toast.makeText(requireContext(), reason.message, Toast.LENGTH_SHORT).show()
    }
}