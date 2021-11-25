package com.preethzcodez.ecommerceexample.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.activitys.ProductoDetalle;
import com.preethzcodez.ecommerceexample.activitys.LoginActivity;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.model.Deseos;
import com.preethzcodez.ecommerceexample.model.Persona;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosAdapter extends ArrayAdapter<Persona> {
    double deseos;
    private Context context;
    private List<Persona> personas;
    private List<Deseos> productList;
    String userEmail = null;
   personaService personaService;
    int i = 0;
    int veri=1;
    int b;
    int c;
    List<Deseos>listadeseos=new ArrayList<>();
    ListView listView;
    public ProductosAdapter(Context context, int resource, List<Persona> objects) {
        super(context, resource, objects);
        this.context=context;
        this.personas=objects;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=layoutInflater.inflate(R.layout.productos_grid_item,parent,false);

        ImageView imagenV=(ImageView) rowView.findViewById(R.id.image);
        TextView txtnombre=(TextView)rowView.findViewById(R.id.name);
        TextView txtprecio=(TextView)rowView.findViewById(R.id.price);
        TextView txtofert=(TextView)rowView.findViewById(R.id.priceofer);
       // String idd = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

     Picasso.get()
             .load("http://192.168.42.63/backend/"+personas.get(position).getPortada())

             .placeholder(R.drawable.ic_launcher_foreground)
             .error(R.drawable.ic_launcher_background)
             .into(imagenV);
     txtnombre.setText(String.format(personas.get(position).getTitulo()));

        b = Integer.parseInt(personas.get(position).getOferta());
     if(b>0){
         txtofert.setText(String.format("S/%s",personas.get(position).getPrecio()));
         txtprecio.setText(String.format("S/%s",personas.get(position).getPrecioferta()));
         txtofert.setPaintFlags(txtofert.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

     }else{
         txtprecio.setText(String.format("S/%s",personas.get(position).getPrecio()));
     }


     final String link="http://192.168.42.63/backend/"+personas.get(position).getPortada();
        // Product Item Click
        holder.itemLay = rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               c = Integer.parseInt(personas.get(position).getOferta());
                Intent intent = new Intent(context, ProductoDetalle.class);
                intent.putExtra("ProductId",String.valueOf(personas.get(position).getId()));
                if(c>0){
                    intent.putExtra("Precio",String.valueOf(personas.get(position).getPrecioferta()));
                }else{
                    intent.putExtra("Precio",String.valueOf(personas.get(position).getPrecio()));
                }
                intent.putExtra("Titulo",String.valueOf(personas.get(position).getTitulo()));
               intent.putExtra("Protada",String.valueOf(link));
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.overridePendingTransition(0,0);
            }
        });


        SessionManager sessionManager = new SessionManager(context);
        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);

       // Toast.makeText(context, String.valueOf(String.valueOf(personas.get(position).getId())), Toast.LENGTH_SHORT).show();
        // Wish List Item Click


        MiBD mibd = new MiBD(context);


        holder.heart = rowView.findViewById(R.id.heart);
        int miid=personas.get(position).getId();
        double mprecio = Double.parseDouble(personas.get(position).getPrecio());
        mibd.insertProducts(miid,mprecio);
       // String miid = String.valueOf(personas.get(position).getId());

        boolean isShortlisted = mibd.isShortlistedItem(miid, userEmail);
       // product.setShortlisted(isShortlisted);
        //Toast.makeText(context, "Holaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();



        if (isShortlisted) {

            deseos = mibd.buscar(personas.get(position).getId(),userEmail);
            double doble = Double.parseDouble(personas.get(position).getPrecio());
            if(doble<deseos){
                String idd=String.valueOf(personas.get(position).getId());
                Toast.makeText(context, "un Deseo Bajo de Precio", Toast.LENGTH_SHORT).show();
            }
          //  String totalString = String.valueOf(deseos);
            //Toast.makeText(context, totalString, Toast.LENGTH_SHORT).show();
;
        }
       if (isShortlisted) {
            holder.heart.setImageResource(R.drawable.ic_heart_grey);
           personas.get(position).setShortlisted(true);
        }

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add / Remove Item To Wish List
                MiBD mibd = new MiBD(context);


                if (userEmail!=""){

                Double precio=Double.parseDouble(personas.get(position).getPrecio());
              if (!personas.get(position).getShortlisted()) {
                  holder.heart.setImageResource(R.drawable.ic_heart_grey);
                  personas.get(position).setShortlisted(true);
                  if (mibd.shortlistItem(personas.get(position).getId(), userEmail,precio) > 0) {
                        Deseos d=new Deseos();
                        d.setEmail(userEmail);
                        d.setId_producto(String.valueOf(personas.get(position).getId()));
                        d.setPrecio(String.valueOf(personas.get(position).getPrecio()));
                        addDeseos(d);
                Toast.makeText(context, "Se agrego de lista de deseos", Toast.LENGTH_SHORT).show();
                  }
               } else {
                    holder.heart.setImageResource(R.drawable.ic_heart_grey600_24dp);
                  personas.get(position).setShortlisted(false);
                    if (mibd.removeShortlistedItem(personas.get(position).getId(), userEmail)) {
                        EliminaDeseo(String.valueOf(personas.get(position).getId()),userEmail);
                        //Toast.makeText(context, "Se quito de lista de deseos", Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    Activity activity = (Activity) context;
                    activity.overridePendingTransition(0,0);


                }

            }
        });


        return rowView;
    }

    public void addDeseos( Deseos d){

        personaService= Apis.getPersonaService();
        Call<Deseos> call=personaService.addDeseos(d);
        call.enqueue(new Callback<Deseos>() {
            @Override
            public void onResponse(Call<Deseos> call, Response<Deseos> response) {

              //  Toast.makeText(getContext(), "Se Registro", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Se Elimino", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Deseos> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }
    public class Holder {
        RelativeLayout itemLay;
        TextView name, price;
        ImageView heart;
    }
}
