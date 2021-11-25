package com.preethzcodez.ecommerceexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Deseos {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("portada")
    @Expose
    private String portada;
    @SerializedName("titulo")
    @Expose
    private String titulo;
    @SerializedName("precioac")
    @Expose
    private String precioac;
    @SerializedName("id_producto")
    @Expose
    private String id_producto;
    @SerializedName("precio")
    @Expose
    private String precio;
    @SerializedName("fecha")
    @Expose
    private String fecha;

    public Deseos() {

    }
    private Boolean isShortlisted = false;
    public void setShortlisted(Boolean shortlisted) {
        isShortlisted = shortlisted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrecioac() {
        return precioac;
    }

    public void setPrecioac(String precioac) {
        this.precioac = precioac;
    }

    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(String id_producto) {
        this.id_producto = id_producto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
