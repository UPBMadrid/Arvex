package com.example.arvex

import android.widget.Button
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase

class RegistroFacturasActivity : AppCompatActivity() {

    private lateinit var rvFacturas: RecyclerView
    private lateinit var adapter: FacturaAdapter

    private val listaFacturas = mutableListOf<Factura>()

    private val db = FirebaseDatabase.getInstance()
        .reference
        .child("facturas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_de_facturas)

        rvFacturas = findViewById(R.id.rvFacturas)

        adapter = FacturaAdapter(listaFacturas)

        rvFacturas.layoutManager = LinearLayoutManager(this)
        rvFacturas.adapter = adapter

        cargarFacturas()
        val btnVolverMenu = findViewById<Button>(R.id.btnVolverMenu)

        btnVolverMenu.setOnClickListener {
            finish()
        }
    }

    private fun cargarFacturas() {

        db.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val nuevasFacturas = mutableListOf<Factura>()

                for (facturaSnapshot in snapshot.children) {

                    val factura = facturaSnapshot.getValue(Factura::class.java)

                    if (factura != null) {

                        val facturaConId = factura.copy(
                            id = facturaSnapshot.key ?: ""
                        )

                        nuevasFacturas.add(facturaConId)
                    }
                }

                adapter.actualizarFacturas(nuevasFacturas)
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(
                    this@RegistroFacturasActivity,
                    "Error al cargar facturas: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}