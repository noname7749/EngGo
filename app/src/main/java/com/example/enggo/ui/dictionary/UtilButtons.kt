package com.example.enggo.ui.dictionary

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enggo.R

@Composable
fun UtilButtons() {

}

@Composable
fun SaveButton(modifier: Modifier = Modifier,
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
fun ShareButton(modifier: Modifier = Modifier,
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
fun AudioButton(modifier: Modifier = Modifier,
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
                painterResource(id = R.drawable.speak),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_image_size))
            )
            Text(
                text = "Audio",
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