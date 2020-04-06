package com.traineragenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class EntradaregistroActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "Tipo_registro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradaregistro);
    }

    /** Called when the user taps the Send button */
    public void onActionRegistroCliente(View view) {
        Intent intent = new Intent(this, EmailRegisterActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        String message = "cliente";//editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
    /** Called when the user taps the Send button */
    public void onActionRegistroTrainer(View view) {
        Intent intent = new Intent(this, EmailRegisterActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        String message = "trainer";//editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }


    /** Called when the user taps the Send button */
    public void onActionVolver(View view) {
        Intent intent = new Intent(this, EntradaActivity.class);
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
