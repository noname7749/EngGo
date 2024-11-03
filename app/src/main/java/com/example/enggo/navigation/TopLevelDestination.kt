package com.example.enggo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.enggo.R

enum class TopLevelDestination(
    val icon: ImageVector,
    @StringRes val title: Int,
) {
    HOME(
        icon = Icons.Rounded.Home,
        title = R.string.home
    ),
    DICTIONARY(
        icon = Icons.Rounded.Book,
        title = R.string.dictionary
    ),
    COURSES(
        icon = Icons.Rounded.School,
        title = R.string.courses
    ),
    FOLDER(
        icon = Icons.Rounded.Folder,
        title = R.string.folder_library
    ),
    PROFILE(
        icon = Icons.Rounded.Person,
        title = R.string.profile
    )
}