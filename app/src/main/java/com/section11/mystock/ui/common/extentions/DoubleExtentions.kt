package com.section11.mystock.ui.common.extentions

fun Double.toPercentageFormat(digits: Int = 2) = "%.${digits}f".format(this)
