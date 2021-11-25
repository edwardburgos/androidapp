package com.preethzcodez.ecommerceexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Detalle implements Serializable {

    @SerializedName("productos")
    @Expose
    private String productos;

    public Detalle() {
    }
    public Detalle(String productos) {
        this.productos = productos;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

}
