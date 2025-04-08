package com.example.proyecto_dashboard.components

import com.example.proyecto_dashboard.R


//Se incializan las variables para el menu del drawer
sealed class MenuItem (
    val icon: Int,
    val title: String,
    val ruta: String
    ) {
    object Page01: MenuItem(R.drawable.ic_principal, "Home", "page01")
    object Main: MenuItem(R.drawable.ic_principal, "Home", "main")
    object Page02: MenuItem(R.drawable.pintura2 , "Oleos", "page02")
    object Page04: MenuItem(R.drawable.foto, "Fotografias", "page04")
    object Page05: MenuItem(R.drawable.frame, "Marcos", "page05")
    object Page03: MenuItem(R.drawable.ic_shopping_cart_24, "Pedidos", "page03")
    object Page06: MenuItem(R.drawable.nostros2, "Sobre nostros", "page06")
    object Ticket: MenuItem(R.drawable.ic_shopping_cart_24, "Ticket", "ticket")

}
