package com.example.proyecto_dashboard.components

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_dashboard.ui.theme.Proyecto_DashboardTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainPage() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current // Obtener el contexto para usar en el botón flotante

    // Opciones de navegación del drawer
    val navigationItems = listOf(
        MenuItem.Page01,
        MenuItem.Page02,
        MenuItem.Page04,
        MenuItem.Page05,
        MenuItem.Page03,
        MenuItem.Page06,

    )
    // Opciones de navegación del BottomBar
    val navigationItemsBottomBar = listOf(
        Items_Bar.Boton2,
        Items_Bar.Boton4,
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                scope,
                scaffoldState,
                navController,
                navigationItems
            )
        },
        drawerContent = {
            DrawerMenu(
                scope,
                scaffoldState,
                navController,
                menu_items = navigationItems
            )
        },
        bottomBar = {
            BottomMenu(
                navController,
                menu_items_bar = navigationItemsBottomBar
            )
        },
        floatingActionButton = {
            // FAB para abrir WhatsApp
            FloatingActionButton(
                onClick = {
                    // Intent para abrir WhatsApp
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://wa.me/3327409328?text=¡Hola!%20Quiero%20información%20sobre%20tu%20producto.")
                    }
                    context.startActivity(intent) // Inicia la actividad con el intent
                }
            ) {
                Icon(Icons.Default.Forum, contentDescription = "Abrir WhatsApp")
            }
        }
    ) { padding ->
        ContentScaffold(padding)
        Navigation_Host(navController)
    }
}

@Composable
fun ContentScaffold(padding: PaddingValues) {
}

@Composable
fun CarritoSimulado() {
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun MainPagePreview() {
    Proyecto_DashboardTheme {
        MainPage()
    }
}













@Composable
fun ProductModal(product: Int, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            androidx.compose.material3.Text(
                text = "Detalles del Producto",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally, //  Centra los elementos horizontalmente
                verticalArrangement = Arrangement.Center //  Centra el contenido verticalmente
            ) {
                Image(
                    painter = painterResource(id = product),
                    contentDescription = "Producto",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material3.Text(
                    text = "Descripción del producto aquí.",
                    color = Color.Black,
                    textAlign = TextAlign.Center, //  Centra el texto de descripción
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Box( // ✅ Usa un Box para centrar el botón "Cerrar"
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material.Button(onClick = { onDismiss() }) {
                    androidx.compose.material.Text(text = "Cerrar")
                }
            }
        }
    )
}
