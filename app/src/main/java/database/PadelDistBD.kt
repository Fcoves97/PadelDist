package database

import android.app.Application
import com.google.firebase.FirebaseApp

class PadelDistBD : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}