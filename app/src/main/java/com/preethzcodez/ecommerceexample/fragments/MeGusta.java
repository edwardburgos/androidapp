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
import com.preethzcodez.ecommerceexample.adapters.MegustaAdapter;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.model.Deseos;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.personaService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/7/2018
 */

public class MeGusta extends Fragment {
    Deseos deseos;
    public int ver = 0;
    String userEmail = null;
   personaService personaService;
    List<Deseos>listadeseos=new ArrayList<>();
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listview, container, false);

        // get dat
        MiBD mibd = new MiBD(getActivity());
        SessionManager sessionManager = new SessionManager(getActivity());
       // List<Persona> productList = mibd.getShortListedItems(userEmail);

        // fill listview with data
         listView= view.findViewById(R.id.listview);
        //listView.setAdapter(new WishlistAdapter(getActivity(), productList));
        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);

        ListDeseos(userEmail);
        ver=0;
        return view;
    }

    public void ListDeseos(String vari){
        personaService= Apis.getPersonaService();
            Call<List<Deseos>> call=personaService.getDeseos(vari);
            call.enqueue(new Callback<List<Deseos>>() {
                @Override
                public void onResponse(Call<List<Deseos>> call, Response<List<Deseos>> response) {
                    if(response.isSuccessful()){
                        listadeseos=response.body();
                        listView.setAdapter(new MegustaAdapter(getActivity(), listadeseos));

                    }
                }

                @Override
                public void onFailure(Call<List<Deseos>> call, Throwable t) {

                    Log.e("error",t.getMessage());
                }
            });


    }
}
