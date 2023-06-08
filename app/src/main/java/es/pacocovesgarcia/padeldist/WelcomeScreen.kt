package es.pacocovesgarcia.padeldist

import Singletone.JugadorSingletone
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import database.Padeldist
import es.pacocovesgarcia.padeldist.imageConversions.byteArrayToBitmap
import es.pacocovesgarcia.padeldist.menuAndToolbar.SetUpMenuAndToolbar

class WelcomeScreen : AppCompatActivity() {

    private lateinit var sideMenu: NavigationView
    private lateinit var ibMenu: ImageButton
    private lateinit var dlMenu: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var ivUserImage : ImageView
    private lateinit var tvUserName : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        // Inicializar variables
        sideMenu = findViewById(R.id.SideMenu)
        ibMenu = findViewById(R.id.ibMenu)
        dlMenu = findViewById(R.id.dlMenu)
        toolbar = findViewById(R.id.toolbar)
        ivUserImage = findViewById(R.id.ivUserImage)
        tvUserName = findViewById(R.id.tvUserName)

        //Establecer opciones del menú y toolbar

        SetUpMenuAndToolbar(dlMenu,ibMenu,sideMenu,ivUserImage,tvUserName,this)

        //val menuWidth = sideMenu.layoutParams.width
        //val menuOpenOffset = menuWidth * 0.8f
    }
}