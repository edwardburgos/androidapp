package com.preethzcodez.ecommerceexample.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.MainActivity;
import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.fragments.Productos;
import com.preethzcodez.ecommerceexample.model.Categoria;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriaAdapter extends ArrayAdapter<Categoria> {

    private Context context;
    private List<Categoria> categorias;
    public CategoriaAdapter(Context context, int resource, List<Categoria> objects) {
        super(context, resource, objects);
        this.context=context;
        this.categorias=objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=layoutInflater.inflate(R.layout.categoria_list_item,parent,false);

        holder.category = rowView.findViewById(R.id.name);
        holder.img= rowView.findViewById(R.id.image);
        holder.category.setText(categorias.get(position).getCategoria());

        final String link="http://192.168.42.63/backend/vistas/img/cabeceras/"+categorias.get(position).getRuta()+".png";
        Picasso.get()
                .load(link)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.img);

 try {

 }catch (Exception ex) {

     Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
 }

        holder.categoria = rowView.findViewById(R.id.ListCategorias);
        holder.categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catego = categorias.get(position).getCategoria();
                int id = categorias.get(position).getId();
                //Toast.makeText(getContext(), catego, Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                bundle.putString("categoria", catego);
                bundle.putInt("cat_id", id);

                bundle.putString(Constants.TITLE,categorias.get(position).getCategoria());

                Productos productos = new Productos();
                productos.setArguments(bundle);

                ft.replace(R.id.content, productos, Constants.FRAG_PDT);
                ft.addToBackStack(null);
                ft.commit();



            }
        });


        return rowView;
    }

    public class Holder {
        RelativeLayout categoria;
        TextView category;
        ImageView img;

    }
}
