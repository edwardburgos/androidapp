package com.preethzcodez.ecommerceexample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.activitys.ProductoDetalle;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.model.Deseos;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/7/2018
 */

public class MegustaAdapter extends BaseAdapter {
    double deseos;
    int miposi;
    double actual;
    private Context context;
    private LayoutInflater inflater;
    private List<Deseos> productList;
    personaService personaService;
    String userEmail = null;
    public MegustaAdapter(Context context, List<Deseos> productList) {
        this.context = context;
        this.productList = productList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.megusta_item, null);

        holder.title = rowView.findViewById(R.id.title);
        holder.price = rowView.findViewById(R.id.price);
        holder.pricenu = rowView.findViewById(R.id.pricenu);
        holder.remove = rowView.findViewById(R.id.remove);
        holder.image = rowView.findViewById(R.id.img);

        ImageView imagenV=(ImageView) rowView.findViewById(R.id.img);


        holder.title.setText(productList.get(position).getTitulo());
        holder.price.setText("S/"+productList.get(position).getPrecio());

        MiBD mibd = new MiBD(context);
        int micod = Integer.parseInt(productList.get(position).getId_producto());
        actual = mibd.miprecio(micod);
        deseos = Double.parseDouble(productList.get(position).getPrecio());
        if(actual<deseos){
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            productList.get(position).setPrecio(String.valueOf(actual));
            holder.pricenu.setText("Precio Actual S/"+String.valueOf(actual));
        }




        Picasso.get()
                .load("http://192.168.42.63/backend/"+productList.get(position).getPortada())
                .resize(200,200)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(imagenV);

        final String link="http://192.168.42.63/backend/"+productList.get(position).getPortada();
        // Product Item Click
        holder.itemLay = rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductoDetalle.class);
                intent.putExtra("ProductId", productList.get(position).getId_producto());
                if(actual<deseos){
                    intent.putExtra("Precio",String.valueOf(productList.get(position).getPrecio()));
                }else{
                    intent.putExtra("Precio",String.valueOf(productList.get(position).getPrecio()));
                }
                intent.putExtra("Titulo",String.valueOf(productList.get(position).getTitulo()));
                intent.putExtra("Protada",String.valueOf(link));
                context.startActivity(intent);
            }
        });

        // Wish List Item Click
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove Item From Wish List
                MiBD mibd = new MiBD(context);
                SessionManager sessionManager = new SessionManager(context);
                userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);
                int idp = Integer.parseInt(productList.get(position).getId_producto());
                if (mibd.removeShortlistedItem(idp, userEmail)) {
                    EliminarDeseo(String.valueOf(productList.get(position).getId_producto()),userEmail);
                    productList.remove(position);
                    notifyDataSetChanged();;
                    //Toast.makeText(context, "Se quito de lista de deseos", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return rowView;
    }


    public void EliminarDeseo( String cod, String mail){

        personaService= Apis.getPersonaService();
        Call<Deseos> call=personaService.eliminardeseo(cod, mail);
        call.enqueue(new Callback<Deseos>() {
            @Override
            public void onResponse(Call<Deseos> call, Response<Deseos> response) {
               // response.body().
                Toast.makeText(context, "Se Elimino", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Deseos> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }
    public class Holder {
        RelativeLayout itemLay;
        TextView title, price,pricenu;
        ImageView remove,image;
    }
}
