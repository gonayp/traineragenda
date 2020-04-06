package com.traineragenda.Planillas.Anamnese;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.traineragenda.Planillas.ActividadesAsesoriaActivity;
import com.traineragenda.R;

public class AnamneseActivity1 extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnese1);


        aSwitch = findViewById(R.id.switch1);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ActividadesAsesoriaActivity.class);
        startActivity(intent);

    }


    public void onActionRegistrar(View view) {


        if(aSwitch.isChecked()) {

            Intent intent = new Intent(this, AnamneseActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else{
            Toast.makeText(this,"Você não pode se registrar se não aceita o consentimento.",Toast.LENGTH_SHORT).show();
        }

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
