package br.edu.ifsp.ads.pdm.aulas.splitthebill.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.edu.ifsp.ads.pdm.aulas.splitthebill.databinding.ActivityPessoaBinding
import br.edu.ifsp.ads.pdm.aulas.splitthebill.model.Constant.EXTRA_PESSOA
import br.edu.ifsp.ads.pdm.aulas.splitthebill.model.Constant.VIEW_PESSOA
import br.edu.ifsp.ads.pdm.aulas.splitthebill.model.Pessoa
import kotlin.random.Random

// Classe para a tela de detalhes da pessoa
class PessoaActivity : AppCompatActivity() {

    // Inicialização do ViewBinding
    private val apb: ActivityPessoaBinding by lazy {
        ActivityPessoaBinding.inflate(layoutInflater)
    }

    // Método chamado quando a Activity é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura o layout usando o ViewBinding
        setContentView(apb.root)

        // Recebe a pessoa da Activity anterior
        val recebPessoa = intent.getParcelableExtra<Pessoa>(EXTRA_PESSOA)

        // Preenche os campos de texto com os dados da pessoa recebida
        recebPessoa?.let { _recebPessoa ->
            with(apb) {
                with(_recebPessoa) {
                    nomeEt.setText(nome)
                    valorGastoEt.setText(devePagar)
                    valorReceberEt.setText(deveReceber)
                    descricaoEt.setText(descricao)
                    valorGastoEt2.setText(devePagar2)
                    valorReceberEt2.setText(deveReceber2)
                    descricaoEt2.setText(descricao2)
                    valorGastoEt3.setText(devePagar3)
                    valorReceberEt3.setText(deveReceber3)
                    descricaoEt3.setText(descricao3)
                }
            }
        }

        // Verifica se a tela está no modo de visualização (não editável)
        val viewPerson = intent.getBooleanExtra(VIEW_PESSOA, false)
        if (viewPerson) {
            // Desabilita a edição dos campos de texto e oculta o botão de salvar
            with(apb) {
                nomeEt.isEnabled = false
                valorGastoEt.isEnabled = false
                valorReceberEt.isEnabled = false
                descricaoEt.isEnabled = false
                valorGastoEt2.isEnabled = false
                valorReceberEt2.isEnabled = false
                descricaoEt2.isEnabled = false
                valorGastoEt3.isEnabled = false
                valorReceberEt3.isEnabled = false
                descricaoEt3.isEnabled = false
                saveBt.visibility = View.GONE
            }
        }

        // Configura o clique no botão de salvar
        apb.saveBt.setOnClickListener {
            // Cria uma nova pessoa com os dados dos campos de texto
            val person = Pessoa(
                id = recebPessoa?.id ?: Random(System.currentTimeMillis()).nextInt(),
                nome = apb.nomeEt.text.toString(),
                devePagar = apb.valorGastoEt.text.toString(),
                deveReceber = apb.valorReceberEt.text.toString(),
                descricao = apb.descricaoEt.text.toString(),
                devePagar2 = apb.valorGastoEt2.text.toString(),
                deveReceber2 = apb.valorReceberEt2.text.toString(),
                descricao2 = apb.descricaoEt2.text.toString(),
                devePagar3 = apb.valorGastoEt3.text.toString(),
                deveReceber3 = apb.valorReceberEt3.text.toString(),
                descricao3 = apb.descricaoEt3.text.toString()
            )
            // Prepara um Intent com a nova pessoa e encerra a Activity, enviando o resultado de volta
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_PESSOA, person)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}