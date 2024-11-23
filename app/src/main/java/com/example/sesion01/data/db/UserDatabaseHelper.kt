package com.example.sesion01.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "user_database.db"
        private const val DATABASE_VERSION = 13
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " +
                    "users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "phone INT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "id_pers INT NOT NULL," +
                    "FOREIGN KEY(id_pers) REFERENCES person(id_pers) ON DELETE CASCADE"+
                    ")")

        db.execSQL(
            "CREATE TABLE " +
                    "person (" +
                    "id_pers INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name_pers TEXT NOT NULL," +
                    "lastname_per TEXT NOT NULL," +
                    "address TEXT NOT NULL" +
                    ")")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS person")
        onCreate(db)

        /*Log.d("onUpgrade Version db oldVersion",oldVersion.toString())
        Log.d("onUpgrade Version db newVersion",newVersion.toString())

        if (oldVersion < newVersion ){
            Log.d("onUpgrade","Agregando la columna...")
            db.execSQL("ALTER TABLE users ADD COLUMN email TEXT DEFAULT 'sin email'")
            db.execSQL("ALTER TABLE users ADD COLUMN phone TEXT DEFAULT 'sin phone'")
        }*/

    }

}