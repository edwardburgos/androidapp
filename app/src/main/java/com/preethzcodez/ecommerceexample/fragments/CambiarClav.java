package com.preethzcodez.ecommerceexample.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.preethzcodez.ecommerceexample.MainActivity;
import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.interfaces.FinishActivity;
import com.preethzcodez.ecommerceexample.model.Usuario;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.Util;
import com.preethzcodez.ecommerceexample.utils.personaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/6/2018
 */

public class CambiarClav extends Fragment {
    RelativeLayout orders, logoutLay;
    Button signIn;
    TextView cambiarclave;
    EditText passantigua, passwordnueva;
    ImageView back, showpassword, showpassword2;
    boolean isPasswordShown = false;
    boolean isPasswordShown2 = false;
    String userEmail = null;
    personaService personaService;
    FinishActivity finishActivityCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cambiopass, container, false);

        setIds(view);
          setClickListeners();

        return view;
    }

    // Set Ids
    private void setIds(View view) {
        passantigua = view.findViewById(R.id.passantigua);
        passwordnueva = view.findViewById(R.id.passwordnueva);
        cambiarclave = view.findViewById(R.id.cambiarclave);
        showpassword2 = view.findViewById(R.id.showpassword2);
        showpassword = view.findViewById(R.id.showpassword);
        logoutLay= view.findViewById(R.id.logoutLa);

    }

    // Set Click Listeners
    private void setClickListeners() {
        // Sign In
        logoutLay.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {

                // Set Values To User Model
                Usuario user = new Usuario();

                // Validate Fields
                if (passantigua.getText().toString().trim().length() > 0 && passwordnueva.getText().toString().trim().length() > 0) {
                    if (passantigua.getText().toString().trim().equals(passwordnueva.getText().toString().trim())) {
                        SessionManager sessionManager = new SessionManager(getActivity());
                        userEmail=sessionManager.getSessionData(Constants.SESSION_EMAIL);
                            cambiar(userEmail, passantigua.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Clave no Coinciden", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "llene todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });




        // Show / Hide Password
        showpassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordShown) {
                    passantigua.setTransformationMethod(new PasswordTransformationMethod());
                    showpassword2.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    isPasswordShown = false;
                } else {
                    passantigua.setTransformationMethod(null);
                    showpassword2.setImageResource(R.drawable.ic_eye_white_24dp);
                    isPasswordShown = true;
                }
            }
        });
        // Show / Hide Password
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordShown2) {
                    passwordnueva.setTransformationMethod(new PasswordTransformationMethod());
                    showpassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    isPasswordShown2 = false;
                } else {
                    passwordnueva.setTransformationMethod(null);
                    showpassword.setImageResource(R.drawable.ic_eye_white_24dp);
                    isPasswordShown2 = true;
                }
            }
        });


    }

    // Show Error Toast
    private void showErrorToast(String value) {
        Toast.makeText(getActivity(), value + getResources().getString(R.string.BlankError), Toast.LENGTH_SHORT).show();
    }

    // Show Error Toast - Email Not Valid
    private void showErrorToastEmailNotValid() {
        Toast.makeText(getActivity(), R.string.EmailError, Toast.LENGTH_SHORT).show();
    }

    // Show Invalid User
    private void showInvalidUser() {
        Toast.makeText(getActivity(), R.string.InvalidUser, Toast.LENGTH_SHORT).show();
    }

    public void cambiar(String mail, String nclave) {

        personaService = Apis.getPersonaService();
        Call<Usuario> call = personaService.cambiarpass(mail, nclave);
        call.enqueue(new Callback<Usuario>() {
                         @Override
                         public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                             Toast.makeText(getActivity(), "Se Cambio la Clave", Toast.LENGTH_SHORT).show();
                             Fragment nuevoFragmento = new DatosUsuario();
                             FragmentTransaction transaction = getFragmentManager().beginTransaction();
                             transaction.replace(R.id.content, nuevoFragmento);
                             transaction.addToBackStack(null);

                             // Commit a la transacción
                             transaction.commit();

                         }

                         @Override
                         public void onFailure(Call<Usuario> call, Throwable t) {
                             Toast.makeText(getActivity(), "Error al Cambiar Clave", Toast.LENGTH_SHORT).show();
                         }


                     }

        );
    }
}
