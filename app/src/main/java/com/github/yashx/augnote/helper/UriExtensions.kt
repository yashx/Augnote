package com.github.yashx.augnote.helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.URLUtil

enum class TagType {
    AUDIO, FILE, IMAGE, VIDEO, WEB
}

fun Uri.tagType(context: Context): TagType {
    val extension: String?

    return when {
        URLUtil.isHttpUrl(toString()) or URLUtil.isHttpsUrl(toString()) -> TagType.WEB
        scheme.equals(ContentResolver.SCHEME_CONTENT) -> {
            when (context.contentResolver.getType(this)?.split("/")?.get(0)) {
                "video" -> TagType.VIDEO
                "image" -> TagType.IMAGE
                "audio" -> TagType.AUDIO
                else -> TagType.FILE
            }
        }
        else -> TagType.FILE
    }
}