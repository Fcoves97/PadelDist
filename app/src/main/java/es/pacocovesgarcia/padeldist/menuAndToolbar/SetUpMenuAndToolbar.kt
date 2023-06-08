package es.pacocovesgarcia.padeldist.menuAndToolbar

import Singletone.JugadorSingletone
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import dao.JugadorDao
import database.Padeldist
import es.pacocovesgarcia.padeldist.PistasScreen
import es.pacocovesgarcia.padeldist.R
import es.pacocovesgarcia.padeldist.TimeTableScreen
import es.pacocovesgarcia.padeldist.WelcomeScreen
import es.pacocovesgarcia.padeldist.imageConversions.byteArrayToBitmap

fun SetUpMenuAndToolbar(
    dlMenu: DrawerLayout,
    ibMenu: ImageButton,
    sideMenu: NavigationView,
    ivUserImage: ImageView,
    tvUserName: TextView,
    context: Context
) {

    sideMenu.setNavigationItemSelectedListener { menuItem ->
        // Acciones a realizar cuando se selecciona una opción del menú
        val itemId = menuItem.itemId
        dlMenu.closeDrawer(sideMenu)

        if(itemId == 1){
            val intent = Intent(context, TimeTableScreen::class.java)
            context.startActivity(intent)
        }else if(itemId == 2){
            val intent = Intent(context, WelcomeScreen::class.java)
            context.startActivity(intent)
        }else if(itemId == 3){
            val intent = Intent(context, PistasScreen::class.java)
            context.startActivity(intent)
        }else if(itemId == 4){
            val intent = Intent(context, WelcomeScreen::class.java)
            context.startActivity(intent)
        }else if(itemId == 5){
            val intent = Intent(context, WelcomeScreen::class.java)
            context.startActivity(intent)
        }else {
            val intent = Intent(context, WelcomeScreen::class.java)
            context.startActivity(intent)
        }
        true
    }

    dlMenu.openDrawer(sideMenu)

    val byteArray: ByteArray?  = JugadorSingletone.getLoggedPlayer().imagen_perfil // Tu ByteArray de la imagen
    val bitmap: Bitmap? = byteArray?.let { byteArrayToBitmap(it) }

    if (bitmap != null) {
        ivUserImage.setImageBitmap(bitmap)
    } else {
        // Si la conversión falla, puedes establecer una imagen de reemplazo o realizar alguna otra acción
        ivUserImage.setImageResource(R.drawable.default_user_image)
    }

    tvUserName.text = JugadorSingletone.getLoggedPlayer().nombre

    ibMenu.setOnClickListener {
        if (dlMenu.isDrawerOpen(GravityCompat.START)) {
            dlMenu.closeDrawer(GravityCompat.START)
        } else {
            dlMenu.openDrawer(GravityCompat.START)
        }
        ibMenu.setImageResource(R.drawable.menu_selected)
    }
}