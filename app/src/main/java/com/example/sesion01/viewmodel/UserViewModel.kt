package com.example.sesion01.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sesion01.data.model.User
import com.example.sesion01.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository): ViewModel(){
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList.asStateFlow()

    fun insertUser(name:String){
        viewModelScope.launch (Dispatchers.IO){
            val newUser = User(name = name)
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

    fun updateUser(id: Long, name: String, email:String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(id, name, email)
            loadUsers() // Refrescar la lista después de la actualización
        }
    }

    fun deleteUser(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(id)
            loadUsers() // Refrescar la lista después de la eliminación
        }
    }

    fun getDatabaseVersion(context: Context): Int {
        return userRepository.getDatabaseVersion(context)
    }
}