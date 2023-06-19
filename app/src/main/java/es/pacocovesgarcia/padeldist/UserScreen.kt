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
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
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
    private lateinit var ivUserImageSetting : ImageView
    private lateinit var ivLogout : ImageView
    private lateinit var tvUserName : TextView
    private lateinit var tvCorreoValue : TextView
    private lateinit var tvLadoPistaValue : TextView
    private lateinit var tvNivelValue : TextView
    private lateinit var tvPartidasJugadasValue : TextView

    private lateinit var btnAplicar: Button
    private lateinit var ibChangeProfileImage : ImageView

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    private var selectedImageUri: Uri? = null
    var isImageSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_screen)

        // Inicializar variables
        sideMenu = findViewById(R.id.SideMenu)
        btncloseMenu = findViewById(R.id.btnCloseMenu)
        btnOpenMenu = findViewById(R.id.btnOpenMenu)
        dlMenu = findViewById(R.id.dlMenu)
        toolbar = findViewById(R.id.toolbar)
        ivUserImageSetting = findViewById(R.id.ivUserImageSetting)
        ivLogout = findViewById(R.id.ivlogout)
        tvUserName = findViewById(R.id.tvUserName)
        tvCorreoValue = findViewById(R.id.tvCorreoValue)
        tvLadoPistaValue = findViewById(R.id.tvLadoPistaValue)
        tvNivelValue = findViewById(R.id.tvNivelValue)
        tvPartidasJugadasValue = findViewById(R.id.tvPartidasJugadasValue)

        btnAplicar = findViewById(R.id.btnAplicar)

        ibChangeProfileImage = findViewById(R.id.ibChangeProfileImage)

        //Establecer opciones del menú y toolbar

        SetUpMenuAndToolbar(dlMenu,btnOpenMenu,sideMenu,ivUserImageSetting,tvUserName,btncloseMenu,this)

        dlMenu.closeDrawer(GravityCompat.START)
        dlMenu.visibility = View.INVISIBLE

        //Toast personalizado
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_layout, findViewById(R.id.toast_layout_root))
        val tvToast = layout.findViewById<TextView>(R.id.tvToast)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout

        cargarDatosJugador()

        ibChangeProfileImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    // Utiliza el URI de la imagen seleccionada para cargarla en el ImageButton o ImageView
                    Glide.with(this)
                        .load(uri)
                        .apply(RequestOptions().transform(CircleCrop()))
                        .into(ivUserImageSetting)
                    isImageSelected = true
                }
            }
        }

        ivLogout.setOnClickListener{
            val intent = Intent(this, LogInScreen::class.java)
            startActivity(intent)
        }

        btnAplicar.setOnClickListener{
            if (isImageSelected){
                val database = FirebaseDatabase.getInstance()
                val jugadoresRef = database.getReference("jugadores")

                val query = jugadoresRef.orderByChild("nombre").equalTo(JugadorSingletone.LoggedPlayer.nombre)

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val jugadorId = snapshot.key

                            // Actualizar el campo imagen_perfil del jugador
                            jugadorId?.let {
                                jugadoresRef.child(it).child("imagen_perfil").setValue(selectedImageUri.toString() )
                                    .addOnSuccessListener {
                                        JugadorSingletone.LoggedPlayer.imagen_perfil = selectedImageUri.toString()
                                        recreate()
                                        tvToast.text = "Imagen cambiada!"
                                        toast.show()
                                    }
                                    .addOnFailureListener {
                                        tvToast.text = "Error al cambiar la imagen"
                                        toast.show()
                                    }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Manejar el error en caso de que ocurra
                    }
                })
            }else{
                tvToast.text = "No se han realizado cambios!"
                toast.show()
            }
        }
    }

    private fun cargarDatosJugador() {
        if (JugadorSingletone.getLoggedPlayer().imagen_perfil != "") {
            Glide.with(this)
                .load(JugadorSingletone.LoggedPlayer.imagen_perfil?.toUri())
                .apply(RequestOptions().transform(CircleCrop()))
                .into(ivUserImageSetting)
        } else {
            // Si la conversión falla, puedes establecer una imagen de reemplazo o realizar alguna otra acción
            ivUserImageSetting.setImageResource(R.drawable.default_user_image)
        }
        tvCorreoValue.text = JugadorSingletone.LoggedPlayerMail
        tvUserName.text = JugadorSingletone.LoggedPlayer.nombre
        tvLadoPistaValue.text = JugadorSingletone.LoggedPlayer.lado_pista
        tvNivelValue.text = JugadorSingletone.LoggedPlayer.nivel
        tvPartidasJugadasValue.text = JugadorSingletone.LoggedPlayer.partidas_jugadas.toString()
    }
}