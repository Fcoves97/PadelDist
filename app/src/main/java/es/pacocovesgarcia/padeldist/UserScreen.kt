package es.pacocovesgarcia.padeldist

import Singletone.JugadorSingletone
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.pacocovesgarcia.padeldist.menuAndToolbar.SetUpMenuAndToolbar

class UserScreen : AppCompatActivity() {

    private lateinit var sideMenu: NavigationView
    private lateinit var btncloseMenu: ImageButton
    private lateinit var btnOpenMenu: ImageButton
    private lateinit var dlMenu: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var ivUserImage : ImageView
    private lateinit var tvUserName : TextView
    private lateinit var tvCorreoValue : TextView
    private lateinit var tvLadoPistaValue : TextView
    private lateinit var tvNivelValue : TextView
    private lateinit var tvPartidasJugadasValue : TextView

    private lateinit var btnAplicar: Button
    private lateinit var ibChangeProfileImage : ImageView

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_screen)

        // Inicializar variables
        sideMenu = findViewById(R.id.SideMenu)
        btncloseMenu = findViewById(R.id.btnCloseMenu)
        btnOpenMenu = findViewById(R.id.btnOpenMenu)
        dlMenu = findViewById(R.id.dlMenu)
        toolbar = findViewById(R.id.toolbar)
        ivUserImage = findViewById(R.id.ivUserImage)
        tvUserName = findViewById(R.id.tvUserName)
        tvCorreoValue = findViewById(R.id.tvCorreoValue)
        tvLadoPistaValue = findViewById(R.id.tvLadoPistaValue)
        tvNivelValue = findViewById(R.id.tvNivelValue)
        tvPartidasJugadasValue = findViewById(R.id.tvPartidasJugadasValue)

        btnAplicar = findViewById(R.id.btnAplicar)

        ibChangeProfileImage = findViewById(R.id.ibChangeProfileImage)

        //Establecer opciones del menú y toolbar

        SetUpMenuAndToolbar(dlMenu,btnOpenMenu,sideMenu,ivUserImage,tvUserName,btncloseMenu,this)

        dlMenu.closeDrawer(GravityCompat.START)
        dlMenu.visibility = View.INVISIBLE

        val selectedImageUri: Uri = Uri.parse("content://com.example.app/images/image.jpg")

        cargarDatosJugador()

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data?.data
                // Utiliza la URI de la imagen seleccionada para cargarla en el ImageButton o ImageView
                ivUserImage.setImageURI(selectedImageUri)
            }
        }

        ibChangeProfileImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        btnAplicar.setOnClickListener{
            actualizarImagenJugador(selectedImageUri)
        }
    }

    private fun cargarDatosJugador() {
        tvCorreoValue.text = JugadorSingletone.LoggedPlayerMail
        tvUserName.text = JugadorSingletone.LoggedPlayer.nombre
        tvLadoPistaValue.text = JugadorSingletone.LoggedPlayer.lado_pista
        tvNivelValue.text = JugadorSingletone.LoggedPlayer.nivel
        tvPartidasJugadasValue.text = JugadorSingletone.LoggedPlayer.partidas_jugadas.toString()
    }

    private fun actualizarImagenJugador(selectedImageUri: Uri) {
        val database = FirebaseDatabase.getInstance()
        val jugadoresRef = database.getReference("jugadores")

        val query = jugadoresRef.orderByChild("nombre").equalTo(Singletone.JugadorSingletone.LoggedPlayer.nombre)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val jugadorId = snapshot.key

                    // Actualizar el campo imagen_perfil del jugador
                    jugadorId?.let {
                        jugadoresRef.child(it).child("imagen_perfil").setValue(selectedImageUri)
                            .addOnSuccessListener {
                                Singletone.JugadorSingletone.LoggedPlayer.imagen_perfil = selectedImageUri?.toString()
                            }
                            .addOnFailureListener { e ->
                                // La actualización falló. Manejar el error según corresponda
                            }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error en caso de que ocurra
            }
        })

    }
}