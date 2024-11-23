package com.example.sesion01.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sesion01.data.model.Person
import com.example.sesion01.data.model.User
import com.example.sesion01.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel(){
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    private val _loginUser = MutableStateFlow<Boolean>(false)
    val userList = _userList.asStateFlow()

    init {
        loadUsers()
    }

    fun insertUser(name:String,email:String,phone:Int,password:String,personName:String,personLastName:String,personAddress:String){

        viewModelScope.launch (Dispatchers.IO){
            val person = Person(0,personName,personLastName,personAddress)
            val newUser = User(name = name,email=email,phone=phone, password = password, person = person)
            userRepository.insertUser(newUser)

            _userList.value = userRepository.getAllUsers()
        }
    }

    fun loadUsers(){
        viewModelScope.launch (Dispatchers.IO) {
            _userList.value = userRepository.getAllUsers()
        }
    }

    // sesion6: Filtrar usuarios por nombre
    fun filterUsersByName(nameFilter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userList.value = userRepository.getUsersFilter(nameFilter)
        }
    }

    fun updateUser(id: Long,
                   name: String,
                   email:String,
                   phone:Int,
                   password:String,
                   personName: String,
                   personLastName: String,
                   personAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {

            userRepository.updateUser(id, name, email,phone,password,personName,personLastName,personAddress)
            loadUsers() // Refrescar la lista después de la actualización
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(id)
            loadUsers() // Refrescar la lista después de la eliminación
        }
    }

    fun loginUser(username: String, password: String):Boolean {
        var login = userRepository.loginUser(username, password)

        Log.d("seguimiento login",login.toString())
        return login
    }

    fun getDatabaseVersion(context: Context): Int {
        return userRepository.getDatabaseVersion(context)
    }
}