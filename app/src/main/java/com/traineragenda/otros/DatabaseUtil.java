package com.traineragenda.otros;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.DiasEtapas.DiaEtapa;
import com.traineragenda.DiasPlanilla.DiaPlanilla;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.Usuarios.Usuario;

public class DatabaseUtil {

//Metodo para buscar compromiso por id
    public static void findCompromisoById(Long id, final Listener listener) {
        FirebaseDatabase.getInstance().getReference("Compromisos")
                .orderByChild("compromisoID")
                .equalTo(id)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Compromiso compromiso = dataSnapshot.getValue(Compromiso.class);

                        listener.onCompromisoRetrieved(compromiso);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                        //Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so displayed the changed comment.
                        //Comment newComment = dataSnapshot.getValue(Comment.class);
                        //String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        //Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so remove it.
                        //String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                        //Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                        // A comment has changed position, use the key to determine if we are
                        // displaying this comment and if so move it.
                        //Comment movedComment = dataSnapshot.getValue(Comment.class);
                        String commentKey = dataSnapshot.getKey();

                        // ...
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                        //Toast.makeText(mContext, "Failed to load comments.",
                         //       Toast.LENGTH_SHORT).show();
                    }
                });


    }



    public interface Listener {
        void onCompromisoRetrieved(Compromiso compromiso);
    }



    //Metodo para buscar usuario por id
    public static void findUsuarioById(String usuario_id, final ListenerUsuario listener) {
        FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("usuarioID")
                .equalTo(usuario_id)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        listener.onUsuarioRetrieved(usuario);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                        String commentKey = dataSnapshot.getKey();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }



    public interface ListenerUsuario {
        void onUsuarioRetrieved(Usuario usuario);
    }


    //Metodo para buscar planilla por id
    public static void findPlanillaById(Long planilla_id, final ListenerPlanilla listener) {
        FirebaseDatabase.getInstance().getReference("Planillas")
                .orderByChild("planilla_id")
                .equalTo(planilla_id)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Planilla planilla = dataSnapshot.getValue(Planilla.class);

                        listener.onPlanillaRetrieved(planilla);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                        String commentKey = dataSnapshot.getKey();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }



    public interface ListenerPlanilla {
        void onPlanillaRetrieved(Planilla planilla);
    }


    //Metodo para buscar dias de planilla por id
    public static void findDiaPlanillaById(Long planilla_id, Long dia_id, final ListenerDiaPlanilla listener) {
        FirebaseDatabase.getInstance().getReference("Planillas/Planilla_"+ planilla_id +"/diaPlanillas")
                .orderByChild("dia_id")
                .equalTo(dia_id)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        DiaPlanilla d_planilla = dataSnapshot.getValue(DiaPlanilla.class);

                        listener.onDiaPlanillaRetrieved(d_planilla);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                        String commentKey = dataSnapshot.getKey();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }



    public interface ListenerDiaPlanilla {
        void onDiaPlanillaRetrieved(DiaPlanilla d_planilla);
    }



    //Metodo para buscar etapa de dias por id
    public static void findDiaEtapaById(Long planilla_id, Long dia_id, Long etapa_id, final ListenerDiaEtapa listener) {
        FirebaseDatabase.getInstance().getReference("Planillas/Planilla_"+ planilla_id +"/diaPlanillas/dia_"+dia_id+"/etapas")
                .orderByChild("etapa_id")
                .equalTo(etapa_id)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        DiaEtapa d_etapa = dataSnapshot.getValue(DiaEtapa.class);

                        listener.onDiaEtapaRetrieved(d_etapa);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                        String commentKey = dataSnapshot.getKey();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }



    public interface ListenerDiaEtapa {
        void onDiaEtapaRetrieved(DiaEtapa d_etapa);
    }




}
