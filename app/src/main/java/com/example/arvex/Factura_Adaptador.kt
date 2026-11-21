package com.example.arvex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale
import com.google.firebase.Firebase
import com.google.firebase.database.database

class FacturaAdapter(
    private val listaFacturas: MutableList<Factura>
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    private val formatoMoneda =
        NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    class FacturaViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNumeroFactura: TextView =
            itemView.findViewById(R.id.tvNumeroFactura)

        val tvCufe: TextView =
            itemView.findViewById(R.id.tvCufe)

        val tvNitVendedor: TextView =
            itemView.findViewById(R.id.tvNitVendedor)

        val tvNitComprador: TextView =
            itemView.findViewById(R.id.tvNitComprador)

        val tvFechaHoraVenta: TextView =
            itemView.findViewById(R.id.tvFechaHoraVenta)

        val tvValorSinImpuestos: TextView =
            itemView.findViewById(R.id.tvValorSinImpuestos)

        val tvIVA: TextView = itemView.findViewById(R.id.tvIVA)
        val tvValorConImpuestos: TextView =
            itemView.findViewById(R.id.tvValorConImpuestos)

        val tvMedioPago: TextView =
            itemView.findViewById(R.id.tvMedioPago)

        val btnVerFactura: Button =
            itemView.findViewById(R.id.btnVerFactura)
        val btnEliminarFactura: Button = itemView.findViewById(R.id.btnEliminarFactura)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacturaViewHolder {

        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)

        return FacturaViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: FacturaViewHolder,
        position: Int
    ) {

        val factura = listaFacturas[position]

        holder.tvNumeroFactura.text =
            "Factura: ${factura.id}"

        holder.tvCufe.text =
            "CUFE: ${factura.cufe}"

        holder.tvNitVendedor.text =
            "NIT vendedor: ${factura.nitVendedor}"

        holder.tvNitComprador.text =
            "NIT comprador: ${factura.nitComprador}"

        holder.tvFechaHoraVenta.text =
            "Fecha: ${factura.fecha}"

        holder.tvValorSinImpuestos.text =
            "Subtotal: ${formatoMoneda.format(factura.valorSinImpuestos)}"

        holder.tvValorConImpuestos.text =
            "Total: ${formatoMoneda.format(factura.valorConImpuestos)}"

        holder.tvMedioPago.text =
            "Medio de pago: ${factura.medioPago}"

        holder.btnVerFactura.setOnClickListener {
            mostrarDetalleFactura(holder.itemView, factura)
        }
        holder.btnEliminarFactura.setOnClickListener {
            Firebase.database.reference.child("facturas").child(factura.id).removeValue()
        }
    }

    override fun getItemCount(): Int {
        return listaFacturas.size
    }

    fun actualizarFacturas(nuevasFacturas: List<Factura>) {
        listaFacturas.clear()
        listaFacturas.addAll(nuevasFacturas)
        notifyDataSetChanged()
    }

    private fun mostrarDetalleFactura(
        vista: View,
        factura: Factura
    ) {

        val contexto = vista.context

        val detalle = StringBuilder()

        detalle.append("Factura: ${factura.id}\n\n")
        detalle.append("Productos:\n\n")

        if (factura.productos.isEmpty()) {

            detalle.append("No hay productos registrados.\n")

        } else {

            for (producto in factura.productos.values) {

                detalle.append("Producto: ${producto.nombre}\n")
                detalle.append(
                    "Precio unitario: ${
                        formatoMoneda.format(producto.precioUnitario)
                    }\n"
                )
                detalle.append("Cantidad: ${producto.cantidad}\n")
                detalle.append(
                    "Subtotal: ${
                        formatoMoneda.format(producto.subtotal)
                    }\n\n"
                )
            }
        }

        val iva = factura.valorConImpuestos -
                factura.valorSinImpuestos

        detalle.append("--------------------------\n")
        detalle.append(
            "Subtotal: ${
                formatoMoneda.format(factura.valorSinImpuestos)
            }\n"
        )
        detalle.append(
            "IVA (19%): ${formatoMoneda.format(iva)}\n"
        )
        detalle.append(
            "Total: ${
                formatoMoneda.format(factura.valorConImpuestos)
            }\n\n"
        )
        detalle.append("Medio de pago: ${factura.medioPago}\n")
        detalle.append("CUFE: ${factura.cufe}")

        AlertDialog.Builder(contexto)
            .setTitle("Detalle de la factura")
            .setMessage(detalle.toString())
            .setPositiveButton("Cerrar", null)
            .show()
    }
}