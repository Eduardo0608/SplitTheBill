package br.edu.ifsp.ads.pdm.aulas.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pessoa(
    var id: Int, // Identificador único da pessoa
    var nome: String, // Nome da pessoa
    var devePagar: String, // Valor que a pessoa deve pagar no produto 1
    var deveReceber: String, // Valor que a pessoa deve receber no produto 1
    var descricao: String, // Descrição do produto 1
    var devePagar2: String, // Valor que a pessoa deve pagar no produto 2
    var deveReceber2: String, // Valor que a pessoa deve receber no produto 2
    var descricao2: String, // Descrição do produto 2
    var devePagar3: String, // Valor que a pessoa deve pagar no produto 3
    var deveReceber3: String, // Valor que a pessoa deve receber no produto 3
    var descricao3: String, // Descrição do produto 3
): Parcelable
