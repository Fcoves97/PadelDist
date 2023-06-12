package es.pacocovesgarcia.padeldist.menuAndToolbar

import Singletone.JugadorSingletone
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import es.pacocovesgarcia.padeldist.*
import es.pacocovesgarcia.padeldist.imageConversions.byteArrayToBitmap

private var selectedMenuItemId: Int = 7
private var isMenuOpen: Boolean = false

fun SetUpMenuAndToolbar(
    dlMenu: DrawerLayout,
    btnOpenMenu: ImageButton,
    sideMenu: NavigationView,
    ivUserImage: ImageView,
    tvUserName: TextView,
    btnCloseMenu: ImageButton,  // Agregamos el botón de cierre como parámetro
    context: Context
) {

    val drawerToggle = object : ActionBarDrawerToggle(
        context as AppCompatActivity,
        dlMenu,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close
    ) {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            super.onDrawerSlide(drawerView, slideOffset)
            btnOpenMenu.visibility = View.INVISIBLE // Ocultar el botón mientras se desliza el menú
        }

        override fun onDrawerClosed(view: View) {
            super.onDrawerClosed(view)
            btnOpenMenu.visibility = View.VISIBLE // Mostrar el botón cuando se cierra el menú
            dlMenu.visibility = View.INVISIBLE
        }

        override fun onDrawerStateChanged(newState: Int) {}
    }

    dlMenu.addDrawerListener(drawerToggle)
    drawerToggle.syncState()

    sideMenu.setNavigationItemSelectedListener { menuItem ->
        selectedMenuItemId = menuItem.itemId
        if (selectedMenuItemId == R.id.navHorarios) {
            val intent = Intent(context, TimeTableScreen::class.java) // Horario
            context.startActivity(intent)
        } else if (selectedMenuItemId == R.id.navReservas) {
            val intent = Intent(context, ReservasScreen::class.java) // Reservas
            context.startActivity(intent)
        } else if (selectedMenuItemId == R.id.navPistas) {
            val intent = Intent(context, PistasScreen::class.java) // Pistas
            context.startActivity(intent)
        } else if (selectedMenuItemId == R.id.navPartidas) {
            val intent = Intent(context, PartidasScreen::class.java) // Partidas
            context.startActivity(intent)
        } else if (selectedMenuItemId == R.id.navHistorialDeReservas) {
            val intent = Intent(context, HistorialReservasScreen::class.java) // Partidas
            context.startActivity(intent)
        } else if (selectedMenuItemId == R.id.navAjustes) {
            val intent = Intent(context, SettingsScreen::class.java) // Ajustes
            context.startActivity(intent)
        } else if (selectedMenuItemId == R.id.navPerfil) {
            val intent = Intent(context, UserScreen::class.java) // Perfil
            context.startActivity(intent)
        } else {
            val intent = Intent(context, WelcomeScreen::class.java) // WelcomeScreen
            context.startActivity(intent)
        }
        dlMenu.closeDrawer(GravityCompat.START)
        true
    }

    sideMenu.setCheckedItem(selectedMenuItemId)

    val imagen_perfil  =
        JugadorSingletone.getLoggedPlayer().imagen_perfil?.toByteArray() // Tu ByteArray de la imagen
    val bitmap: Bitmap? = imagen_perfil?.let { byteArrayToBitmap(it) }

    if (bitmap != null) {
        ivUserImage.setImageBitmap(bitmap)
    } else {
        // Si la conversión falla, puedes establecer una imagen de reemplazo o realizar alguna otra acción
        ivUserImage.setImageResource(R.drawable.default_user_image)
    }

    tvUserName.text = JugadorSingletone.getLoggedPlayer().nombre

    btnCloseMenu.setOnClickListener {
        if (dlMenu.isDrawerOpen(GravityCompat.START)) {
            dlMenu.closeDrawer(GravityCompat.START)
            isMenuOpen = false
        } else {
            dlMenu.openDrawer(GravityCompat.START)
            isMenuOpen = true
        }
    }

    btnOpenMenu.setOnClickListener {
        // Hacer visible el DrawerLayout al abrir el menú
        dlMenu.visibility = View.VISIBLE
        dlMenu.openDrawer(GravityCompat.START)
    }
}

