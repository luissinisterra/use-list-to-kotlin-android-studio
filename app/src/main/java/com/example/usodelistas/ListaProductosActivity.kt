package com.example.usodelistas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.usodelistas.data.RepositorioProductos
import com.example.usodelistas.model.Producto
import com.example.usodelistas.ui.ImagenDesdeInternet
import com.example.usodelistas.ui.theme.UsoDeListasTheme
import java.util.Locale

// LazyColumn = lista que solo dibuja lo visible. Al volver a esta pantalla (onResume), recargamos la lista.
@OptIn(ExperimentalMaterial3Api::class)
class ListaProductosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UsoDeListasTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.lista_productos)) }
                        )
                    }
                ) { margen ->
                    PantallaLista(Modifier.padding(margen))
                }
            }
        }
    }
}

@Composable
private fun PantallaLista(modifier: Modifier = Modifier) {
    val cicloDeVida = LocalLifecycleOwner.current.lifecycle
    var productos by remember { mutableStateOf(RepositorioProductos.obtenerTodos()) }

    // Cuando la Activity pasa a primer plano otra vez, pedimos otra vez la lista al repositorio.
    DisposableEffect(cicloDeVida) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                productos = RepositorioProductos.obtenerTodos()
            }
        }
        cicloDeVida.addObserver(observador)
        onDispose { cicloDeVida.removeObserver(observador) }
    }

    if (productos.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.lista_vacia),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(productos) { _, producto ->
                TarjetaProducto(producto)
            }
        }
    }
}

@Composable
private fun TarjetaProducto(producto: Producto) {
    val precioTexto = String.format(Locale.getDefault(), "%.2f", producto.precio)
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImagenDesdeInternet(
                url = producto.tipo.urlImagen,
                contentDescription = producto.tipo.etiqueta,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = producto.tipo.etiqueta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$precioTexto €",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
