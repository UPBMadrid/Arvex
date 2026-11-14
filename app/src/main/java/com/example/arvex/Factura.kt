package com.example.arvex

data class Factura(
    val numero: String,
    val cufe: String,
    val nitVendedor: String,
    val nitComprador: String,
    val nombreCliente: String,
    val fechaHoraVenta: String,
    val valorSinImpuestos: Double,
    val valorConImpuestos: Double,
    val medioPago: String
)