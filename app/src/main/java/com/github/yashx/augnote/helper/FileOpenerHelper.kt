package com.github.yashx.augnote.helper

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import com.github.yashx.augnote.R
import timber.log.Timber
import java.io.FileNotFoundException


/**
 * Helper class to open or check a file given a Uri
 *
 * @property context
 * @property listener
 */
class FileOpenerHelper(private val context: Context, private val listener: Listener) {

    enum class FailureReason(val message: String) {
        FILE_DOES_NOT_EXIST("File doesn't exist"),
        PERMISSION_DENIED("Can't open file permission denied"),
        NO_ACTIVITY_TO_OPEN_FILE("No activity to open file"),
        UNKNOWN("Unexpected Error")
    }

    interface Listener {
        /**
         * Failed to select a file
         *
         * @param reason Reason why failed to select file
         */
        fun onFail(reason: FailureReason)
    }

    /**
     * Launches intent to open a Uri of file
     *
     * @param uri Uri of file to open
     */
    fun openUri(uri: Uri) {

        Timber.e(uri.toString())

        // checking if file can be accessed
        if (!(URLUtil.isHttpUrl(uri.toString()) or URLUtil.isHttpsUrl(uri.toString())))
            if (!canAccessFile(uri))
                return

        // trying to open file
        try {
            with(Intent(Intent.ACTION_VIEW)) {
                data = uri
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(
                    Intent.createChooser(
                        this,
                        context.resources.getString(R.string.open_file)
                    )
                )
            }
        }
//      As chooser is being created this is not need but still keeping it here
        catch (e: ActivityNotFoundException) {
            with(FailureReason.NO_ACTIVITY_TO_OPEN_FILE) {
                Timber.e(e)
                listener.onFail(this)
            }
        } catch (e: Exception) {
            with(FailureReason.UNKNOWN) {
                Timber.e(e)
                listener.onFail(this)
            }
            return
        }
    }

    /**
     * Checks if file that Uri is pointing to exists and permission to access it is given
     * and calls [Listener] methods if needed
     *
     * @return Boolean telling if the file exists or not
     */
    private fun canAccessFile(uri: Uri): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.close()
            true
        } catch (e: FileNotFoundException) {
            with(FailureReason.FILE_DOES_NOT_EXIST) {
                Timber.e(e)
                listener.onFail(this)
            }
            false
        } catch (e: SecurityException) {
            with(FailureReason.PERMISSION_DENIED) {
                Timber.e(e)
                listener.onFail(this)
            }
            false
        } catch (e: Exception) {
            with(FailureReason.UNKNOWN) {
                Timber.e(e)
                listener.onFail(this)
            }
            false
        }
    }
}