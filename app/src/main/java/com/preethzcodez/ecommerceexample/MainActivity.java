package com.preethzcodez.ecommerceexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.preethzcodez.ecommerceexample.activitys.Carrito;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;

import com.preethzcodez.ecommerceexample.fragments.Categorias;
import com.preethzcodez.ecommerceexample.fragments.DatosUsuario;
import com.preethzcodez.ecommerceexample.fragments.MeGusta;
import com.preethzcodez.ecommerceexample.fragments.Productos;

import com.preethzcodez.ecommerceexample.interfaces.FinishActivity;
import com.preethzcodez.ecommerceexample.interfaces.ShowBackButton;
import com.preethzcodez.ecommerceexample.interfaces.ToolbarTitle;
import com.preethzcodez.ecommerceexample.utils.Constants;

public class MainActivity extends AppCompatActivity implements  ToolbarTitle, ShowBackButton, FinishActivity {

    BottomNavigationView navigation;

    SessionManager sessionManager;
    Toolbar toolbar;
   TextView titleToolbar;
    int cartCount = 0;
    ImageView backButton;
    String subCategoryTitle = null;
    String userEmail = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sessionManager = new SessionManager(this);


         toolbar = findViewById(R.id.toolbar);
   setSupportActionBar(toolbar);


      titleToolbar = findViewById(R.id.titleToolbar);
       titleToolbar.setText(R.string.TitleHome);


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonClick();
            }
        });


        navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton btn =findViewById(R.id.wsp);
        btn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     String micel="51936522264";
                     String text="Hola, Necesito Ayuda";
                     Intent sendIntent=new Intent();
                     sendIntent.setAction(Intent.ACTION_VIEW);
                     String url="whatsapp://send?phone="+micel+"&text="+text;
                     sendIntent.setData(Uri.parse(url));
                     startActivity(sendIntent);

                 }
             });

        callProductsFragment();
        setToolbarIconsClickListeners();
    }

    // Set Toolbar Icons Click Listeners
    private void setToolbarIconsClickListeners() {
        ImageView cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartCount > 0) {
                    startActivity(new Intent(getApplicationContext(), Carrito.class));
                    overridePendingTransition(0,0);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.cart_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * BottomNavigationView Listener
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Hide Back Button
            backButton.setVisibility(View.INVISIBLE);

            // Prevent Reload Same Fragment
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.nav_home: // Home
                    // Prevent Reload
                    try {
                        if (!fm.findFragmentByTag("HOME").isVisible()) {
                            callProductsFragment();
                            titleToolbar.setText(R.string.TitleHome);
                        }
                    } catch (NullPointerException e) {
                        callProductsFragment();
                        titleToolbar.setText(R.string.TitleHome);
                    }
                    return true;

                case R.id.nav_categories: // Categories
                    ft.replace(R.id.content, new Categorias());
                    ft.commit();
                    titleToolbar.setText(R.string.TitleCategories);
                    return true;

                case R.id.nav_shortlist: // Wish List
                    ft.replace(R.id.content, new MeGusta());
                    ft.commit();
                    titleToolbar.setText(R.string.TitleShortlist);
                    return true;

                case R.id.nav_account: // User Account
                    ft.replace(R.id.content, new DatosUsuario());
                    ft.commit();
                    titleToolbar.setText(R.string.TitleAccount);
                    return true;
            }
            return false;
        }
    };

    // call products fragment
    private void callProductsFragment() {

        Bundle args = new Bundle();
        args.putInt(Constants.CAT_ID_KEY, 0);

        Productos productos = new Productos();
        productos.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, productos, "HOME");
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);
        final MiBD midb= new MiBD(getApplicationContext());
        cartCount = midb.getCartItemCount();
        TextView count = findViewById(R.id.count);
        if (cartCount > 0) {
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(cartCount));
        } else {
            count.setVisibility(View.GONE);
        }
    }

    // Back Button Click
    private void backButtonClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        try {
            if (fragmentManager.findFragmentByTag(Constants.FRAG_SUBCAT).isVisible()) {
                fragmentTransaction.replace(R.id.content, new Categorias());
                fragmentTransaction.commit();
                titleToolbar.setText(R.string.TitleCategories);
                backButton.setVisibility(View.INVISIBLE);
            }
        } catch (NullPointerException e) {
            super.onBackPressed();
        }
    }



    @Override
    public void onBackPressed() {
        backButtonClick();
    }

    // Set Toolbar Title
    @Override
    public void setToolbarTitle(String toolbarTitle) {
        titleToolbar.setText(toolbarTitle);
    }

    // show back button
    @Override
    public void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
    }

    // Save Subcategory Title - Need for backButtonClick method
    @Override
    public void saveSubcategoryTitle(String toolbaTitle) {
        subCategoryTitle = toolbaTitle;
    }

    // Finish Activity From Fragment
    @Override
    public void finishActivity() {
        overridePendingTransition(0,0);
        finish();
    }
}
