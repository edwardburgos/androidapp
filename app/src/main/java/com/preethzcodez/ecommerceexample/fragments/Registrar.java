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
import com.preethzcodez.ecommerceexample.activitys.LoginActivity;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.interfaces.FinishActivity;
import com.preethzcodez.ecommerceexample.model.Usuario;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.Util;
import com.preethzcodez.ecommerceexample.utils.personaService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/6/2018
 */

public class Registrar extends Fragment {
    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText name, email, password, mobile,dni,direccion;
    TextView txtclave;
    Button signUp;
    Usuario datos;
    RelativeLayout tv;
    String nom="";
    ImageView back, showpassword;
    boolean isPasswordShown = false;
    FinishActivity finishActivityCallback;
    personaService personaService;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.registrar, container, false);

        setIds(view);
        setClickListeners();

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {

        }

try{

     nom = getArguments().getString("nombre");
    String correo = getArguments().getString("correo");
    // Imprimimos, pero en tu caso haz lo necesario
        Log.d("SignUp", "El nombre: " + nom);
        if(nom.length()>2){
        name.setText(nom);
        email.setText(correo);
        txtclave.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
}


} catch (NullPointerException e) {

    }


        return view;
    }

    // Set Ids
    private void setIds(View view) {
        name = view.findViewById(R.id.name);
        dni = view.findViewById(R.id.dni);
        direccion = view.findViewById(R.id.direccion);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        mobile = view.findViewById(R.id.mobile);
        signUp = view.findViewById(R.id.signup);
        back = view.findViewById(R.id.back);
        txtclave = view.findViewById(R.id.txtclave);
        showpassword = view.findViewById(R.id.showpassword);
        tv = (RelativeLayout) view.findViewById(R.id.relaclave);

    }

    public void addUsuario( Usuario u){

        personaService= Apis.getPersonaService();
        Call<Usuario> call=personaService.addUsuario(u);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                response.body().getClave();
                    Toast.makeText(getContext(), "Se Registro", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

                Log.e("error",t.getMessage());
            }
        });
    }

    public void ingresar( String mail){

        personaService= Apis.getPersonaService();
        Call<Usuario> call=personaService.verificar(mail);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.body().getEmail().equals(email.getText().toString())){
                    Toast.makeText(getContext(), "Email ya registrado" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

                // Set Values To User Model
                Usuario user = new Usuario();
                user.setNombre(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setTelefono(mobile.getText().toString());
                user.setClave(password.getText().toString());
                user.setDireccion(direccion.getText().toString());
                // Register User
                MiBD midb = new MiBD(getActivity());
                    if(nom.length()>2){
                        user.setClave("null");
                        try   {
                        Date date = new Date();
                        DateFormat horas = new SimpleDateFormat("h:mm:s k");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String hora = horas.format(date);
                        String fecha = dateFormat.format(date);
                        Usuario u=new Usuario();
                        u.setNombre(name.getText().toString());
                        u.setDocumento(dni.getText().toString());
                        u.setRuc("0");
                        u.setEmail(email.getText().toString());
                        u.setTelefono(mobile.getText().toString());
                        u.setDireccion(direccion.getText().toString());
                        u.setClave("null");
                        u.setFecha(fecha+" "+hora);
                        u.setModo("facebook");
                        u.setTipocliente("usuario");
                        u.setEstado("1");
                        addUsuario(u);

                        long isInserted = midb.registerUser(user.getNombre(), user.getEmail(), user.getTelefono(),"null",user.getDireccion());
                        if (isInserted != -1) {
                            // Save Session
                            SessionManager sessionManager = new SessionManager(getActivity());
                            sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                            sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getClave());

                            // Load Main Activity
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            finishActivityCallback.finishActivity();
                        } else {
                            showErrorToastEmailExists();
                        }
                    } catch (NullPointerException e) {
                    Log.d(TAG, "someOtherMethod()", e);
                }
                    }else{
                        try   {
                    Date date = new Date();
                    DateFormat horas = new SimpleDateFormat("h:mm:s k");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String hora = horas.format(date);
                    String fecha = dateFormat.format(date);
                    Usuario u=new Usuario();
                    u.setNombre(name.getText().toString());
                    u.setDocumento(dni.getText().toString());
                    u.setRuc("0");
                    u.setEmail(email.getText().toString());
                    u.setTelefono(mobile.getText().toString());
                    u.setDireccion(direccion.getText().toString());
                    u.setClave(password.getText().toString());
                    u.setFecha(fecha+" "+hora);
                    u.setModo("directo");
                    u.setTipocliente("usuario");
                            u.setEstado("1");
                    addUsuario(u);


                    long isInserted = midb.registerUser(user.getNombre(), user.getEmail(), user.getTelefono(), user.getClave(),user.getDireccion());
                    if (isInserted != -1) {
                        // Save Session
                        SessionManager sessionManager = new SessionManager(getActivity());
                        sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                        sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getClave());

                        // Load Main Activity
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        finishActivityCallback.finishActivity();
                    } else {
                        showErrorToastEmailExists();
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, "someOtherMethod()", e);
                }
                    }
            }

        });
    }

    // Set Click Listeners
    private void setClickListeners() {
        // Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Set Values To User Model
                Usuario user = new Usuario();
                user.setNombre(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setTelefono(mobile.getText().toString());
                user.setClave(password.getText().toString());


                // Validate Fields
                if (user.getNombre().trim().length() > 0) {
                    if (user.getEmail().trim().length() > 0) {
                        if (Util.isValidEmail(user.getEmail())) {
                            if (user.getEmail().trim().length() > 0) {
                                if (user.getEmail().trim().length() > 0) {
                                    ingresar(email.getText().toString());
                                } else {
                                    showErrorToast(getActivity().getResources().getString(R.string.password));
                                }
                            } else {
                                showErrorToast(getActivity().getResources().getString(R.string.mobile));
                            }
                        } else {
                            showErrorToastEmailNotValid();
                        }
                    } else {
                        showErrorToast(getActivity().getResources().getString(R.string.email));
                    }
                } else {
                    showErrorToast(getActivity().getResources().getString(R.string.name));
                }
            }
        });

        // Back Button Click
        back.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                ft.replace(R.id.fragment, new BlankFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // Show Password
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordShown) {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    showpassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    isPasswordShown = false;
                } else {
                    password.setTransformationMethod(null);
                    showpassword.setImageResource(R.drawable.ic_eye_white_24dp);
                    isPasswordShown = true;
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

    // Show Error Toast - Email Exists
    private void showErrorToastEmailExists() {
        Toast.makeText(getActivity(), R.string.EmailExistsError, Toast.LENGTH_SHORT).show();
    }
}
