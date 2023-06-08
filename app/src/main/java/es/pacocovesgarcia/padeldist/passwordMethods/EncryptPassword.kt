package es.pacocovesgarcia.padeldist.passwordMethods

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun EncryptPassword(password: String): String {
    try {
        val digest = MessageDigest.getInstance("SHA-256")
        val encodedHash = digest.digest(password.toByteArray(StandardCharsets.UTF_8))

        // Convertir el hash en una cadena hexadecimal
        val hexString = StringBuilder()
        for (b in encodedHash) {
            val hex = Integer.toHexString(0xff and b.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}