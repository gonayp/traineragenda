package com.traineragenda.personal;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.Compromisos.CompromisoActivity;
import com.traineragenda.EntradaActivity;
import com.traineragenda.MapFragment;
import com.traineragenda.PermissionUtils;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class TrainerMenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    MapFragment fragment;


    private NotificationManagerCompat mNotificationManagerCompat;
    private DrawerLayout mMainRelativeLayout;
    private Long id_comp_notificado = 0L;


    Button btn_estado;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_menu);

        //Notificaciones
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        mMainRelativeLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_perfil);
        btn_estado = (Button) findViewById(R.id.btn_estado);
        btn_estado.setText("ONLINE");



        // mapa de google
        fragment = new MapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        enableMyLocation();



        //Estado del treiner
        String usuario = mAuth.getUid();
        // Since I can connect from multiple devices, we store each connection instance separately
        // any time that connectionsRef's value is null (i.e. has no children) I am offline
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference myConnectionsRef = database.getReference("Users/"+usuario+"/connections");

        // Stores the timestamp of my last disconnect (the last time I was seen online)
        //final DatabaseReference lastOnlineRef = database.getReference("/Users/"+usuario+"/lastOnline");
        final DatabaseReference OnlineStatus = database.getReference("/Users/"+usuario+"/estado");

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    //DatabaseReference con = myConnectionsRef.push();

                    // When this device disconnects, remove it
                    //con.onDisconnect().removeValue();

                    // When I disconnect, update the last time I was seen online
                    //lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                    OnlineStatus.onDisconnect().setValue(false);

                    OnlineStatus.setValue(true);
                    // Add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    //con.setValue(Boolean.TRUE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        Intent intent = getIntent();
        fragment.latitud  = intent.getDoubleExtra("currentLatitude", 0);
        fragment.longitud  = intent.getDoubleExtra("currentLongitude", 0);

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Compromisos");

        //Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
        //se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot){

                //Comprobacion de eventos cada 2 minutos + o -
                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {

                        Long cantidad = dataSnapshot.getChildrenCount();
                        Iterable<DataSnapshot> list_compromisos = dataSnapshot.getChildren();
                        int total_actividades = 0;
                        float suma_puntuacion = 0;
                        int total_entrenamientos = 0;

                        //ArrayList<String> names= new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            Compromiso a = ds.getValue(Compromiso.class);
                            Usuario entrenador = ds.child("entrenador").getValue(Usuario.class);

                            if (entrenador != null) {
                                //Log.d("Diferencia:", entrenador.toString());
                                if(entrenador.getUsuarioID() != null) {
                                    if (entrenador.getUsuarioID().equals(mAuth.getUid())) {//Si el entrenador del evento es el usuario actual
                                        if (a.getEstado().equals("pendiente") && !a.getVisto()) {//Si no lo ha visto todavia
                                            Date fecha_actividad = a.getFecha();
                                            //Calendar cal = Calendar.getInstance();
                                            Calendar calendar = Calendar.getInstance(Locale.US);
                                            Date fecha_ahora = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
                                            //Date fecha_ahora=cal.getTime();
                                            int diferencia = fecha_actividad.compareTo(fecha_ahora);


                                            Calendar cal_actividad = Calendar.getInstance();
                                            cal_actividad.setTime(fecha_actividad);
                                            Calendar cal_ahora = Calendar.getInstance();
                                            cal_ahora.setTime(fecha_ahora);
                                            long diff = cal_actividad.getTimeInMillis() - cal_ahora.getTimeInMillis(); //result in millis
                                            long days = diff / (24 * 60 * 60 * 1000);
                                            long hours = diff / (60 * 60 * 1000);
                                            long min = diff / (60 * 1000);

                                            Log.d("Diferencia:", "****************************************************");
                                            Log.d("Diferencia:", "Actividad "+ a.getCompromisoID() +": "+fecha_actividad);
                                            Log.d("Diferencia:", "Ahora: "+fecha_ahora);
                                            Log.d("Diferencia:", "dias: "+days);
                                            Log.d("Diferencia:", "Horas: "+hours);
                                            Log.d("Diferencia:", "Minutos: "+min);


                                            if (diferencia > 0) {//si la fecha del evento es mayor a la actual



                                                if (days < 1 && hours < 1 && min < 30) {//menor a 30 minuts
                                                    generarNotificacion(a, min);
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference dbCompromiso = database.getReference("/Compromisos/Compromiso_" + a.getCompromisoID() + "/visto");
                                                    dbCompromiso.setValue(true);
                                                }
                                            }
                                        }
                                        else{//Si ya vio la actividad

                                            if(!a.getEstado().equals("cancelado")){//Contamos todos los entrenamientos no cancelados
                                                total_entrenamientos ++;
                                            }
                                            //Sumamos puntuacion
                                            HashMap<String, Float> puntuacion = a.getPuntuacion();
                                            if(puntuacion != null){
                                                total_actividades ++;
                                                suma_puntuacion += puntuacion.get(a.getParticipantes().get(0));
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        if(suma_puntuacion > 0){
                            float media_puntuacion = suma_puntuacion / total_actividades;
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference dbEntrenador = database.getReference("/Users/"+mAuth.getUid()+"/puntuacion");
                            dbEntrenador.setValue(media_puntuacion);
                            DatabaseReference dbEntrenadorCantidadEntrenamientos = database.getReference("/Users/"+mAuth.getUid()+"/numEntrenamientos");
                            dbEntrenadorCantidadEntrenamientos.setValue(total_entrenamientos);
                        }


                }
                }, 0, 250000);


            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });




    }

    public void generarNotificacion(Compromiso c, long min ){
        boolean areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled();

        if (!areNotificationsEnabled) {
            // Because the user took an action to create a notification, we create a prompt to let
            // the user re-enable notifications for this application again.
            Snackbar snackbar = Snackbar
                    .make(
                            mMainRelativeLayout,
                            "You need to enable notifications for this app",
                            Snackbar.LENGTH_LONG)
                    .setAction("ENABLE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Links to this app's notification settings
                            openNotificationSettingsForApp();
                        }
                    });
            snackbar.show();
            return;
        }

        crearNotificacion(c,min);
    }

    private void openNotificationSettingsForApp() {
        // Links to this app's notification settings.
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", getPackageName());
        intent.putExtra("app_uid", getApplicationInfo().uid);
        startActivity(intent);
    }

    private void crearNotificacion(Compromiso c, long min){

        if(c.getCompromisoID() != id_comp_notificado) {

            int id = UUID.randomUUID().hashCode();

            Intent notificationIntent = new Intent(getApplicationContext(), CompromisoActivity.class);
            notificationIntent.putExtra("mesg_compromiso_id", c.getCompromisoID());
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "TEST")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Pronto para correr?")
                    .setContentText("Você já tem seu próximo treno!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingNotificationIntent)
                    .setSound(alarmSound)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


// notificationId is a unique int for each notification that you must define
            notificationManager.notify(id, builder.build());

            id_comp_notificado = c.getCompromisoID();
        }

    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission (this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        else if (fragment.mGoogleMap != null) {
            // Access to the location has been granted to the app.
            fragment.mGoogleMap.setMyLocationEnabled(true);
            fragment.myLocation();

        }

    }



    public void onActionSalir(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, EntradaActivity.class);
        startActivity(intent);

    }

    public void onActionPerfil(View view) {

        Intent intent = new Intent(this, TrainerPerfilMenuActivity.class);
        startActivity(intent);

    }

    public void onActionCambiarEstado(View view) {

        //Estado del treiner
        String usuario = mAuth.getUid();
        // Since I can connect from multiple devices, we store each connection instance separately
        // any time that connectionsRef's value is null (i.e. has no children) I am offline
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference("Users/"+usuario+"/connections");


        if(btn_estado.getText() == "OFFLINE"){
            btn_estado.setText("ONLINE");
            DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());
            mRef.child("estado").setValue(true);
        }
        else{
            btn_estado.setText("OFFLINE");
            DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());
            mRef.child("estado").setValue(false);
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


