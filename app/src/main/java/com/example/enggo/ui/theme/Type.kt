package com.example.enggo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.enggo.R

val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_thin, FontWeight.Thin),
    Font(R.font.roboto_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Bold),
    Font(R.font.roboto_medium_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.roboto_bold, FontWeight.ExtraBold),
    Font(R.font.roboto_bold_italic, FontWeight.ExtraBold, FontStyle.Italic)
)

val baseline = Typography()

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = Roboto),
    displayMedium = baseline.displayMedium.copy(fontFamily = Roboto),
    displaySmall = baseline.displaySmall.copy(fontFamily = Roboto),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = Roboto),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = Roboto),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = Roboto),
    titleLarge = baseline.titleLarge.copy(fontFamily = Roboto),
    titleMedium = baseline.titleMedium.copy(fontFamily = Roboto),
    titleSmall = baseline.titleSmall.copy(fontFamily = Roboto),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = Roboto),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = Roboto),
    bodySmall = baseline.bodySmall.copy(fontFamily = Roboto),
    labelLarge = baseline.labelLarge.copy(fontFamily = Roboto),
    labelMedium = baseline.labelMedium.copy(fontFamily = Roboto),
    labelSmall = baseline.labelSmall.copy(fontFamily = Roboto),
)