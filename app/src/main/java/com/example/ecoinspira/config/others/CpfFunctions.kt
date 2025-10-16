package com.example.ecoinspira.config.others

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

fun isValidCPF(cpf: String): Boolean {
    val cleanCPF = cpf.replace(Regex("[^\\d]"), "")
    if (cleanCPF.length != 11 || cleanCPF.all { it == cleanCPF[0] }) return false

    fun calcDigit(cpf: String, factor: Int): Int {
        var sum = 0
        var weight = factor
        for (char in cpf) {
            sum += (char.toString().toInt() * weight--)
        }
        val remainder = sum % 11
        return if (remainder < 2) 0 else 11 - remainder
    }

    val digit1 = calcDigit(cleanCPF.substring(0, 9), 10)
    val digit2 = calcDigit(cleanCPF.substring(0, 9) + digit1, 11)

    return cleanCPF == cleanCPF.substring(0, 9) + digit1.toString() + digit2.toString()
}


fun cpfVisualTransformation(): VisualTransformation = object : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Garante só dígitos e no máximo 11
        val digits = text.text.filter(Char::isDigit).take(11)

        // Monta "xxx.xxx.xxx-xx" (sem separador pendurado)
        val masked = buildString {
            for (i in digits.indices) {
                append(digits[i])
                when (i) {
                    2 -> if (digits.length > 3) append('.')
                    5 -> if (digits.length > 6) append('.')
                    8 -> if (digits.length > 9) append('-')
                }
            }
        }

        val hasSep1 = digits.length > 3   // ponto após 3º dígito
        val hasSep2 = digits.length > 6   // ponto após 6º dígito
        val hasSep3 = digits.length > 9   // hífen após 9º dígito

        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var o = offset
                if (hasSep1 && offset > 3) o += 1
                if (hasSep2 && offset > 6) o += 1
                if (hasSep3 && offset > 9) o += 1
                return o
            }
            override fun transformedToOriginal(offset: Int): Int {
                var t = offset
                if (hasSep1 && t > 3) t -= 1
                if (hasSep2 && t > 7) t -= 1
                if (hasSep3 && t > 11) t -= 1
                return t.coerceIn(0, digits.length)
            }
        }
        return TransformedText(AnnotatedString(masked), mapping)
    }
}