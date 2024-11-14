package com.example.enggo.ui.dictionary

import android.provider.SearchRecentSuggestions
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponent(
    modifier: Modifier = Modifier,
//    onSearch: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var expandedState by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expandedState,
        onExpandedChange = {
            expandedState = !expandedState
        }
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
//                if (it.text.isNotBlank())
//                    onSearch(it.text)
            },
            singleLine = true,
            placeholder = {
                Text(
                    text = "Search for a word",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview
fun SearchComponentTest() {
    SearchComponent()
}