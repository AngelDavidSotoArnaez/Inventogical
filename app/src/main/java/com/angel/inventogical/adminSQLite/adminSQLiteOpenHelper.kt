package com.angel.inventogical.adminSQLite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.angel.inventogical.viewModel.Articulo
import com.angel.inventogical.viewModel.ListaArticulo
import com.angel.inventogical.viewModel.TasasLista
import com.angel.inventogical.viewModel.Tasas

class adminSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "Inventogical", null, 4) {
    override fun onCreate(db: SQLiteDatabase) {
        //Creacion de las tablas
        //Tabla de validaion de la empresa
        db.execSQL("CREATE TABLE articulos(codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "unidad TEXT, " +
                "existencia REAL, " +
                "precioS REAL, " +
                "precioBs REAL, " +
                "fechamodifi NUMERIC," +
                "precioPorTasa INTEGER )")

        db.execSQL("CREATE TABLE tasa( codigo INTEGER PRIMARY KEY AUTOINCREMENT, tasa REAL, fechamodifi NUMERIC )")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS articulos")
        db.execSQL("DROP TABLE IF EXISTS tasa")
        onCreate(db)
    }

    private fun cerrandoBD(db: SQLiteDatabase) {
        //db.close()
    }

    fun articulosAdd(articulo: Articulo) {

        val db = this.writableDatabase
        try {
            db.beginTransaction()

            val datos = ContentValues()
            datos.put("nombre", articulo.nombre)
            datos.put("unidad", articulo.unidad)
            datos.put("existencia", articulo.existencia)
            datos.put("precioS", articulo.precioS)
            datos.put("precioBs", articulo.precioBs)
            datos.put("fechamodifi", articulo.fechamodifi)
            datos.put("precioPorTasa", articulo.precioPorTasa)

            db.insert("articulos", null, datos)

            db.setTransactionSuccessful()
        } catch (e: java.lang.Exception) {
            println("Error -> $e")
        } finally {
            db.endTransaction()
            cerrandoBD(db)
        }
    }

    fun articulosUp(articulo: Articulo) {

        val db = this.writableDatabase
        try {
            db.beginTransaction()

            val datos = ContentValues()
            datos.put("nombre", articulo.nombre)
            datos.put("unidad", articulo.unidad)
            datos.put("existencia", articulo.existencia)
            datos.put("precioS", articulo.precioS)
            datos.put("precioBs", articulo.precioBs)
            datos.put("fechamodifi", articulo.fechamodifi)
            datos.put("precioPorTasa", articulo.precioPorTasa)

            val codigo = articulo.codigo.toString()

            println("EL CODIGO -> $articulo")

            db.update("articulos", datos, "codigo = ?", arrayOf(codigo))

            db.setTransactionSuccessful()
        } catch (e: java.lang.Exception) {
            println("Error -> $e")
        } finally {
            db.endTransaction()
            cerrandoBD(db)
        }
    }

    //Funcion que verifica la existenia de vendedores o no
    fun selArtiulosNum(): Boolean {
        val db = this.writableDatabase
        var flag = false
        var cursor: Cursor = db.rawQuery("", null)
        try {

            cursor = db.rawQuery("SELECT COUNT(codigo) FROM articulos", null)
            cursor.moveToFirst()
            val conteo = cursor.getInt(0)

            flag = conteo > 0

        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            cursor.close()
            cerrandoBD(db)
        }

        return flag
    }

    fun selTasasNum(): Boolean {
        val db = this.writableDatabase
        var flag = false
        var cursor: Cursor = db.rawQuery("", null)
        try {

            cursor = db.rawQuery("SELECT COUNT(*) FROM tasa", null)
            cursor.moveToFirst()
            val conteo = cursor.getInt(0)

            flag = conteo > 0

        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            cursor.close()
            cerrandoBD(db)
        }

        return flag
    }

    //Seleccion de todos los vendedores de la tabla Vendedores
    fun selAllAriculo(): ListaArticulo {
        val db = this.writableDatabase
        val listaArticulo = ListaArticulo()
        var cursor: Cursor = db.rawQuery("", null)
        try {

            cursor = db.rawQuery("SELECT * FROM articulos ORDER BY nombre ASC", null)
            while (cursor.moveToNext()) {

                val codigo = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val unidad = cursor.getString(2)
                val existencia = cursor.getDouble(3)
                val precioS = cursor.getDouble(4)
                val precioBs = cursor.getDouble(5)
                val fechamodifi = cursor.getString(6)
                val precioPorTasa = cursor.getInt(7)

                val vendedoresItem = Articulo(
                    codigo,
                    nombre,
                    unidad,
                    existencia,
                    precioS,
                    precioBs,
                    fechamodifi,
                    precioPorTasa
                )

                listaArticulo.add(vendedoresItem)
            }

        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            cursor.close()
            cerrandoBD(db)
        }

        //Envio de todos los vendedores encontrados
        return listaArticulo
    }

    fun selAllTasas(): TasasLista {
        val db = this.writableDatabase
        val tasasLista = TasasLista()
        var cursor: Cursor = db.rawQuery("", null)
        try {

            cursor = db.rawQuery("SELECT * FROM tasa ORDER BY fechamodifi ASC", null)
            while (cursor.moveToNext()) {

                val codigo = cursor.getInt(0)
                val tasa = cursor.getDouble(1)
                val fechatasa = cursor.getString(2)

                val tasaItem = Tasas(
                    codigo,
                    tasa,
                    fechatasa
                )

                tasasLista.add(tasaItem)
            }

        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            cursor.close()
            cerrandoBD(db)
        }

        //Envio de todos los vendedores encontrados
        return tasasLista
    }

    fun delArticulo(codigo: Int) {
        val db = this.writableDatabase
        try {
            db.beginTransaction()

            db.execSQL("DELETE FROM articulos WHERE codigo = $codigo")

            db.setTransactionSuccessful()
        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            db.endTransaction()
            cerrandoBD(db)
        }
    }

    fun delTasa(codigo: Int) {
        val db = this.writableDatabase
        try {
            db.beginTransaction()

            db.execSQL("DELETE FROM tasa WHERE codigo = $codigo")

            db.setTransactionSuccessful()
        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            db.endTransaction()
            cerrandoBD(db)
        }
    }

    fun selArticuloCodigo(codigoE: Int): Articulo {

        val db = this.writableDatabase
        var listaArticulo = Articulo(0, "", "", 0.0, 0.0, 0.0, "", 0)
        var cursor: Cursor = db.rawQuery("", null)
        try {

            cursor = db.rawQuery("SELECT * FROM articulos WHERE codigo = $codigoE", null)
            while (cursor.moveToNext()) {

                val codigo = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val unidad = cursor.getString(2)
                val existencia = cursor.getDouble(3)
                val precioS = cursor.getDouble(4)
                val precioBs = cursor.getDouble(5)
                val fechamodifi = cursor.getString(6)
                val precioPorTasa = cursor.getInt(7)

                listaArticulo = Articulo(
                    codigo,
                    nombre,
                    unidad,
                    existencia,
                    precioS,
                    precioBs,
                    fechamodifi,
                    precioPorTasa
                )

            }

        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            cursor.close()
            cerrandoBD(db)
        }

        //Envio de todos los vendedores encontrados
        return listaArticulo

    }

    fun addTasa(tasa:Double, fechaTasa:String) {

        val db = this.writableDatabase
        try {
            db.beginTransaction()

            val datos = ContentValues()
            datos.put("tasa", tasa)
            datos.put("fechamodifi", fechaTasa)

            db.insert("tasa", null, datos)

            db.setTransactionSuccessful()
        } catch (e: java.lang.Exception) {
            println("Error -> $e")
        } finally {
            db.endTransaction()
            cerrandoBD(db)
        }

    }

    fun selTasa(): Tasas {
        val db = this.writableDatabase
        var tasasLista = Tasas(0, 0.0, "")
        var cursor: Cursor = db.rawQuery("", null)
        try {

            cursor = db.rawQuery("SELECT * FROM tasa ORDER BY fechamodifi DESC", null)
            cursor.moveToFirst()
            val codigo = cursor.getInt(0)
            val tasa = cursor.getDouble(1)
            val fecha = cursor.getString(2)

            tasasLista = Tasas(
                codigo,
                tasa,
                fecha
            )


        } catch (e: java.lang.Exception) {
            println(e)
        } finally {
            cursor.close()
            cerrandoBD(db)
        }

        return tasasLista
    }

}