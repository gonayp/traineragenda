package com.traineragenda.DiasEtapas;

import java.util.Date;

public class DiaEtapa {

    private Long dia_id;
    private Long etapa_id;
    private Long planilla_id;
    private Long distancia;
    private Long tipo;
    private String duracion;
    private String intensidad;
    private String fc;
    private String velocidad;
    private Integer terminado;
    private Long zona;


    public DiaEtapa() {
    }

    public DiaEtapa(Long dia_id, Long planilla_id, Long etapa_id, Long distancia, Long tipo, String duracion, String intensidad, Long zona, String fc, String velocidad) {
        this.dia_id = dia_id;
        this.planilla_id = planilla_id;
        this.etapa_id = etapa_id;
        this.distancia = distancia;
        this.tipo = tipo;
        this.duracion = duracion;
        this.intensidad = intensidad;
        this.terminado = 0;
        this.zona = zona;
        this.fc = fc;
        this.velocidad = velocidad;
    }

    public Long getDia_id() {
        return dia_id;
    }

    public void setDia_id(Long dia_id) {
        this.dia_id = dia_id;
    }

    public Long getEtapa_id() {
        return etapa_id;
    }

    public void setEtapa_id(Long etapa_id) {
        this.etapa_id = etapa_id;
    }


    public Long getPlanilla_id() {
        return planilla_id;
    }

    public void setPlanilla_id(Long planilla_id) {
        this.planilla_id = planilla_id;
    }

    public Integer getTerminado() {
        return terminado;
    }

    public void setTerminado(Integer t) {
        this.terminado = t;
    }

    public Long getDistancia() {
        return distancia;
    }

    public void setDistancia(Long distancia) {
        this.distancia = distancia;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String tiempo) {
        this.duracion = tiempo;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(String i) {
        this.intensidad = i;
    }

    public Long getZona() {
        return zona;
    }

    public void setZona(Long zona) {
        this.zona = zona;
    }

    public String getFc() {
        return fc;
    }

    public void setFc(String fc) {
        this.fc = fc;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }
}
