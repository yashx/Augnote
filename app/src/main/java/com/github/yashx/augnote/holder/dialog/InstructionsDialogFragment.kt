package com.github.yashx.augnote.holder.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.yashx.augnote.R
import com.github.yashx.augnote.utils.Constants

class InstructionsDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return with(AlertDialog.Builder(context, R.style.AlertDialogStyle)) {
            setTitle(R.string.instructions_title)
            setMessage(R.string.instructions_body)
            setPositiveButton(R.string.watch_video) { _, _ ->
                Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL)).also {
                    startActivity(Intent.createChooser(it,getString(R.string.watch_video)))
                }
            }
            setNegativeButton(R.string.dismiss,null)
            create()
        }
    }
}