package com.traineragenda.Actividades;

import java.util.UUID;

public class Actividad {

    private String mId;
    private String mName;
    private String mTitle;
    private String mCompany;
    private String mDescription;
    private String mTipo;
    private String mOwner;
    private int mImage;

    //required default constructor
    public Actividad(){

    }

    public Actividad(String name, String title, String company, String description, String owner,String tipo,int image) {
        mId = UUID.randomUUID().toString();
        mName = name;
        mTitle = title;
        mCompany = company;
        mTipo = tipo;
        mOwner = owner;
        mDescription = description;
        mImage = image;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
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

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
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

    @Override
    public String toString() {
        return "Lead{" +
                "ID='" + mId + '\'' +
                ", Compañía='" + mCompany + '\'' +
                ", Nombre='" + mName + '\'' +
                ", Cargo='" + mTitle + '\'' +
                ", Dueño='" + mOwner + '\'' +
                ", Tipo='" + mTipo + '\'' +
                ", Descrição='" + mDescription + '\'' +
                '}';
    }

}

