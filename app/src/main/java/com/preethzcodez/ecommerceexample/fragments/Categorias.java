package com.preethzcodez.ecommerceexample.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.preethzcodez.ecommerceexample.R;

import com.preethzcodez.ecommerceexample.adapters.CategoriaAdapter;
import com.preethzcodez.ecommerceexample.model.Categoria;


import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.personaService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/3/2018
 */

public class Categorias extends Fragment {
    List<Categoria>listacategorias=new ArrayList<>();
    personaService personaService;
    ListView listView;
    List<Categoria> categoryList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listview, container, false);

        // load products
      //  DB_Handler db_handler = new DB_Handler(getActivity());
     // categoryList = db_handler.getCategoryList();

        // fill listview with data
        listView= view.findViewById(R.id.listview);
      //  listView.setAdapter(new CategoryListAdapter(getActivity(), categoryList));
        listarcategorias();
        return view;
    }
    public void listarcategorias(){

        personaService= Apis.getPersonaService();
        Call<List<Categoria>> call=personaService.getCategorias();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful()){
                    categoryList=response.body();
                    listView.setAdapter(new CategoriaAdapter(getContext(), R.layout.categoria_grid_item,categoryList));
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }
}
