package com.traineragenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.traineragenda.Usuarios.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//public class EmailLoginActivity extends AppCompatActivity {
public class EmailRegisterActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNombreField;
    private EditText mApellidoField;
    private EditText mConfirmarPasswordField;
    private EditText mCelularField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    String  tipo_usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email_register);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword_1);
        mNombreField = findViewById(R.id.fieldNombre);
        mApellidoField = findViewById(R.id.fieldApellido);
        mConfirmarPasswordField = findViewById(R.id.fieldPassword_2);
        mCelularField = findViewById(R.id.fieldCelular);
        // Buttons
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        tipo_usuario = intent.getStringExtra(EntradaregistroActivity.EXTRA_MESSAGE);


    }


    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        //Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            mStatusTextView.setText("@string/faltan_campos");//Falta alguno de los campos para registro");
            return;
        }

        showProgressDialog();

        mAuth = FirebaseAuth.getInstance();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Grabar usuario
                            FirebaseDatabase database =  FirebaseDatabase.getInstance();
                            String userId = user.getUid();
                            String email = mEmailField.getText().toString();
                            String nombre = mNombreField.getText().toString();
                            String apellido = mApellidoField.getText().toString();
                            String celular = mCelularField.getText().toString();
//                            String string = mFechaField.getText().toString();
                            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                            Date fecha_nacimiento;
//                            try {
//                                fecha_nacimiento = format.parse(string);
//                            }
//                            catch (Exception e){
                            fecha_nacimiento = new Date();
//                            }
                            Date fecha_alta = new Date();

                            LatLng recife = new LatLng(-10.94416758, -37.05300897);

                            Usuario usuario= new Usuario(userId,26, "M", email,nombre,apellido,celular,fecha_nacimiento,fecha_alta,tipo_usuario,recife.latitude,recife.longitude,5000,false,null);
                            DatabaseReference mRef =  database.getReference().child("Users").child(userId);
                            try {
                                mRef.setValue(usuario);
                            }
                            catch (Exception e){
                                Log.w("ERROR", "ERROR: cargar usuario firedatabase: "+ e.getMessage());
                            }
                            Intent intent = new Intent(EmailRegisterActivity.this, DivisorActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailRegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mStatusTextView.setText("Erro ao registrar");
//                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, EntradaActivity.class);
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

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String password2 = mConfirmarPasswordField.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            mConfirmarPasswordField.setError("Required.");
            valid = false;
        } else {
            mConfirmarPasswordField.setError(null);
        }

		if(valid) {
				//Requisitos de contraseña
//				if (txt_pass.length() < 6) {
//                    mPasswordField.setError("Required.");
//					Toast.makeText(RegisterActivity.this, "La contraseña tiene que tener mas de 5 caracteres.", Toast.LENGTH_SHORT).show();
//					valid = false;
//				} else {
//					password.setError(null);
//				}

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

//    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
//        if (user != null) {
//            mStatusTextView.setText("Test");
//            //getString(R.string.emailpassword_status_fmt,                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText("Test2");
//            //getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.emailSignInButton).setVisibility(View.GONE);
//            findViewById(R.id.fieldPassword).setVisibility(View.GONE);
//            findViewById(R.id.emailCreateAccountButton).setVisibility(View.GONE);
//            findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
//        } else {
//            mStatusTextView.setText(R.string.action_salir);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.emailSignInButton).setVisibility(View.VISIBLE);
//            findViewById(R.id.fieldPassword).setVisibility(View.VISIBLE);
//            findViewById(R.id.emailCreateAccountButton).setVisibility(View.VISIBLE);
//            findViewById(R.id.signOutButton).setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailCreateAccountButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }



    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.cargando));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
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
