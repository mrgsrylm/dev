package io.github.mrgsrylm.utils

import java.math.BigDecimal
import java.util.*

/**
 * Utility class for generating random values.
 */
object RandomUtil {
    private val random = Random()

    /**
     * Generates a random UUID (Universally Unique Identifier) string.
     * The generated string will be a unique identifier with a length of 36 characters, consisting of hexadecimal digits
     * separated by hyphens.
     *
     * @return a random UUID string
     */
    fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * Generates a random name string.
     * The generated string will be a UUID string without hyphens.
     *
     * @return a random name string
     */
    fun generateRandomString(): String {
        val uuidString = generateUUID().replace("-", "")
        return uuidString.substring(0, kotlin.math.min(uuidString.length, 20))
    }

    /**
     * Generates a random integer within a specified range.
     *
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @return a random integer within the specified range
     */
    fun generateRandomInteger(min: Int, max: Int): Int {
        return random.nextInt(max - min + 1) + min
    }

    /**
     * Generates a random BigDecimal within a specified range.
     *
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @return a random BigDecimal within the specified range
     */
    fun generateRandomBigDecimal(min: Double, max: Double): BigDecimal {
        val scale = 2
        val randomDouble = min + (max - min) * random.nextDouble()
        return BigDecimal(randomDouble, java.math.MathContext(scale))
    }
}
