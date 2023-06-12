package viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.google.firebase.database.FirebaseDatabase
import entities.Pista
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PistasViewModel(private val context: Context, application: Application) : AndroidViewModel(application) {
    private val _listaPistas = MutableLiveData<List<Pista>>()
    val listaPistas: LiveData<List<Pista>> get() = _listaPistas

    constructor(application: Application) : this(application.applicationContext, application) {
        // Constructor sin argumentos
    }

    init {
        viewModelScope.launch {
            val pistas = getListaPistas()
            _listaPistas.postValue(pistas)
        }
    }

    suspend fun getListaPistas(): List<Pista> {
        val database = FirebaseDatabase.getInstance()
        val pistasRef = database.getReference("pistas")

        val pistasList = mutableListOf<Pista>()

        try {
            val dataSnapshot = pistasRef.get().await()
            for (snapshot in dataSnapshot.children) {
                val pistaMap = snapshot.value as? Map<String, Any>
                val nombrePista = pistaMap?.get("nombre_pista") as? String ?: ""
                val precioPorHora = pistaMap?.get("precio_por_hora") as? Long ?: 0
                val pista = Pista().apply {
                    this.nombre_pista = nombrePista
                    this.precio_por_hora = precioPorHora
                }
                pistasList.add(pista)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pistasList
    }
}
