package com.example.sesion01.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.sesion01.data.model.User

class UserDao(context: Context){
    private val dbHelper= UserDatabaseHelper(context)

    fun insertUser(user: User) : Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name",user.name)
            put("email",user.email)
            put("phone",user.phone)
            put("password",hashPassword(user.phone))
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
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))

                users.add(User(id,name,email,phone,password))
            } while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        Log.d("Seguimiento usuarios existentes", cursor.toString())
        return users
    }

    //sesion6 cursores
    fun getUsersFilter(nameFilter: String): List<User>{
        val db = dbHelper.readableDatabase
        val users = mutableListOf<User>()

        try {
            val projection = arrayOf("id", "name","email","phone","password")
            val selection = "name LIKE ?"
            val selectionArgs = arrayOf("$nameFilter%")
            val sortOrder = "id DESC"



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
                    val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                    val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))

                    // Procesar los datos obtenidos
                    users.add(User(id, name, email,phone,password))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()

            return users
        }catch (e: Exception){
            Log.d("SEGUIMIENTO", e.toString())
            return users
        }
    }

    fun updateUser(id: Long, newName: String, newEmail:String, newPhone:String, newPassword:String): Int {

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", newName)
            put("email",newEmail)
            put("phone",newPhone)
            put("password",hashPassword(newPassword))
        }
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())

        return try {
            db.update("users", values, selection, selectionArgs)
        } catch (e: Exception) {
            Log.d("DB_ERROR", "Error al actualizar el usuario: ${e.message}")
            -1 // Devolver -1 si hay un error
        } finally {
            db.close()  // Asegúrate de cerrar la base de datos
        }
    }

    fun deleteUser(id: Long): Int {
        val db = dbHelper.readableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete("users", selection, selectionArgs)
    }


    fun authenticateUser(username: String, password: String): Boolean {
        getAllUsers()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("id"),
            "email = ? AND password = ?",
            arrayOf(username, hashPassword(password)),
            null, null, null
        )

        val isAuthenticated = cursor.moveToFirst()
        cursor.close()
        return isAuthenticated
    }

    private fun hashPassword(password: String): String {
        // Aquí puedes usar una librería para encriptación; esta es una versión simple
        return password.hashCode().toString()  // Usa un método de hash seguro en una implementación real
    }

    fun getDatabaseVersion(context: Context): Int {
        val dbHelper = UserDatabaseHelper(context)
        val db = dbHelper.readableDatabase
        val version = db.version
        Log.d("DB_INFO", "Versión de la base de datos: $version")

        // Consultar la estructura de la tabla users
        val cursor = db.rawQuery("PRAGMA table_info(users)", null)

        if (cursor.moveToFirst()) {
            Log.d("DB_INFO", "Columnas de la tabla users:")
            do {
                val columnName = cursor.getString(1)  // Nombre de la columna en el índice 1
                Log.d("DB_INFO", "Columna: $columnName")
            } while (cursor.moveToNext())
        } else {
            Log.d("DB_INFO", "No se encontró la tabla users.")
        }

        cursor.close()
        db.close()
        return version
    }




}