package com.derpllc.animlem

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.derpllc.animlem.ui.theme.KittyCatsTheme
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.*
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KittyCatsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Layout()
                }
            }
        }
    }
}

private var imgUrl = mutableStateOf("")

@ExperimentalCoilApi
@Composable
fun Layout() {
    Box(modifier = Modifier
        .fillMaxSize()
    ) {
        if(imgUrl.value.isBlank()) fetchImageURL()
        DisplayImage(imgUrl.value)
        GenerateKittyButton()
    }
}

@ExperimentalCoilApi
@Composable
fun DisplayImage(url: String) {
    val painter = rememberImagePainter(
        data = url,
        builder = {
            crossfade(true)
            memoryCachePolicy(CachePolicy.DISABLED)
        }
    )

    val painterState = painter.state

    if (painterState is ImagePainter.State.Loading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

    Image(painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun GenerateKittyButton() {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp)
    ) {
        Button(onClick = {
            fetchImageURL()
        }) {
            Text(text = "Another One!")
        }
    }
}

data class Mlem(
    @SerializedName("url") val URI: String
)

fun fetchImageURL() {
    val url = "https://mlem.tech/api/randommlem"

    val request = Request.Builder().url(url).build()

    val client = OkHttpClient()

    val gson = Gson()

    client.newCall(request).enqueue(object : Callback {

        override fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            if (body != null) {
                val mlem = gson.fromJson(body, Mlem::class.java)
                imgUrl.value = mlem.URI
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            Log.e("Image Get Request Failure: ", e.message.toString())
        }

    })
}

@Preview(showSystemUi=true)
@Composable
fun DefaultPreview() {
    KittyCatsTheme {
        Layout()
    }
}