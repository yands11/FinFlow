package com.finflow.app.ui.composable.main.flow.add

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

class AmountVisualTransformation : VisualTransformation {

    private val formatter = DecimalFormat("#,###")

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        if (digits.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val number = digits.toLongOrNull() ?: return TransformedText(text, OffsetMapping.Identity)
        val formatted = formatter.format(number)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Count commas inserted before this offset
                var commas = 0
                val len = digits.length
                for (i in 0 until offset) {
                    val posFromRight = len - 1 - i
                    if (posFromRight > 0 && posFromRight % 3 == 0) commas++
                }
                return offset + commas
            }

            override fun transformedToOriginal(offset: Int): Int {
                var original = 0
                var transformed = 0
                while (transformed < offset && original < digits.length) {
                    transformed++
                    val posFromRight = digits.length - 1 - original
                    if (posFromRight > 0 && posFromRight % 3 == 0) {
                        transformed++ // skip comma
                    }
                    original++
                }
                return original.coerceAtMost(digits.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
