package com.example.usodelistas.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

// Cada tipo tiene un nombre para mostrar y un icono (la "imagen" del producto en la lista).
enum class TipoProducto(val etiqueta: String, val icono: ImageVector) {
    TECNOLOGIA("Tecnología", Icons.Filled.PhoneAndroid),
    ROPA("Ropa", Icons.Filled.Checkroom),
    ALIMENTOS("Alimentos", Icons.Filled.Restaurant),
    HOGAR("Hogar", Icons.Filled.Home)
}

// Lo que guardamos por cada producto registrado.
data class Producto(
    val nombre: String,
    val precio: Double,
    val tipo: TipoProducto
)
