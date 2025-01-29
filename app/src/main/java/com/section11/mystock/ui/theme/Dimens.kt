package com.section11.mystock.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Dimens(
    val mOctave: Dp = 1.dp,
    val mQuarter: Dp = 2.dp,
    val mHalf: Dp = 4.dp,
    val m1: Dp = 8.dp,
    val m1AndHalf: Dp = 12.dp,
    val m2: Dp = 16.dp,
    val m2AndHalf: Dp = 20.dp,
    val m3: Dp = 24.dp,
    val m4: Dp = 32.dp,
    val m5: Dp = 40.dp,
    val m6: Dp = 48.dp,

    val textSmallest: TextUnit = 10.sp,
    val textVerySmall: TextUnit = 12.sp,
    val textSmall: TextUnit = 14.sp,
    val textMediumSmall: TextUnit = 16.sp,
    val textExtraLarge: TextUnit = 24.sp
)
