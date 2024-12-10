package com.example.enggo.ui.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enggo.model.Flashcard
import com.example.enggo.ui.theme.EngGoTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun flashCardView(
    fc: Flashcard,
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500)
    )

    Card(
        onClick = { isFlipped = !isFlipped },
        modifier = modifier.graphicsLayer {
            rotationY = rotation
            cameraDistance = 12f * density
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (rotation <= 90f) {
                Text(
                    text = fc.FirstCard,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                Text(
                    text = fc.SecondCard,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .graphicsLayer { rotationY = 180f }
                        .padding(32.dp)
                )
            }
        }
    }
}

@Composable
fun TwoSideFlashCardView(fc : Flashcard, modifier : Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = fc.FirstCard,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
            )

            Text(
                text = fc.SecondCard,
                fontSize = 25.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 10.dp, bottom = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardPreview() {
    EngGoTheme {
        var x : Flashcard = Flashcard("Duck", "Vit")
        flashCardView(x, modifier = Modifier.height(200.dp).width(400.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TwoSideFlashcardPreview() {
    EngGoTheme {
        var x : Flashcard = Flashcard("Duck", "Vit")
        TwoSideFlashCardView(x, modifier = Modifier.fillMaxWidth())
    }
}