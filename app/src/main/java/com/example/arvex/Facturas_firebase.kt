package com.example.arvex

    import android.annotation.SuppressLint
    import android.os.Handler
    import android.content.Intent
    import android.os.Looper
    import android.os.Bundle
    import android.widget.Button
    import android.widget.EditText
    import android.widget.TextView
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.google.firebase.Firebase
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.database.database

    class Facturas_firebase : AppCompatActivity() {

        private val db = Firebase.database.reference

        @SuppressLint("MissingInflatedId")
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
                    medioP.isEmpty() || cufe.isEmpty() || valor == null || valor < 0) {
                    tvEstado2.text = "Llena todos los campos primero y no mezcles números con letras"
                    return@setOnClickListener
                }
                val facturaId = db.child("facturas").push().key
                if (facturaId == null) {
                    tvEstado2.text = "Error al generar ID"
                    return@setOnClickListener
                }

                val factura = mapOf(
                    "nitVendedor" to nitV,
                    "nitComprador" to nitC,
                    "fecha" to fecha,
                    "valorSinImpuestos" to valor,
                    "medioPago" to medioP,
                    "cufe" to cufe
                )

                    db.child("facturas").child(facturaId).setValue(factura).addOnSuccessListener {

                            etNITV.text.clear()
                            etNITC.text.clear()
                            etFecha.text.clear()
                            etValorSI.text.clear()
                            etMedioP.text.clear()
                            etCUFE.text.clear()
                        }
                        .addOnFailureListener { e ->
                            tvEstado2.text = "Error al guardar"
                        }
                }
            btnMenu.setOnClickListener {
                setContentView(R.layout.main_menu)
            }
        }
}