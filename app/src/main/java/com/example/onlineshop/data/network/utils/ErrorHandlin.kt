package com.example.onlineshop.data.network.utils

import com.google.gson.Gson
import okhttp3.ResponseBody


fun setErrorMessage(e: Exception? = null, code: Int = 0, errorBody: ResponseBody? = null,
                    fragmentName: String = ""): String {

    var errorMessage: String
    if (code != 0 && errorBody != null) {
        errorMessage =
            "خطا در ارتباط با سرور\n\nلطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید"
        errorMessage = when (code) {
            400 -> {
                if (fragmentName == "profile")
                    "$errorMessage\n\nایمیل اشتباه است یا قبلا ثبت شده است\n\n${convertErrorBody(errorBody)?.message}"
                else
                    "$errorMessage\n\nدرخواست اشتباه است\n\n${convertErrorBody(errorBody)?.message}"
            }
            403 -> {
                if (fragmentName == "detail") " این نظر وجود ندارد\n\n${convertErrorBody(errorBody)?.message}"
                else
                    "$errorMessage\n\n${convertErrorBody(errorBody)?.message}"
            }

            401 -> "$errorMessage\n\nاعتبار سنجی انجام نشد\n\n${convertErrorBody(errorBody)?.message}"
            404 -> "$errorMessage\n\nلینک اشتباه است\n\n${convertErrorBody(errorBody)?.message}"
            500 -> "$errorMessage\n\nارور سرور\n\n${convertErrorBody(errorBody)?.message}"
            else ->"$errorMessage\n\n${convertErrorBody(errorBody)?.message}"
        }
    } else
        when (e) {
            is java.lang.IllegalArgumentException -> {
                errorMessage =
                    "خطا در اطلاعات ارسالی به سرور\n\n لطفا چند دقیقه دیگر مجددا تلاش نمایید\n\n در صورت تداوم مشکل با ما تماس بگیرید\n\n${e.message}"
            }
            is java.net.SocketTimeoutException, is java.net.UnknownHostException -> {
                errorMessage =
                    "خطا در ارتباط با اینترنت\n\n لطفا اتصال اینترنت خود را چک نمایید\n\n${e.message}"
            }
            else -> {
                errorMessage = "خطا در دریافت اطلاعات\n\n${e?.message}"
            }
    }
    return errorMessage
}


fun convertErrorBody(errorBody: ResponseBody?): ErrorResponse? {
    return try {
        errorBody?.source()?.let {
            Gson().fromJson(it.readUtf8(), ErrorResponse::class.java)
        }
    } catch (exception: Exception) {
        null
    }
}

data class ErrorResponse(val message: String)