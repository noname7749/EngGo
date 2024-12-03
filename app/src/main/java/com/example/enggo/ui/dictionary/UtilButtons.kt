package com.example.enggo.ui.dictionary

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enggo.R

@Composable
fun UtilButtons(viewModel: WordModelViewModel, word: String) {
    val context = LocalContext.current
    var clickedState by remember { mutableStateOf(false) }
    val ttsListener by remember {
        mutableStateOf(TTSListener(context) {
            clickedState = false
        })
    }
    LocalLifecycleOwner.current.lifecycle.addObserver(ttsListener)
    AudioButton (
        clicked = clickedState
    ) {
        Log.d("AUDIO BUTTON", "Nhan nut audio")
        clickedState = !clickedState
        if (clickedState) {
            ttsListener.speak(word)
        } else {
            ttsListener.stop()
        }
    }

    SaveButton() {
        val wordModel = viewModel.wordState.value.wordModel
        if (wordModel != null) {
            viewModel.addBookmark(wordModel)
        }
    }

    ShareButton() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, word)
        }
        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        context.startActivity(chooserIntent)
    }
}

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = {
            onButtonClick()
        },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(dimensionResource(R.dimen.padding_small))
                .size(48.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.save),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_image_size))
            )
            Text(
                text = "Save",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = {
            onButtonClick()
        },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(dimensionResource(R.dimen.padding_small))
                .size(48.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.share),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_image_size))
            )
            Text(
                text = "Share",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun AudioButton(
    modifier: Modifier = Modifier,
    clicked: Boolean,
    onButtonClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = {
            if (!clicked) {
                onButtonClick()
            }
        },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(dimensionResource(R.dimen.padding_small))
                .size(48.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.speak),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_image_size))
            )
            Text(
                text = if (clicked) "Stop" else "Audio",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
@Preview
fun SaveButtonPreview() {
    SaveButton {
        Log.i("hehe", "nhan luu vao bookmark")
    }
}