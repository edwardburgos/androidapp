package com.preethzcodez.ecommerceexample.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import com.preethzcodez.ecommerceexample.MainActivity;
import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.model.Carrit;
import com.preethzcodez.ecommerceexample.model.Pedido;
import com.preethzcodez.ecommerceexample.model.Usuario;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Pago extends AppCompatActivity {

    Button btnPagar,btnBuscar;
    ImageView iv;
    String json;
    EditText direc,refe;
    Double pagofinal=0.0;
    String portada,cod,valor,titu;
    TextView totalpago, midirec;
    String monto="";
    //MiBD midbb;
    String userEmail = null;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String UPLOAD_URL = "http://192.168.42.63/backend/upload.php";
    personaService personaService;
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pago);


        setIds();
        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Set Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText("Pago");

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Bundle bundle=getIntent().getExtras();
        valor= bundle.getString("totalpago");
        double doble = Double.parseDouble(valor);
        pagofinal=doble/3.5;
        totalpago.setText("Total a Pagar S/ "+valor);
        // Hide Cart Icon
        ImageView cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);

        // Get Orders From DB

        final SessionManager sessionManager = new SessionManager(this);
        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);
        final MiBD midb= new MiBD(getApplicationContext());
        //final MiBD midbb = new MiBD(this);

        final List<Carrit> shoppingCart = midb.getCartItems();
        json = new Gson().toJson(shoppingCart );
        ultimopedi();
     //   List<Cart> orderHistory = db_handler.getOrders(sessionManager.getSessionData(Constants.SESSION_EMAIL));

        // Fill ListView
        ListView listView = findViewById(R.id.listview);
       // listView.setAdapter(new MyOrdersAdapter(this,orderHistory));
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // procesarPago();
                verifica(userEmail);
               // uploadImage();
            }
        });

        final Usuario userr= midb.getUser(sessionManager.getSessionData(Constants.SESSION_EMAIL),sessionManager.getSessionData(Constants.SESSION_PASSWORD));


        midirec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                midirec(userr);
            }
        });
    }
    public void verifica( String mail){

        personaService= Apis.getPersonaService();
        Call<Usuario> call=personaService.verificar(mail);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, retrofit2.Response<Usuario> response) {
                if(response.body().getEstado().equals("1")){
                    uploadImage();

                }else{
                    Toast.makeText(getApplicationContext(), "Cuenta Bloqueada" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {


            }

        });
    }

    // Set Values
    private void midirec(Usuario user) {
        direc.setText(user.getDireccion());
    }


    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        String direnvi=direc.getText().toString()+ " - "+ refe.getText().toString();
                        Pedido p=new Pedido();
                        p.setEstado("0");
                        p.setMetodo("transferencia movil");
                        p.setEmail(userEmail);
                        p.setDireccion(direnvi);
                        p.setProductos(json);
                        p.setPago(valor);
                        addPedido(p);
                        Toast.makeText(Pago.this, "Pedido Realizado", Toast.LENGTH_LONG).show();
                        MiBD midbb = new MiBD(getApplicationContext());
                        midbb.deleteCartItems();
                        Intent intent = new Intent(getApplicationContext(), Ordenes.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Pago.this, "Debe Elegir una Foto", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = cod;

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, nombre);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }
    public void ultimopedi(){

        personaService= Apis.getPersonaService();
        Call<Pedido> call=personaService.ultimpedido();
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, retrofit2.Response<Pedido> response) {
                if(response.isSuccessful()){
                    Integer mi = response.body().getId() + 1;
                  cod = String.valueOf(mi);
                   // Toast.makeText(Pago.this, cod, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }
    public void addPedido( Pedido p){

        personaService= Apis.getPersonaService();
        Call<Pedido> call=personaService.addPedido(p);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, retrofit2.Response<Pedido> response) {

                Toast.makeText(getApplicationContext(), "Se Registro", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    // Set Ids
    private void setIds() {
        btnPagar= findViewById(R.id.pagar);
        totalpago = findViewById(R.id.totalpago);
        midirec= findViewById(R.id.midirec);
        btnBuscar = findViewById(R.id.btnBuscar);
        direc = (EditText)findViewById(R.id.direc);
        refe=(EditText)findViewById(R.id.refe);
        iv = findViewById(R.id.imageView);
    }
}
