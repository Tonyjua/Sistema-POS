package com.example.proyectoappmovil.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.proyectoappmovil.util.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_CIUDAD);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_COMUNA);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_CATEGORIA);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_SUBCATEGORIA);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_ARTICULO);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_EMPRESA);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_USUARIO);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_VENTAS);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_FOLIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int versionAntigua, int versionNueva) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_CIUDAD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_COMUNA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_CATEGORIA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_SUBCATEGORIA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_ARTICULO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_EMPRESA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_VENTAS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.CREAR_TABLA_FOLIO);
        onCreate(sqLiteDatabase);
    }
}
