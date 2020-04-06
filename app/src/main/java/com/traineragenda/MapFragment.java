package com.traineragenda;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Usuarios.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class MapFragment extends SupportMapFragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

  public GoogleMap mGoogleMap;
  private Marker mMeuMarcador;

  private Marker mPuntoFijoMarcador1;
  private Marker mPuntoFijoMarcador2;
  private Marker mPuntoFijoMarcador3;
  private Marker mPuntoFijoMarcador4;
  private Marker mPuntoFijoMarcador5;

  private Boolean Activar_agendamento = true;

  private FirebaseAuth mAuth;
  private FirebaseUser user;

  public Double latitud ;
  public Double longitud;
  public float zoom_level = 15.0f;

  private Usuario usuario;

  /**
   * Request code for location permission request.
   *
   * @see #onRequestPermissionsResult(int, String[], int[])
   */
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    enableMyLocation();

    mAuth = FirebaseAuth.getInstance();
    user = mAuth.getCurrentUser();
    String uid = user.getUid();

    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference myRef = fdb.getReference();
    //Single event listener
    myRef.child("Users").child(uid).addListenerForSingleValueEvent(
            new  ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                usuario = dataSnapshot.getValue(Usuario.class);
                String t = usuario.getTipo();
                if(t.equals( "cliente")){
                  Activar_agendamento =true;
                }
                else{
                  Activar_agendamento = false;
                }
                //user.email now has your email value
              }
              public void onCancelled(DatabaseError e){

              }
            });



  }

  public void enableMyLocation() {
  }

  /** Called when the user clicks a marker. */
  @Override
  public boolean onMarkerClick(final Marker marker) {


    // Retrieve the data from the marker.
    Integer tag = (Integer) marker.getTag();

    if(tag == 0){//marcador personal

      LatLng latLng = marker.getPosition();
      Intent intent = new Intent(this.getContext(), AgendarActivity.class);
      intent.putExtra("currentLatitude",latLng.latitude );
      intent.putExtra("currentLongitude", latLng.longitude);
      startActivity(intent);


    }
    else{//otros marcadores
      if(Activar_agendamento) {
        LatLng latLng = marker.getPosition();
        Intent intent = new Intent(this.getContext(), AgendarActivity.class);
        intent.putExtra("currentLatitude", latLng.latitude);
        intent.putExtra("currentLongitude", latLng.longitude);
        startActivity(intent);
      }
    }



    // Return false to indicate that we have not consumed the event and that we wish
    // for the default behavior to occur (which is for the camera to move such that the
    // marker is centered and for the marker's info window to open, if it has one).
    return false;
  }



  @Override
  public void onResume() {
    super.onResume();

    setUpMapIfNeeded();
  }

  private void setUpMapIfNeeded() {

    if (mGoogleMap == null) {
      getMapAsync(this);
    }
  }

  @Override
  public void onMapReady(GoogleMap googleMap)
  {
    mGoogleMap=googleMap;
    //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


    LatLng recife = new LatLng(-10.94416758, -37.05300897);
    //float zoomLevel = 12.0f; //This goes up to 21
    //if(zoom_level == 0)
    //zoom_level = 12.0f;
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recife, zoom_level));

    myLocation();

    if(latitud != null) {
      if (latitud != 0.0) {
        myActividadPosicion(latitud, longitud);
      }
    }

    cargarPuntosFijos();

    if(!Activar_agendamento)
      cargarZonaEntrenamiento();

    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng point) {
//        if (mMeuMarcador != null) {
//          if ((Integer) mMeuMarcador.getTag() == 1) {
//            mMeuMarcador.setTag(0);
//            mMeuMarcador.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//          }
//        }

        if (mMeuMarcador == null ){
          if(Activar_agendamento) {
            ponerNuevoMarcador(point);
          }
          else{
            moverZonaEntrenamiento(point);
          }
        }

      }
    });




    mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

      @Override
      public void onMarkerDragStart(Marker marker) {
        // ...
      }

      @Override
      public void onMarkerDragEnd(Marker marker) {
        // Use this method to detect when the user stopped dragging the marker.
      }

      @Override
      public void onMarkerDrag(Marker marker) {
        // Listen to this method to check the marker position.
      }
    });
  }

  public void elminarMarcadores(){
      mGoogleMap.clear();
      mMeuMarcador = null;
    cargarPuntosFijos();
  }

  //Metodo que setea un nuevo marcador en la posicion donde hacemos click
  public void ponerNuevoMarcador(LatLng latLng){
    //float zoomLevel = 15.0f; //This goes up to 21
    zoom_level = 15.0f;

    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(latLng);

    mGoogleMap.clear();
    cargarPuntosFijos();
    //mGoogleMap.addMarker(markerOptions);
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_level));
    //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

    crearMarcador(mMeuMarcador,latLng,"Agendando",0,0,BitmapDescriptorFactory.fromResource(R.drawable.marcador_google));



    // Set a listener for marker click.
  }

  public void crearMarcador(Marker mProvisional, LatLng latLng, String titulo, int tag,int showtitle, BitmapDescriptor bitm){
    mProvisional = mGoogleMap.addMarker(new MarkerOptions()
            .position(latLng)
            .title(titulo)
            .icon(bitm));
            //.draggable(true)
            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_google)));
    mProvisional.setTag(tag);
    if(showtitle==1){
      //mProvisional.showInfoWindow();
    }

    //mMeuMarcador.setDraggable(false);
    mGoogleMap.setOnMarkerClickListener(this);
  }

  public void cargarPuntosFijos(){
    //Parque da sementeira
    LatLng latLng = new LatLng(-10.94416758, -37.05300897);
    crearMarcador(mPuntoFijoMarcador1,latLng,"Parque da Sementeira",1,1,BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

    //Treze de Julho
    latLng = new LatLng(-10.9315842, -37.0474135);
    crearMarcador(mPuntoFijoMarcador2,latLng,"Treze de Julho",1,1,BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

    //Coroa do Meio
    latLng = new LatLng(-10.9783469, -37.0412432);
    crearMarcador(mPuntoFijoMarcador3,latLng,"Coroa do Meio",1,1,BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));



    //Parque da sementeira
//    latLng = new LatLng(-10.94416758, -37.05300897);º Sementeira",1,1,BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


  }

  //Metodo que setea y crea un marcador en la posicion actual del usuario
  public String setaearLocation() {

    GPSTracker gps = new GPSTracker(this.getContext());
    if(gps.canGetLocation()){

      double currentLatitude = gps.getLatitude();
      double currentLongitude = gps.getLongitude();

      LatLng latLng = new LatLng(currentLatitude, currentLongitude);
      //float zoomLevel = 15.0f; //This goes up to 21
      zoom_level = 15.0f;

      MarkerOptions markerOptions = new MarkerOptions();
      markerOptions.position(latLng);

      mGoogleMap.clear();
      cargarPuntosFijos();
      //mGoogleMap.addMarker(markerOptions);
      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
      mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_level));
      //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));


      crearMarcador(mMeuMarcador,latLng,"Agendando",0,0,BitmapDescriptorFactory.fromResource(R.drawable.marcador_google));

      return getDireccion(currentLatitude,currentLongitude);

    }
    else {
      return "";
    }
  }



  public void myLocation() {

    GPSTracker gps = new GPSTracker(this.getContext());
    if(gps.canGetLocation()){

      double currentLatitude = gps.getLatitude();
      double currentLongitude = gps.getLongitude();

      LatLng latLng = new LatLng(currentLatitude, currentLongitude);
      //float zoomLevel = 15.0f; //This goes up to 21
      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }
  }


  public void myActividadPosicion(double currentLatitude, double currentLongitude) {



      LatLng latLng = new LatLng(currentLatitude, currentLongitude);
      //float zoomLevel = 15.0f; //This goes up to 21
    //zoom_level = 15.0f;
      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
    Marker m= null;
    crearMarcador(m,latLng,"Actividade",11,0,BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
   // crearMarcador(mMeuMarcador,latLng,"Agendando",0,0,BitmapDescriptorFactory.fromResource(R.drawable.marcador_google));
  }



  //Metodo que crea un marcador en la posicion buscada
  protected void search(List<Address> addresses) {

    Address address = (Address) addresses.get(0);
    Double currentLongitude = address.getLongitude();
    Double currentLatitude = address.getLatitude();
    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

    String addressText = String.format(
            "%s, %s",
            address.getMaxAddressLineIndex() > 0 ? address
                    .getAddressLine(0) : "", address.getCountryName());

    MarkerOptions markerOptions = new MarkerOptions();

    markerOptions.position(latLng);
    //markerOptions.title(addressText);

    mGoogleMap.clear();
    cargarPuntosFijos();
    //mGoogleMap.addMarker(markerOptions);
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    //locationTv.setText("Latitude:" + address.getLatitude() + ", Longitude:"+ address.getLongitude());
    crearMarcador(mMeuMarcador,latLng,"Agendando",0,0,BitmapDescriptorFactory.fromResource(R.drawable.marcador_google));

  }


  public void onMyLocationClick(@NonNull Location location) {

    double currentLatitude = location.getLatitude();
    double currentLongitude = location.getLongitude();

    LatLng latLng = new LatLng(currentLatitude, currentLongitude);
    //float zoomLevel = 20.0f; //This goes up to 21
    zoom_level = 20.0f;
    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
  }

  public void getMyLocation(){

    GPSTracker gps = new GPSTracker(this.getContext());
    if(gps.canGetLocation()) {

      double currentLatitude = gps.getLatitude();
      double currentLongitude = gps.getLongitude();


      Geocoder geocoder;
      List<Address> addresses;
      geocoder = new Geocoder(this.getContext(), Locale.getDefault());

      //addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL


      LatLng latLng = new LatLng(currentLatitude, currentLongitude);
      //float zoomLevel = 15.0f; //This goes up to 21
      zoom_level = 15.0f;
      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom_level));
    }
  }

  public void findLocation(){
    Geocoder geocoder;
    List<Address> addresses;
    geocoder = new Geocoder(this.getContext(), Locale.getDefault());



  }

  public String getDireccion(double latitude,  double longitude) {
    Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
    String result = "";
    try {
      List<Address> addressList = geocoder.getFromLocation(
              latitude, longitude, 1);
      if (addressList != null && addressList.size() > 0) {
        Address address = addressList.get(0);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
          sb.append(address.getAddressLine(i)).append("\n");
        }

        sb.append(address.getAddressLine(0)).append("\n");
        //sb.append(address.getLocality()).append("\n");
        //sb.append(address.getPostalCode()).append("\n");
        //sb.append(address.getCountryName());
        result = sb.toString();
      }
    } catch (IOException e) {
      Log.e(TAG, "Unable connect to Geocoder", e);
    } finally {

      return result;
    }
  }


  public void cargarZonaEntrenamiento(){

    LatLng latLng = new LatLng(usuario.getLatitud(),usuario.getLongitud());
    if(latLng != null) {
      crearMarcador(mPuntoFijoMarcador3, latLng, "Área de trabalho", 3, 1, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

      mGoogleMap.addCircle(new CircleOptions()
              .center(latLng)
              .radius(usuario.getRadio())
              .strokeWidth(0f)
              .fillColor(0x55000000));
    }

  }

//reposisiona el marcador de zona del entrenador
  public void moverZonaEntrenamiento(LatLng latLng){
      usuario.setLatitud(latLng.latitude);
      usuario.setLongitud(latLng.longitude);

    String userId = user.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference().child("Users").child(userId);
    mRef.child("latitud").setValue(latLng.latitude);
    mRef.child("longitud").setValue(latLng.longitude);
    mRef.child("radio").setValue(5000);

    mGoogleMap.clear();
    cargarPuntosFijos();

    cargarZonaEntrenamiento();


  }

}





