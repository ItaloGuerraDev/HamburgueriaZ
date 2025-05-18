/*
* Autor: Italo Guerra
* email: italo2dev@gmail.com
* */
package com.example.hamburgueriaz

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.Toast
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.hamburgueriaz.ui.theme.HamburgueriaZTheme

class MainActivity : ComponentActivity() {
    private lateinit var baconCheckBox: CheckBox
    private lateinit var queijoCheckBox: CheckBox
    private lateinit var onionRingsCheckBox: CheckBox
    private lateinit var quantityTextView: TextView
    private lateinit var decreaseButton: Button
    private lateinit var increaseButton: Button
    private lateinit var totalTextView: TextView
    private lateinit var emailEditText: TextInputEditText
    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var fazerPedidoButton: Button

    private var quantity = 0
    private var totalPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar as views
        baconCheckBox = findViewById(R.id.baconCheckBox)
        queijoCheckBox = findViewById(R.id.queijoCheckBox)
        onionRingsCheckBox = findViewById(R.id.onionRingsCheckBox)
        quantityTextView = findViewById(R.id.quantityTextView)
        decreaseButton = findViewById(R.id.decreaseButton)
        increaseButton = findViewById(R.id.increaseButton)
        totalTextView = findViewById(R.id.totalTextView)
        emailEditText = findViewById(R.id.emailEditText)
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout)
        fazerPedidoButton = findViewById(R.id.fazerPedidoButton)

        decreaseButton.setOnClickListener {
            if (quantity > 0) {
                quantity--
                updateQuantity()
                updateTotal()
            }
        }

        increaseButton.setOnClickListener {
            quantity++
            updateQuantity()
            updateTotal()
        }

        baconCheckBox.setOnCheckedChangeListener { _, isChecked ->
            updateTotal()
        }

        queijoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            updateTotal()
        }

        onionRingsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            updateTotal()
        }

        fazerPedidoButton.setOnClickListener {
            val pedido = buildPedido()
            val recipientEmail = emailEditText.text.toString()

            if (recipientEmail.isNotEmpty()) {
                enviarEmail(recipientEmail, "Novo Pedido da Hamburgueria", "Detalhes do pedido: $pedido")
                Toast.makeText(this, "Pedido sendo enviado para $recipientEmail", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, digite o e-mail para envio.", Toast.LENGTH_SHORT).show()
            }
        }

        updateQuantity()
        updateTotal()
    }

        private fun updateQuantity() {
            quantityTextView.text = quantity.toString()
        }

        private fun updateTotal() {
            totalPrice = 0.0
            if (baconCheckBox.isChecked) totalPrice += 2.0 // Preço do bacon
            if (queijoCheckBox.isChecked) totalPrice += 2.0 // Preço do queijo
            if (onionRingsCheckBox.isChecked) totalPrice += 3.0 // Preço do onion rings

            totalPrice *= quantity
            totalTextView.text = "R$ %.2f".format(totalPrice)
        }

        private fun enviarEmail(destinatario: String, assunto: String, corpo: String) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$destinatario")
                putExtra(Intent.EXTRA_SUBJECT, assunto)
                putExtra(Intent.EXTRA_TEXT, corpo)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado.", Toast.LENGTH_SHORT).show()
            }
        }

        private fun buildPedido(): String {
            val itens = mutableListOf<String>()
            if (baconCheckBox.isChecked) itens.add("Bacon")
            if (queijoCheckBox.isChecked) itens.add("Queijo")
            if (onionRingsCheckBox.isChecked) itens.add("Onion Rings")

            return "Quantidade: $quantity, Itens: ${itens.joinToString(", ")}, Total: R$ %.2f".format(totalPrice)
        }
}