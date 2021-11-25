package com.preethzcodez.ecommerceexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categoria {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("categoria")
    @Expose
    private String categoria;
    @SerializedName("ruta")
    @Expose
    private String ruta;




    private Boolean isShortlisted = false;

    public Boolean getShortlisted() {
        return isShortlisted;
    }

    public void setShortlisted(Boolean shortlisted) {
        isShortlisted = shortlisted;
    }

    public Categoria(){

    }

    public Categoria(Integer id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
