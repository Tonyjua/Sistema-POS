package com.example.proyectoappmovil.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Usuarios {
    private String  password;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    private String id;
    private String tipoUser;
    private String nombre;
    private Bitmap imagen;
    private String dato;
    private String Apellido;
    private String Correo;
    private String direcion;
    private String rut;
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    private String fecha;
    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    private String precio;
    private String stock;
    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    private String detalle;
    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private String telefono;
    public String getDirrecion() {
        return direcion;
    }

    public void setDirrecion(String dirrecion) {
        this.direcion = dirrecion;
    }



    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }



    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }



    public String getDato() {
        return dato;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    private String cantidad;

    public void setDato(String dato) {
        this.dato = dato;

        try {
            byte[] byteCode= Base64.decode(dato,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto=200;//alto en pixeles
            int ancho=250;//ancho en pixeles

            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen=Bitmap.createScaledBitmap(foto,alto,ancho,true);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

//desde este metodo se transforma la imagen registrada en la bd byte(Blob)a bitmap para que se muestre en el recyclerview
    public void setImagen(byte[] blob) {
        if (blob.length != 0) {
            InputStream is = new ByteArrayInputStream(blob);
            imagen = BitmapFactory.decodeStream(is);
        }

    }


}
