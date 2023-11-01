package br.edu.ifsp.ads.pdm.aulas.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.ads.pdm.aulas.splitthebill.R
import br.edu.ifsp.ads.pdm.aulas.splitthebill.adapter.PessoaAdapter
import br.edu.ifsp.ads.pdm.aulas.splitthebill.databinding.ActivityMainBinding
import br.edu.ifsp.ads.pdm.aulas.splitthebill.model.Constant.EXTRA_PESSOA
import br.edu.ifsp.ads.pdm.aulas.splitthebill.model.Constant.VIEW_PESSOA
import br.edu.ifsp.ads.pdm.aulas.splitthebill.model.Pessoa

// Classe principal da Activity
class MainActivity : AppCompatActivity() {

    // Declaração de variáveis e inicialização do ViewBinding
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Função para atualizar o valor total das compras na interface
    private fun setValorTotal(valorTotal: String) {
        amb.valorTotal.setText("Valor total das compras: R$" + valorTotal)
    }

    // Função para atualizar o valor dividido por pessoa na interface
    private fun setValorPorPessoa(valorPorPessoa: String) {
        amb.valorPorPessoa.setText("Valor total divido por pessoa: R$" + valorPorPessoa)
    }

    // Função para calcular e atualizar os valores
    private fun contaValores() {
        var valorTotal = 0.0
        // Itera sobre a lista de pessoas para calcular o valor total das compras
        for (i in 0 until pessoaList.count { true }) {
            val devePagar = pessoaList[i].devePagar.toDouble()
            val devePagar2 = pessoaList[i].devePagar2.toDouble()
            val devePagar3 = pessoaList[i].devePagar3.toDouble()
            valorTotal += (devePagar + devePagar2 + devePagar3)
            setValorTotal(valorTotal.toString())
        }
        // Calcula o valor dividido por pessoa e atualiza a interface
        val valorPorPessoa = valorTotal / pessoaList.count { true }
        for (i in 0 until pessoaList.count { true }) {
            val devePagar =
                pessoaList[i].devePagar.toDouble() + pessoaList[i].devePagar2.toDouble() + pessoaList[i].devePagar3.toDouble()
            pessoaList[i].deveReceber = (valorPorPessoa - devePagar).toString()
            setValorPorPessoa(valorPorPessoa.toString())
        }
    }

    // Lista de pessoas (data source)
    private val pessoaList: MutableList<Pessoa> = mutableListOf()

    // Adaptador para a lista de pessoas
    private lateinit var pessoaAdapter: PessoaAdapter

    // Inicialização do ActivityResultLauncher para tratar resultados de outras Activities
    private lateinit var parl: ActivityResultLauncher<Intent>

    // Método chamado quando a Activity é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura o layout usando o ViewBinding
        setContentView(amb.root)

        // Inicializa o adaptador e associa à ListView
        pessoaAdapter = PessoaAdapter(this, pessoaList)
        amb.pessoaLv.adapter = pessoaAdapter

        // Calcula e exibe os valores se a lista de pessoas não estiver vazia
        if (pessoaList.count { true } > 0) contaValores()

        // Configura o ActivityResultLauncher para tratar resultados de outras Activities
        parl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            // Trata o resultado da Activity de adicionar/editar pessoa
            if (result.resultCode == RESULT_OK) {
                val pessoa = result.data?.getParcelableExtra<Pessoa>(EXTRA_PESSOA)

                pessoa?.let { _pessoa ->
                    val position = pessoaList.indexOfFirst { it.id == _pessoa.id }
                    if (position != -1) {
                        // Se a pessoa já existe, atualiza na lista
                        pessoaList[position] = _pessoa
                    } else {
                        // Se a pessoa não existe, adiciona à lista
                        pessoaList.add(_pessoa)
                    }
                    pessoaAdapter.notifyDataSetChanged()
                }
            }
        }

        // Registra o ListView para o menu de contexto
        registerForContextMenu(amb.pessoaLv)

        // Configura o clique em um item da ListView para visualizar/editar uma pessoa
        amb.pessoaLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val pessoa = pessoaList[position]
                val pessoaIntent = Intent(this@MainActivity, PessoaActivity::class.java)
                pessoaIntent.putExtra(EXTRA_PESSOA, pessoa)
                pessoaIntent.putExtra(VIEW_PESSOA, true)
                startActivity(pessoaIntent)
            }
    }

    // Método chamado quando a Activity volta à frente
    override fun onResume() {
        super.onResume()
        // Recalcula e exibe os valores se a lista de pessoas não estiver vazia
        if (pessoaList.count { true } > 0) contaValores()
    }

    // Método para criar o menu de opções na barra de ação
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Método chamado quando um item do menu de opções é selecionado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addPessoaMi -> {
                // Inicia a Activity de adicionar pessoa usando o ActivityResultLauncher
                parl.launch(Intent(this, PessoaActivity::class.java))
                true
            }

            else -> {
                false
            }
        }
    }

    // Método chamado para criar o menu de contexto ao pressionar e segurar um item da lista
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    // Método chamado quando um item do menu de contexto é selecionado
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.removePessoaMi -> {
                // Remove a pessoa da lista e recalcula os valores
                pessoaList.removeAt(position)
                contaValores()
                pessoaAdapter.notifyDataSetChanged()
                true
            }

            R.id.editPessoaMi -> {
                // Inicia a Activity de editar pessoa usando o ActivityResultLauncher
                val pessoa = pessoaList[position]
                val pessoaIntent = Intent(this, PessoaActivity::class.java)
                pessoaIntent.putExtra(EXTRA_PESSOA, pessoa)
                pessoaIntent.putExtra(VIEW_PESSOA, false)
                parl.launch(pessoaIntent)
                true
            }

            else -> {
                false
            }
        }
    }
}