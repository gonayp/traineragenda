package com.traineragenda.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.EntradaActivity;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

public class TreinerContactivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView mNombreField;

    private SeekBar seekBar;
    private TextView tv_kilometros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treiner_contactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seekBar = findViewById(R.id.seekBar);
        tv_kilometros = findViewById(R.id.tv_kilometros);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mNombreField = findViewById(R.id.fieldNombre);

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Users");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){


                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();

                DataSnapshot d = dataSnapshot.child(mAuth.getUid());
                Usuario u = d.getValue(Usuario.class);

                tv_kilometros.setText(Integer.toString(u.getRadio()/1000));
                seekBar.setProgress(u.getRadio());
                mNombreField.setText(u.getNombre());

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            int progressChangedValue = 0;
            int MIN_VALUE = 5000;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getProgress() <= MIN_VALUE){
                    seekBar.setProgress(MIN_VALUE);
                    progressChangedValue = MIN_VALUE;
                }
                else{
                    progressChangedValue = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_kilometros.setText(Integer.toString(progressChangedValue/1000));

                mAuth = FirebaseAuth.getInstance();

                FirebaseUser user = mAuth.getCurrentUser();
                //Grabar usuario
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());
                mRef.child("radio").setValue(progressChangedValue);

            }
        });

    }



    public void onActionVolver(View view) {

        Intent intent = new Intent(this, TrainerPerfilMenuActivity.class);
        startActivity(intent);

    }

    public void onActionDatosPersonales(View view) {

        Intent intent = new Intent(this, TrainerDatosPersonalesActivity.class);
        startActivity(intent);

    }

    public void onActionSalir(View view) {

        mAuth.signOut();
        Intent entrada = new Intent(this, EntradaActivity.class);
        startActivity(entrada);

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
