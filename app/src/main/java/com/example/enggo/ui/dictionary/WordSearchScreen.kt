package com.example.enggo.ui.dictionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enggo.R
import com.example.enggo.ui.dictionary.model.Meaning
import com.example.enggo.ui.dictionary.model.WordModel

var dictionaryStringBuilder = StringBuilder()
val mockWordModel = WordModel(
    word = "apple",
    meanings = listOf(
        Meaning(
            speechPart = "Noun",
            def = "A round fruit with red or green skin and a whitish interior.",
            labels = null,
            example = "I ate an apple for breakfast.",
            synonyms = listOf("pome", "Malus")
        ),
        Meaning(
            speechPart = "Noun",
            def = "A round fruit with red or green skin and a whitish interior.",
            labels = null,
            example = "I ate an apple for breakfast.",
            synonyms = listOf("pome", "Malus")
        ),
        Meaning(
            speechPart = "Noun",
            def = "A round fruit with red or green skin and a whitish interior.",
            labels = null,
            example = "I ate an apple for breakfast.",
            synonyms = listOf("pome", "Malus")
        ),
        Meaning(
            speechPart = "Noun",
            def = "A round fruit with red or green skin and a whitish interior.",
            labels = null,
            example = "I ate an apple for breakfast.",
            synonyms = listOf("pome", "Malus")
        )
    ),
    wordsetId = "11111"
)

@Composable
fun WordSearchScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchComponent()
        Text(
            text = "Result",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(2.dp))
        SearchContent(wordModel = mockWordModel)
    }
}

@Composable
fun SearchContent(
    wordModel: WordModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = wordModel.word,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UtilButtons()
            }

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                wordModel.meanings?.forEachIndexed { index, meaning ->
                    dictionaryStringBuilder.append(meaning.speechPart).append("\n")
                    Text(
                        text = meaning.speechPart,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    dictionaryStringBuilder.append("${index + 1}. ${meaning.def}").append("\n")
                    Text(
                        text = "${index + 1}. ${meaning.def}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!meaning.labels.isNullOrEmpty()) {
                        val label = meaning.labels.map { label -> label.name }.toString()
                            .removePrefix("[")
                            .removeSuffix("]").replace(",", " •")
                        dictionaryStringBuilder.append(label).append("\n")
                        Text(
                            text = label,
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                    if (!meaning.example.isNullOrEmpty()) {
                        val example = "Example: ${meaning.example}"
                        dictionaryStringBuilder.append(example).append("\n")
                        Text(
                            text = example,
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                            color = Color.Gray
                        )
                    }
                    if (!meaning.synonyms.isNullOrEmpty()) {
                        val synonym = "Synonym(s): ${
                            meaning.synonyms.toString()
                                .removePrefix("[")
                                .removeSuffix("]")
                        }"
                        dictionaryStringBuilder.append(synonym).append("\n")
                        Text(
                            text = synonym,
                            fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(
                        thickness = 0.6.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
@Preview
fun WordSearchScreenPreview() {
    WordSearchScreen()
}

@Composable
@Preview
fun SearchContentPreview() {
    SearchContent(wordModel = mockWordModel)
}
