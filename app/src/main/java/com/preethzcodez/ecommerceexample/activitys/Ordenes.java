package com.preethzcodez.ecommerceexample.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.preethzcodez.ecommerceexample.R;

import com.preethzcodez.ecommerceexample.adapters.OrdenAdapter;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.model.Pedido;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/7/2018
 */

public class Ordenes extends AppCompatActivity {
   personaService personaService;
    List<Pedido>listapedidos=new ArrayList<>();
    ListView listView;
    String userEmail = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordenes);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText(R.string.my_orders);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
               //  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               //getApplicationContext().startActivity(intent);
            }
        });

        // Hide Cart Icon
        ImageView cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);

        // Get Orders From DB
        SessionManager sessionManager = new SessionManager(this);
     //   List<Cart> orderHistory = db_handler.getOrders(sessionManager.getSessionData(Constants.SESSION_EMAIL));

        // Fill ListView
        listView = findViewById(R.id.listview);
       // listView.setAdapter(new MyOrdersAdapter(this,orderHistory));

        sessionManager = new SessionManager(this);
        // userEmail = sessionManager.getSessionData(Constants.SESSION_EMAIL);
        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);
        listarordenes(userEmail);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }




    public void listarordenes(String cat){

        personaService= Apis.getPersonaService();
        Call<List<Pedido>> call=personaService.mispedidos(cat);
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if(response.isSuccessful()){

                    listapedidos=response.body();
                    listView.setAdapter(new OrdenAdapter(getApplicationContext(),listapedidos));

                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }
}
