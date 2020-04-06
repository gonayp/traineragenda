package com.traineragenda.Planillas.Anamnese;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AnamneseActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ToggleButton aSwitch;
    EditText etPlannedDate;
    private TextView mNombreField;
    private TextView mApellidoField;

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnese2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        aSwitch = findViewById(R.id.switch1);

        mNombreField = findViewById(R.id.fieldNombre);
        mApellidoField = findViewById(R.id.fieldApellido);

        etPlannedDate = (EditText) findViewById(R.id.et_fecha);
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AnamneseActivity2.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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

                mNombreField.setText(u.getNombre());
                mApellidoField.setText(u.getApellido());
                if(u.getNacimiento() != null) {

                    calendario.set(Calendar.YEAR, u.getNacimiento().getYear());
                    calendario.set(Calendar.MONTH, u.getNacimiento().getMonth());
                    calendario.set(Calendar.DAY_OF_MONTH, u.getNacimiento().getDate());
                    actualizarInput();

                }

                if(u.getGender()!= null) {
                    if (u.getGender().equals("F")) {
                        aSwitch.setChecked(false);
                    } else {
                        aSwitch.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etPlannedDate.setText(sdf.format(calendario.getTime()));
    }



    public void onActionVolver(View view) {
        Intent intent = new Intent(this, AnamneseActivity1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void onActionRegistrar(View view) {

        String nombre = mNombreField.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            mNombreField.setError("Required.");
            return;
        } else {
            mNombreField.setError(null);
        }

        String apellido = mApellidoField.getText().toString();
        if (TextUtils.isEmpty(apellido)) {
            mApellidoField.setError("Required.");
            return;
        } else {
            mApellidoField.setError(null);
        }




        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        //Grabar usuario
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());
        if(aSwitch.isChecked()){
            mRef.child("gender").setValue("M");
        }
        else{
            mRef.child("gender").setValue("F");
        }

        mRef.child("nacimiento").setValue(calendario.getTime());
        mRef.child("nombre").setValue(mNombreField.getText().toString());
        mRef.child("apellido").setValue(mApellidoField.getText().toString());


        Intent intent = new Intent(this, AnamneseActivity3.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


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
