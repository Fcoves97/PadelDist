package es.pacocovesgarcia.padeldist.imageConversions

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}