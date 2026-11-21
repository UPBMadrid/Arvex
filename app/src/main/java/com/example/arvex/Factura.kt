package com.example.arvex

data class Factura(
    val id: String = "",
    val nitVendedor: String = "",
    val nitComprador: String = "",
    val fecha: String = "",
    val valorSinImpuestos: Double = 0.0,
    val valorIVA: Double = 0.0,
    val valorConImpuestos: Double = 0.0,
    val medioPago: String = "",
    val cufe: String = ""
)