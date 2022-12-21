package com.angel.inventogical.viewModel

data class Articulo(
    var codigo: Int?,
    val nombre: String,
    val unidad: String,
    val existencia: Double,
    val precioS: Double,
    val precioBs: Double,
    val fechamodifi: String?,
    val precioPorTasa: Int,
)
