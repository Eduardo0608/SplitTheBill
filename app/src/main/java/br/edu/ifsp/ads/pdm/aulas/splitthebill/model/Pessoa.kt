package br.edu.ifsp.ads.pdm.aulas.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pessoa(
    var id: Int,
    var nome: String,
    var devePagar: String,
    var deveReceber: String,
    var descricao: String,
): Parcelable