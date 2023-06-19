package es.pacocovesgarcia.padeldist.dialogFragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import entities.Reserva
import es.pacocovesgarcia.padeldist.R

class HistorialReservasDialog : DialogFragment() {

    companion object {
        const val ARG_RESERVA = "reserva"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val reserva = arguments?.getParcelable<Reserva>(ARG_RESERVA)

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_reserva_details, null)
        builder.setView(dialogView)

        val tvDuracionReserva = dialogView.findViewById<TextView>(R.id.tvDuracionReserva)
        val tvFechaReserva = dialogView.findViewById<TextView>(R.id.tvFechaReserva)
        val tvHoraFinal = dialogView.findViewById<TextView>(R.id.tvHoraFinal)
        val tvHoraInicial = dialogView.findViewById<TextView>(R.id.tvHoraInicial)
        val tvIdJugador = dialogView.findViewById<TextView>(R.id.tvIdJugador)
        val tvIdPista = dialogView.findViewById<TextView>(R.id.tvIdPista)
        val tvCoste = dialogView.findViewById<TextView>(R.id.tvCoste)
        val btnAceptar = dialogView.findViewById<Button>(R.id.btnAceptar)

        val FechaInicial = reserva?.hora_inicial
        val FechaFinal = reserva?.hora_final

        tvDuracionReserva.text = "Duración de la reserva: ${reserva?.duracion_reserva}"
        tvFechaReserva.text = "Fecha de la reserva: ${reserva?.fecha_reserva}"
        tvHoraFinal.text = "Hora final: ${FechaFinal}"
        tvHoraInicial.text = "Hora inicial: ${FechaInicial}"
        tvCoste.text = "Coste total: 2€"
        tvIdJugador.text = "Nombre del jugador: ${Singletone.JugadorSingletone.LoggedPlayer.nombre}"
        tvIdPista.text = "Número de pista: ${reserva?.id_pista}"

        val dialog = builder.create()

        btnAceptar.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }
}


