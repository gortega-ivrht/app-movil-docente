package com.example.sesion01.pantallas
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sesion01.data.model.User
import com.example.sesion01.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCursores(viewModel: UserViewModel) {
    var nameFilter by remember { mutableStateOf("") }
    val users by viewModel.userList.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Campo de entrada para el filtro de nombre
        TextField(
            value = nameFilter,
            onValueChange = { nameFilter = it },
            label = { Text("Filtrar por nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        // Botón para aplicar el filtro
        Button (
            onClick = { viewModel.filterUsersByName(nameFilter) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabla para mostrar los usuarios filtrados
        LazyColumn (modifier = Modifier.fillMaxSize()) {
            items(users) { user ->
                //UserRow(user)
                UserRow(user,
                    onDelete =  { id ->viewModel.deleteUser(id)},  // Llama a la función de eliminar en el ViewModel
                    onUpdate = { id, name, email,phone,password, personName, personLastName, personAddress -> viewModel.updateUser(id, name, email,phone,password, personName, personLastName, personAddress ) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRow(user: User, onDelete: (Long) -> Unit, onUpdate: (Long, String, String, Int, String, String, String, String) -> Unit) {

    var isEditing by remember { mutableStateOf(false) }
    var updatedName by remember { mutableStateOf(user.name) }
    var updatedEmail by remember { mutableStateOf(user.email) }
    var updatedPhone by remember { mutableStateOf(user.phone) }
    var updatedPassword by remember { mutableStateOf(user.password) }
    var updatedPersonName by remember { mutableStateOf(user.person.name) }
    var updatedPersonLastName by remember { mutableStateOf(user.person.lastName) }
    var updatedPersonAddress by remember { mutableStateOf(user.person.address) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(1.dp, Color.Gray)
            .padding(4.dp)
    ) {
        Text(text = user.id.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 10.sp)

        if (isEditing) {
            Log.d("isEditing",updatedName)
            // Modo edición: permitir que el nombre sea editable
            TextField(
                value = updatedName,
                onValueChange = { updatedName = it },
                modifier = Modifier.weight(2f),
                textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
            )

            TextField(
                value = updatedEmail,
                onValueChange = { updatedEmail = it },
                modifier = Modifier.weight(2f),
                textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
            )

            TextField(
                value = updatedPhone.toString(),
                onValueChange = { updatedPhone = it.toInt() },
                modifier = Modifier.weight(2f),
                textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
            )

            /*TextField(
                value = updatedPhone.toString(), // Este será el valor del campo
                onValueChange = { newValue: String -> // Especificar el tipo explícitamente
                    updatedPhone = (newValue.filter { it.isDigit() }).toInt()
                },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )*/

            TextField(
                value = updatedPassword,
                onValueChange = { updatedPassword = it },
                modifier = Modifier.weight(2f),
                textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
            )

            TextField(
                value = updatedPersonName,
                onValueChange = { updatedPersonName = it },
                modifier = Modifier.weight(2f),
                textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
            )

            TextField(
                value = updatedPersonLastName,
                onValueChange = { updatedPersonLastName = it },
                modifier = Modifier.weight(2f),
                textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
            )

            TextField(
                value = updatedPersonAddress,
                onValueChange = { updatedPersonAddress = it },
                modifier = Modifier.weight(2f),
                textStyle = LocalTextStyle.current.copy(fontSize = 10.sp)
            )

            Button( onClick = {
                                onUpdate(user.id, updatedName,updatedEmail,updatedPhone,updatedPassword, updatedPersonName, updatedPersonLastName, updatedPersonAddress)
                                isEditing = false  // Salir del modo edición
                            },
                    modifier = Modifier.padding(end = 4.dp),
                    contentPadding = PaddingValues(1.dp)
            ) {
                Text("Guardar",fontSize = 10.sp)
            }

            // Botón para cancelar la edición
            Button(
                onClick = {
                    updatedName = user.name  // Revertir los cambios
                    updatedEmail = user.email
                    updatedPhone = user.phone
                    updatedPassword = user.password
                    updatedPersonName = user.person.name
                    updatedPersonLastName = user.person.lastName
                    updatedPersonAddress = user.person.address

                    isEditing = false  // Salir del modo edición sin guardar
                },
                modifier = Modifier.padding(start = 4.dp),
                contentPadding = PaddingValues(1.dp)
            ) {
                Text("Cancelar",fontSize = 10.sp)
            }

        }else {
            Text(text = user.name, modifier = Modifier.weight(2f))
            Text(text = user.email, modifier = Modifier.weight(2f))
            Text(text = user.phone.toString(), modifier = Modifier.weight(2f))
            Text(text = user.password, modifier = Modifier.weight(2f))
            Text(text = user.person.name, modifier = Modifier.weight(2f))
            Text(text = user.person.lastName, modifier = Modifier.weight(2f))
            Text(text = user.person.address, modifier = Modifier.weight(2f))

            Button( onClick = { isEditing = true },
                    modifier = Modifier.padding(start = 4.dp),
                    contentPadding = PaddingValues(1.dp)
            ) {
                Text("Editar",fontSize = 10.sp)
            }
        }
        //sesion7:  Botón para eliminar
        Button(
            onClick = { onDelete(user.id) },
            modifier = Modifier.padding(start = 4.dp),
            contentPadding = PaddingValues(1.dp)
        ) {
            Text("Eliminar",fontSize = 10.sp)
        }
    }
}
