package com.preethzcodez.ecommerceexample.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.R;

import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.model.Deseos;
import com.preethzcodez.ecommerceexample.model.Persona;

import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;
import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/3/2018
 */

public class ProductoDetalle extends AppCompatActivity {

    Persona productos;
   MiBD mibd;
    personaService personaService;
    String selectedItemPrice = null;
    int selectedItemQuantity = 1;
    int selectedItemVariantId = 0;
    String userEmail = null;
    List<Persona> listaProductos;
    LinearLayout colorParentLay, sizeParentLay;
    FlowLayout colorsLay, sizeLay;
    TextView price, quantityValue;
    ImageView minus, plus;
    Button cartt, buyNow;
    SessionManager sessionManager;
    int cartCount = 0;
    Toolbar toolbar;
    String portada,id,valor,titu;
    String codpro;
    boolean isShortlisted;
    int codg;
    int idp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_detalles);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setVisibility(View.GONE);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
              //  getApplicationContext().startActivity(intent);

                onBackPressed();
            }
        });
        setIds();

        TextView precio = findViewById(R.id.price);
        TextView title = findViewById(R.id.titl);
        TextView codi = findViewById(R.id.cod);

        ImageView imagenV=(ImageView)findViewById(R.id.image);
        Bundle bundle=getIntent().getExtras();



        portada= bundle.getString("Protada");
        codpro= bundle.getString("ProductId");
         valor= bundle.getString("Precio");
        titu= bundle.getString("Titulo");

        // Get User Email
        sessionManager = new SessionManager(this);
       // userEmail = sessionManager.getSessionData(Constants.SESSION_EMAIL);
        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);


        Picasso.get()
                .load(portada)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(imagenV);
        precio.setText("S/ "+ valor);
        title.setText(titu);
        codi.setText(codpro);
        // Get Product Details By Id

        // Get Product Details By Id
        mibd = new MiBD(this);

        codg = Integer.parseInt(codpro);
       // deseos = mibd.getProductDetailsById(codg, userEmail);
       // String aa= String.valueOf(deseos.getId());
      //  Toast.makeText(getApplicationContext(), titu, Toast.LENGTH_SHORT).show();

       setValues();
     setToolbarIconsClickListeners();
        setQuantityUpdateListeners();
        setBottomPanelClickListeners();

    }

    // Set Ids
    private void setIds() {
        buyNow = findViewById(R.id.buyNow);
        cartt = findViewById(R.id.cartButton);
        price = findViewById(R.id.price);
        quantityValue = findViewById(R.id.quantityValue);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
    }

    // Set Toolbar Icons Click Listeners
    private void setToolbarIconsClickListeners() {
        ImageView cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartCount > 0) {
                    startActivity(new Intent(getApplicationContext(), Carrito.class));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.cart_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Set Bottom Panel Click Listeners
    private void setBottomPanelClickListeners() {



        cartt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isSuccessAddingToCart(false)) {
                   Toast.makeText(getApplicationContext(), R.string.add_success, Toast.LENGTH_SHORT).show();
                   updateCartCount();
                }
            }
        });

        // Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSuccessAddingToCart(true)) {
                    startActivity(new Intent(getApplicationContext(), Carrito.class));
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    private boolean isSuccessAddingToCart(boolean isBuyNow) {

           // String idd = Settings.Secure.getString(ProductDetails.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        int codg = Integer.parseInt(codpro);
        Double val = Double.parseDouble(valor);
        final MiBD midb= new MiBD(getApplicationContext());

 try{
     long result = midb.insertIntoCart(codg, titu,portada,val, selectedItemQuantity);
     if (result > 0 || isBuyNow) {
         return true;
     } else {
         Toast.makeText(getApplicationContext(), R.string.item_exists, Toast.LENGTH_SHORT).show();
         return false;
     }
 } catch (NullPointerException e) {
     Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
     return true;
 }




    }

    // Set Values
    private void setValues() {

        // WishList Icon
        final ImageView heart = findViewById(R.id.heart);
        mibd = new MiBD(this);
        int miid = Integer.parseInt(codpro);

       isShortlisted = mibd.isShortlistedItem(miid, userEmail);
        if (isShortlisted) {
            heart.setImageResource(R.drawable.ic_heart_grey);

        }

        // Wishlist Icon Click
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add / Remove Item From Wish List
                if (userEmail!=""){
                if (!isShortlisted) {
                    Double val = Double.parseDouble(valor);
                    heart.setImageResource(R.drawable.ic_heart_grey);
                    idp = Integer.parseInt(codpro);
                    isShortlisted=true;
                   // deseos.setShortlisted(true);
                    if (mibd.shortlistItem(idp, userEmail,val) > 0) {

                        Deseos d=new Deseos();
                        d.setEmail(userEmail);
                        d.setId_producto(String.valueOf(idp));
                        d.setPrecio(String.valueOf(val));
                        addDeseos(d);
                        Toast.makeText(getApplicationContext(), "Se agrego de lista de deseos", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    idp = Integer.parseInt(codpro);
                    if (mibd.removeShortlistedItem(idp, userEmail)) {
                        isShortlisted=false;
                      //  deseos.setShortlisted(false);
                        heart.setImageResource(R.drawable.ic_heart_grey600_24dp);
                        EliminaDeseo(codpro,userEmail);

                       // Toast.makeText(getApplicationContext(), R.string.item_rem_wishlist, Toast.LENGTH_SHORT).show();
                    }
                }
                }else{


                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    getApplicationContext().startActivity(intent);
                    Activity activity = (Activity) getApplicationContext();
                    activity.overridePendingTransition(0,0);
                }
            }
        });


        // Title
        try{
        TextView Title = findViewById(R.id.title);
        Title.setText(productos.getTitulo());
            productos.setShortlisted(false);
    }catch (Exception ex) {

       // Toast.makeText(ProductDetails.this, ex.toString(), Toast.LENGTH_SHORT).show();
    }

    }

    public void addDeseos( Deseos d){

        personaService= Apis.getPersonaService();
        Call<Deseos> call=personaService.addDeseos(d);
        call.enqueue(new Callback<Deseos>() {
            @Override
            public void onResponse(Call<Deseos> call, Response<Deseos> response) {
               // Toast.makeText(getApplicationContext(), "Se agrego de lista de deseos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Deseos> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }

    public void EliminaDeseo( String cod, String mail){
        personaService= Apis.getPersonaService();
        Call<Deseos> call=personaService.eliminardeseo(cod, mail);
        call.enqueue(new Callback<Deseos>() {
            @Override
            public void onResponse(Call<Deseos> call, Response<Deseos> response) {
                Toast.makeText(getApplicationContext(), "Se elimino de lista de deseos", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Deseos> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }



    // Quantity Update Listeners
    private void setQuantityUpdateListeners() {
        // Decrement Listener
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemQuantity != 1) {
                    selectedItemQuantity--;
                    quantityValue.setText(String.valueOf(selectedItemQuantity));
                }
            }
        });

        // Increment Listener
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItemQuantity++;
                quantityValue.setText(String.valueOf(selectedItemQuantity));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    updateCartCount();
    }

    // Update Cart Item Count In Toolbar
    private void updateCartCount() {
        final MiBD midb= new MiBD(getApplicationContext());
         cartCount = midb.getCartItemCount();
        //cartCount=cartCount+1;
        TextView count = findViewById(R.id.count);
        if (cartCount > 0) {
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(cartCount));
        } else {
            count.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
