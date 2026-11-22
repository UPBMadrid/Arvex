package com.example.arvex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase

class ProductoAdapter(
    private val listaProductos: MutableList<Producto>
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private val formatoMoneda =
        NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    class ProductoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvNombre: TextView =
            itemView.findViewById(R.id.tvNombre)

        val tvDescripcion: TextView =
            itemView.findViewById(R.id.tvDescripción)

        val tvPrecio: TextView =
            itemView.findViewById(R.id.tvValor)

        val btnEliminar: Button =
            itemView.findViewById(R.id.btnEliminarProducto)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)

        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductoViewHolder,
        position: Int
    ) {

        val producto = listaProductos[position]

        holder.tvNombre.text = "Nombre: ${producto.nombre}"
        holder.tvDescripcion.text = "Descripción: ${producto.descripcion}"

        val formato = NumberFormat.getCurrencyInstance(
            Locale("es", "CO")
        )

        holder.tvPrecio.text = "Total: ${formatoMoneda.format(producto.precio)}"

        holder.btnEliminar.setOnClickListener {
            FirebaseDatabase.getInstance()
                .reference
                .child("productos")
                .child(producto.id)
                .removeValue()
        }
    }

    override fun getItemCount(): Int {
        return listaProductos.size
    }

    fun actualizarProductos(nuevosProductos: List<Producto>) {
        listaProductos.clear()
        listaProductos.addAll(nuevosProductos)
        notifyDataSetChanged()
    }
}
