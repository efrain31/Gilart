package com.example.proyecto_dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyecto_dashboard.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
 * Composable que muestra el menú lateral (Drawer) con elementos de navegación.
 *
 * @param scope Alcance de la corrutina para cerrar el Drawer.
 * @param scaffoldState Estado de Scaffold para controlar el Drawer.
 * @param navController Controlador de navegación para manejar las transiciones entre pantallas.
 * @param menu_items Lista de elementos de navegación representados por la clase MenuItem.
 */
@Composable
fun DrawerMenu(
    /*
    scope Alcance de la corrutina para cerrar el Drawer.
    scaffoldState Estado de Scaffold para controlar el Drawer.
    navController Controlador de navegación para manejar las transiciones entre pantallas.
    menu_items Lista de elementos de navegación representados por la clase MenuItem.
 */
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    menu_items: List<MenuItem>
) {
    Column {
        // Encabezado del menú con una imagen de fondo
        Image(
            painterResource(id = R.drawable.portada5),
            contentDescription = "Menu de Opciones",
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        )
        // Obtener la ruta actual para marcar el elemento de navegación seleccionado
        val currentRoute = Current_Route(navController)
        menu_items.forEach { item ->
            DrawerItem(
                item = item,
                selected = currentRoute == item.ruta,
                onItemClick = {
                    // Navegar a la ruta seleccionada y cerrar el Drawer
                    navController.navigate(item.ruta) {
                        launchSingleTop = true
                    }
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    }
}

/*
 * Composable que representa un elemento del menú lateral (Drawer) con su icono y título.
 */
@Composable
fun DrawerItem(
    item: MenuItem,
    selected: Boolean,
    onItemClick: (MenuItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(16))
            .background(
                if (selected) MaterialTheme.colors.primaryVariant.copy(alpha = 0.25f)
                else Color.Transparent
            )
            .padding(8.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono del elemento del menú
        Image(
            painterResource(id = item.icon),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Título del elemento del menú
        Text(
            text = item.title,
            style = MaterialTheme.typography.body1,
            color = if (selected) MaterialTheme.colors.secondaryVariant
            else MaterialTheme.colors.onBackground
        )
    }
}
