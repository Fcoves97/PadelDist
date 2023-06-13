package es.pacocovesgarcia.padeldist.pistaDialogFragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import entities.Pista
import entities.Reserva

class ReservaDialogFragment: DialogFragment() {
    companion object {
        fun newInstance(reserva: Reserva): ReservaDialogFragment {
            val fragment = ReservaDialogFragment()
            val args = Bundle()
            args.putString("pista:", reserva.id_pista)
            fragment.arguments = args
            return fragment
        }
    }
}