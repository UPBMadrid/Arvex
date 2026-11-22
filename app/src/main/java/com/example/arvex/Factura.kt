
package com.example.arvex

data class DetalleProductoFactura(
    val nombre: String = "",
    val precioUnitario: Double = 0.0,
    val cantidad: Int = 0,
    val subtotal: Double = 0.0
)

data class Factura(
    val id: String = "",
    val nitVendedor: String = "",
    val nitComprador: String = "",
    val fecha: String = "",
    val productos: Map<String, DetalleProductoFactura> = emptyMap(),
    val valorSinImpuestos: Double = 0.0,
    val valorIVA: Double = 0.0,
    val valorConImpuestos: Double = 0.0,
    val medioPago: String = "",
    val cufe: String = ""
)
