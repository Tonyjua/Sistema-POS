package com.example.proyectoappmovil.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Utilidades {
    public static final String TABLA_CIUDAD = "ciudad";
    public static final String TABLA_COMUNA = "comuna";
    public static final String TABLA_CATEGORIA = "categoria";
    public static final String TABLA_SUBCATEGORIA = "subcategoria";
    public static final String TABLA_ARTICULO = "articulo";
    public static final String TABLA_EMPRESA = "empresa";
    public static final String TABLA_USUARIO="usuario";
    public static final String TABLA_VENTAS = "ventas";
    public static final String TABLA_FOLIO = "folio";




    public static final String CAMPO_ID_CIUDAD="id_ciudad";
    public static final String CAMPO_NOMBRE_CIUDAD="nombre_ciudad";

    public static final String CREAR_TABLA_CIUDAD="CREATE TABLE " +
            ""+TABLA_CIUDAD+" ("+CAMPO_ID_CIUDAD+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_CIUDAD+" TEXT NOT NULL)";

    public static final String CAMPO_ID_COMUNA="id_comuna";
    public static final String CAMPO_NOMBRE_COMUNA="nombre_comuna";

    public static final String CREAR_TABLA_COMUNA="CREATE TABLE " +
            ""+TABLA_COMUNA+" ("+CAMPO_ID_COMUNA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_CIUDAD+" INTEGER NOT NULL, "+CAMPO_NOMBRE_COMUNA+" TEXT NOT NULL, FOREIGN KEY ("+CAMPO_ID_CIUDAD+") REFERENCES "+TABLA_CIUDAD+" ("+CAMPO_ID_CIUDAD+")ON DELETE CASCADE ON UPDATE NO ACTION)";

    public static final String CAMPO_ID_CATEGORIA="id_categoria";
    public static final String CAMPO_NOMBRE_CATEGORIA="nombre_categoria";

    public static final String CREAR_TABLA_CATEGORIA="CREATE TABLE " +
            ""+TABLA_CATEGORIA+" ("+CAMPO_ID_CATEGORIA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_CATEGORIA+" TEXT NOT NULL)";

    public static final String CAMPO_ID_SUBCATEGORIA ="id_subcategoria";
    public static final String CAMPO_NOMBRE_SUBCATEGORIA="nombre_subcategoria";

    public static final String CREAR_TABLA_SUBCATEGORIA="CREATE TABLE " +
            ""+TABLA_SUBCATEGORIA+" ("+CAMPO_ID_SUBCATEGORIA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_CATEGORIA+" INTEGER NOT NULL, "+CAMPO_NOMBRE_SUBCATEGORIA+" TEXT NOT NULL, FOREIGN KEY ("+CAMPO_ID_CATEGORIA+") REFERENCES "+TABLA_CATEGORIA+" ("+CAMPO_ID_CATEGORIA+")ON DELETE CASCADE ON UPDATE NO ACTION)";

    public static final String CAMPO_ID_ARTICULO="id_articulo";
    public static final String CAMPO_NOMBRE_ARTICULO="nombre_articulo";
    public static final String CAMPO_DETALLE_ARTICULO="detalle_articulo";
    public static final String CAMPO_PRECIO="precio";
    public static final String CAMPO_STOCK="stock";
    public static final String CAMPO_IMAGEN="imagen_articulo";
    public static final String CREAR_TABLA_ARTICULO="CREATE TABLE " +
            ""+TABLA_ARTICULO+" ("+CAMPO_ID_ARTICULO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_SUBCATEGORIA+" INTEGER NOT NULL, "+CAMPO_NOMBRE_ARTICULO+" TEXT NOT NULL, "+CAMPO_DETALLE_ARTICULO+" TEXT NOT NULL, "+CAMPO_IMAGEN+" BLOB, "+CAMPO_PRECIO+" INTEGER NOT NULL, "+CAMPO_STOCK+" INTEGER DEFAULT 0 NOT NULL, FOREIGN KEY ("+CAMPO_ID_SUBCATEGORIA+") REFERENCES "+TABLA_SUBCATEGORIA+" ("+CAMPO_ID_SUBCATEGORIA+")ON DELETE CASCADE ON UPDATE NO ACTION)";

    public static final String CAMPO_ID_EMPRESA="id_empresa";
    public static final String CAMPO_NOMBRE_EMPRESA="nombre_empresa";
    public static final String CAMPO_RUT_EMPRESA="rut_empresa";
    public static final String CAMPO_CORREO_EMPRESA="correo_empresa";
    public static final String CAMPO_TELEFONO_EMPRESA="telefono_empresa";
    public static final String CAMPO_DIRECCION_EMPRESA="direccion_empresa";
    public static final String CAMPO_ESTADO_EMPRESA="estado_empresa";
    public static final String CREAR_TABLA_EMPRESA="CREATE TABLE " +
            ""+TABLA_EMPRESA+" ("+CAMPO_ID_EMPRESA+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_COMUNA+" INTEGER NOT NULL, "+CAMPO_NOMBRE_EMPRESA+" TEXT NOT NULL, "+CAMPO_RUT_EMPRESA+" TEXT NOT NULL, "
            +CAMPO_CORREO_EMPRESA+" TEXT NOT NULL, "+CAMPO_TELEFONO_EMPRESA+" TEXT NOT NULL, "
            +CAMPO_DIRECCION_EMPRESA+" TEXT NOT NULL, "+CAMPO_ESTADO_EMPRESA+" INTEGER, FOREIGN KEY ("+CAMPO_ID_COMUNA+") REFERENCES "
            +TABLA_COMUNA+" ("+CAMPO_ID_COMUNA+")ON DELETE CASCADE ON UPDATE NO ACTION)";

    public static final String CAMPO_ID_USUARIO="id_usuario";
    public static final String CAMPO_NOMBRE_USUARIO="nombre_usuario";
    public static final String CAMPO_APELLIDO_USUARIO="apellido_usuario";
    public static final String CAMPO_RUT_USUARIO="rut_usuario";
    public static final String CAMPO_CORREO_USUARIO="correo_usuario";
    public static final String CAMPO_TELEFONO_USUARIO="telefono_usuario";
    public static final String CAMPO_DIRECCION_USUARIO="direccion_usuario";
    public static final String CAMPO_CLAVE="clave";
    public static final String CAMPO_TIPO_USUARIO="tipo_usuario";
    public static final String CAMPO_ESTADO_USUARIO="estado_usuario";

    public static final String CREAR_TABLA_USUARIO="CREATE TABLE " +
            ""+TABLA_USUARIO+" ("+CAMPO_ID_USUARIO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_EMPRESA+" INTEGER NOT NULL, "+CAMPO_ID_COMUNA+" INTEGER NOT NULL, "+CAMPO_NOMBRE_USUARIO+" TEXT NOT NULL, "
            +CAMPO_APELLIDO_USUARIO+" TEXT NOT NULL, " +CAMPO_RUT_USUARIO+" TEXT NOT NULL, "+CAMPO_CORREO_USUARIO+" TEXT, "
            +CAMPO_TELEFONO_USUARIO+" TEXT, "+CAMPO_DIRECCION_USUARIO+" TEXT NOT NULL, "
            +CAMPO_CLAVE+" TEXT NOT NULL,"+CAMPO_TIPO_USUARIO+" TEXT NOT NULL, "+CAMPO_ESTADO_USUARIO+" INTEGER, FOREIGN KEY ("+CAMPO_ID_COMUNA+") REFERENCES "
            +TABLA_COMUNA+" ("+CAMPO_ID_COMUNA+"), FOREIGN KEY ("+CAMPO_ID_EMPRESA+") REFERENCES "+TABLA_EMPRESA+" ("+CAMPO_ID_EMPRESA+")ON DELETE CASCADE ON UPDATE NO ACTION)";

    public static final String CAMPO_ID_FOLIO="id_folio";
    public static final String CAMPO_VALOR_FOLIO="valor_folio";
    public static final String CAMPO_FECHA_FOLIO="fecha_folio";

    public static final String CREAR_TABLA_FOLIO="CREATE TABLE " +
            ""+TABLA_FOLIO+" ("+CAMPO_ID_FOLIO+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_EMPRESA+" INTEGER NOT NULL, "+CAMPO_VALOR_FOLIO+" INTEGER, "+CAMPO_FECHA_FOLIO+" TEXT, FOREIGN KEY ("+CAMPO_ID_EMPRESA+") REFERENCES "+TABLA_EMPRESA+" ("+CAMPO_ID_EMPRESA+")ON DELETE CASCADE ON UPDATE NO ACTION)";
    public static final String CAMPO_ID_VENTAS="id_venta";
    public static final String CAMPO_VALOR_VENTA="valor_venta";
    public static final String CAMPO_CANTIDAD="cantidad";
    public static final String CAMPO_FECHA_VENTA="fecha";
    public static final String CAMPO_PENDIENTE_PAGO="pago";

    public static final String CREAR_TABLA_VENTAS="CREATE TABLE " +
            ""+TABLA_VENTAS+" ("+CAMPO_ID_VENTAS+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_ID_ARTICULO+" INTEGER NOT NULL, "+CAMPO_ID_USUARIO+" INTEGER NOT NULL, "+CAMPO_ID_FOLIO+" INTEGER NOT NULL, "
            +CAMPO_VALOR_VENTA+" INTEGER NOT NULL, "+CAMPO_CANTIDAD+" INTEGER NOT NULL, "+CAMPO_FECHA_VENTA+" TEXT NOT NULL, "
            +CAMPO_PENDIENTE_PAGO+" INTEGER NOT NULL, FOREIGN KEY ("+CAMPO_ID_ARTICULO+") REFERENCES "
            +TABLA_ARTICULO+" ("+CAMPO_ID_ARTICULO+"), FOREIGN KEY ("+CAMPO_ID_USUARIO+") REFERENCES "
            +TABLA_USUARIO+" ("+CAMPO_ID_USUARIO+"),  FOREIGN KEY ("+CAMPO_ID_FOLIO+") REFERENCES "
             +TABLA_FOLIO+" ("+CAMPO_ID_FOLIO+") ON DELETE CASCADE ON UPDATE NO ACTION)";



    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public static final String CORREO_INGRESO = "admin@gmail.com";
    public static final String CLAVE_INGRESO = "1234";
}
