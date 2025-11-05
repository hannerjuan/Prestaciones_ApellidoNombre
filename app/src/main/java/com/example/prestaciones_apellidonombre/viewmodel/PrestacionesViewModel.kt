package com.example.prestaciones_apellidonombre.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.prestaciones_apellidonombre.model.PrestacionesResult
import java.text.NumberFormat
import java.util.Locale

class PrestacionesViewModel : ViewModel() {

    val salario = mutableStateOf("")
    val diasTrabajados = mutableStateOf("")
    val incluyeAuxilioTransporte = mutableStateOf(true)
    val prestacionesResult = mutableStateOf<PrestacionesResult?>(null)
    val errorSalario = mutableStateOf<String?>(null)
    val errorDias = mutableStateOf<String?>(null)

    private val SMLMV = 1300000
    private val AUXILIO_TRANSPORTE = 162000

    fun calcularPrestaciones() {
        val salarioDouble = salario.value.toDoubleOrNull()
        val diasInt = diasTrabajados.value.toIntOrNull()

        val salarioValido = salarioDouble != null && salarioDouble > 0
        val diasValidos = diasInt != null && diasInt in 1..360

        errorSalario.value = if (!salarioValido) "El salario debe ser mayor a 0" else null
        errorDias.value = if (!diasValidos) "Los días deben estar entre 1 y 360" else null

        if (!salarioValido || !diasValidos) {
            prestacionesResult.value = null
            return
        }

        if (salarioDouble != null && diasInt != null) {
            val aplicaAuxilio = salarioDouble <= (2 * SMLMV)

            val salarioBaseCesantias = if (incluyeAuxilioTransporte.value && aplicaAuxilio) {
                salarioDouble + AUXILIO_TRANSPORTE
            } else {
                salarioDouble
            }

            val cesantias = (salarioBaseCesantias * diasInt) / 360
            val interesesCesantias = (cesantias * diasInt * 0.12) / 360
            val prima = (salarioBaseCesantias * diasInt) / 360
            val vacaciones = (salarioDouble * diasInt) / 720

            prestacionesResult.value = PrestacionesResult(
                prima = prima,
                cesantias = cesantias,
                interesesCesantias = interesesCesantias,
                vacaciones = vacaciones,
                total = prima + cesantias + interesesCesantias + vacaciones
            )
        }
    }

    fun limpiarCampos() {
        salario.value = ""
        diasTrabajados.value = ""
        incluyeAuxilioTransporte.value = true
        prestacionesResult.value = null
        errorSalario.value = null
        errorDias.value = null
    }

    fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        return format.format(amount)
    }
}