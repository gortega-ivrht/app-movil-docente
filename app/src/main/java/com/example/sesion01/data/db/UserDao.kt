package com.example.sesion01.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.sesion01.data.model.Person
import com.example.sesion01.data.model.User

class UserDao(context: Context){
    private val dbHelper= UserDatabaseHelper(context)

    fun insertUser(user: User): Long {
        val db = dbHelper.writableDatabase
        var userId: Long = -1

        try {
            db.beginTransaction() // Inicia una transacción para garantizar consistencia

            // Inserta en la tabla `person` primero
            val personValues = ContentValues().apply {
                put("name_pers", user.person.name)
                put("lastname_per", user.person.lastName)
                put("address", user.person.address)
            }
            val personId = db.insert("person", null, personValues)

            if (personId == -1L) {
                throw Exception("Error al insertar en la tabla `person`")
            }

            // Inserta en la tabla `users` con el `id_pers` generado
            val userValues = ContentValues().apply {
                put("name", user.name)
                put("email", user.email)
                put("phone", user.phone)
                put("password", hashPassword(user.password))
                put("id_pers", personId)
            }
            userId = db.insert("users", null, userValues)

            if (userId == -1L) {
                throw Exception("Error al insertar en la tabla `users`")
            }

            db.setTransactionSuccessful() // Marca la transacción como exitosa
        } catch (e: Exception) {
            Log.d("DB_ERROR", "Error al insertar usuario y persona: ${e.message}")
        } finally {
            db.endTransaction() // Finaliza la transacción
            db.close() // Asegúrate de cerrar la base de datos
        }

        return userId // Devuelve el `id` del usuario insertado o -1 si falló
    }

    // con con relación person
    fun getAllUsers(): List<User> {
        val db = dbHelper.readableDatabase
        val query = """
        SELECT 
            users.id AS user_id, 
            users.name AS user_name, 
            users.email AS user_email, 
            users.phone AS user_phone, 
            users.password AS user_password, 
            person.id_pers AS person_id, 
            person.name_pers AS person_name, 
            person.lastname_per AS person_lastname, 
            person.address AS person_address
        FROM 
            users
        INNER JOIN 
            person 
        ON 
            users.id_pers = person.id_pers
        """

        val cursor: Cursor = db.rawQuery(query, null)
        val users = mutableListOf<User>()

        if (cursor.moveToFirst()) {
            do {
                val userId = cursor.getLong(cursor.getColumnIndexOrThrow("user_id"))
                val userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"))
                val userEmail = cursor.getString(cursor.getColumnIndexOrThrow("user_email"))
                val userPhone = cursor.getInt(cursor.getColumnIndexOrThrow("user_phone"))
                val userPassword = cursor.getString(cursor.getColumnIndexOrThrow("user_password"))

                val personId = cursor.getLong(cursor.getColumnIndexOrThrow("person_id"))
                val personName = cursor.getString(cursor.getColumnIndexOrThrow("person_name"))
                val personLastName = cursor.getString(cursor.getColumnIndexOrThrow("person_lastname"))
                val personAddress = cursor.getString(cursor.getColumnIndexOrThrow("person_address"))

                // Crea el objeto `Person` con los datos correspondientes
                val personObject = Person(
                    id = personId,
                    name = personName,
                    lastName = personLastName,
                    address = personAddress
                )

                // Crea el objeto `User` que incluye el objeto `Person`
                users.add(
                    User(
                        id = userId,
                        name = userName,
                        email = userEmail,
                        phone = userPhone,
                        password = userPassword,
                        person = personObject
                    )
                )

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }

    //sesion6 cursores
    fun getUsersFilter(nameFilter: String): List<User> {
        val db = dbHelper.readableDatabase
        val users = mutableListOf<User>()

        try {
            // Consulta con JOIN para filtrar por el nombre y obtener datos de la relación con `person`
            val query = """
            SELECT 
                users.id AS user_id, 
                users.name AS user_name, 
                users.email AS user_email, 
                users.phone AS user_phone, 
                users.password AS user_password, 
                person.id_pers AS person_id, 
                person.name_pers AS person_name, 
                person.lastname_per AS person_lastname, 
                person.address AS person_address
            FROM 
                users
            INNER JOIN 
                person 
            ON 
                users.id_pers = person.id_pers
            WHERE 
                users.name LIKE ?
            ORDER BY 
                users.id DESC
        """

            // Ejecutar la consulta con el filtro
            val cursor: Cursor = db.rawQuery(query, arrayOf("$nameFilter%"))

            if (cursor.moveToFirst()) {
                do {
                    val userId = cursor.getLong(cursor.getColumnIndexOrThrow("user_id"))
                    val userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"))
                    val userEmail = cursor.getString(cursor.getColumnIndexOrThrow("user_email"))
                    val userPhone = cursor.getInt(cursor.getColumnIndexOrThrow("user_phone"))
                    val userPassword = cursor.getString(cursor.getColumnIndexOrThrow("user_password"))

                    val personId = cursor.getLong(cursor.getColumnIndexOrThrow("person_id"))
                    val personName = cursor.getString(cursor.getColumnIndexOrThrow("person_name"))
                    val personLastName = cursor.getString(cursor.getColumnIndexOrThrow("person_lastname"))
                    val personAddress = cursor.getString(cursor.getColumnIndexOrThrow("person_address"))

                    // Crear el objeto `Person`
                    val personObject = Person(
                        id = personId,
                        name = personName,
                        lastName = personLastName,
                        address = personAddress
                    )

                    // Crear el objeto `User` con el objeto `Person`
                    users.add(
                        User(
                            id = userId,
                            name = userName,
                            email = userEmail,
                            phone = userPhone,
                            password = userPassword,
                            person = personObject
                        )
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return users
        } catch (e: Exception) {
            Log.d("SEGUIMIENTO", e.toString())
            return users
        }
    }

    fun updateUser(
        userId: Long,
        newName: String,
        newEmail: String,
        newPhone: Int,
        newPassword: String,
        newPersonName: String,
        newPersonLastName: String,
        newPersonAddress: String
    ): Int {
        val db = dbHelper.writableDatabase
        var rowsUpdated = 0

        try {
            db.beginTransaction() // Inicia una transacción para garantizar la consistencia

            // Actualizar datos en la tabla `users`
            val userValues = ContentValues().apply {
                put("name", newName)
                put("email", newEmail)
                put("phone", newPhone)
                put("password", hashPassword(newPassword))
            }
            val userSelection = "id = ?"
            val userSelectionArgs = arrayOf(userId.toString())

            rowsUpdated += db.update("users", userValues, userSelection, userSelectionArgs)

            // Obtener `id_pers` relacionado con el usuario
            val cursor = db.query(
                "users", arrayOf("id_pers"), userSelection, userSelectionArgs,
                null, null, null
            )
            var personId: Long? = null
            if (cursor.moveToFirst()) {
                personId = cursor.getLong(cursor.getColumnIndexOrThrow("id_pers"))
            }
            cursor.close()

            // Actualizar datos en la tabla `person` si se encontró el `id_pers`
            if (personId != null) {
                val personValues = ContentValues().apply {
                    put("name_pers", newName)
                    put("lastname_per", newPersonLastName)
                    put("address", newPersonAddress)
                }
                val personSelection = "id_pers = ?"
                val personSelectionArgs = arrayOf(personId.toString())

                rowsUpdated += db.update("person", personValues, personSelection, personSelectionArgs)
            }

            db.setTransactionSuccessful() // Marca la transacción como exitosa
        } catch (e: Exception) {
            Log.d("DB_ERROR", "Error al actualizar el usuario y persona: ${e.message}")
            return -1 // Devolver -1 si hay un error
        } finally {
            db.endTransaction() // Finaliza la transacción
            db.close() // Asegúrate de cerrar la base de datos
        }

        return rowsUpdated // Devuelve el número total de filas actualizadas
    }

    fun deleteUser(userId: Long): Int {
        val db = dbHelper.writableDatabase
        var rowsDeleted = 0

        try {
            db.beginTransaction() // Inicia una transacción para garantizar consistencia

            // Obtener el `id_pers` relacionado con el usuario
            val cursor = db.query(
                "users", arrayOf("id_pers"), "id = ?", arrayOf(userId.toString()),
                null, null, null
            )
            var personId: Long? = null
            if (cursor.moveToFirst()) {
                personId = cursor.getLong(cursor.getColumnIndexOrThrow("id_pers"))
            }
            cursor.close()

            // Eliminar el usuario de la tabla `users`
            val userSelection = "id = ?"
            val userSelectionArgs = arrayOf(userId.toString())
            rowsDeleted += db.delete("users", userSelection, userSelectionArgs)

            // Eliminar la persona relacionada si se encontró el `id_pers`
            if (personId != null) {
                val personSelection = "id_pers = ?"
                val personSelectionArgs = arrayOf(personId.toString())
                rowsDeleted += db.delete("person", personSelection, personSelectionArgs)
            }

            db.setTransactionSuccessful() // Marca la transacción como exitosa
        } catch (e: Exception) {
            Log.d("DB_ERROR", "Error al eliminar usuario y persona: ${e.message}")
            return -1 // Devolver -1 si hay un error
        } finally {
            db.endTransaction() // Finaliza la transacción
            db.close() // Asegúrate de cerrar la base de datos
        }

        return rowsDeleted // Devuelve el número total de filas eliminadas
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