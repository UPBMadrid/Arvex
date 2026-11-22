package com.example.arvex

import android.widget.Button
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class productos_registro : AppCompatActivity() {

    private lateinit var rvProductos: RecyclerView
    private lateinit var adapter: ProductoAdapter

    private val listaProductos = mutableListOf<Producto>()

    private val db = FirebaseDatabase.getInstance()
        .reference
        .child("productos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_productos)

        rvProductos = findViewById(R.id.rvProductos)

        adapter = ProductoAdapter(listaProductos)

        rvProductos.layoutManager = LinearLayoutManager(this)
        rvProductos.adapter = adapter

        cargarProductos()
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu)

        btnVolverMenu.setOnClickListener {
            finish()
        }
    }

    private fun cargarProductos() {

        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val nuevosProductos = mutableListOf<Producto>()

                for (productoSnapshot in snapshot.children) {

                    val producto = productoSnapshot.getValue(Producto::class.java)

                    if (producto != null) {

                        val productoConId = producto.copy(
                            id = productoSnapshot.key ?: ""
                        )

                        nuevosProductos.add(productoConId)
                    }
                }

                adapter.actualizarProductos(nuevosProductos)
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(
                    this@productos_registro,
                    "Error al cargar productos: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}