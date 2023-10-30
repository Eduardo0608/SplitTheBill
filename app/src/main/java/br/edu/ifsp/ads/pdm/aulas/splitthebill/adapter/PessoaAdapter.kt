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

    // Classe de dados para armazenar as referências para as views no layout do tile
    private data class TileContactHolder(
        val nomeTv: TextView,
        val deveReceberTv: TextView,
        val devePagarTv: TextView
    )

    // Sobrescreve o método getView() para inflar o layout do tile e popular as views com os dados da pessoa
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Obtem a pessoa da lista na posição especificada
        val pessoa = pessoaList[position]

        // Verifica se o tile já foi criado anteriormente
        var pessoaTileView = convertView
        if (pessoaTileView == null) {
            // Infla o layout do tile
            pessoaTileView = LayoutInflater.from(context).inflate(
                R.layout.tile_pessoa,
                parent,
                false
            )

            // Cria uma instância da classe TileContactHolder para armazenar as referências para as views no layout
            val tileContactHolder = TileContactHolder(
                pessoaTileView.findViewById(R.id.nomeTv),
                pessoaTileView.findViewById(R.id.deveReceberTv),
                pessoaTileView.findViewById(R.id.devePagarTv),
            )

            // Armazena a instância da classe TileContactHolder como uma tag na view do tile
            pessoaTileView.tag = tileContactHolder
        }

        // Obtem a instância da classe TileContactHolder da tag da view do tile
        with(pessoaTileView?.tag as TileContactHolder) {
            // Popula as views com os dados da pessoa
            nomeTv.text = "Nome: ${pessoa.nome}"
            devePagarTv.text = "Valor pago: R$${pessoa.devePagar}"

            // Verifica se o valor a receber é negativo
            val valor = pessoa.deveReceber.toDouble()
            if (valor < 0) {
                // Se for negativo, inverte o valor e altera o texto da view
                pessoa.deveReceber = (valor * -1).toString()
                deveReceberTv.text = "Deve receber: R$" + pessoa.deveReceber
            } else deveReceberTv.text = "Deve pagar: R$" + pessoa.deveReceber
        }

        // Retorna a view do tile
        return pessoaTileView
    }
}