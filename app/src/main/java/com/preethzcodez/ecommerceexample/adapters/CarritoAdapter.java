package com.preethzcodez.ecommerceexample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.preethzcodez.ecommerceexample.model.Carrit;
import com.preethzcodez.ecommerceexample.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Preeth on 1/7/2018
 */

public class CarritoAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Carrit> shoppingCart;

    // interface to update payable amount
    public interface UpdatePayableAmount {
        void updatePayableAmount(List<Carrit> shoppingCart);
    }

    // inteface to finish activity if cart empty
    public interface MonitorListItems {
        void finishActivity(List<Carrit> shoppingCart);
    }

    public CarritoAdapter(Context context, List<Carrit> shoppingCart) {
        this.context = context;
        this.shoppingCart = shoppingCart;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shoppingCart.size();
    }

    @Override
    public Object getItem(int i) {
        return shoppingCart.get(i);
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

        rowView = inflater.inflate(R.layout.carrito_item, null);
        holder.title = rowView.findViewById(R.id.title);
        holder.price = rowView.findViewById(R.id.price);
        holder.qty = rowView.findViewById(R.id.quantityValue);
        holder.remove = rowView.findViewById(R.id.remove);
        holder.minus = rowView.findViewById(R.id.minus);
        holder.plus = rowView.findViewById(R.id.plus);
        ImageView imagenV=(ImageView) rowView.findViewById(R.id.img);


        holder.title.setText(shoppingCart.get(position).getProduct());


        // Calculate Price Value
        final int[] quantity = {shoppingCart.get(position).getItemQuantity()};

        Picasso.get()
                .load(shoppingCart.get(position).getPortada())
                .resize(200,200)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(imagenV);

        final Double priceValue = Double.valueOf(shoppingCart.get(position).getVariant());

        holder.qty.setText(String.valueOf(quantity[0]));
        holder.price.setText("S/." + Util.formatDouble(calculatePrice( priceValue, quantity[0])));


        final String link=shoppingCart.get(position).getPortada();

        // Product Item Click
        holder.itemLay = rowView.findViewById(R.id.itemLay);
        holder.itemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context, String.valueOf(shoppingCart.get(position).getVariant()), Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(context, ProductoDetalle.class);
                intent.putExtra("ProductId",String.valueOf(shoppingCart.get(position).getCod_produc()));
               intent.putExtra("Precio",String.valueOf(shoppingCart.get(position).getVariant()));
                intent.putExtra("Titulo",String.valueOf(shoppingCart.get(position).getProduct()));
                 intent.putExtra("Protada",String.valueOf(link));
                context.startActivity(intent);
            }
        });

        // Remove Item
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Delete Item From DB
                MiBD mibd = new MiBD(context);
                if (mibd.deleteCartItem(shoppingCart.get(position).getCod_produc())) {
                    shoppingCart.remove(position);
                    notifyDataSetChanged();

                    if (context instanceof UpdatePayableAmount) {
                        ((UpdatePayableAmount) context).updatePayableAmount(shoppingCart);
                    }

                    if (context instanceof MonitorListItems) {
                        ((MonitorListItems) context).finishActivity(shoppingCart);
                    }
                } else {
                    Toast.makeText(context, "error al Eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Quantity Decrement Listener
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity[0] != 1) {
                    quantity[0]--;
                    updateQuantity(quantity[0],position); // update in DB
                    shoppingCart.get(position).setItemQuantity(quantity[0]);
                    holder.qty.setText(String.valueOf(quantity[0]));
                    holder.price.setText("S/." + Util.formatDouble(calculatePrice( priceValue, quantity[0])));

                    if (context instanceof UpdatePayableAmount) {
                        ((UpdatePayableAmount) context).updatePayableAmount(shoppingCart);
                    }
                }
            }
        });

        // Quantity Increment Listener
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity[0]++;
                Integer asd=shoppingCart.get(position).getCod_produc();


               updateQuantity(quantity[0],position); // update in DB
                shoppingCart.get(position).setItemQuantity(quantity[0]);
                holder.qty.setText(String.valueOf(quantity[0]));
                holder.price.setText("S/." + Util.formatDouble(calculatePrice( priceValue, quantity[0])));

                if (context instanceof UpdatePayableAmount) {
                    ((UpdatePayableAmount) context).updatePayableAmount(shoppingCart);
                }
            }
        });

        return rowView;
    }

    private Double calculatePrice(Double taxValue, int quantity) {
        return taxValue  * quantity;
    }

    // Update Quantity In DB
    private void updateQuantity(int quantity, int position) {
      MiBD midb = new MiBD(context);
        midb.updateItemQuantity(quantity, shoppingCart.get(position).getCod_produc());
       // Integer asd=shoppingCart.get(position).getCod_produc();
        //Toast.makeText(context, String.valueOf(asd), Toast.LENGTH_SHORT).show();
    }

    public class Holder {
        RelativeLayout itemLay;
        TextView title, price, qty;
        ImageView remove, minus, plus;
    }
}
