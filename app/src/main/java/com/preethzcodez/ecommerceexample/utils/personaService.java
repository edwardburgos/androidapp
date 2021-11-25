package com.preethzcodez.ecommerceexample.utils;

import com.preethzcodez.ecommerceexample.model.Categoria;
import com.preethzcodez.ecommerceexample.model.Deseos;
import com.preethzcodez.ecommerceexample.model.Detalle;
import com.preethzcodez.ecommerceexample.model.Pedido;
import com.preethzcodez.ecommerceexample.model.Persona;
import com.preethzcodez.ecommerceexample.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface personaService {

     //CATEGORIAS
    @GET("Categorias/lista")
    Call<List<Categoria>> getCategorias();

    // PRODUCTOS
    @GET("Productos/lista")
    Call<List<Persona>> getPersonas();

    @GET("Productos/productosxcategoria/{cat}")
    Call<List<Persona>> getProductosCategoria(@Path ("cat") String cat);

    @GET("Productos/productosxcategoriadesc/{cat}")
    Call<List<Persona>> getProductosCategoriadesc(@Path ("cat") String cat);

    @GET("Productos/buscarproducto/{pro}")
    Call<List<Persona>> getBuscarProductos(@Path ("pro") String pro);

    @GET("Productos/buscarproductodesc/{pro}")
    Call<List<Persona>> getBuscarProductosdesce(@Path ("pro") String pro);

    @GET("Productos/listaxprecio/{val}")
    Call<List<Persona>> getListaxprecio(@Path ("val") String val);

    @GET("Productos/listaxoferta")
    Call<List<Persona>> getListaxoferta();

    //USUARIOS
    @POST("Usuario/verificar/{mail}")
    Call<Usuario> verificar(@Path ("mail") String mail);

    @POST("Usuario/registrar")
    Call<Usuario> addUsuario(@Body Usuario usuario);

    @POST("Usuario/ingresar/{mail}/{clave}")
    Call<Usuario> ingresar(@Path ("mail") String mail,@Path ("clave") String clave );

    @POST("Usuario/cambioclave/{mail}/{nclave}")
    Call<Usuario> cambiarpass(@Path ("mail") String mail,@Path ("nclave") String nclave );

    //DESEOS

    @POST("Deseos/registrar")
    Call<Deseos> addDeseos(@Body Deseos deseos);

    @GET("Deseos/Lista/{vari}")
    Call<List<Deseos>> getDeseos(@Path ("vari") String vari);

    @GET("Deseos/Lista/{vari}")
    Call<List<Deseos>> getDeseospro(@Path ("vari") String vari);

    @DELETE("Deseos/Eliminar/{cod}/{mail}")
    Call<Deseos> eliminardeseo(@Path ("cod") String cod,@Path ("mail") String mail );

     //PEDIDOS
    @POST("Pedidos/registrar")
    Call<Pedido> addPedido(@Body Pedido pedido);

    @GET("Pedidos/lista")
    Call<Pedido> ultimpedido();

    @GET("Pedidos/listarpedidos/{vari}")
    Call<List<Pedido>> mispedidos(@Path ("vari") String vari);

    @GET("Detalle/ListaDetalle/{cod}")
    Call<List<Detalle>> getDetalles(@Path("cod") String cod);

    @POST("Pedidos/cancelar/{cod}")
    Call<Pedido> cancelarpedido(@Path ("cod") String cod);

}
