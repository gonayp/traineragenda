package com.traineragenda.Administrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.traineragenda.Planillas.ActividadesAsesoriaActivity;
import com.traineragenda.R;

public class ListadoEntrenadoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_entrenadores);
    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, MainAdministradorActivity.class);
        startActivity(intent);

    }

    //Para bloquear el boton volver del telefono
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
