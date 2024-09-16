package com.example.sesion01

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sesion01.pantallas.PantallaPrincipal
import com.example.sesion01.ui.theme.Sesion01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sesion01Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
/* Greeting("Android")*/
                    PantallaPrincipal()
                }
            }
        }

        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show()
        Log.d("MainActivity","onCreate")
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this,"onStart-test",Toast.LENGTH_SHORT).show()
        Log.d("MainActivity","onStart")
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show()
        Log.d("MainActivity","onResume")
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show()
        Log.d("MainActivity","onPause")
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this,"onStop",Toast.LENGTH_SHORT).show()
        Log.d("MainActivity","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"onDestroy",Toast.LENGTH_SHORT).show()
        Log.d("MainActivity","onDestroy")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Sesion01Theme {
        PantallaPrincipal()
    }
}