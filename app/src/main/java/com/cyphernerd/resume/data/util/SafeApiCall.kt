package com.cyphernerd.resume.data.util

import retrofit2.Response


suspend inline fun <reified T : Any> safeApiCall(apiCall: () -> Response<T>): DataResource<T> {
    return try {
        val response = apiCall()

        if (response.isSuccessful) {
            response.body()?.let {
                DataResource.Companion.success(it)
            } ?: DataResource.Companion.error(Throwable("No data received"), null)
        } else if (response.code() == 401) {
            DataResource.Companion.error(Throwable("401 Unauthorized"), null)
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown Error"
            DataResource.Companion.error(Throwable(errorBody), null)
        }
    } catch (exception: Exception) {
        DataResource.Companion.error(exception, null)
    }
}