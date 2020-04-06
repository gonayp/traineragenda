package com.traineragenda.Administrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.traineragenda.EntradaActivity;
import com.traineragenda.Planillas.ActividadesAsesoriaActivity;
import com.traineragenda.R;

public class MainAdministradorActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);

        mAuth = FirebaseAuth.getInstance();

    }

    //Para bloquear el boton volver del telefono
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onActionSalir(View view) {

        mAuth.signOut();
        Intent entrada = new Intent(this, EntradaActivity.class);
        startActivity(entrada);

    }

    public void onActionPlanillas(View view) {
        Intent intent = new Intent(this, ClientesPlanillasActivity.class);
        startActivity(intent);

    }

    public void onActionEntrenadores(View view) {
        Intent intent = new Intent(this, ListadoEntrenadoresActivity.class);
        startActivity(intent);

    }

}
