package com.traineragenda.DiasPlanilla;

import java.util.Date;

public class DiaPlanilla {

    private Long dia_id;
    private Long planilla_id;
    private Date dia;
    private Long distancia;
    private String entrenamiento;
    private Long tipo;
    private String tiempo;
    private String pace;
    private Long terminado;


    public DiaPlanilla() {
    }

    public DiaPlanilla(Long dia_id, Long planilla_id, Date dia, Long distancia, String entrenamiento, Long tipo, String tiempo, String pace, Long terminado) {
        this.dia_id = dia_id;
        this.planilla_id = planilla_id;
        this.dia = dia;
        this.distancia = distancia;
        this.entrenamiento = entrenamiento;
        this.tipo = tipo;
        this.tiempo = tiempo;
        this.pace = pace;
        this.terminado = terminado;
    }

    public Long getDia_id() {
        return dia_id;
    }

    public void setDia_id(Long dia_id) {
        this.dia_id = dia_id;
    }

    public Long getPlanilla_id() {
        return planilla_id;
    }

    public void setPlanilla_id(Long planilla_id) {
        this.planilla_id = planilla_id;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Long getDistancia() {
        return distancia;
    }

    public void setDistancia(Long distancia) {
        this.distancia = distancia;
    }

    public String getEntrenamiento() {
        return entrenamiento;
    }

    public void setEntrenamiento(String entrenamiento) {
        this.entrenamiento = entrenamiento;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public Long getTerminado() {
        return terminado;
    }

    public void setTerminado_id(Long terminado) {
        this.terminado = terminado;
    }
}
