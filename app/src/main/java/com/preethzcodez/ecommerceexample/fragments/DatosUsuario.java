package com.preethzcodez.ecommerceexample.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.preethzcodez.ecommerceexample.MainActivity;
import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.activitys.Ordenes;
import com.preethzcodez.ecommerceexample.activitys.LoginActivity;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.interfaces.FinishActivity;
import com.preethzcodez.ecommerceexample.model.Usuario;

import com.preethzcodez.ecommerceexample.utils.Constants;

/**
 * Created by Preeth on 1/6/2018
 */

public class DatosUsuario extends Fragment {
    Usuario user;
    MiBD midb;
    TextView name,email,logout,  mobile, direc,cambiopass;
    RelativeLayout orders, logoutLay;
    FinishActivity finishActivityCallback;

    String userEmail = null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.datosusuario, container, false);

        // Get User
        midb = new MiBD(getActivity());
        SessionManager sessionManager = new SessionManager(getActivity());
      user= midb.getUser(sessionManager.getSessionData(Constants.SESSION_EMAIL),sessionManager.getSessionData(Constants.SESSION_PASSWORD));

        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);
      //  Toast.makeText(getContext(), sessionManager.getSessionData(Constants.SESSION_PASSWORD), Toast.LENGTH_SHORT).show();



        // Set Values
        setIds(view);
        setValues(user);
        setClickListeners();
        if(userEmail!=""){
            orders.setVisibility(View.VISIBLE);
            cambiopass.setVisibility(View.VISIBLE);
            logout.setText("Cerrar Sesion");
        }else{
            orders.setVisibility(View.GONE);
            cambiopass.setVisibility(View.GONE);
            logout.setText("Iniciar Sesion");
        }

        cambiopass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nuevoFragmento = new CambiarClav();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content, nuevoFragmento);
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();
            }
        });
        return view;
    }

    // Set Ids
    private void setIds(View view) {
        logoutLay = view.findViewById(R.id.logoutLay);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        direc=view.findViewById(R.id.direccion);
        mobile = view.findViewById(R.id.mobile);
        orders = view.findViewById(R.id.myOrdersLay);
        logout = view.findViewById(R.id.logout);
        cambiopass= view.findViewById(R.id.cambiopass);
    }

    // Set Values
    private void setValues(Usuario user) {
        // Name
        name.setText(user.getNombre());

        direc.setText(user.getDireccion());
        // Email
        email.setText(user.getEmail());

        // Mobile
        mobile.setText(user.getTelefono());
    }

    // Set Click Listeners
    private void setClickListeners() {
        // My Orders
        orders.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Ordenes.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });

        // Logout
        logoutLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(logout.getText()=="Iniciar Sesion"){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);


                    // Crear fragmento de tu clase
                  /*  Fragment fragment = new SignIn();
// Obtener el administrador de fragmentos a través de la actividad
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
// Definir una transacción
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
// Remplazar el contenido principal por el fragmento
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.addToBackStack(null);
// Cambiar
                    fragmentTransaction.commit();*/
                }else{
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.clearPreferences();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishActivityCallback.finishActivity();
                }


            }
        });
    }


}
