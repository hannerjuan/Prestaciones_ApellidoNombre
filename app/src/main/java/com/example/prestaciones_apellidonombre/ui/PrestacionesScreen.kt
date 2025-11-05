package com.example.prestaciones_apellidonombre.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prestaciones_apellidonombre.viewmodel.PrestacionesViewModel

@Composable
fun PrestacionesScreen(viewModel: PrestacionesViewModel = viewModel()) {
    val salario = viewModel.salario.value
    val diasTrabajados = viewModel.diasTrabajados.value
    val incluyeAuxilio = viewModel.incluyeAuxilioTransporte.value
    val result = viewModel.prestacionesResult.value
    val errorSalario = viewModel.errorSalario.value
    val errorDias = viewModel.errorDias.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = salario,
            onValueChange = { viewModel.salario.value = it },
            label = { Text("Salario Mensual (COP)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorSalario != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (errorSalario != null) {
            Text(errorSalario, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = diasTrabajados,
            onValueChange = { viewModel.diasTrabajados.value = it },
            label = { Text("Días Trabajados (máx. 360)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorDias != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (errorDias != null) {
            Text(errorDias, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = incluyeAuxilio,
                onCheckedChange = { viewModel.incluyeAuxilioTransporte.value = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Incluir Auxilio de Transporte")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { viewModel.calcularPrestaciones() }) {
                Text("Calcular")
            }
            Button(onClick = { viewModel.limpiarCampos() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Limpiar")
            }
        }

        result?.let {
            Spacer(modifier = Modifier.height(24.dp))
            Text("RESULTADOS", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()){
                Text("Prima: ${viewModel.formatCurrency(it.prima)}")
                Text("Cesantías: ${viewModel.formatCurrency(it.cesantias)}")
                Text("Intereses sobre Cesantías: ${viewModel.formatCurrency(it.interesesCesantias)}")
                Text("Vacaciones: ${viewModel.formatCurrency(it.vacaciones)}")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("TOTAL: ${viewModel.formatCurrency(it.total)}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
