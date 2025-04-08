@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyecto_dashboard.pages

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen() {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var phone by remember { mutableStateOf(TextFieldValue()) }
    var address by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var productDescription by remember { mutableStateOf(TextFieldValue()) }
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) } // to store the captured image URI

    var expanded by remember { mutableStateOf(false) }
    var selectedProducts by remember { mutableStateOf("") }
    val productOptions = listOf("Oleos", "Fotografías", "Marcos")
    val context = LocalContext.current

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Formulario de Pedido",
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del cliente") },
                    singleLine = true
                )

                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    singleLine = true
                )

                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección") },
                    singleLine = true
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    singleLine = true
                )
            }
        }

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedProducts,
                        onValueChange = {},
                        label = { Text("Seleccionar productos") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        productOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedProducts = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                TextField(
                    value = productDescription,
                    onValueChange = { productDescription = it },
                    label = { Text("Descripción del Producto") },
                    singleLine = false,
                    modifier = Modifier.heightIn(min = 56.dp, max = 120.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val orderDetails =
                                "Pedido:\nNombre: ${name.text}\nTeléfono: ${phone.text}\nDirección: ${address.text}\nCorreo: ${email.text}\nProducto: $selectedProducts\nDescripción: ${productDescription.text}"

                            val pdfFile = generatePdf(context, orderDetails)
                            if (pdfFile != null) {
                                showNotification(context, pdfFile)
                            }
                        }
                    ) {
                        Text("Generar Ticket")
                    }

                    Button(
                        onClick = {
                            val orderDetails =
                                "Pedido:\nNombre: ${name.text}\nTeléfono: ${phone.text}\nDirección: ${address.text}\nCorreo: ${email.text}\nProducto: $selectedProducts\nDescripción: ${productDescription.text}"

                            qrCodeBitmap = generateQrCode(orderDetails)
                        }
                    ) {
                        Text("Generar QR")
                    }
                  // If the imageUri is not null, display the captured image
                    imageUri?.let {
                        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(it))
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier.size(200.dp)
                        )
                    }

                }
            }
        }

        qrCodeBitmap?.let {
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Código QR del Pedido",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Código QR del Pedido",
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}



fun generateQrCode(data: String): Bitmap {
    val size = 512
    val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

    // Generar el código QR
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
        }
    }

    // Crear un "ticket" con bordes y algún estilo visual
    val ticketBitmap = Bitmap.createBitmap(size + 40, size + 60, Bitmap.Config.RGB_565)
    val canvas = Canvas(ticketBitmap)
    val paint = Paint()

    // Fondo blanco
    paint.color = Color.WHITE
    canvas.drawRect(0f, 0f, ticketBitmap.width.toFloat(), ticketBitmap.height.toFloat(), paint)

    // Agregar un borde (puedes personalizar el grosor y color del borde)
    paint.color = Color.BLACK
    paint.strokeWidth = 5f
    paint.style = Paint.Style.STROKE
    canvas.drawRect(10f, 10f, (ticketBitmap.width - 10).toFloat(), (ticketBitmap.height - 10).toFloat(), paint)

    // Dibujar el código QR en el "ticket"
    canvas.drawBitmap(bitmap, 20f, 20f, null)

    // Opcional: puedes agregar texto o detalles extra (como un logo o nombre del negocio)
    paint.style = Paint.Style.FILL
    paint.color = Color.BLACK
    paint.textSize = 40f
    canvas.drawText("Mi Ticket QR", 30f, (size + 50).toFloat(), paint)

    return ticketBitmap
}

fun generatePdf(context: Context, content: String): File? {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Configuración básica del diseño del ticket
    val paint = android.graphics.Paint()
    paint.textSize = 12f
    paint.color = android.graphics.Color.BLACK

    val headerPaint = android.graphics.Paint().apply {
        textSize = 16f
        color = android.graphics.Color.BLACK
        isFakeBoldText = true
    }

    val linePaint = android.graphics.Paint().apply {
        color = android.graphics.Color.LTGRAY
        strokeWidth = 2f
    }

    // Dibuja encabezado
    canvas.drawText("Ticket de Venta", 90f, 30f, headerPaint)
    canvas.drawLine(10f, 40f, 290f, 40f, linePaint)

    // Dibuja información principal
    var yPosition = 60f
    val lines = content.split("\n")
    lines.forEach { line ->
        canvas.drawText(line, 10f, yPosition, paint)
        yPosition += 20f
    }

    // Dibuja líneas horizontales para dividir secciones
    canvas.drawLine(10f, yPosition + 10f, 290f, yPosition + 10f, linePaint)
    yPosition += 30f

    // Dibuja agradecimiento al cliente
    canvas.drawText("¡Gracias por su compra!", 70f, yPosition, headerPaint)

    pdfDocument.finishPage(page)

    // Guardar el archivo
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, "ticket_venta.pdf")

    try {
        pdfDocument.writeTo(FileOutputStream(file))
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        pdfDocument.close()
    }

    return file
}

fun showNotification(context: Context, pdfFile: File) {
    val channelId = "ticket_channel"
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Ticket Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        pdfFile
    )

    val openIntent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    val openPendingIntent = PendingIntent.getActivity(
        context,
        0,
        openIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Ticket de venta generado")
        .setContentText("Toca para abrir o descargar el ticket.")
        .setSmallIcon(android.R.drawable.ic_menu_save)
        .setContentIntent(openPendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(1, notification)
}

