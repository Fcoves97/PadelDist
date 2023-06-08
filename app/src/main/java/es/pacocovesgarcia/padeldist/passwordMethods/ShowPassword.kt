package es.pacocovesgarcia.padeldist.passwordMethods

import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageButton
import es.pacocovesgarcia.padeldist.R

fun ShowPassword(etPassword: EditText, btnShowPassword: ImageButton) {
    val selectionStart = etPassword.selectionStart
    val selectionEnd = etPassword.selectionEnd
    if (etPassword.transformationMethod is PasswordTransformationMethod) {
        etPassword.transformationMethod = null
        btnShowPassword.setImageResource(R.drawable.open_eye_password_24)
    } else {
        etPassword.transformationMethod = PasswordTransformationMethod()
        btnShowPassword.setImageResource(R.drawable.closed_eye_password_24)
    }
    etPassword.setSelection(selectionStart, selectionEnd)
}