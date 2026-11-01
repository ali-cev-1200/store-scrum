package com.ali1200.store.util

import org.junit.Assert.assertEquals
import org.junit.Test

class StylePriceTest {

    @Test
    fun shortPrice_hasNoSeparator() {
        assertEquals("500 Tomans", stylePrice("500"))
    }

    @Test
    fun fourDigitPrice_hasOneSeparator() {
        assertEquals("1,500 Tomans", stylePrice("1500"))
    }

    @Test
    fun sixDigitPrice_hasNoLeadingSeparator() {
        assertEquals("250,000 Tomans", stylePrice("250000"))
    }

    @Test
    fun sevenDigitPrice_hasTwoSeparators() {
        assertEquals("1,250,000 Tomans", stylePrice("1250000"))
    }
}
