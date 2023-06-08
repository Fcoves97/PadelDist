package es.pacocovesgarcia.padeldist.imageConversions

import java.io.ByteArrayOutputStream
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun convertDrawableToByteArray(context: Context, drawableId: Int): ByteArray {
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
    val outputStream = ByteArrayOutputStream()

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

    outputStream.flush()
    outputStream.close()

    return outputStream.toByteArray()
}


