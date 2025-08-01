package com.nullo.openrouterclient.data

import android.app.Application
import androidx.core.content.ContextCompat
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.domain.entities.ChatResponseResult
import javax.inject.Inject

class ErrorResponseProvider @Inject constructor(
    private val application: Application
) {

    fun createNetworkError(): ChatResponseResult.Error {
        return ChatResponseResult.Error(
            ContextCompat.getString(application, R.string.error_header_network),
            ContextCompat.getString(application, R.string.error_network),
        )
    }

    fun createCancelledError(): ChatResponseResult.Error {
        return ChatResponseResult.Error(
            ContextCompat.getString(application, R.string.error_header_cancelled),
            ContextCompat.getString(application, R.string.error_cancelled),
        )
    }

    fun createUnknownError(): ChatResponseResult.Error {
        return ChatResponseResult.Error(
            ContextCompat.getString(application, R.string.error_header_open_router),
            ContextCompat.getString(application, R.string.error_unknown),
        )
    }
}
