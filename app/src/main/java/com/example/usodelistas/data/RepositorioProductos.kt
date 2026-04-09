package com.example.usodelistas.data

import com.example.usodelistas.model.Producto

// Lista compartida en memoria (se pierde al cerrar la app). Todas las actividades leen/escriben aquí.
object RepositorioProductos {
    private val lista = mutableListOf<Producto>()

    fun agregar(producto: Producto) {
        lista.add(producto)
    }

    fun obtenerTodos(): List<Producto> = lista.toList()
}
