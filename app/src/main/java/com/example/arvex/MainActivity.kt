package com.example.arvex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mostrar pantalla de inicio de sesión
        setContentView(R.layout.activity_main)

        // Enlazar los campos del inicio de sesión
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etClave = findViewById<EditText>(R.id.etClave)

        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        val tvEstado = findViewById<TextView>(R.id.tvEstado)

        // REGISTRAR USUARIO
        btnRegistrar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val clave = etClave.text.toString()

            if (nombre.isEmpty() || correo.isEmpty() || clave.length < 6) {
                tvEstado.text = "Completa los campos. La clave debe tener mínimo 6 caracteres."
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(correo, clave)
                .addOnSuccessListener { resultado ->

                    val uid = resultado.user!!.uid

                    val usuario = mapOf(
                        "nombre" to nombre,
                        "correo" to correo
                    )

                    db.child("usuarios").child(uid).setValue(usuario)
                        .addOnSuccessListener {
                            tvEstado.text = "Usuario registrado: $nombre"
                        }
                        .addOnFailureListener { error ->
                            tvEstado.text = "Error al guardar: ${error.message}"
                        }
                }
                .addOnFailureListener { error ->
                    tvEstado.text = "Error al registrar: ${error.message}"
                }
        }

        // INICIAR SESIÓN
        btnIngresar.setOnClickListener {

            val correo = etCorreo.text.toString().trim()
            val clave = etClave.text.toString()

            if (correo.isEmpty() || clave.isEmpty()) {
                tvEstado.text = "Escribe tu correo y contraseña."
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(correo, clave)
                .addOnSuccessListener { resultado ->

                    val uid = resultado.user!!.uid

                    db.child("usuarios").child(uid).child("nombre").get()
                        .addOnSuccessListener { dato ->

                            val nombre = dato.getValue(String::class.java)
                            mostrarMenu()

                        }
                        .addOnFailureListener {
                            // Si no se puede leer el nombre,
                            // igualmente se permite entrar.
                            mostrarMenu()
                        }
                }
                .addOnFailureListener { error ->
                    tvEstado.text = "Error al iniciar sesión: ${error.message}"
                }
        }
    }

    // MOSTRAR MENÚ PRINCIPAL
    private fun mostrarMenu() {

        setContentView(R.layout.main_menu)

        val btnRegistrarFactura =
            findViewById<Button>(R.id.btnRegistrarFactura)

        val btnRegistroFacturas =
            findViewById<Button>(R.id.btnRegistroFacturas)

        val btnGuardarProducto =
            findViewById<Button>(R.id.btnGuardarProducto)

        val btnConsultarProductos =
            findViewById<Button>(R.id.btnConsultarProductos)

        // Abrir formulario de facturas
        btnRegistrarFactura.setOnClickListener {
            val intent = Intent(this, Facturas_firebase::class.java)
            startActivity(intent)
        }

        // Los otros tres botones se conectarán posteriormente.
    }
}