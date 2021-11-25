package com.preethzcodez.ecommerceexample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("metodo")
    @Expose
    private String metodo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("productos")
    @Expose
    private String productos;
    @SerializedName("pago")
    @Expose
    private String pago;
    @SerializedName("items")
    private List<Pedido> mItems;


    public Pedido() {

    }

    public List<Pedido> getItems() {
        return mItems;
    }

    public void setItems(List<Pedido> items) {
        mItems = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }
}
