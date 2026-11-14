
package com.example.arvex

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegistroFacturasActivity : AppCompatActivity() {

    private lateinit var rvFacturas: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_de_facturas)

        rvFacturas = findViewById(R.id.rvFacturas)

        val listaFacturas = listOf(

            Factura(
                numero = "001",
                cufe = "A1B2C3D4E5F67890",
                nitVendedor = "900123456-1",
                nitComprador = "1023456789",
                nombreCliente = "Juan Pérez",
                fechaHoraVenta = "14/11/2026 10:30 AM",
                valorSinImpuestos = 210084.03,
                valorConImpuestos = 250000.0,
                medioPago = "Transferencia"
            ),

            Factura(
                numero = "002",
                cufe = "F9E8D7C6B5A43210",
                nitVendedor = "900123456-1",
                nitComprador = "987654321",
                nombreCliente = "María López",
                fechaHoraVenta = "13/11/2026 03:15 PM",
                valorSinImpuestos = 151260.50,
                valorConImpuestos = 180000.0,
                medioPago = "Efectivo"
            ),

            Factura(
                numero = "003",
                cufe = "123ABC456DEF7890",
                nitVendedor = "900123456-1",
                nitComprador = "456789123",
                nombreCliente = "Carlos Gómez",
                fechaHoraVenta = "12/11/2026 09:45 AM",
                valorSinImpuestos = 268907.56,
                valorConImpuestos = 320000.0,
                medioPago = "Tarjeta"
            )
        )

        rvFacturas.layoutManager = LinearLayoutManager(this)

        rvFacturas.adapter = FacturaAdapter(listaFacturas) { factura ->

            Toast.makeText(
                this,
                "Factura seleccionada: ${factura.numero}",
                Toast.LENGTH_SHORT
            ).show()

        }
    }
}