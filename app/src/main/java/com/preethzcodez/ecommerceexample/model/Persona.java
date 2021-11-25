package com.preethzcodez.ecommerceexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Persona implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("portada")
    @Expose
    private String portada;
    @SerializedName("titulo")
    @Expose
    private String titulo;
    @SerializedName("precio")
    @Expose
    private String precio;
    @SerializedName("oferta")
    @Expose
    private String oferta;
    @SerializedName("precioferta")
    @Expose
    private String precioferta;
    @SerializedName("descuentoferta")
    @Expose
    private String descuentoferta;


    private Boolean isShortlisted = false;

    public Boolean getShortlisted() {
        return isShortlisted;
    }

    public void setShortlisted(Boolean shortlisted) {
        isShortlisted = shortlisted;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }

    public String getPrecioferta() {
        return precioferta;
    }

    public void setPrecioferta(String precioferta) {
        this.precioferta = precioferta;
    }

    public String getDescuentoferta() {
        return descuentoferta;
    }

    public void setDescuentoferta(String descuentoferta) {
        this.descuentoferta = descuentoferta;
    }

    public Persona(){

    }

    public Persona(Integer id, String portada, String titulo, String precio,String oferta,String precioferta,String descuentoferta) {
        this.id = id;
        this.portada = portada;
        this.titulo = titulo;
        this.precio = precio;
        this.oferta = oferta;
        this.precioferta = precioferta;
        this.descuentoferta = descuentoferta;
    }
}
