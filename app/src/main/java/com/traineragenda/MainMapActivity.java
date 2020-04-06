package com.traineragenda;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.Compromisos.CompromisoActivity;
import com.traineragenda.Usuarios.Usuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public  class MainMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


  private Long id_comp_notificado = 0L;
  /**
   * Request code for location permissionL request.
   *
   * @see #onRequestPermissionsResult(int, String[], int[])
   */
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

  private FirebaseAuth mAuth;
  TextView input;

  Usuario user;

  MapFragment fragment;

  Boolean flag = true;

  List<Usuario> listado_usuarios_online;

  Message msg = new Message();

  private NotificationManagerCompat mNotificationManagerCompat;
  // RelativeLayout required for SnackBars to alert users when Notifications are disabled for app.
  private DrawerLayout mMainRelativeLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_map);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FirebaseApp.initializeApp(this);
    mAuth = FirebaseAuth.getInstance();

    //Notificaciones
    mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
    mMainRelativeLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_borrar);
    FloatingActionButton fab_my_location = (FloatingActionButton) findViewById(R.id.fab_my_location);
    //FloatingActionButton fab_borrar_marcadores = (FloatingActionButton) findViewById(R.id.fab_cancel_marckers);
    ImageButton btn_search = (ImageButton) findViewById(R.id.btn_search) ;
    input = (TextView) findViewById(R.id.input);



    input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          onActionBuscarDireccion(null);
          return true;
        }
        return false;
      }
    });

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        fragment.myLocation();
      }
    });
    btn_search.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        onActionBuscarDireccion(v);

      }
    });
    fab_my_location.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        input.setText("");
        input.setText(fragment.setaearLocation());

      }
    });




    //menu desplegable
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    //FRAGMENTO DE MAPA
    // Set to start with fragment
    fragment = new MapFragment();
    android.support.v4.app.FragmentTransaction fragmentTransaction =
            getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, fragment);
    fragmentTransaction.commit();
    enableMyLocation();


      // Get the Intent that started this activity and extract the string
      Intent intent = getIntent();
      fragment.latitud  = intent.getDoubleExtra("currentLatitude", 0);
      fragment.longitud  = intent.getDoubleExtra("currentLongitude", 0);
      if(fragment.latitud  != 0){
        fragment.zoom_level = 18.0f;
      }



    //Estado del usuario
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




  //Instancia a la base de datos
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    //apuntamos al nodo que queremos leer
    DatabaseReference myRef = fdb.getReference("Compromisos");

    FirebaseApp.initializeApp(this);
    mAuth = FirebaseAuth.getInstance();


    //USUARO ACTUAL
    DatabaseReference myRefUs = fdb.getReference();
    myRefUs.child("Users").child(mAuth.getUid()).addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                user = dataSnapshot.getValue(Usuario.class);

              }

              public void onCancelled(DatabaseError e) {

              }
            });

    //apuntamos al nodo que queremos leer
    DatabaseReference myRefU = fdb.getReference("Users");


//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
    myRefU.addValueEventListener(new ValueEventListener() {

      @Override
      public void onDataChange(DataSnapshot dataSnapshot){


        Long cantidad = dataSnapshot.getChildrenCount();
        Iterable<DataSnapshot> list_usuarios = dataSnapshot.getChildren();

        listado_usuarios_online = new ArrayList<Usuario>();

        for (int i=0; i < cantidad; i++) {
          Usuario a = list_usuarios.iterator().next().getValue(Usuario.class);
          if(a.getEstado() && a.getTipo().equals("trainer") && !a.getOcupado()){//Si esta online y es personal y no esta ocupado
            listado_usuarios_online.add(a);
          }

        }

      }
      @Override
      public void onCancelled(DatabaseError error){
        Log.e("ERROR FIREBASE",error.getMessage());
      }


    });

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
    myRef.addValueEventListener(new ValueEventListener() {

      @Override
      public void onDataChange(final DataSnapshot dataSnapshot){

        //Comprobacion de eventos cada 2 minutos + o -
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

          @Override
          public void run() {

            if (!user.getOcupado()) {//SI EL USUARIO ACTUAL NO ESTA EN OTRA ACTIVIDAD

              Long cantidad = dataSnapshot.getChildrenCount();
              Iterable<DataSnapshot> list_compromisos = dataSnapshot.getChildren();


              for (int i = 0; i < cantidad; i++) {
                Compromiso a = list_compromisos.iterator().next().getValue(Compromiso.class);
                List<String> Participantes = a.getParticipantes();
                if (Participantes.get(0).equals(mAuth.getUid())) {//Si el participante del evento es el usuario actual
                  Usuario entrenador = a.getEntrenador();
                  if (entrenador == null) {
                    //a = new Actividad(a3.getName(),a3.getTitle(),a3.getCompany(),a3.getDescription(),a3.getImage());
                    Date fecha_actividad = a.getFecha();
                    Calendar calendar = Calendar.getInstance(Locale.US);
                    Date fecha_ahora = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);

                    int diferencia = fecha_actividad.compareTo(fecha_ahora);
                    if (diferencia > 0) {//si la fecha del evento es mayor a la actual

                      Calendar cal_actividad = Calendar.getInstance();
                      cal_actividad.setTime(fecha_actividad);
                      Calendar cal_ahora = Calendar.getInstance();
                      cal_ahora.setTime(fecha_ahora);
                      long diff = cal_actividad.getTimeInMillis() - cal_ahora.getTimeInMillis(); //result in millis
                      long days = diff / (24 * 60 * 60 * 1000);
                      long hours = diff / (60 * 60 * 1000);
                      long min = diff / (60 * 1000);

                      if (days < 1 && hours < 1 && min < 30) {//menor a 30 minuts
                        Calendar rightNow = Calendar.getInstance();
                        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
                        int currentMinIn24Format = rightNow.get(Calendar.MINUTE);
                        int currentSecondIn24Format = rightNow.get(Calendar.SECOND);
//                        Log.d("TEST:", a.getFecha() + "**************************************** " + currentHourIn24Format + ":" + currentMinIn24Format + ":" + currentSecondIn24Format);
//                        Log.d("Diferencia:", Integer.toString(diferencia));
                        if (id_comp_notificado != a.getCompromisoID()) {
                          asignarPersonal(a);
                          i = cantidad.intValue();
                        }
                      }
                    }
                  }
                }
              }

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

  //Busca las actividades pendientes en menos de 15 minutos les asigna un entrenador y manda una notificacion
  public void asignarPersonal(Compromiso compromiso){

      LatLng lugar_treno = new LatLng(compromiso.getLatitud(),compromiso.getLongitud());
      List<Usuario> usuarios_cercanos = new ArrayList<Usuario>();

      int cantidad = listado_usuarios_online.size();
      for(int i = 0;i < cantidad; i++){
          Usuario u = listado_usuarios_online.get(i);
          int radio = u.getRadio() / 1000;
          double distancia = distance(lugar_treno.latitude,lugar_treno.longitude,u.latitud,u.longitud);
          if(distancia <= radio) {
              u.validado = 1;
              usuarios_cercanos.add(u);
          }
          else {
              u.validado = 0;
          }

      }

      int total_usuarios_cercanos = usuarios_cercanos.size();

      if(total_usuarios_cercanos > 0) {
        Random rand = new Random();
        int n = rand.nextInt(total_usuarios_cercanos); // Gives n such that 0 <= n < usuarios_cercanos.size()

        Usuario entrenador = usuarios_cercanos.get(n);
        compromiso.setEntrenador(entrenador);

        Long compromiso_id = compromiso.getCompromisoID();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbCompromiso = database.getReference("/Compromisos/Compromiso_" + compromiso_id + "/entrenador");
        dbCompromiso.setValue(entrenador);
        DatabaseReference dbCompromisoVisto = database.getReference("/Compromisos/Compromiso_" + compromiso_id + "/visto");
        dbCompromisoVisto.setValue(false);
        DatabaseReference dbEntrenador = database.getReference("/Users/"+entrenador.getUsuarioID()+"/ocupado");
        dbEntrenador.setValue(true);
        DatabaseReference dbCliente = database.getReference("/Users/"+mAuth.getUid()+"/ocupado");
        dbCliente.setValue(true);


        msg.arg1=2;
        handler.sendMessage(msg);
        generarNotificacion(compromiso);

      }
      else{
        msg.arg1=1;
        handler.sendMessage(msg);
      }

      //MEtodo para buscar un compromiso de la lista
//    DatabaseUtil.findCompromisoById(id, new DatabaseUtil.Listener() {
//      @Override
//      public void onCompromisoRetrieved(Compromiso compromiso) {
//        Log.d("********************",compromiso.getId());
//      }
//    });
  }

  public void generarNotificacion(Compromiso c){
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

    crearNotificacion(c);
  }

  private void openNotificationSettingsForApp() {
    // Links to this app's notification settings.
    Intent intent = new Intent();
    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
    intent.putExtra("app_package", getPackageName());
    intent.putExtra("app_uid", getApplicationInfo().uid);
    startActivity(intent);
  }

  Handler handler = new Handler(new Handler.Callback() {

    @Override
    public boolean handleMessage(Message msg) {
      Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);;

      switch (msg.arg1){
        case 1:
          toast = Toast.makeText(getApplicationContext(), "Não há treinadores disponíveis", Toast.LENGTH_SHORT);

          break;
        case 2:
          toast = Toast.makeText(getApplicationContext(), "Você recebeu um treinador para uma próxima atividade", Toast.LENGTH_SHORT);

          break;
      }
      toast.show();


      return false;
    }
  });

    //Retorna diferencia en kilometros entre dos puntos del mapa
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);}

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);}

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);}


  public void onActionElminarMarcadores(View v){
    fragment.elminarMarcadores();
    input.setText("");
  }

  public void onActionBuscarDireccion(View v){
    String g = input.getText().toString();

    Geocoder geocoder = new Geocoder(getBaseContext());
    List<Address> addresses = null;

    try {
      // Getting a maximum of 3 Address that matches the input
      // text
      addresses = geocoder.getFromLocationName(g, 3);
      if (addresses != null && !addresses.equals(""))
        fragment.search(addresses);

    } catch (Exception e) {
      Snackbar.make(v, "Error", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
  }


  /**
   * Enables the My Location layer if the fine location permission has been granted.
   */
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

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_map, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_salir) {

      mAuth.signOut();
      Intent entrada = new Intent(this, EntradaActivity.class);
      startActivity(entrada);
      return true;
    } else if (id == R.id.nav_meus) {
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);

    } else if (id == R.id.nav_treinos) {
      Intent intent = new Intent(this, MeusTreinosActivity.class);
      startActivity(intent);

    }else if (id == R.id.nav_actividades) {
      Intent intent = new Intent(this, ActividadesMenuActivity.class);
      startActivity(intent);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


  //Para bloquear el boton volver del telefono
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }




  private void crearNotificacion(Compromiso c){

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
              .setContentText("Você já tem um treinador para o seu próximo treno!")
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


}
