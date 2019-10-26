package com.example.s2appcontable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String ruta = "http://scontable.somee.com/Token";
    private Map<String, String> parametros;
    public LoginRequest(String email, String password, Response.Listener<String> listener){
        super(Request.Method.POST, ruta, listener,null);
        parametros = new HashMap<>();
        parametros.put("username",email+"");
        parametros.put("password",password+"");
        parametros.put("grant_type","password");
    }


    @Override
    protected Map<String, String> getParams(){
        return parametros;
    }
}
