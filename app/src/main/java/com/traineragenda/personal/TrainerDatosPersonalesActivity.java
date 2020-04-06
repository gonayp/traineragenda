package com.traineragenda.personal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;
import com.traineragenda.otros.DatePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TrainerDatosPersonalesActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;


    private EditText mPasswordField;
    private EditText mNombreField;
    private EditText mApellidoField;
    private EditText mConfirmarPasswordField;
    private EditText mCelularField;
    private TextView mStatusTextView;
    EditText etPlannedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_datos_personales);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mPasswordField = findViewById(R.id.fieldPassword_1);
        mNombreField = findViewById(R.id.fieldNombre);
        mApellidoField = findViewById(R.id.fieldApellido);
        mConfirmarPasswordField = findViewById(R.id.fieldPassword_2);
        mCelularField = findViewById(R.id.fieldCelular);

        //Combo para fecha
        etPlannedDate = (EditText) findViewById(R.id.etPlannedDate);
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                switch (view.getId()) {
                    case R.id.etPlannedDate:
                        showDatePickerDialog();
                        break;
                }
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
                mCelularField.setText(u.getCelular());
                mPasswordField.setText("");
                mConfirmarPasswordField.setText("");
                Date nacimiento = u.getNacimiento();
                etPlannedDate.setText(nacimiento.getDate()+"/"+(nacimiento.getMonth()+1)+"/"+nacimiento.getYear());

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + "/" + (month+1) + "/" + year;
                etPlannedDate.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }




    public void onActionVolver(View view) {

        Intent intent = new Intent(this, TreinerContactivity.class);
        startActivity(intent);

    }

    public void onActionGuardar(View view) {

        updateAccount();

        Intent intent = new Intent(TrainerDatosPersonalesActivity.this, TreinerContactivity.class);
        startActivity(intent);

    }

    private boolean validateForm() {
        boolean valid = true;


        String nombre = mNombreField.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            mNombreField.setError("Required.");
            valid = false;
        } else {
            mNombreField.setError(null);
        }


        String password = mPasswordField.getText().toString();
        String password2 = mConfirmarPasswordField.getText().toString();
        if (!TextUtils.isEmpty(password) || !TextUtils.isEmpty(password2)){
            if (TextUtils.isEmpty(password)) {
                mPasswordField.setError("Required.");
                valid = false;
            } else {
                mPasswordField.setError(null);
            }


            if (TextUtils.isEmpty(password2)) {
                mConfirmarPasswordField.setError("Required.");
                valid = false;
            } else {
                mConfirmarPasswordField.setError(null);
            }
            //Contraseñas iguales
            if(!password.equals(password2)){
                mPasswordField.setError("Required.");
                mConfirmarPasswordField.setError("Required.");
                valid = false;
            }
            else{
                mPasswordField.setError(null);
                mConfirmarPasswordField.setError(null);
            }
        }



        String celular = mCelularField.getText().toString();
        if (TextUtils.isEmpty(celular)) {
            mCelularField.setError("Required.");
            valid = false;
        } else {
            mCelularField.setError(null);
        }


        return valid;
    }

    private void updateAccount() {
        //Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            mStatusTextView.setText("@string/faltan_campos");//Falta alguno de los campos para registro");
            return;
        }


        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();
        //Grabar usuario
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = user.getUid();
        String nombre = mNombreField.getText().toString();
        String apellido = mApellidoField.getText().toString();
        String celular = mCelularField.getText().toString();
        String string = etPlannedDate.getText().toString();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date fecha_nacimiento;
                            try {
                                fecha_nacimiento = format.parse(string);
                            }
                            catch (Exception e){
                                fecha_nacimiento = new Date();
                            }
        Date fecha_alta = new Date();


        //Usuario usuario= new Usuario(26, "M", email,nombre,apellido,celular,fecha_nacimiento,fecha_alta,tipo_usuario);
        DatabaseReference mRef = database.getReference().child("Users").child(userId);
        mRef.child("nombre").setValue(nombre);
        mRef.child("apellido").setValue(apellido);
        mRef.child("celular").setValue(celular);
        mRef.child("nacimiento").setValue(fecha_nacimiento);



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
