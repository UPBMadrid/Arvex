package com.example.arvex

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Facturas_firebase : AppCompatActivity() {

    private val db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facturas_ingreso_layout)

        val etNITV = findViewById<EditText>(R.id.etNITV)
        val etNITC = findViewById<EditText>(R.id.etNITC)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val etValorSI = findViewById<EditText>(R.id.etvalorSI)
        val etMedioP = findViewById<EditText>(R.id.etMedioP)
        val etCUFE = findViewById<EditText>(R.id.etCUFE)

        val btnIntroducir = findViewById<Button>(R.id.btnIntroducir)
        val btnMenu = findViewById<Button>(R.id.btnMenu)
        val tvEstado2 = findViewById<TextView>(R.id.tvEstado2)

        btnIntroducir.setOnClickListener {

            val nitV = etNITV.text.toString().trim()
            val nitC = etNITC.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val valorSI = etValorSI.text.toString().trim()
            val medioP = etMedioP.text.toString().trim()
            val cufe = etCUFE.text.toString().trim()

            val valor = valorSI.toDoubleOrNull()

            if (nitV.isEmpty() || nitC.isEmpty() ||
                fecha.isEmpty() || valorSI.isEmpty() ||
                medioP.isEmpty() || cufe.isEmpty() ||
                valor == null || valor < 0.0
            ) {
                tvEstado2.text = "Llena todos los campos correctamente"
                return@setOnClickListener
            }

            // Calcular IVA del 19% y valor total
            val iva = kotlin.math.round(valor * 0.19 * 100) / 100.0
            val valorTotal = valor + iva

            // Generar ID para la factura
            val facturaId = db.child("facturas").push().key

            if (facturaId == null) {
                tvEstado2.text = "Error al generar ID"
                return@setOnClickListener
            }

            // Datos que se guardarán en Firebase
            val factura = mapOf(
                "nitVendedor" to nitV,
                "nitComprador" to nitC,
                "fecha" to fecha,
                "valorSinImpuestos" to valor,
                "valorConImpuestos" to valorTotal,
                "medioPago" to medioP,
                "cufe" to cufe
            )

            db.child("facturas").child(facturaId).setValue(factura)
                .addOnSuccessListener {

                    tvEstado2.text = "Factura guardada correctamente"

                    etNITV.text.clear()
                    etNITC.text.clear()
                    etFecha.text.clear()
                    etValorSI.text.clear()
                    etMedioP.text.clear()
                    etCUFE.text.clear()
                }
                .addOnFailureListener { error ->
                    tvEstado2.text = "Error al guardar: ${error.message}"
                }
        }

        btnMenu.setOnClickListener {
            val intent = android.content.Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}