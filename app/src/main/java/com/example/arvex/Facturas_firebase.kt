package com.example.arvex

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.text.NumberFormat
import java.util.Locale

class Facturas_firebase : AppCompatActivity() {

    private val db = Firebase.database.reference

    private lateinit var llProductos: LinearLayout
    private lateinit var tvSubtotal: TextView
    private lateinit var tvIVA: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvEstado2: TextView

    private val productosDisponibles = mutableListOf<ProductoUI>()

    private val formatoMoneda =
        NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    private data class ProductoUI(
        val id: String,
        val nombre: String,
        val precio: Double,
        val checkBox: CheckBox,
        val etCantidad: EditText
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facturas_ingreso_layout)

        val etNITV = findViewById<EditText>(R.id.etNITV)
        val etNITC = findViewById<EditText>(R.id.etNITC)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val etMedioP = findViewById<EditText>(R.id.etMedioP)
        val etCUFE = findViewById<EditText>(R.id.etCUFE)

        llProductos = findViewById(R.id.llProductos)
        tvSubtotal = findViewById(R.id.tvSubtotal)
        tvIVA = findViewById(R.id.tvIVA)
        tvTotal = findViewById(R.id.tvTotal)
        tvEstado2 = findViewById(R.id.tvEstado2)

        val btnIntroducir = findViewById<Button>(R.id.btnIntroducir)
        val btnMenu = findViewById<Button>(R.id.btnMenu)

        cargarProductos()

        btnIntroducir.setOnClickListener {

            val nitV = etNITV.text.toString().trim()
            val nitC = etNITC.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val medioP = etMedioP.text.toString().trim()
            val cufe = etCUFE.text.toString().trim()

            if (nitV.isEmpty() || nitC.isEmpty() ||
                fecha.isEmpty() || medioP.isEmpty() ||
                cufe.isEmpty()
            ) {
                tvEstado2.text = "Completa todos los campos"
                return@setOnClickListener
            }

            val productosFactura = mutableMapOf<String, Any>()
            var subtotal = 0.0

            for (producto in productosDisponibles) {

                if (producto.checkBox.isChecked) {

                    val cantidad = producto.etCantidad.text
                        .toString().toIntOrNull()

                    if (cantidad == null || cantidad <= 0) {
                        tvEstado2.text =
                            "Ingresa una cantidad válida para ${producto.nombre}"
                        return@setOnClickListener
                    }

                    val subtotalProducto = producto.precio * cantidad

                    subtotal += subtotalProducto

                    productosFactura[producto.id] = mapOf(
                        "nombre" to producto.nombre,
                        "precioUnitario" to producto.precio,
                        "cantidad" to cantidad,
                        "subtotal" to subtotalProducto
                    )
                }
            }

            if (productosFactura.isEmpty()) {
                tvEstado2.text = "Selecciona al menos un producto"
                return@setOnClickListener
            }

            val iva = kotlin.math.round(subtotal * 0.19 * 100) / 100.0
            val valorTotal = subtotal + iva

            val facturaId = db.child("facturas").push().key

            if (facturaId == null) {
                tvEstado2.text = "Error al generar el ID de la factura"
                return@setOnClickListener
            }

            val factura = mapOf(
                "nitVendedor" to nitV,
                "nitComprador" to nitC,
                "fecha" to fecha,
                "productos" to productosFactura,
                "valorSinImpuestos" to subtotal,
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
                    etMedioP.text.clear()
                    etCUFE.text.clear()

                    for (producto in productosDisponibles) {
                        producto.checkBox.isChecked = false
                        producto.etCantidad.setText("1")
                        producto.etCantidad.isEnabled = false
                    }

                    actualizarTotales()
                }
                .addOnFailureListener { error ->
                    tvEstado2.text =
                        "Error al guardar: ${error.message}"
                }
        }

        btnMenu.setOnClickListener {
            finish()
        }
    }

    private fun cargarProductos() {

        db.child("productos").get()
            .addOnSuccessListener { snapshot ->

                llProductos.removeAllViews()
                productosDisponibles.clear()

                if (!snapshot.exists()) {
                    val mensaje = TextView(this)
                    mensaje.text = "No hay productos registrados"
                    llProductos.addView(mensaje)
                    return@addOnSuccessListener
                }

                for (productoSnapshot in snapshot.children) {

                    val id = productoSnapshot.key ?: continue

                    val nombre = productoSnapshot
                        .child("nombre")
                        .getValue(String::class.java) ?: continue

                    val precio = productoSnapshot
                        .child("precio")
                        .getValue(Double::class.java) ?: 0.0

                    val fila = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(16, 12, 16, 12)
                    }

                    val checkBox = CheckBox(this).apply {
                        text = nombre
                        textSize = 16f
                    }

                    val tvPrecio = TextView(this).apply {
                        text = "Precio unitario: ${formatoMoneda.format(precio)}"
                        textSize = 14f
                    }

                    val etCantidad = EditText(this).apply {
                        hint = "Cantidad"
                        inputType =
                            android.text.InputType.TYPE_CLASS_NUMBER
                        setText("1")
                        isEnabled = false
                    }

                    fila.addView(checkBox)
                    fila.addView(tvPrecio)
                    fila.addView(etCantidad)

                    llProductos.addView(
                        fila,
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    )

                    val productoUI = ProductoUI(
                        id,
                        nombre,
                        precio,
                        checkBox,
                        etCantidad
                    )

                    productosDisponibles.add(productoUI)

                    checkBox.setOnCheckedChangeListener { _, seleccionado ->
                        etCantidad.isEnabled = seleccionado
                        actualizarTotales()
                    }

                    etCantidad.addTextChangedListener(object : TextWatcher {

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {}

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            actualizarTotales()
                        }

                        override fun afterTextChanged(s: Editable?) {}
                    })
                }

                actualizarTotales()
            }
            .addOnFailureListener { error ->
                tvEstado2.text =
                    "Error al cargar productos: ${error.message}"
            }
    }

    private fun actualizarTotales() {

        var subtotal = 0.0

        for (producto in productosDisponibles) {

            if (producto.checkBox.isChecked) {

                val cantidad = producto.etCantidad.text
                    .toString().toIntOrNull() ?: 0

                if (cantidad > 0) {
                    subtotal += producto.precio * cantidad
                }
            }
        }

        val iva = kotlin.math.round(subtotal * 0.19 * 100) / 100.0
        val total = subtotal + iva

        tvSubtotal.text = "Subtotal: ${formatoMoneda.format(subtotal)}"
        tvIVA.text = "IVA (19%): ${formatoMoneda.format(iva)}"
        tvTotal.text = "Total: ${formatoMoneda.format(total)}"
    }
}