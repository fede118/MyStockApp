package com.section11.mystock.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Dimens(
    val smallest: Dp = 1.dp,
    val verySmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val default: Dp = 8.dp,

    val textVerySmall: TextUnit = 10.sp,
    val textSmall: TextUnit = 14.sp,
    val textMediumSmall: TextUnit = 16.sp,
    val textExtraLarge: TextUnit = 24.sp
)
