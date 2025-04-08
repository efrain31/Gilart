package com.example.proyecto_dashboard.pages

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.proyecto_dashboard.R
import com.example.proyecto_dashboard.R.string.*
import com.example.proyecto_dashboard.components.CreateChannelNotification
import com.example.proyecto_dashboard.components.notificacionImagen


var contadorState by mutableStateOf(0)



/*
 * Composable que representa una página de la aplicación TiendaApp con una lista de elementos de flores.
 * Permite ver los detalles de un producto seleccionado y mostrar una notificación al hacer clic en un botón.
 */
@Composable
fun Page_Flores(modifier: Modifier = Modifier) {
    // Estado para mantener el producto seleccionado y mostrar su detalle en un diálogo.
    var selectedProduct by remember { mutableStateOf<DrawableStringPairFlores?>(null) }

    // Código para la creación del canal de notificación al inicio del composable.
    // Este canal se crea una sola vez al inicio del composable.
    val idNotification: Int = 0
    val context: Context = LocalContext.current
    val idChannel: String = stringResource(R.string.canal_tienda)
    val name = stringResource(R.string.canal_tienda)
    val descriptionText = stringResource(R.string.canal_notificaciones)

    val textLong: String = "Bienvenido a catalogo " +
            "Oleos"



    //Función de creación propia como corrutina para crear el canal de notificación.
    LaunchedEffect(Unit) {
        CreateChannelNotification(
            idChannel,
            context,
            name,
            descriptionText
        )
    }
    val imagenBig = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.escuela
    )

    //Funcion de creacion propia como corrutina
    LaunchedEffect(Unit) {
        CreateChannelNotification(
            idChannel,
            context,
            name,
            descriptionText
        )
    }

    LazyColumn(modifier = modifier){
        items(favoriteCollectionsData.chunked(2)){
                row->
            Row{

                row.forEach{ card->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        val isFavorite = remember { mutableStateOf(false) }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Image(
                                painter = painterResource(card.drawable),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(200.dp)
                            )
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    stringResource(card.titulo),
                                    fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(stringResource(card.descripcion))
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(modifier = Modifier) {
                                IconButton(
                                    onClick = { isFavorite.value = !isFavorite.value },
                                    modifier = Modifier
                                        .clip(shape = CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Corazón",
                                        tint = if (isFavorite.value) Color.Red else Color.Unspecified
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        notificacionImagen(
                                            context,
                                            idChannel,
                                            idNotification + 2,
                                            "Creart te da la bienvenida",
                                            textLong,
                                            imagenBig
                                        )
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Oleo"
                                    )
                                }
                            }
                            Row(modifier = Modifier) {
                                TextButton(
                                    onClick = {
                                        selectedProduct = card
                                    },
                                ) {
                                    Text(text = "Detalles")
                                }
                                ComprarButton()
                            }

                        }

                    }
                }
            }
        }
    }

    selectedProduct?.let { product ->
        Dialog(
            onDismissRequest = { selectedProduct = null },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
            content = {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(product.drawable),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(product.titulo),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(product.descripcion),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row() {
                            TextButton(
                                onClick = { selectedProduct = null },
                            ) {
                                Text(text = "Cerrar")
                            }
                            TextButton(
                                onClick = {
                                    contadorState++
                                    selectedProduct = null
                                }
                            ) {
                                Text(text = "Disponible")
                            }
                        }
                    }
                }
            }
        )
    }

}

/*
 * Clase de datos que representa una lista de productos de flores.
 */
private data class DrawableStringPairFlores(
    @DrawableRes val drawable: Int,
    @StringRes val titulo: Int,
    @StringRes val descripcion: Int
)

/*
 * Listado de productos de flores junto con sus detalles (título y descripción).
 * Los productos se muestran en la lista de elementos de la página Page_Flores.
 */
private val favoriteCollectionsData = listOf(
    Triple(R.drawable.oleo11, Oleo1, detalles1),
    Triple(R.drawable.oleo12, Oleo1, detalles2),
    Triple(R.drawable.oleo13, Oleo1, detalles3),
    Triple(R.drawable.oleo14, Oleo1, detalles4),
    Triple(R.drawable.oleo7, Oleo1, detalles5),
    Triple(R.drawable.oleo43, Oleo1, detalles6),
    Triple(R.drawable.oleo48, Oleo1, detalles7),
    Triple(R.drawable.oleo222, Oleo1, detalles8),
    Triple(R.drawable.oleo64, Oleo1, detalles9),
    Triple(R.drawable.oleo49, Oleo1, detalles10),
    Triple(R.drawable.oleo50, Oleo1, detalles11),
    Triple(R.drawable.oleo52, Oleo1, detalles12),
    Triple(R.drawable.oleo51, Oleo1, detalles13),
    Triple(R.drawable.oleo67, Oleo1, detalles14),
    Triple(R.drawable.bya1, Oleo1, detalles15),



    ).map { DrawableStringPairFlores(it.first, it.second, it.third) }

/*
 * Composable que muestra un botón para comprar productos. Incrementa el contador de compra al hacer clic.
 */
@Composable
fun ComprarButton() {

}

/*
 * Composable que muestra un ícono de carrito de compras y un contador de la cantidad de productos comprados.
 */
@Composable
fun CarritoButton() {
    Box(modifier = Modifier.padding(top = 8.dp)) {
        IconButton(onClick = { /* TODO: Acción al hacer clic */ }) {

        }

        if (contadorState > 0) {
            // Mostrar el contador solo si el valor de contadorState es mayor que 0.
            Text(
                text = contadorState.toString(),
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .padding(2.dp)
                    .align(Alignment.TopEnd),
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}
