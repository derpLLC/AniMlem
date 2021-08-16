package com.derpllc.animlem

import android.os.Bundle
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
import com.derpllc.animlem.repository.fetchImageURL
import com.derpllc.animlem.ui.theme.KittyCatsTheme

class MainActivity : ComponentActivity() {
    @ExperimentalCoilApi
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
        while (imgUrl.value.isBlank()) {
            imgUrl.value = fetchImageURL()
            // Sleep for 0.5 seconds and let it fetch the url
            Thread.sleep(500)
        }
        DisplayImage()
        GenerateKittyButton()
    }
}

@ExperimentalCoilApi
@Composable
fun DisplayImage() {
    val painter = rememberImagePainter(
        data = imgUrl.value,
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

@Preview
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
            imgUrl.value = fetchImageURL()
        }) {
            Text(text = "Another One!")
        }
    }
}