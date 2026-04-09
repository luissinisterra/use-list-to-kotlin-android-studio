package com.example.usodelistas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.usodelistas.data.RepositorioProductos
import com.example.usodelistas.model.Producto
import com.example.usodelistas.model.TipoProducto
import com.example.usodelistas.ui.ImagenDesdeInternet
import com.example.usodelistas.ui.theme.UsoDeListasTheme

// Formulario: escribe en variables de estado (remember); al registrar, se guarda en RepositorioProductos.
@OptIn(ExperimentalMaterial3Api::class)
class RegistroProductoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UsoDeListasTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.registro_productos)) }
                        )
                    }
                ) { margen ->
                    FormularioRegistro(Modifier.padding(margen))
                }
            }
        }
    }
}

@Composable
private fun FormularioRegistro(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var precioTexto by remember { mutableStateOf("") }
    var tipoElegido by remember { mutableStateOf(TipoProducto.TECNOLOGIA) }
    var hayErrorNombre by remember { mutableStateOf(false) }
    var hayErrorPrecio by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                hayErrorNombre = false
            },
            label = { Text(stringResource(R.string.nombre_producto)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = hayErrorNombre,
            supportingText = {
                if (hayErrorNombre) Text(stringResource(R.string.error_nombre))
            }
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = precioTexto,
            onValueChange = {
                precioTexto = it
                hayErrorPrecio = false
            },
            label = { Text(stringResource(R.string.precio)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = hayErrorPrecio,
            supportingText = {
                if (hayErrorPrecio) Text(stringResource(R.string.error_precio))
            }
        )
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.tipo_producto), style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))

        // Un radio por cada tipo: al pulsar la fila o el círculo, cambiamos tipoElegido.
        for (tipo in TipoProducto.entries) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { tipoElegido = tipo }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = tipo == tipoElegido,
                    onClick = { tipoElegido = tipo }
                )
                Text(tipo.etiqueta, modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Vista previa: la URL depende del tipo (se descarga de internet).
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.imagen_del_tipo),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.height(8.dp))
        ImagenDesdeInternet(
            url = tipoElegido.urlImagen,
            contentDescription = tipoElegido.etiqueta,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                val precio = leerPrecio(precioTexto)
                val nombreOk = nombre.isNotBlank()
                val precioOk = precio != null && precio >= 0
                if (!nombreOk || !precioOk) {
                    hayErrorNombre = !nombreOk
                    hayErrorPrecio = !precioOk
                    return@Button
                }
                RepositorioProductos.agregar(
                    Producto(nombre = nombre.trim(), precio = precio, tipo = tipoElegido)
                )
                nombre = ""
                precioTexto = ""
                tipoElegido = TipoProducto.TECNOLOGIA
                hayErrorNombre = false
                hayErrorPrecio = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.registrar))
        }
    }
}

// Texto del campo → número, o null si no se puede leer.
private fun leerPrecio(texto: String): Double? =
    texto.replace(',', '.').toDoubleOrNull()
