package com.derpllc.animlem.repository

import android.util.Log
import com.derpllc.animlem.model.Mlem
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

var returnImgURL = ""

fun fetchImageURL(): String {
    val url = "https://mlem.tech/api/randommlem"

    val request = Request.Builder().url(url).build()

    val client = OkHttpClient()

    val gson = Gson()

    client.newCall(request).enqueue(object : Callback {

        override fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            if (body != null) {
                val mlem = gson.fromJson(body, Mlem::class.java)
                returnImgURL = mlem.URI
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Image Get Request Failure: ", e.message.toString())
            }
        })
    return returnImgURL
}