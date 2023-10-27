package br.edu.ifsp.ads.pdm.aulas.splitthebill.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.ads.pdm.aulas.splitthebill.R
import br.edu.ifsp.ads.pdm.aulas.splitthebill.model.Pessoa

class PessoaAdapter(
    context: Context,
    private val pessoaList: MutableList<Pessoa>
) : ArrayAdapter<Pessoa>(context, R.layout.tile_pessoa, pessoaList) {

    private data class TileContactHolder(
        val nomeTv: TextView,
        val deveReceberTv: TextView,
        val devePagarTv: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val pessoa = pessoaList[position]
        var pessoaTileView = convertView

        if (pessoaTileView == null) {
            pessoaTileView = LayoutInflater.from(context).inflate(
                R.layout.tile_pessoa,
                parent,
                false
            )

            val tileContactHolder = TileContactHolder(
                pessoaTileView.findViewById(R.id.nomeTv),
                pessoaTileView.findViewById(R.id.deveReceberTv),
                pessoaTileView.findViewById(R.id.devePagarTv),
            )
            pessoaTileView.tag = tileContactHolder
        }

        with(pessoaTileView?.tag as TileContactHolder) {
            nomeTv.text = "Nome: ${pessoa.nome}"
            devePagarTv.text = "Valor pago: R$${pessoa.valorPago}"

            val valor = pessoa.valorReceber.toDouble()
            if (valor < 0) {
                deveReceberTv.text = "Deve receber: R$${pessoa.valorReceber}"
            } else {
                deveReceberTv.text = "Deve pagar: R$${pessoa.valorReceber}"
            }
        }

        return pessoaTileView
    }
}
