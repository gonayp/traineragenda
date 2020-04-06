package com.traineragenda;

import java.util.UUID;

public class Error {

    private String mId;
    private String mName;
    private String mTitle;
    private String mDescription;
    private String mTipo;
    private String mOwner;

    //required default constructor
    public Error(){

    }

    public Error(String name, String title,String description, String owner,String tipo) {
        mId = UUID.randomUUID().toString();
        mName = name;
        mTitle = title;
        mTipo = tipo;
        mOwner = owner;
        mDescription = description;
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

    @Override
    public String toString() {
        return "Lead{" +
                "ID='" + mId + '\'' +
                ", Nombre='" + mName + '\'' +
                ", Cargo='" + mTitle + '\'' +
                ", Dueño='" + mOwner + '\'' +
                ", Tipo='" + mTipo + '\'' +
                ", Descrição='" + mDescription + '\'' +
                '}';
    }

}
