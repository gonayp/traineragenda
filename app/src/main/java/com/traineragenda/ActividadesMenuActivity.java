package com.traineragenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.traineragenda.Planillas.ActividadesAsesoriaActivity;

public class ActividadesMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_menu);
    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, MainMapActivity.class);
        startActivity(intent);

    }

    public void onActionPersonal(View view) {
        Intent intent = new Intent(this, ActividadesPersonalRunnerActivity.class);
        startActivity(intent);

    }

    public void onActionClube(View view) {
        Intent intent = new Intent(this, ActividadesClubeActivity.class);
        startActivity(intent);

    }

    public void onActionAssesoria(View view) {
        Intent intent = new Intent(this, ActividadesAsesoriaActivity.class);
        startActivity(intent);

    }
}
