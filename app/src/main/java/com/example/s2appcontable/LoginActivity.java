package com.example.s2appcontable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Switch switchRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindIU();


        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExist();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();
                Response.Listener<String> respuesta= new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // return a succes if it's ok or fake
                        try {
                            JSONObject jsonRespuesta = new JSONObject(response);
                            String ok = jsonRespuesta.getString("access_token");


                            if (!TextUtils.isEmpty(ok)){
                                Toast.makeText(LoginActivity.this,"sI Token",Toast.LENGTH_LONG).show();

                                String email = jsonRespuesta.getString("userName");
                                String password = jsonRespuesta.getString("access_token");
                                Intent bienvenido = new Intent(LoginActivity.this, Home.class);
                                bienvenido.putExtra("email", email);
                                bienvenido.putExtra("password", password);

                                Toast.makeText(LoginActivity.this,"Paso",Toast.LENGTH_LONG).show();

                                LoginActivity.this.startActivity(bienvenido);
                             //   saveOnPreferences(email,password);
                                LoginActivity.this.finish();
                            }else{
                                AlertDialog.Builder alerta = new AlertDialog.Builder(LoginActivity.this);
                                alerta.setMessage("Fallo el Login")
                                        .setNegativeButton("Reintentar",null)
                                        .create()
                                        .show();
                            }
                        }catch (JSONException e){
                            e.getMessage();
                        }
                    }
                };
                LoginRequest l = new LoginRequest(email,password, respuesta);
                RequestQueue cola = Volley.newRequestQueue(LoginActivity.this);
                cola.add(l);
            }
        });
    }

    private void setCredentialsIfExist() {
        String email = getUserMailPrefs();
        String password = getPassPrefs();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            editTextEmail.setText(email);
            editTextPassword.setText(password);
        }
    }

    private void bindIU() {
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        switchRemember = (Switch) findViewById(R.id.switchRemember);
    }

    private void saveOnPreferences(String email, String password){
        if (switchRemember.isChecked()){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email",email);
            editor.putString("pass",password);
            editor.commit();
            editor.apply();
        }
    }

    private String getUserMailPrefs(){
        return prefs.getString("email","");
    }

    private String getPassPrefs(){
        return prefs.getString("pass","");
    }

}
