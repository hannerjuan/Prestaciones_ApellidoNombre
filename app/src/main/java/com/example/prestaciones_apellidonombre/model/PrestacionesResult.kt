package com.example.prestaciones_apellidonombre.model

data class PrestacionesResult(
    val prima: Double = 0.0,
    val cesantias: Double = 0.0,
    val interesesCesantias: Double = 0.0,
    val vacaciones: Double = 0.0,
    val total: Double = 0.0
)
