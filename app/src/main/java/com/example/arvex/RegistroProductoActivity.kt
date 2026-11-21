package com.example.arvex

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database

class RegistroProductoActivity : AppCompatActivity() {

    private val db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_de_producto)

        val etNombre = findViewById<EditText>(R.id.etNombreProducto)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcionProducto)
        val etPrecio = findViewById<EditText>(R.id.etPrecioProducto)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarProducto)
        val btnVolver = findViewById<Button>(R.id.btnVolverMenu)

        val tvEstado = findViewById<TextView>(R.id.tvEstadoProducto)

        btnGuardar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val precioTexto = etPrecio.text.toString().trim()

            val precio = precioTexto.toDoubleOrNull()

            if (nombre.isEmpty() || descripcion.isEmpty() || precio == null) {
                tvEstado.text = "Completa todos los campos correctamente"
                return@setOnClickListener
            }

            if (precio <= 0.0) {
                tvEstado.text = "El precio debe ser mayor que cero"
                return@setOnClickListener
            }

            val productoId = db.child("productos").push().key

            if (productoId == null) {
                tvEstado.text = "Error al generar el ID del producto"
                return@setOnClickListener
            }

            val producto = mapOf(
                "nombre" to nombre,
                "descripcion" to descripcion,
                "precio" to precio
            )

            db.child("productos").child(productoId).setValue(producto)
                .addOnSuccessListener {

                    tvEstado.text = "Producto registrado correctamente"

                    etNombre.text.clear()
                    etDescripcion.text.clear()
                    etPrecio.text.clear()
                }
                .addOnFailureListener { error ->
                    tvEstado.text = "Error al guardar: ${error.message}"
                }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}