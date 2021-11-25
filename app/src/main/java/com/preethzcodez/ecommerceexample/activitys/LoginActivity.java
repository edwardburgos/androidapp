package com.preethzcodez.ecommerceexample.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.preethzcodez.ecommerceexample.MainActivity;
import com.preethzcodez.ecommerceexample.R;
import com.preethzcodez.ecommerceexample.database.MiBD;
import com.preethzcodez.ecommerceexample.database.SessionManager;
import com.preethzcodez.ecommerceexample.fragments.Registrar;
import com.preethzcodez.ecommerceexample.interfaces.FinishActivity;
import com.preethzcodez.ecommerceexample.model.Usuario;
import com.preethzcodez.ecommerceexample.utils.Apis;
import com.preethzcodez.ecommerceexample.utils.Constants;
import com.preethzcodez.ecommerceexample.utils.Util;
import com.preethzcodez.ecommerceexample.utils.personaService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Preeth on 1/3/2018
 */

public class LoginActivity extends AppCompatActivity implements FinishActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    Button signIn;
    ImageView cerrar;
    TextView signUp;
    Handler handler;
    String name, correo;
    TableLayout bottomLay;
    Snackbar snackbar = null;
    CoordinatorLayout coordinatorLayout;
   personaService personaService;
    EditText email, password;
    ImageView back, showpassword;
    List<Usuario>lista=new ArrayList<>();
    Usuario datos;
    boolean isPasswordShown = false;
    ListView listVieww;
    FinishActivity finishActivityCallback;
    private TextView info;
    private LoginButton loginButton;
    private ImageView img;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        loginButton = (LoginButton)findViewById(R.id.login_button);
        info = (TextView)findViewById(R.id.txtTitulo);

        callbackManager = CallbackManager.Factory.create();
        // Service To Fetch Data From URL
      //  setHandler();
       // startIntentService();

        setIds();
        setClickListeners();

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {
                                    Log.d("demo ",object.toString());

                                     name =object.getString("name");
                                     correo =object.getString("email");
                                    ingresarfb(correo);

                                   // info.setText(name +" "+ correo + " "+id);
                                    //String imgurl="https://graph.facebook.com/"+ id +"/picture?type=large";
                                    //Toast.makeText(getApplicationContext(), "ingreso "+info.getText().toString(), Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "error al ingresar", Toast.LENGTH_SHORT).show();

                                }

                            };

                        }


                );
                Bundle bundle=new Bundle();
                bundle.putString("fields","gender,name,id,email");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
                // info.setText("onSuccess" + loginResult.getAccessToken().getUserId()  );
                // Profile profile = Profile.getCurrentProfile();
                //  info.setText(profile.getFirstName() + "");
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(), "Cancelo el Login", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {

                Toast.makeText(getApplicationContext(), "error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        callbackManager.onActivityResult(requestCode , resultCode , data);

       /* GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            Log.d("demo ",object.toString());
                            String name =object.getString("name");
                            String correo =object.getString("email");
                            String id=object.getString("id");
                            info.setText(name +" "+ correo + " "+id);
                            String imgurl="https://graph.facebook.com/"+ id +"/picture?type=large";
                            Picasso.get()
                                    .load(imgurl)
                                    .resize(500,500)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_background)
                                    .into(img);

                        } catch (JSONException e) {
                         e.printStackTrace();
                        }

                    };

                }


                );
Bundle bundle=new Bundle();
bundle.putString("fields","gender,name,id,email");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();*/
    }
    AccessTokenTracker accessTokenTracker= new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            try{  if(currentAccessToken==null){
                LoginManager.getInstance().logOut();
                //info.setText("..........");
                //img.setImageResource(0);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            accessTokenTracker.stopTracking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Set Ids
    private void setIds() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signup);
        bottomLay = findViewById(R.id.bottomLay);
        cerrar = findViewById(R.id.atras);
        coordinatorLayout = findViewById(R.id.coordinatorLay);
        showpassword = findViewById(R.id.showpassword);
    }


    // Set Click Listeners
    private void setClickListeners() {


        // Sign In
        signIn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {

                // Set Values To User Model
                Usuario user = new Usuario();
                user.setEmail(email.getText().toString());
                user.setClave(password.getText().toString());

                // Validate Fields
                if (user.getEmail().trim().length() > 0) {
                    if (Util.isValidEmail(user.getEmail())) {
                        if (user.getClave().trim().length() > 0) {
                            ingresar(email.getText().toString(),password.getText().toString());
                        } else {
                            showErrorToast(getApplicationContext().getResources().getString(R.string.password));
                        }

                    } else {
                        showErrorToastEmailNotValid();
                    }
                } else {
                    showErrorToast(getApplicationContext().getResources().getString(R.string.email));
                }

            }
        });
        // Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.fragment, new Registrar());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        // Show / Hide Password
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

    public void ingresar( String mail, String clave){

        personaService= Apis.getPersonaService();
        Call<Usuario> call=personaService.ingresar(mail,clave);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.body().getEstado().equals("1")){
                    datos=response.body();
                   // listVieww.setAdapter(new UsuarioListAdapter(getApplicationContext(), lista));
                    Toast.makeText(getApplicationContext(), "ingreso", Toast.LENGTH_SHORT).show();
                    try {
                        Usuario user = new Usuario();
                        user= datos;
                        MiBD midb = new MiBD(getApplicationContext());
                         midb.registerUser(datos.getNombre(),datos.getEmail(),datos.getTelefono(),datos.getClave(),datos.getDireccion());
                        if (datos.getEmail().trim().length() > 0) {
                            // Save Session
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                            sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getClave());

                            // Load Main Activity
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            //finishActivityCallback.finishActivity();
                        } else {
                            showInvalidUser();
                        }
                    } catch (NullPointerException e) {

                        Log.d(TAG, "someOtherMethod()", e);
                        showInvalidUser();

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Cuenta Bloqueada", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error de Datos", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void ingresarfb( String mail){

        personaService= Apis.getPersonaService();
        Call<Usuario> call=personaService.verificar(mail);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.body().getEmail().equals(correo)){
                    datos=response.body();
                    Toast.makeText(getApplicationContext(), "ingreso", Toast.LENGTH_SHORT).show();
                    try {
                        Usuario user = new Usuario();
                        user= datos;
                        MiBD midb = new MiBD(getApplicationContext());
                        midb.registerUser(datos.getNombre(),datos.getEmail(),datos.getTelefono(),datos.getClave(),datos.getDireccion());
                        if (datos.getEmail().trim().length() > 0) {
                            // Save Session
                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                            sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getClave());

                            // Load Main Activity
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            //finishActivityCallback.finishActivity();
                        } else {
                            Toast.makeText(getApplicationContext(), "Paso algun error", Toast.LENGTH_SHORT).show();
                            //showInvalidUser();
                        }
                    } catch (NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "Paso algun error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "someOtherMethod()", e);
                        //showInvalidUser();
                    }



                    //Toast.makeText(getApplicationContext(), "Email ya registrado" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("nombre", name);
                datosAEnviar.putString("correo", correo);
                Registrar fragmento = new Registrar();
                fragmento.setArguments(datosAEnviar);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.fragment, fragmento);
                ft.addToBackStack(null);
                ft.commit();

            }

        });
    }


    // Check Session
    private void checkSession() {
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.getSessionData(Constants.SESSION_EMAIL) != null && sessionManager.getSessionData(Constants.SESSION_EMAIL).trim().length() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNextActivity();
                    bottomLay.setVisibility(View.VISIBLE);
                }

            }, 1000);

        } else {
            bottomLay.setVisibility(View.VISIBLE);
        }
    }

    // Load Next Activity
    private void loadNextActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }



    @Override
    public void finishActivity() {
        overridePendingTransition(0, 0);
        finish();
    }
    // Show Error Toast
    private void showErrorToast(String value) {
        Toast.makeText(getApplicationContext(), value + getResources().getString(R.string.BlankError), Toast.LENGTH_SHORT).show();
    }

    // Show Error Toast - Email Not Valid
    private void showErrorToastEmailNotValid() {
        Toast.makeText(getApplicationContext(), R.string.EmailError, Toast.LENGTH_SHORT).show();
    }
    // Show Invalid User
    private void showInvalidUser() {
        Toast.makeText(getApplicationContext(), R.string.InvalidUser, Toast.LENGTH_SHORT).show();
    }


}
