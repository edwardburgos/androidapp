package com.preethzcodez.ecommerceexample.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.adapters.OrdenListaAdapter;
import com.preethzcodez.ecommerceexample.adapters.ProductosAdapter;
import com.preethzcodez.ecommerceexample.interfaces.ShowBackButton;
import com.preethzcodez.ecommerceexample.interfaces.ToolbarTitle;
import com.preethzcodez.ecommerceexample.model.Deseos;
import com.preethzcodez.ecommerceexample.model.Persona;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/3/2018
 */

public class Productos extends Fragment implements SearchView.OnQueryTextListener {

    private SearchView svSearch;
    RelativeLayout sort, filter;
    TextView sortByText, buscador;
    String[] sortByArray = {"Menor Precio", "Mayor Precio","Oferta"};
    int sortById = 0, cat_id=0;
    String categ="nada" ;
    GridView productsGrid;
    List<String> sizeFilter = new ArrayList<>();
    List<String> colorFilter = new ArrayList<>();

    List<Deseos>listadeseos=new ArrayList<>();
    ListView listVieww;
    String busc=null;

    ToolbarTitle toolbarTitleCallback;
    ShowBackButton showBackButtonCallback;
    personaService personaService;
    List<Persona>listapersona=new ArrayList<>();
    ListView listView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        toolbarTitleCallback = (ToolbarTitle) context;
        showBackButtonCallback = (ShowBackButton) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.productos_list, container, false);




        setIds(view);
        initListener();
        setSortListener();
        //setFilterListener();

        // get category id
        Bundle args = getArguments();
        assert args != null;
        //Reload();
        categ = args.getString("categoria");
        cat_id=args.getInt("cat_id");


      if (cat_id>0) {
            listarproductoscat(categ);
            toolbarTitleCallback.setToolbarTitle(args.getString(Constants.TITLE));

          }else{
            listarproductos();
         }

        // Get Data and Fill Grid
       sortByText.setText(sortByArray[0]);

        svSearch.clearFocus();

        return view;

    }

  public void Reload(){
      getActivity().getSupportFragmentManager().beginTransaction().replace(Productos.this.getId(), new Productos()).commit();
  }
    // Set Ids
    private void setIds(View view) {
        sort = view.findViewById(R.id.sortLay);
        //filter = view.findViewById(R.id.filterLay);
        sortByText = view.findViewById(R.id.sortBy);
        productsGrid = view.findViewById(R.id.productsGrid);
        svSearch = view.findViewById(R.id.svSearch);
        listVieww= view.findViewById(R.id.listvieww);
    }



    // Set Sort Listener
    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.listview);

                ListView listView = dialog.findViewById(R.id.listview);
                listView.setAdapter(new OrdenListaAdapter(getActivity(), sortByArray, sortById));
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        sortById = i;
                        sortByText.setText(sortByArray[sortById]);
                       // buscador.getText();

                        if( sortByText.getText()=="Menor Precio"  && categ==null && busc!=null){
                            if(busc.length()>0){
                                buscarproductos(busc);
                            }else{
                                //Toast.makeText(getContext(), "ooo", Toast.LENGTH_SHORT).show();
                                listaxprecio("ASC");
                            }
                        }else if(sortByText.getText()=="Menor Precio" && busc==null && categ==null){
                            listaxprecio("ASC");
                        }else if(sortByText.getText()=="Mayor Precio"  && busc!=null){
                            if(busc.length()>0){
                                buscarproductosdesc(busc);
                            }else{
                               // Toast.makeText(getContext(), "22", Toast.LENGTH_SHORT).show();
                                listaxprecio("DESC");
                            }
                        }else if(sortByText.getText()=="Mayor Precio" && busc==null && categ==null){
                            listaxprecio("DESC");
                        }else if(sortByText.getText()=="Menor Precio" && categ!=null){
                            listarproductoscat(categ);
                        }else if(sortByText.getText()=="Mayor Precio" && categ!=null){
                            listarproductoscatdesc(categ);
                        }else{
                            listarproductosoferta();
                        }

                        // Reload Products List
                       // fillGridView();
                        svSearch.clearFocus();
                        dialog.dismiss();
                    }
                });
            }
        });


    }

    // Set Filter Listener
    private void initListener() {
        svSearch.setOnQueryTextListener(this);
    }
    public void listarproductos(){

        personaService= Apis.getPersonaService();
        Call<List<Persona>> call=personaService.getPersonas();
        call.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if(response.isSuccessful()){
                    listapersona=response.body();
                   // productsGrid.setAdapter(new ProductListAdapter(getActivity(), listapersona));

                    productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));
                }
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }

    public void listarproductosoferta(){

        personaService= Apis.getPersonaService();
        Call<List<Persona>> call=personaService.getListaxoferta();
        call.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if(response.isSuccessful()){
                    listapersona=response.body();
                    // productsGrid.setAdapter(new ProductListAdapter(getActivity(), listapersona));

                    productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));
                }
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }

    public void listarproductoscat(String cat){

        personaService= Apis.getPersonaService();
        Call<List<Persona>> call=personaService.getProductosCategoria(cat);
        call.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if(response.isSuccessful()){
                    listapersona=response.body();
                    productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));

                }
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }

    public void listarproductoscatdesc(String cat){

        personaService= Apis.getPersonaService();
        Call<List<Persona>> call=personaService.getProductosCategoriadesc(cat);
        call.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if(response.isSuccessful()){
                    listapersona=response.body();
                    productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));

                }
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }


    public void buscarproductos(String pro){

        personaService= Apis.getPersonaService();
        if(pro.length()>0){

            Call<List<Persona>> call=personaService.getBuscarProductos(pro);
            call.enqueue(new Callback<List<Persona>>() {
                @Override
                public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                    if(response.isSuccessful()){
                        listapersona=response.body();
                        productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));

                    }
                }

                @Override
                public void onFailure(Call<List<Persona>> call, Throwable t) {

                    Log.e("error",t.getMessage());
                }
            });
        }else{
            svSearch.clearFocus();
            Call<List<Persona>> call=personaService.getPersonas();
            call.enqueue(new Callback<List<Persona>>() {
                @Override
                public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                    if(response.isSuccessful()){
                        listapersona=response.body();
                        productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));

                    }
                }

                @Override
                public void onFailure(Call<List<Persona>> call, Throwable t) {

                    Log.e("error",t.getMessage());
                }
            });
        }


    }

    public void buscarproductosdesc(String pro){
        personaService= Apis.getPersonaService();
        if(pro.length()>0){
            Call<List<Persona>> call=personaService.getBuscarProductosdesce(pro);
            call.enqueue(new Callback<List<Persona>>() {
                @Override
                public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                    if(response.isSuccessful()){
                        listapersona=response.body();
                        productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));
                    }
                }

                @Override
                public void onFailure(Call<List<Persona>> call, Throwable t) {

                    Log.e("error",t.getMessage());
                }
            });
        }else{
            svSearch.clearFocus();
            Call<List<Persona>> call=personaService.getPersonas();
            call.enqueue(new Callback<List<Persona>>() {
                @Override
                public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                    if(response.isSuccessful()){
                        listapersona=response.body();
                        productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));

                    }
                }

                @Override
                public void onFailure(Call<List<Persona>> call, Throwable t) {

                    Log.e("error",t.getMessage());
                }
            });
        }


    }

    public void listaxprecio(String val){

        personaService= Apis.getPersonaService();
        if(val.length()>0){
            Call<List<Persona>> call=personaService.getListaxprecio(val);
            call.enqueue(new Callback<List<Persona>>() {
                @Override
                public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                    if(response.isSuccessful()){
                        listapersona=response.body();

                        productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));

                    }
                }

                @Override
                public void onFailure(Call<List<Persona>> call, Throwable t) {

                    Log.e("error",t.getMessage());
                }
            });
        }else{
            Call<List<Persona>> call=personaService.getPersonas();
            call.enqueue(new Callback<List<Persona>>() {
                @Override
                public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                    if(response.isSuccessful()){
                        listapersona=response.body();
                        productsGrid.setAdapter(new ProductosAdapter(getContext(), R.layout.productos_grid_item,listapersona));

                    }
                }

                @Override
                public void onFailure(Call<List<Persona>> call, Throwable t) {

                    Log.e("error",t.getMessage());
                }
            });
        }


    }



    public void addDeseos( Deseos d){

        personaService= Apis.getPersonaService();
        Call<Deseos> call=personaService.addDeseos(d);
        call.enqueue(new Callback<Deseos>() {
            @Override
            public void onResponse(Call<Deseos> call, Response<Deseos> response) {
                Toast.makeText(getContext(), "Se Registro", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Deseos> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        buscarproductos(newText);
        busc=newText;
        //buscador.setText(newText);
        return false;
    }
}
