package com.section11.mystock.ui.common.extensions

fun Double.toPercentageFormat(digits: Int = 2) = "%.${digits}f".format(this)
