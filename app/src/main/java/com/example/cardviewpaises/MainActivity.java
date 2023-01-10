package com.example.cardviewpaises;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import Adapter.PaisAdaptador;
import Models.Pais;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerPais = (RecyclerView) findViewById(R.id.recyclerPaises);
        recyclerPais.setHasFixedSize(true);
        recyclerPais.setLayoutManager(new LinearLayoutManager(this));
        recyclerPais.setItemAnimator(new DefaultItemAnimator());

        Bundle bundle = this.getIntent().getExtras();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.geognos.com/api/en/countries/info/all.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Pais> paises = new ArrayList<Pais> ();
                        try {
                            JSONObject JSONOBJECTpais = new JSONObject(response);
                            JSONObject JSONOBJECTpais2 = JSONOBJECTpais.getJSONObject("Results");
                            Iterator < String > codigosPaises = JSONOBJECTpais2.keys();
                            while (codigosPaises.hasNext())
                                paises.add(new Pais(JSONOBJECTpais2.getJSONObject(codigosPaises.next())));
                            int resId = R.anim.layout_animation_down_to_up;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(),
                                    resId);
                            recyclerPais.setLayoutAnimation(animation);
                            PaisAdaptador adapatorPais = new PaisAdaptador(getApplicationContext(), paises);
                            recyclerPais.setAdapter(adapatorPais);

                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}