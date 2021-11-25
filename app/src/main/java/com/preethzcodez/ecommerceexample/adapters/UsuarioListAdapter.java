package com.preethzcodez.ecommerceexample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.preethzcodez.ecommerceexample.R;

import com.preethzcodez.ecommerceexample.model.Usuario;


import java.util.List;

/**
 * Created by Preeth on 1/4/18
 */

public class UsuarioListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Usuario> productList;

    public UsuarioListAdapter(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.productList = usuarioList;
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
        final Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.datosusuario, null);
        holder.name = rowView.findViewById(R.id.name);
        holder.mobile = rowView.findViewById(R.id.price);
        holder.heart = rowView.findViewById(R.id.heart);

        holder.name.setText(productList.get(position).getNombre());
        holder.email.setText(productList.get(position).getEmail());
        holder.mobile.setText(productList.get(position).getTelefono());


        return rowView;
    }

    public class Holder {
        RelativeLayout itemLay;
        TextView name, mobile,email;
        ImageView heart;
    }
}
