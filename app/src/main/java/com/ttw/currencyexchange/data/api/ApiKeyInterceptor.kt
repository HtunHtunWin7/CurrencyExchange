package com.ttw.currencyexchange.data.api

import com.ttw.currencyexchange.utils.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url = original.url.newBuilder().addQueryParameter("access_key", API_KEY).build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}