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
    private val listaFacturas: List<Factura>,
    private val onVerFactura: (Factura) -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    class FacturaViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val numero = itemView.findViewById<TextView>(R.id.tvNumeroFactura)
        val cufe = itemView.findViewById<TextView>(R.id.tvCufe)
        val nitVendedor = itemView.findViewById<TextView>(R.id.tvNitVendedor)
        val nitComprador = itemView.findViewById<TextView>(R.id.tvNitComprador)
        val nombreCliente = itemView.findViewById<TextView>(R.id.tvNombreCliente)
        val fechaHora = itemView.findViewById<TextView>(R.id.tvFechaHoraVenta)
        val valorSinImpuestos = itemView.findViewById<TextView>(R.id.tvValorSinImpuestos)
        val valorConImpuestos = itemView.findViewById<TextView>(R.id.tvValorConImpuestos)
        val medioPago = itemView.findViewById<TextView>(R.id.tvMedioPago)
        val boton = itemView.findViewById<Button>(R.id.btnVerFactura)
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

        val formatoMoneda = NumberFormat.getCurrencyInstance(
            Locale("es", "CO")
        )

        holder.numero.text = "Factura #${factura.numero}"
        holder.cufe.text = "CUFE: ${factura.cufe}"
        holder.nitVendedor.text = "NIT vendedor: ${factura.nitVendedor}"
        holder.nitComprador.text = "NIT comprador: ${factura.nitComprador}"
        holder.nombreCliente.text = "Cliente: ${factura.nombreCliente}"
        holder.fechaHora.text = "Fecha y hora: ${factura.fechaHoraVenta}"

        holder.valorSinImpuestos.text =
            "Valor sin impuestos: ${formatoMoneda.format(factura.valorSinImpuestos)}"

        holder.valorConImpuestos.text =
            "Valor con impuestos: ${formatoMoneda.format(factura.valorConImpuestos)}"

        holder.medioPago.text = "Medio de pago: ${factura.medioPago}"

        holder.boton.setOnClickListener {
            onVerFactura(factura)
        }
    }

    override fun getItemCount(): Int {
        return listaFacturas.size
    }
}
