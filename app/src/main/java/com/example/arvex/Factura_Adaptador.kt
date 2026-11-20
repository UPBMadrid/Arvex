package com.example.arvex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class FacturaAdapter(
    private val listaFacturas: MutableList<Factura>
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    private val IVA = 0.19

    class FacturaViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvNumeroFactura: TextView = itemView.findViewById(R.id.tvNumeroFactura)
        val tvCufe: TextView = itemView.findViewById(R.id.tvCufe)
        val tvNitVendedor: TextView = itemView.findViewById(R.id.tvNitVendedor)
        val tvNitComprador: TextView = itemView.findViewById(R.id.tvNitComprador)
        val tvFechaHoraVenta: TextView = itemView.findViewById(R.id.tvFechaHoraVenta)
        val tvValorSinImpuestos: TextView = itemView.findViewById(R.id.tvValorSinImpuestos)
        val tvIVA: TextView = itemView.findViewById(R.id.tvIVA)
        val tvValorConImpuestos: TextView = itemView.findViewById(R.id.tvValorConImpuestos)
        val tvMedioPago: TextView = itemView.findViewById(R.id.tvMedioPago)
        val btnVerFactura: Button = itemView.findViewById(R.id.btnVerFactura)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacturaViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_factura, parent, false)
        return FacturaViewHolder(vista)
    }
    override fun onBindViewHolder(
        holder: FacturaViewHolder,
        position: Int
    ) {
        val factura = listaFacturas[position]
        val ivaCalculado = factura.valorSinImpuestos * IVA
        val valorFinal = factura.valorSinImpuestos + ivaCalculado

        holder.tvNumeroFactura.text = "Factura #${factura.id.takeLast(6)}"
        holder.tvCufe.text = "CUFE: ${factura.cufe}"
        holder.tvNitVendedor.text = "NIT vendedor: ${factura.nitVendedor}"
        holder.tvNitComprador.text = "NIT comprador: ${factura.nitComprador}"
        holder.tvFechaHoraVenta.text = "Fecha y hora: ${factura.fecha}"
        holder.tvValorSinImpuestos.text = "Valor sin impuestos: ${(factura.valorSinImpuestos)}"
        holder.tvIVA.text = "IVA (19 %): ${(ivaCalculado)}"
        holder.tvValorConImpuestos.text = "Valor con impuestos: ${(valorFinal)}"
        holder.tvMedioPago.text = "Medio de pago: ${factura.medioPago}"


        holder.btnVerFactura.setOnClickListener {
            val contexto = holder.itemView.context
            android.app.AlertDialog.Builder(contexto).setTitle("Factura ${factura.id.takeLast(6)}").setMessage(
                    """
                    CUFE: ${factura.cufe}
                    NIT vendedor: ${factura.nitVendedor}
                    NIT comprador: ${factura.nitComprador}
                    Fecha: ${factura.fecha}
                    Medio de pago: ${factura.medioPago}
                    Valor sin IVA: ${(factura.valorSinImpuestos)}
                    IVA (19 %): ${(ivaCalculado)}
                    TOTAL: ${(valorFinal)}
                    """.trimIndent()
                )
                .setPositiveButton("Cerrar", null).show()
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
}
