package com.nullo.openrouterclient.data

import android.app.Application
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.domain.entities.ChatResponseResult
import javax.inject.Inject

class ErrorProvider @Inject constructor(
    private val application: Application
) {

    fun networkError() = ChatResponseResult.Error(
        application.getString(R.string.error_header_network),
        application.getString(R.string.error_network),
    )

    fun cancelledError() = ChatResponseResult.Error(
        application.getString(R.string.error_header_cancelled),
        application.getString(R.string.error_cancelled),
    )

    fun unknownError() = ChatResponseResult.Error(
        application.getString(R.string.error_header_open_router),
        application.getString(R.string.error_unknown),
    )
}
