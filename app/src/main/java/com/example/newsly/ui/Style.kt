package com.example.newsly.ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.newsly.R

val NYTFont = FontFamily(
    Font(R.font.georgia, FontWeight.Normal)
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = NYTFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    defaultFontFamily = NYTFont
)