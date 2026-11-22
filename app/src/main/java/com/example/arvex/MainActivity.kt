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

        // Mostrar inicio de sesión
        setContentView(R.layout.activity_main)

        // Enlazar los campos
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
            val clave = etClave.text.toString().trim()

            if (correo.isEmpty() || clave.isEmpty()) {
                tvEstado.text = "Completa todos los campos"
                return@setOnClickListener
            }

            // Mostrar pantalla de carga
            LoadingInstance.showLoading(this)

            auth.signInWithEmailAndPassword(correo, clave)
                .addOnSuccessListener { resultado ->

                    val uid = resultado.user?.uid

                    if (uid != null) {

                        db.child("usuarios").child(uid).child("nombre")
                            .get()
                            .addOnSuccessListener { snapshot ->

                                val nombre = snapshot.getValue(String::class.java) ?: "Usuario"

                                LoadingInstance.hideLoading()
                                mostrarMenu()
                            }
                            .addOnFailureListener { error ->
                                LoadingInstance.hideLoading()
                                tvEstado.text = "Error al obtener usuario: ${error.message}"
                            }

                    } else {
                        LoadingInstance.hideLoading()
                        tvEstado.text = "No se pudo identificar al usuario"
                    }
                }
                .addOnFailureListener { error ->
                    LoadingInstance.hideLoading()
                    tvEstado.text = "Error al iniciar sesión: ${error.message}"
                }
        }
    } // Aquí termina onCreate()

    // MOSTRAR MENÚ PRINCIPAL
    private fun mostrarMenu() {

        setContentView(R.layout.main_menu)

        val btnRegistrarFactura =
            findViewById<Button>(R.id.btnRegistrarFactura)
        btnRegistrarFactura.setOnClickListener {
            val intent = Intent(this, Facturas_firebase::class.java)
            startActivity(intent)
        }
        val btnRegistroFacturas =
            findViewById<Button>(R.id.btnRegistroFacturas)
        btnRegistroFacturas.setOnClickListener {
            val intent = Intent(this, RegistroFacturasActivity::class.java)
            startActivity(intent)
        }

        val btnGuardarProducto =
            findViewById<Button>(R.id.btnGuardarProducto)
        btnGuardarProducto.setOnClickListener {
            val intent = Intent(this, RegistroProductoActivity::class.java)
            startActivity(intent)
        }

        val btnConsultarProductos =
            findViewById<Button>(R.id.btnConsultarProductos)
        btnConsultarProductos.setOnClickListener {
            val intent = Intent(this, productos_registro::class.java)
            startActivity(intent)
        }
    }
}
