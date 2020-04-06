package com.traineragenda.Planillas;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Administrador.ListaPlanillasXUsuarioActivity;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.Planillas.ConfeccionarPlanilla.ConfeccionarPlanillaActivity4;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.io.File;

public class VerPlanillaPDFActivity extends AppCompatActivity {

    private Long planilla_id;
    private String usuario_id;
    private String usuario_actual;
    private String tipo;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_planilla);

        tipo = "CLIENTE";

        mAuth = FirebaseAuth.getInstance();
        usuario_actual = mAuth.getUid();


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id", 0);
        usuario_id = intent.getStringExtra("mesg_usuario_id");

        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/planilla_"+planilla_id+".pdf";
        //pdfView.fromAsset(directory_path+"test-2.pdf").load();
        File file = new File(directory_path);
        pdfView.fromFile(file).load();

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Users");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();

                DataSnapshot d = dataSnapshot.child(usuario_actual);
                Usuario u = d.getValue(Usuario.class);

                if(u.getTipo().equals("admin")){
                    tipo = "ADMIN";
                }
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }

    public void onActionVolver(View view) {
        if(tipo.equals("ADMIN")) {
            Intent intent = new Intent(this, ListaPlanillasXUsuarioActivity.class);
            intent.putExtra("mesg_usuario_id", usuario_id);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ListadoPlanillasActivity.class);
            startActivity(intent);
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
