package com.example.sesion01.repository

import android.content.ContentValues
import android.content.Context
import com.example.sesion01.data.db.UserDao
import com.example.sesion01.data.model.User

class UserRepository(private val userDao: UserDao){

    fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    fun getAllUsers():List<User>{
        return userDao.getAllUsers()
    }

    fun getUsersFilter(nameFilter: String):List<User>{
        return userDao.getUsersFilter(nameFilter)
    }

    fun updateUser(id: Long, newName: String, newEmail:String): Int {
        return userDao.updateUser(id,newName, newEmail)
    }

    fun deleteUser(id: Long): Int {
        return userDao.deleteUser(id)
    }

    fun getDatabaseVersion(context: Context): Int {
        return userDao.getDatabaseVersion(context)
    }
}