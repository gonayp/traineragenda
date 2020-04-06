package com.traineragenda.Planillas;

import com.traineragenda.DiasPlanilla.DiaPlanilla;

import java.util.Date;
import java.util.List;

public class Planilla {

    private Long planilla_id;
    private String usuario_id;
    private Date fecha;
    private Boolean terminado;
    private Long datosAptitud;
    private String datosObjetivo;
    private Long datosModalidad;
    private Long datosPeriodicidad;
    private String datosPeriodo;
    private String datosFrecuencia;
    private Long datosCooper;
    private Long datosFCmax;
    private Long datosEdad;
    private Long datosPeso;
    private Long datosAltura;
    private Double datosIMC;
    private Long datosBrazo;
    private Long datosCintura;
    private Long datosAbdominal;
    private Long datosQuadril;
    private Long datosCoxa;
    private Long datosKilometros;
    private Long datosPaceM;
    private Long datosPaceS;
    private Long datosTiempoM;
    private Long datosTiempoS;
    //private List<DiaPlanilla> diaPlanillas;


    public Planilla() {
    }

    public Planilla(Long planilla_id, String usuario_id, Date fecha, Boolean terminado, Long datosAptitud, String datosObjetivo, Long datosModalidad, Long datosPeriodicidad, Long pPeso, Long pAltura, Long pEdad, Long pace) {
        this.planilla_id = planilla_id;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
        this.terminado = terminado;
        this.datosAptitud = datosAptitud;
        this.datosObjetivo = datosObjetivo;
        this.datosModalidad = datosModalidad;
        this.datosPeriodicidad = datosPeriodicidad;
        this.datosPeso = pPeso;
        this.datosAltura = pAltura;
        this.datosEdad = pEdad;
        this.datosPaceM = pace;
    }

    public Planilla(Long planilla_id, String usuario_id, Date fecha, Boolean terminado, Long datosAptitud, String datosObjetivo, Long datosModalidad, Long datosPeriodicidad,
                    String datosPeriodo, String datosFrecuencia, Long datosCooper, Long datosFCmax, Long datosPeso, Double datosIMC, Long datosBrazo, Long datosCintura,
                    Long datosAbdominal, Long datosQuadril, Long datosCoxa, Long Kilometros, Long paceM, Long PaceS, Long TiempoM, Long TiempoS) {
        this.planilla_id = planilla_id;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
        this.terminado = terminado;
        this.datosAptitud = datosAptitud;
        this.datosObjetivo = datosObjetivo;
        this.datosModalidad = datosModalidad;
        this.datosPeriodicidad = datosPeriodicidad;
        this.datosPeriodo = datosPeriodo;
        this.datosFrecuencia = datosFrecuencia;
        this.datosCooper = datosCooper;
        this.datosFCmax = datosFCmax;
        this.datosPeso = datosPeso;
        this.datosIMC = datosIMC;
        this.datosBrazo = datosBrazo;
        this.datosCintura = datosCintura;
        this.datosAbdominal = datosAbdominal;
        this.datosQuadril = datosQuadril;
        this.datosCoxa = datosCoxa;
        this.datosKilometros = Kilometros;
        this.datosPaceM = paceM;
        this.datosPaceS = PaceS;
        this.datosTiempoM = TiempoM;
        this.datosTiempoS = TiempoS;
    }


    public Long getPlanilla_id() {
        return planilla_id;
    }

    public void setPlanilla_id(Long planilla_id) {
        this.planilla_id = planilla_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getTerminado() {
        return terminado;
    }

    public void setTerminado(Boolean terminado) {
        this.terminado = terminado;
    }

    public Long getDatosAptitud() {
        return datosAptitud;
    }

    public void setDatosAptitud(Long datosAptitud) {
        this.datosAptitud = datosAptitud;
    }

    public String getDatosObjetivo() {
        return datosObjetivo;
    }

    public void setDatosObjetivo(String datosObjetivo) {
        this.datosObjetivo = datosObjetivo;
    }

    public Long getDatosModalidad() {
        return datosModalidad;
    }

    public void setDatosModalidad(Long datosModalidad) {
        this.datosModalidad = datosModalidad;
    }

    public Long getDatosPeriodicidad() {
        return datosPeriodicidad;
    }

    public void setDatosPeriodicidad(Long datosPeriodicidad) {
        this.datosPeriodicidad = datosPeriodicidad;
    }

    public String getDatosPeriodo() {
        return datosPeriodo;
    }

    public void setDatosPeriodo(String datosPeriodo) {
        this.datosPeriodo = datosPeriodo;
    }

    public String getDatosFrecuencia() {
        return datosFrecuencia;
    }

    public void setDatosFrecuencia(String datosFrecuencia) {
        this.datosFrecuencia = datosFrecuencia;
    }

    public Long getDatosCooper() {
        return datosCooper;
    }

    public void setDatosCooper(Long datosCooper) {
        this.datosCooper = datosCooper;
    }

    public Long getDatosFCmax() {
        return datosFCmax;
    }

    public void setDatosFCmax(Long datosFCmax) {
        this.datosFCmax = datosFCmax;
    }

    public Long getDatosEdad() {
        return datosEdad;
    }

    public void setDatosEdad(Long datosEdad) {
        this.datosEdad = datosEdad;
    }

    public Long getDatosPeso() {
        return datosPeso;
    }

    public void setDatosPeso(Long datosPeso) {
        this.datosPeso = datosPeso;
    }

    public Long getDatosAltura() {
        return datosAltura;
    }

    public void setDatosAltura(Long datosAltura) {
        this.datosAltura = datosAltura;
    }

    public Double getDatosIMC() {
        return datosIMC;
    }

    public void setDatosIMC(Double datosIMC) {
        this.datosIMC = datosIMC;
    }

    public Long getDatosBrazo() {
        return datosBrazo;
    }

    public void setDatosBrazo(Long datosBrazo) {
        this.datosBrazo = datosBrazo;
    }

    public Long getDatosCintura() {
        return datosCintura;
    }

    public void setDatosCintura(Long datosCintura) {
        this.datosCintura = datosCintura;
    }

    public Long getDatosAbdominal() {
        return datosAbdominal;
    }

    public void setDatosAbdominal(Long datosAbdominal) {
        this.datosAbdominal = datosAbdominal;
    }

    public Long getDatosQuadril() {
        return datosQuadril;
    }

    public void setDatosQuadril(Long datosQuadril) {
        this.datosQuadril = datosQuadril;
    }

    public Long getDatosCoxa() {
        return datosCoxa;
    }

    public void setDatosCoxa(Long datosCoxa) {
        this.datosCoxa = datosCoxa;
    }

    public Long getDatosKilometros() {
        return datosKilometros;
    }

    public void setDatosKilometros(Long datosKilometros) {
        this.datosKilometros = datosKilometros;
    }

    public Long getDatosPaceM() {
        return datosPaceM;
    }

    public void setDatosPaceM(Long datosPaceM) {
        this.datosPaceM = datosPaceM;
    }

    public Long getDatosPaceS() {
        return datosPaceS;
    }

    public void setDatosPaceS(Long datosPaceS) {
        this.datosPaceS = datosPaceS;
    }

    public Long getDatosTiempoM() {
        return datosTiempoM;
    }

    public void setDatosTiempoM(Long datosTiempoM) {
        this.datosTiempoM = datosTiempoM;
    }

    public Long getDatosTiempoS() {
        return datosTiempoS;
    }

    public void setDatosTiempoS(Long datosTiempoS) {
        this.datosTiempoS = datosTiempoS;
    }


    /*
    public List<DiaPlanilla> getDiaPlanillas() {
        return diaPlanillas;
    }

    public void setDiaPlanillas(List<DiaPlanilla> diaPlanillas) {
        this.diaPlanillas = diaPlanillas;
    }*/
}
