package com.example.sesion01.pantallas
import android.app.AlertDialog
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ModalDialogoAlerta(
    showDialog: Boolean,
    onDismiss:() -> Unit
){
    if (showDialog){
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                    Text(text = "Titulo del Modal")
            },
            text = {
                   Text(text = "Este es un ejemplo de modal")
            },
            confirmButton = { 
                TextButton(onClick = onDismiss) {
                    Text(text = "Cerrar")
                }
            })
    }
}