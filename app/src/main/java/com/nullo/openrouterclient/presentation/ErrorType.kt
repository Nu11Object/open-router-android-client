package com.nullo.openrouterclient.presentation

import com.nullo.openrouterclient.R

enum class ErrorType(val stringRes: Int) {
    BLANK_INPUT(R.string.error_blank_input),
    BLANK_API_KEY(R.string.error_blank_api_key),
    NO_API_KEY(R.string.error_no_api_key),
    MISSING_MODEL(R.string.error_missing_model),
    NETWORK_ERROR(R.string.error_network),
}
