package com.traineragenda.Compromisos;

import com.traineragenda.Usuarios.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class Compromiso {

    public Date mFecha;
    public String mLugar;
    private String mId;
    private List<String> mParticipantes; //listado de userID
    private String mTitle;
    private String mCompany;
    private String mDescription;
    private String mTipo;
    private String mOwner;
    private Double mLatitud;
    private Double mLongitud;
    private int mImage;
    private Usuario mEntrenador;
    private Long compromiso_id;
    private String estado;
    private Boolean visto;
    private HashMap<String, String> comentario;//Listado de comentarios <usuario,comentario>
    private HashMap<String, Float> puntuacion;//Listado de puntuaciones <usuario, puntuacion>


    //required default constructor
    public Compromiso() {
        mParticipantes = new ArrayList<String>();
    }

//    public Compromiso(String titulo,String participante, Date fecha, String lugar, String description, String owner,  int image) {
//        mId = UUID.randomUUID().toString();
//        mParticipantes = new ArrayList<String>();
//        mParticipantes.add( participante);
//        mFecha = fecha;
//        mLugar = lugar;
//        mOwner = owner;
//        mDescription = description;
//        mImage = image;
//        mTitle = titulo;
//    }

//    public Compromiso(String titulo,String participante, Date fecha, Double p_lat, Double p_long, String owner,  int image) {
//        mId = UUID.randomUUID().toString();
//        mParticipantes = new ArrayList<String>();
//        mParticipantes.add( participante);
//        mFecha = fecha;
//        mLatitud = p_lat;
//        mLongitud = p_long;
//        mOwner = owner;
//        mImage = image;
//        mTitle = titulo;
//    }

    public Compromiso(Long id,String titulo,List<String> participantes, Date fecha, Double p_lat, Double p_long, String owner,  int image, String pEstado, Usuario pEntrenador) {
        mId = UUID.randomUUID().toString();
        mParticipantes = new ArrayList<String>();
        mParticipantes = participantes;
        mFecha = fecha;
        mLatitud = p_lat;
        mLongitud = p_long;
        mOwner = owner;
        mImage = image;
        mTitle = titulo;
        compromiso_id = id;
        estado = pEstado;
        mEntrenador = pEntrenador;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setParticipantes(List<String> pparticipantes){mParticipantes = pparticipantes;}

    public List<String> getParticipantes() {
        return mParticipantes;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getCompany() {
        return mCompany;
    }

    public void setCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setLatitud(Double p_lat) {
        mLatitud = p_lat;
    }

    public Double getLatitud() {
        return mLatitud;
    }

    public void setLongitud(Double p_long) {
        mLongitud = p_long;
    }

    public Double getLongitud() {
        return mLongitud;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public Date getFecha() {
        return mFecha;
    }

    public void setFecha(Date mFecha) {
        this.mFecha = mFecha;
    }

    public String getLugar() {
        return mLugar;
    }

    public void setLugar(String mLugar) {
        this.mLugar = mLugar;
    }

    public String getTipo() {
        return mTipo;
    }

    public void setTipo(String mTipo) {
        this.mTipo = mTipo;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int mImage) {
        this.mImage = mImage;
    }

    public Long getCompromisoID() {
        return compromiso_id;
    }

    public void setCompromisoID(Long p_compromiso_id) {
        compromiso_id = p_compromiso_id;
    }

    public Usuario getEntrenador() {
        return mEntrenador;
    }

    public void setEntrenador(Usuario mEntrenador) {
        this.mEntrenador = mEntrenador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String pEstado) {
        estado = pEstado;
    }

    public Boolean getVisto(){return visto;}

    public void setVisto(Boolean pVisto){ visto = pVisto;}

    public HashMap<String, String> getComentario(){ return comentario;}

    public void setComentario (HashMap<String, String> pComentario){ comentario = pComentario;}

    public HashMap<String, Float> getPuntuacion(){ return puntuacion;}

    public void setPuntuacion (HashMap<String, Float> pPuntuacion){ puntuacion = pPuntuacion;}



    @Override
    public String toString() {
        String valor = "Compromiso{" +
                "ID='" + compromiso_id + '\'' +
                ", Compañía='" + mCompany + '\'' +
                ", Nombre='" + mParticipantes + '\'' +
                ", Cargo='" + mTitle + '\'' +
                ", Dueño='" + mOwner + '\'' +
                ", Tipo='" + mTipo + '\'' +
                ", Descrição='" + mDescription + '\'' +
                ", Fecha='" + mFecha + '\'' +
                ", Lugar='" + mLugar + '\'' +
                ", Estado='" + estado + '\'' +
                ", Visto='" + visto + '\'' +
                ", Coordenadas='" + mLatitud.toString() + ':'+ mLongitud.toString() + '\'';
                if(mEntrenador != null)
                valor += ",\n\t Entrenador='" + mEntrenador.toString() + '\'' ;
                valor += '}';
        return valor;
    }
}

