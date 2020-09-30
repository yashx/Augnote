package com.github.yashx.augnote.helper

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import timber.log.Timber


/**
 * Helper class to start activity to select a file and persist its permission
 *
 * @property fragment
 * @property listener
 */
//class FileSelectorHelper(private val activity: Activity, private val listener: Listener) {
    class FileSelectorHelper(private val fragment: Fragment, private val listener: Listener) {

    enum class FailureReason(val message: String) {
        CANT_GET_URI("Not able to find an activity to get Uri"),
        CANT_GET_PERMISSION("Not able to get persistable Uri permission"),
        UNKNOWN("Unexpected Error")
    }

    interface Listener {
        /**
         * Successfully selected a file
         *
         * @param uri Uri of selected file
         */
        fun onSuccess(uri: Uri)

        /**
         * Failed to select a file
         *
         * @param reason Reason why failed to select file
         */
        fun onFail(reason: FailureReason)
    }

    companion object {
        const val PIN_CREATOR_ID = 451
    }

    /**
     * Launches intent to select a file
     * Remember to call [handleActivityResult] in [Activity.onActivityResult] of calling Activity
     */
    fun launchIntentToSelectFile() {
        Intent().apply {
            action = Intent.ACTION_OPEN_DOCUMENT
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "*/*"
        }.let {
            try {
                fragment.startActivityForResult(Intent.createChooser(it, "Pick"), PIN_CREATOR_ID)
            } catch (e: ActivityNotFoundException) {
                with(FailureReason.CANT_GET_URI) {
                    Timber.e(e)
                    listener.onFail(this)
                }
            } catch (e: Exception) {
                with(FailureReason.UNKNOWN) {
                    Timber.e(e)
                    listener.onFail(this)
                }
            }
        }
    }

    /**
     * Handles activity result to get Uri of file and persisting permission
     */
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK)
            if (requestCode == PIN_CREATOR_ID)
                data?.data?.let {
                    try {
                        fragment.requireContext().contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        listener.onSuccess(it)
                    } catch (e: Exception) {
                        with(FailureReason.CANT_GET_PERMISSION) {
                            Timber.e(e)
                            listener.onFail(this)
                        }
                    }
                }
    }
}