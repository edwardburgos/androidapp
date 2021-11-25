package com.preethzcodez.ecommerceexample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.R;

import com.preethzcodez.ecommerceexample.activitys.ProductoOrden;
import com.preethzcodez.ecommerceexample.activitys.LoginActivity;
import com.preethzcodez.ecommerceexample.database.MiBD;

import com.preethzcodez.ecommerceexample.model.Carrit;
import com.preethzcodez.ecommerceexample.model.Pedido;

import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.personaService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/7/2018
 */

public class OrdenAdapter extends BaseAdapter {
    private static final String TAG = LoginActivity.class.getSimpleName();
    List<Map<String,String>> productlist = new ArrayList<Map<String,String>>();
    personaService personaService;
    final ArrayList<String> productlista = new ArrayList<>();
    JSONObject jObject;
    private Context context;
    private LayoutInflater inflater;
    private List<Carrit> listpro;
    private List<Pedido> mismedidos;

    public OrdenAdapter(Context context, List<Pedido> shoppingCart) {
        this.context = context;
        this.mismedidos = shoppingCart;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mismedidos.size();
    }

    @Override
    public Object getItem(int i) {
        return mismedidos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        final Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.list_pedidos, null);

        holder.estado = rowView.findViewById(R.id.estado);
        holder.pagot = rowView.findViewById(R.id.pagot);
        holder.nro = rowView.findViewById(R.id.nro);
        holder.cancelar = rowView.findViewById(R.id.cancelar);

        int esta=Integer.valueOf(mismedidos.get(position).getEstado());
    if(esta==0){
        holder.estado.setText("En Espera");
    }else if (esta==1){
        holder.estado.setText("En Camino");
        holder.cancelar.setVisibility(View.GONE);

    }else if (esta==2){
        holder.estado.setText("Entregado");
        holder.cancelar.setVisibility(View.GONE);
        }else{
        holder.estado.setText("Cancelado");
        holder.cancelar.setVisibility(View.GONE);
    }
        holder.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String miid=String.valueOf(mismedidos.get(position).getId());

                holder.estado.setText("Cancelado");
                holder.cancelar.setVisibility(View.GONE);
                CancelarPedido(miid);

            }
        });

        holder.nro.setText("nro "+mismedidos.get(position).getId());
        holder.pagot.setText("S/ "+mismedidos.get(position).getPago());

        //Toast.makeText(context, mismedidos.get(position).getEstado(), Toast.LENGTH_SHORT).show();
        try {
            String mijson = (String) "{"+'"'+"Detalle"+'"'+":"+ mismedidos.get(position).getProductos()+"}";
            jObject = new JSONObject(mijson);
            JSONArray data  = jObject.getJSONArray("Detalle");
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String id = c.getString("cod_produc");
                String descripcion = c.getString("product");
                String cantidad = c.getString("itemQuantity");
                String portada= c.getString("portada");
                String precio = c.getString("variant");
                MiBD midb = new MiBD(context);
                int miidpro = Integer.parseInt(id);
                int micantidad= Integer.parseInt(cantidad);
                int miid= mismedidos.get(position).getId();
                double miprecio = Double.parseDouble(precio);
                midb.productorden(miid,miidpro,miprecio,micantidad,portada,descripcion);
               // String outPut = id + "-" +descripcion+ "-" +cantidad+ "-" +precio;
                //  productlist.add(createProduct("pedidosprod", outPut));
            }

        } catch (JSONException e) {
            Log.d(TAG, "mijson", e);
        }

        // Product Item Click
        holder.itemLay = rowView.findViewById(R.id.itemLaype);
        holder.itemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductoOrden.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 intent.putExtra("cod_pedi", mismedidos.get(position).getId());
                context.startActivity(intent);
            }
        });


        return rowView;
    }


    private HashMap<String, String> createProduct(String name, String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }
    public class Holder {
        RelativeLayout itemLay;
        Button cancelar;
        LinearLayout qtyLay;
        TextView estado, pagot, nro, color, tax, qty;
        ImageView remove;
    }
    public void CancelarPedido(String cod){

        personaService= Apis.getPersonaService();
        Call<Pedido> call=personaService.cancelarpedido(cod);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, "pedido Cancelado", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(context, "asdasas", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }
}
