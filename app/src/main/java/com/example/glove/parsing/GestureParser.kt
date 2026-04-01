package com.example.glove.parsing

object GestureParser {
    fun parseGesture(input: String): Int? {
        val trimmed = input.trim()
        
        // Try to parse as exactly 5-bit binary string (e.g. "01001")
        if (trimmed.length == 5 && trimmed.all { it == '0' || it == '1' }) {
            return trimmed.toIntOrNull(2)
        }
        
        // Try to parse as direct integer 0-31 (if Arduino sends raw value)
        val intValue = trimmed.toIntOrNull()
        if (intValue != null && intValue in 0..31) {
            return intValue
        }
        
        return null // Invalid format
    }
    
    fun intToBinaryString(value: Int): String {
        return value.toString(2).padStart(5, '0')
    }
}
