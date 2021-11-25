package com.preethzcodez.ecommerceexample.model;

/**
 * Created by Preeth on 1/3/2018
 */

public class Carrit {

    private Integer id;
    private Integer itemQuantity;
    private String product;
    private String portada;
    private Integer cod_produc;
    private Double variant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public Integer getCod_produc() {
        return cod_produc;
    }

    public void setCod_produc(Integer cod_produc) {
        this.cod_produc = cod_produc;
    }

    public Double getVariant() {
        return variant;
    }

    public void setVariant(Double variant) {
        this.variant = variant;
    }
}
