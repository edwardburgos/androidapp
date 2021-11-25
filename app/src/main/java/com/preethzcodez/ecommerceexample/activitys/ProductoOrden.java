package com.preethzcodez.ecommerceexample.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.preethzcodez.ecommerceexample.R;

import com.preethzcodez.ecommerceexample.adapters.ProductoOrdenAdapter;
import com.preethzcodez.ecommerceexample.database.MiBD;

import com.preethzcodez.ecommerceexample.model.Carrit;
import com.preethzcodez.ecommerceexample.model.Pedido;
import com.preethzcodez.ecommerceexample.utils.personaService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preeth on 1/7/2018
 */

public class ProductoOrden extends AppCompatActivity {
    MiBD mibd;
   personaService personaService;
    List<Pedido>listapedidos=new ArrayList<>();
    ListView listView;
    String userEmail = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productosordenes);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText("Mis Productos Comprados");

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Hide Cart Icon
        ImageView cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);

        // Fill ListView
        listView = findViewById(R.id.listview);

        mibd = new MiBD(this);
        // Get Product Id
        int id = getIntent().getIntExtra("cod_pedi", 0);

        String miid = String.valueOf(id);
       // Toast.makeText(getApplicationContext(),miid,Toast.LENGTH_SHORT).show();
        List<Carrit> orderHistory = mibd.getOrders(miid);

        // Fill ListView
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new ProductoOrdenAdapter(this,orderHistory));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }


}
