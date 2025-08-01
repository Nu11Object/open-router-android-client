package com.nullo.openrouterclient.data.database.chat

import android.app.Application
import androidx.core.content.ContextCompat
import com.nullo.openrouterclient.R
import com.nullo.openrouterclient.data.Constants.ROLE_ASSISTANT
import com.nullo.openrouterclient.data.Constants.ROLE_USER
import com.nullo.openrouterclient.data.Constants.UNDEFINED_ID
import javax.inject.Inject

class MessageDbEntityProvider @Inject constructor(
    private val application: Application
) {

    fun createLoadingMessage(): MessageDbEntity {
        return MessageDbEntity(
            id = UNDEFINED_ID,
            text = ContextCompat.getString(application, R.string.response_placeholder),
            isLoading = true,
            role = ROLE_ASSISTANT,
        )
    }

    fun createQueryMessage(queryText: String): MessageDbEntity {
        return MessageDbEntity(
            id = UNDEFINED_ID,
            text = queryText,
            isLoading = false,
            role = ROLE_USER,
        )
    }
}
