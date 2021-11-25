package com.preethzcodez.ecommerceexample.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.preethzcodez.ecommerceexample.R;

import com.preethzcodez.ecommerceexample.adapters.CarritoAdapter;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.model.Carrit;
import com.preethzcodez.ecommerceexample.model.Pedido;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/6/2018
 */

public class Carrito extends AppCompatActivity implements CarritoAdapter.UpdatePayableAmount, CarritoAdapter.MonitorListItems {

    Toolbar toolbar;
    String userEmail = null;
    String json;
    String r_name=null;
    Double totalAmount=0.0;
    personaService personaService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText(R.string.shopping_cart);

        // Hide Cart Icon
        ImageView cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Get Cart Items
        final SessionManager sessionManager = new SessionManager(this);

        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);

        final MiBD midb= new MiBD(getApplicationContext());
        final MiBD midbb = new MiBD(this);

        final List<Carrit> shoppingCart = midb.getCartItems();

        // Fill ListView With Items
        ListView listView = findViewById(R.id.listview);

        listView.setAdapter(new CarritoAdapter(this, shoppingCart));

        setPayableAmount(shoppingCart);

        for (Carrit e : shoppingCart) {
            r_name = r_name + e.getProduct();
        }



        json = new Gson().toJson(shoppingCart );
       // Log.e("order",  new Gson().toJson( shoppingCart ) );



        // Order Button Click
        Button placeOrder = findViewById(R.id.procesar);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userEmail!=""){
                    // delete from cart and place order
                  //  midbb.deleteCartItems();

                    Date date = new Date();
                    DateFormat horas = new SimpleDateFormat("h:mm:s k");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String hora = horas.format(date);
                    String fecha = dateFormat.format(date);

                    String totalString = String.valueOf(totalAmount);

                 /*   Pedido p=new Pedido();
                    p.setEstado("0");
                    p.setMetodo("paypal");
                    p.setEmail(userEmail);
                    p.setDireccion("av lima 23424");
                    p.setProductos(json);
                    p.setPago(totalString);
                    addPedido(p);
                   Toast.makeText(getApplicationContext(),json,Toast.LENGTH_SHORT).show();*/
                    // Call Main Activity
                    Intent intent = new Intent(getApplicationContext(), Pago.class);
                    intent.putExtra("totalpago",String.valueOf(totalString));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }

            }
        });
    }
    public void addPedido( Pedido p){

        personaService= Apis.getPersonaService();
        Call<Pedido> call=personaService.addPedido(p);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {

                Toast.makeText(getApplicationContext(), "Se Registro", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }
    // Calculate Payable Amount
    @SuppressLint("SetTextI18n")
    private void setPayableAmount(List<Carrit> shoppingCart) {
         totalAmount = 0.0;
        for (int i = 0; i < shoppingCart.size(); i++) {
            int itemQuantity = shoppingCart.get(i).getItemQuantity();
            Double price = Double.valueOf(shoppingCart.get(i).getVariant());
            price = price * itemQuantity;
            totalAmount = totalAmount + price;
            json = new Gson().toJson(shoppingCart );
        }

        // Set Value
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        TextView payable = findViewById(R.id.payableAmt);
        payable.setText("S/."+formatter.format(totalAmount));
    }

    // update payable amount
    @Override
    public void updatePayableAmount(List<Carrit> shoppingCart) {
        setPayableAmount(shoppingCart);
        json = new Gson().toJson(shoppingCart );

    }

    // finish activity if cart empty
    @Override
    public void finishActivity(List<Carrit> shoppingCart) {
        try {
            if (shoppingCart.size() == 0) {
                overridePendingTransition(0,0);
                finish();
            }
        } catch (Exception e) {
            overridePendingTransition(0,0);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
