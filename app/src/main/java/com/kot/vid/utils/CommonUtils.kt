package com.kot.vid.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.api.services.youtube.model.ThumbnailDetails
import java.io.IOException

class CommonUtils {

    companion object {
        const val AUTH_ERROR_REQUEST = 123
        const val AUTH_ERROR = "auth_error"
        var isAuthError = false
        
        @JvmStatic
        fun log(msg: String) {
            Log.i("TAG", msg)
        }

        @JvmStatic
        fun loge(msg: String) {
            Log.e("TAG", msg)
        }

        @JvmStatic
        fun toast(context: Context, msg: String) {
            Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT).show()
        }

        fun getThumbnail(thumbnails: ThumbnailDetails): String {
            thumbnails.high?.let {
                return it.url
            }
            thumbnails.medium?.let {
                return it.url
            }
            thumbnails.standard?.let {
                return it.url
            }
            thumbnails.default?.let {
                return it.url
            }
            return ""
        }

        fun getErrorMessage(e: Throwable): String {
            if (e is IOException) {
                return "Please check your data connection."
            }
            return "Oops, Something went wrong, try again later."
        }
    }
}