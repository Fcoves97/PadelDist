package viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import database.Padeldist
import entities.Pista
import kotlinx.coroutines.launch

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
        val padeldist = Padeldist.getDatabase(context)
        val pistaDao = padeldist.pistaDao()

        val pistas = pistaDao.getAllPistas() // Suponiendo que tienes un método en el DAO para obtener todas las pistas

        return pistas
    }
}
