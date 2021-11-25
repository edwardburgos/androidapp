package com.preethzcodez.ecommerceexample.utils;

public class Apis {

    public static final String URL_001="http://192.168.42.63:8089/";

    public static personaService getPersonaService(){
        return Cliente.getCliente(URL_001).create(personaService.class);

    }
}
