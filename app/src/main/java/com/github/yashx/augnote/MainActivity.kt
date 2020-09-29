package com.github.yashx.augnote
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_main.*
//import org.koin.android.viewmodel.ext.android.viewModel
//
//
//class MainActivity : AppCompatActivity(), FileSelectorHelper.Listener, FileOpenerHelper.Listener {
//
//    private val fileSelectorHelper: FileSelectorHelper by lazy { FileSelectorHelper(this, this) }
//    private val fileOpenerHelper: FileOpenerHelper by lazy { FileOpenerHelper(this, this) }
////    private val fileOpenerHelper: FileOpenerHelper by inject {  }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        selectButton.setOnClickListener {
//            fileSelectorHelper.launchIntentToSelectFile()
//        }
//
//        loadButton.setOnClickListener {
//            val uri = Uri.parse(uri_holder.text.toString())
//            fileOpenerHelper.openUri(uri)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        fileSelectorHelper.handleActivityResult(requestCode, resultCode, data)
//    }
//
//    override fun onSuccess(uri: Uri) {
//        uri_holder.setText(uri.toString())
//    }
//
//    override fun onFail(reason: FileOpenerHelper.FailureReason) {
//        Toast.makeText(this, reason.message, Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onFail(reason: FileSelectorHelper.FailureReason) {
//        Toast.makeText(this, reason.message, Toast.LENGTH_SHORT).show()
//    }
//}