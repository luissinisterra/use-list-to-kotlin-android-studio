package com.example.usodelistas.model

// Imágenes por categoría (URLs HTTPS). Coil las descarga y las muestra en pantalla.
enum class TipoProducto(val etiqueta: String, val urlImagen: String) {
    TECNOLOGIA(
        "Tecnología",
        "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&auto=format&fit=crop&q=80"
    ),
    ROPA(
        "Ropa",
        "https://images.unsplash.com/photo-1523381210438-271e8be1f52b?w=400&auto=format&fit=crop&q=80"
    ),
    ALIMENTOS(
        "Alimentos",
        "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&auto=format&fit=crop&q=80"
    ),
    HOGAR(
        "Hogar",
        "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400&auto=format&fit=crop&q=80"
    )
}

data class Producto(
    val nombre: String,
    val precio: Double,
    val tipo: TipoProducto
)
