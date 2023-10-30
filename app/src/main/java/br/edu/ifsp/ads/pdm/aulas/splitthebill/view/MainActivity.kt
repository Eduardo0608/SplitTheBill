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


class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setValorTotal(valorTotal: String) {
        amb.valorTotal.setText("Valor total das compras: R$" + valorTotal)
    }

    private fun setValorPorPessoa(valorPorPessoa: String) {
        amb.valorPorPessoa.setText("Valor total divido por pessoa: R$" + valorPorPessoa)
    }

    private fun contaValores() {
        var valorTotal = 0.0
        for (i in 0 until pessoaList.count { true }) {
            val devePagar = pessoaList[i].devePagar.toDouble()
            valorTotal += devePagar
            setValorTotal(valorTotal.toString())
        }
        val valorPorPessoa = valorTotal / pessoaList.count { true }
        for (i in 0 until pessoaList.count { true }) {
            val devePagar = pessoaList[i].devePagar.toDouble()
            pessoaList[i].deveReceber = (valorPorPessoa - devePagar).toString()
            setValorPorPessoa(valorPorPessoa.toString())
        }
    }

    // Data source
    private val pessoaList: MutableList<Pessoa> = mutableListOf()

    // Adapter
    private lateinit var pessoaAdapter: PessoaAdapter

    private lateinit var parl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        //fillPessoaList()

        pessoaAdapter = PessoaAdapter(this, pessoaList)
        amb.pessoaLv.adapter = pessoaAdapter

        if (pessoaList.count { true } > 0) contaValores()

        pessoaAdapter.notifyDataSetChanged()

        parl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val pessoa = result.data?.getParcelableExtra<Pessoa>(EXTRA_PESSOA)

                pessoa?.let { _pessoa ->
                    val position = pessoaList.indexOfFirst { it.id == _pessoa.id }
                    if (position != -1) {
                        // Alterar na posição
                        pessoaList[position] = _pessoa
                    } else {
                        pessoaList.add(_pessoa)
                    }
                    pessoaAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.pessoaLv)

        amb.pessoaLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val pessoa = pessoaList[position]
                val pessoaIntent = Intent(this@MainActivity, PessoaActivity::class.java)
                pessoaIntent.putExtra(EXTRA_PESSOA, pessoa)
                pessoaIntent.putExtra(VIEW_PESSOA, true)
                startActivity(pessoaIntent)
            }
    }

    override fun onResume() {
        super.onResume()
        if (pessoaList.count { true } > 0) contaValores()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addPessoaMi -> {
                parl.launch(Intent(this, PessoaActivity::class.java))
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.removePessoaMi -> {
                // Remove a pessoa
                pessoaList.removeAt(position)
                contaValores()
                pessoaAdapter.notifyDataSetChanged()
                true
            }

            R.id.editPessoaMi -> {
                // Chama a tela para editar a pessoa
                val contact = pessoaList[position]
                val contactIntent = Intent(this, PessoaActivity::class.java)
                contactIntent.putExtra(EXTRA_PESSOA, contact)
                contactIntent.putExtra(VIEW_PESSOA, false)
                parl.launch(contactIntent)
                true
            }

            else -> {
                false
            }
        }
    }
}