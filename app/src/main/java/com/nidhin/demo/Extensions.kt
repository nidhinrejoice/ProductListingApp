package com.nidhin.demo

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Double.formatCurrency(currencyCode: String = "USD"): String {
    val df = NumberFormat.getNumberInstance(Locale.UK)
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = 0

    val baseCurrency = Currency.getInstance(currencyCode).symbol
    return baseCurrency + df.format(this)

}
