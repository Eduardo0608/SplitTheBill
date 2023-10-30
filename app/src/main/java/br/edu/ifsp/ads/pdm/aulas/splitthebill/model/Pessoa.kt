package br.edu.ifsp.ads.pdm.aulas.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pessoa(
    var id: Int, // Identificador único da pessoa
    var nome: String, // Nome da pessoa
    var devePagar: String, // Valor que a pessoa deve pagar
    var deveReceber: String, // Valor que a pessoa deve receber
    var descricao: String, // Descrição da pessoa
): Parcelable
