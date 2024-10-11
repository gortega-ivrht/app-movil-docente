package com.example.sesion01.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.sesion01.data.model.User

class UserDao(context: Context){
    private val dbHelper= UserDatabaseHelper(context)

    fun insertUser(user: User) : Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name",user.name)
        }

        return db.insert("users",null,values).also {
            db.close()
        }
    }

    fun getAllUsers(): List<User>{
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query("users",null,null,null,null,null,null)
        val users = mutableListOf<User>()

        if (cursor.moveToFirst()){
            do {
                val  id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                users.add(User(id,name))
            } while (cursor.moveToNext())

        }
        cursor.close()
        db.close()

        return users
    }

    //sesion6 cursores
    fun getUsersFilter(nameFilter: String): List<User>{
        val db = dbHelper.readableDatabase
        val projection = arrayOf("id", "name")
        val selection = "name LIKE ?"
        val selectionArgs = arrayOf("$nameFilter%")
        val sortOrder = "id DESC"

        val users = mutableListOf<User>()

        val cursor: Cursor = db.query(
            "users",   // Tabla
            projection,  // Columnas a devolver
            selection,   // Filtro WHERE
            selectionArgs,  // Valores para el filtro
            null,   // Group By
            null,   // Having
            sortOrder   // Orden
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))

                // Procesar los datos obtenidos
                users.add(User(id,name))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return users
    }

    fun updateUser(id: Long, newName: String): Int {
        val db = dbHelper.readableDatabase
        val values = ContentValues().apply {
            put("name", newName)
        }
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())

        return db.update("users", values, selection, selectionArgs)
    }

    fun deleteUser(id: Long): Int {
        val db = dbHelper.readableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete("users", selection, selectionArgs)
    }


}