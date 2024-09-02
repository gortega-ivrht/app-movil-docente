package com.example.sesion01.pantallas

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.sesion01.MainActivity
import com.example.sesion01.PresentarActivity

@Composable
fun Presentar(){
    val contex = LocalContext.current
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "")
        Text(text = "")
        Text(text = "Hola desde presentar")
        OutlinedButton(onClick = {
            Log.d("PantallaPresentar","Click en el botón regresar")
            val intent = Intent(
                contex,
                MainActivity::class.java
            )
            contex.startActivity(intent)
        }) {
            Text(text = "Volver", style = MaterialTheme.typography.bodyLarge)
        }
    }

}