package com.traineragenda.Usuarios;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

public class Usuario {

  public double age;
  public String gender;
  public String userName;
  public String nombre;
  public String apellido;
  public String celular;
  public Date nacimiento;
  public Date alta;
  public String tipo;
  public double latitud;
  public double longitud;
  public int radio;
  public Boolean estado;
  public Boolean ocupado;
  public int validado;
  public String usuarioID;
  public float puntuacion;
  private int numEntrenamientos;
  private Boolean planillas;
  private Date ult_planilla;
  private Integer peso;
  private Integer altura;
  private Long corridaCuantoTiempo;
  private String corridaCantidad;
  private long corridaMotivacion;
  private long corridaVecesSemana;
  private long corridaDistancia;
  private String corridaRitmo;
  private long corridaLugar;
  private long corridaTurno;
  private long clinicaColesterol;
  private long clinicaCardiopatia;
  private long clinicaHistoricoCardiaco;
  private long clinicaHipertenso;
  private long clinicaDiabetico;
  private long clinicaPulmones;
  private long clinicaMedicamentos;
  private long clinicaCirugia;
  private long clinicaDolores;
  private long clinicaRecomendaciones;
  private String clinicaColesterolCual;
  private String clinicaCardiopatiaCual;
  private String clinicaHistoricoCardiacoCual;
  private String clinicaHipertensoCual;
  private String clinicaDiabeticoCual;
  private String clinicaPulmonesCual;
  private String clinicaMedicamentosCual;
  private String clinicaCirugiaCual;
  private String clinicaDoloresCual;
  private String clinicaRecomendacionesCual;



  //required default constructor
  public Usuario() {
  }

  public Usuario(String id,double age, String gender, String userName, String pNombre, String pApellido, String pCelular, Date pNacimiento, Date pAlta, String pTipo,
                 double platitud, double plongitud, int pRadio, Boolean pPlanilla,Date pUltPlanilla, Integer pPeso, Integer pAltura, long corridaCuantoTiempo, String corridaCantidad, long corridaMotivacion, long corridaVecesSemana,
                 long corridaDistancia, String corridaRitmo, long corridaLugar, long corridaTurno, long clinicaColesterol, long clinicaCardiopatia, long clinicaHistoricoCardiaco,
                 long clinicaHipertenso, long clinicaDiabetico, long clinicaPulmones, long clinicaMedicamentos, long clinicaCirugia, long clinicaDolores, long clinicaRecomendaciones,
                 String clinicaColesterolCual, String clinicaCardiopatiaCual, String clinicaHistoricoCardiacoCual, String clinicaHipertensoCual, String clinicaDiabeticoCual, String clinicaPulmonesCual,
                 String clinicaMedicamentosCual, String clinicaCirugiaCual, String clinicaDoloresCual, String clinicaRecomendacionesCual) {
    this.age = age;
    this.gender = gender;
    this.userName = userName;
    this.nombre = pNombre;
    this.apellido = pApellido;
    this.celular = pCelular;
    this.nacimiento = pNacimiento;
    this.alta = pAlta;
    this.tipo = pTipo;
    this.latitud = platitud;
    this.longitud = plongitud;
    this.radio = pRadio;
    this.estado = false;
    this.ocupado = false;
    this.validado = 0;
    this.usuarioID = id;
    this.planillas = pPlanilla;
    this.ult_planilla = pUltPlanilla;
    this.peso = pPeso;
    this.altura = pAltura;
    this.corridaCuantoTiempo = corridaCuantoTiempo;
    this.corridaCantidad = corridaCantidad;
    this.corridaMotivacion = corridaMotivacion;
    this.corridaVecesSemana = corridaVecesSemana;
    this.corridaDistancia = corridaDistancia;
    this.corridaRitmo = corridaRitmo;
    this.corridaLugar = corridaLugar;
    this.corridaTurno = corridaTurno;
    this.clinicaColesterol = clinicaColesterol;
    this.clinicaCardiopatia = clinicaCardiopatia;
    this.clinicaHistoricoCardiaco = clinicaHistoricoCardiaco;
    this.clinicaHipertenso = clinicaHipertenso;
    this.clinicaDiabetico = clinicaDiabetico;
    this.clinicaPulmones = clinicaPulmones;
    this.clinicaMedicamentos = clinicaMedicamentos;
    this.clinicaCirugia = clinicaCirugia;
    this.clinicaDolores = clinicaDolores;
    this.clinicaRecomendaciones = clinicaRecomendaciones;
    this.clinicaColesterolCual = clinicaColesterolCual;
    this.clinicaCardiopatiaCual = clinicaCardiopatiaCual;
    this.clinicaHistoricoCardiacoCual = clinicaHistoricoCardiacoCual;
    this.clinicaHipertensoCual = clinicaHipertensoCual;
    this.clinicaDiabeticoCual = clinicaDiabeticoCual;
    this.clinicaPulmonesCual = clinicaPulmonesCual;
    this.clinicaMedicamentosCual = clinicaMedicamentosCual;
    this.clinicaCirugiaCual = clinicaCirugiaCual;
    this.clinicaDoloresCual = clinicaDoloresCual;
    this.clinicaRecomendacionesCual = clinicaRecomendacionesCual;
  }




  public Usuario(String id, double age, String gender, String userName, String pNombre, String pApellido, String pCelular, Date pNacimiento, Date pAlta, String pTipo,
                 double platitud, double plongitud, int pRadio, Boolean pPlanilla,Date pUltPlanilla) {
    this.age = age;
    this.gender = gender;
    this.userName = userName;
    nombre = pNombre;
    apellido = pApellido;
    celular = pCelular;
    nacimiento = pNacimiento;
    alta = pAlta;
    tipo = pTipo;
    latitud = platitud;
    longitud = plongitud;
    radio = pRadio;
    estado = false;
    ocupado = false;
    usuarioID = id;
    validado = 0;
    planillas = pPlanilla;
    ult_planilla = pUltPlanilla;
  }




  public double getAge() {
    return age;
  }

  public void setAge(double age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getUsuarioID() {
    return usuarioID;
  }

  public void setUsuarioID(String id) {
    usuarioID = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String pNombre) {
    nombre= pNombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String pApellido) {
    apellido = pApellido;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String pCelular) {
    celular = pCelular;
  }

  public Date getNacimiento() {
    return nacimiento;
  }

  public void setNacimiento(Date pNacimiento) {
    nacimiento = pNacimiento;
  }

  public Date getAlta() {
    return alta;
  }

  public void setAlta(Date pAlta) {
    alta = pAlta;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String pTipo) {
    tipo = pTipo;
  }

  public double getLatitud() {
    return latitud;
  }

  public void setLatitud(double pLatitud) {
    latitud = pLatitud;
  }

  public double getLongitud() {
    return longitud;
  }

  public void setLongitud(double pLongitud) {
    longitud= pLongitud;
  }

  public int getRadio() {
    return radio;
  }

  public void setRadio(int pRadio) {
    radio = pRadio;
  }

  public Boolean getEstado() {
    return estado;
  }

  public void setEstado(Boolean pEstado) {
    estado = pEstado;
  }

  public Boolean getOcupado() {
    return ocupado;
  }

  public void setOcupado(Boolean pOcupado) {
    ocupado = pOcupado;
  }

  public float getPuntuacion() {
    return puntuacion;
  }

  public void setPuntuacion(float pPuntuacion) {
    puntuacion= pPuntuacion;
  }

  public int getNumEntrenamientos(){ return numEntrenamientos;}

  public void setNumEntrenamientos (int pNum){ numEntrenamientos = pNum;}

  public int getValidado() {
    return validado;
  }

  public void setValidado(int validado) {
    this.validado = validado;
  }

  public Boolean getPlanillas() {
    return planillas;
  }

  public void setPlanillas(Boolean planillas) {
    this.planillas = planillas;
  }

  public Date getUlt_planilla() {
    return ult_planilla;
  }

  public void setUlt_planilla(Date ult_planilla) {
    this.ult_planilla = ult_planilla;
  }

  public Integer getPeso() {
    return peso;
  }

  public void setPeso(Integer peso) {
    this.peso = peso;
  }

  public Integer getAltura() {
    return altura;
  }

  public void setAltura(Integer altura) {
    this.altura = altura;
  }

  public Long getCorridaCuantoTiempo() {
    return corridaCuantoTiempo;
  }

  public void setCorridaCuantoTiempo(Long corridaCuantoTiempo) {
    this.corridaCuantoTiempo = corridaCuantoTiempo;
  }

  public String getCorridaCantidad() {
    return corridaCantidad;
  }

  public void setCorridaCantidad(String corridaCantidad) {
    this.corridaCantidad = corridaCantidad;
  }

  public Long getCorridaMotivacion() {
    return corridaMotivacion;
  }

  public void setCorridaMotivacion(Long corridaMotivacion) {
    this.corridaMotivacion = corridaMotivacion;
  }

  public Long getCorridaVecesSemana() {
    return corridaVecesSemana;
  }

  public void setCorridaVecesSemana(long corridaVecesSemana) {
    this.corridaVecesSemana = corridaVecesSemana;
  }

  public Long getCorridaDistancia() {
    return corridaDistancia;
  }

  public void setCorridaDistancia(long corridaDistancia) {
    this.corridaDistancia = corridaDistancia;
  }

  public String getCorridaRitmo() {
    return corridaRitmo;
  }

  public void setCorridaRitmo(String corridaRitmo) {
    this.corridaRitmo = corridaRitmo;
  }

  public Long getCorridaLugar() {
    return corridaLugar;
  }

  public void setCorridaLugar(long corridaLugar) {
    this.corridaLugar = corridaLugar;
  }

  public Long getCorridaTurno() {
    return corridaTurno;
  }

  public void setCorridaTurno(long corridaTurno) {
    this.corridaTurno = corridaTurno;
  }

  public Long getClinicaColesterol() {
    return clinicaColesterol;
  }

  public void setClinicaColesterol(long clinicaColesterol) {
    this.clinicaColesterol = clinicaColesterol;
  }

  public Long getClinicaCardiopatia() {
    return clinicaCardiopatia;
  }

  public void setClinicaCardiopatia(long clinicaCardiopatia) {
    this.clinicaCardiopatia = clinicaCardiopatia;
  }

  public Long getClinicaHistoricoCardiaco() {
    return clinicaHistoricoCardiaco;
  }

  public void setClinicaHistoricoCardiaco(long clinicaHistoricoCardiaco) {
    this.clinicaHistoricoCardiaco = clinicaHistoricoCardiaco;
  }

  public Long getClinicaHipertenso() {
    return clinicaHipertenso;
  }

  public void setClinicaHipertenso(long clinicaHipertenso) {
    this.clinicaHipertenso = clinicaHipertenso;
  }

  public Long getClinicaDiabetico() {
    return clinicaDiabetico;
  }

  public void setClinicaDiabetico(long clinicaDiabetico) {
    this.clinicaDiabetico = clinicaDiabetico;
  }

  public Long getClinicaPulmones() {
    return clinicaPulmones;
  }

  public void setClinicaPulmones(long clinicaPulmones) {
    this.clinicaPulmones = clinicaPulmones;
  }

  public Long getClinicaMedicamentos() {
    return clinicaMedicamentos;
  }

  public void setClinicaMedicamentos(long clinicaMedicamentos) {
    this.clinicaMedicamentos = clinicaMedicamentos;
  }

  public Long getClinicaCirugia() {
    return clinicaCirugia;
  }

  public void setClinicaCirugia(long clinicaCirugia) {
    this.clinicaCirugia = clinicaCirugia;
  }

  public Long getClinicaDolores() {
    return clinicaDolores;
  }

  public void setClinicaDolores(long clinicaDolores) {
    this.clinicaDolores = clinicaDolores;
  }

  public Long getClinicaRecomendaciones() {
    return clinicaRecomendaciones;
  }

  public void setClinicaRecomendaciones(long clinicaRecomendaciones) {
    this.clinicaRecomendaciones = clinicaRecomendaciones;
  }

  public String getClinicaColesterolCual() {
    return clinicaColesterolCual;
  }

  public void setClinicaColesterolCual(String clinicaColesterolCual) {
    this.clinicaColesterolCual = clinicaColesterolCual;
  }

  public String getClinicaCardiopatiaCual() {
    return clinicaCardiopatiaCual;
  }

  public void setClinicaCardiopatiaCual(String clinicaCardiopatiaCual) {
    this.clinicaCardiopatiaCual = clinicaCardiopatiaCual;
  }

  public String getClinicaHistoricoCardiacoCual() {
    return clinicaHistoricoCardiacoCual;
  }

  public void setClinicaHistoricoCardiacoCual(String clinicaHistoricoCardiacoCual) {
    this.clinicaHistoricoCardiacoCual = clinicaHistoricoCardiacoCual;
  }

  public String getClinicaHipertensoCual() {
    return clinicaHipertensoCual;
  }

  public void setClinicaHipertensoCual(String clinicaHipertensoCual) {
    this.clinicaHipertensoCual = clinicaHipertensoCual;
  }

  public String getClinicaDiabeticoCual() {
    return clinicaDiabeticoCual;
  }

  public void setClinicaDiabeticoCual(String clinicaDiabeticoCual) {
    this.clinicaDiabeticoCual = clinicaDiabeticoCual;
  }

  public String getClinicaPulmonesCual() {
    return clinicaPulmonesCual;
  }

  public void setClinicaPulmonesCual(String clinicaPulmonesCual) {
    this.clinicaPulmonesCual = clinicaPulmonesCual;
  }

  public String getClinicaMedicamentosCual() {
    return clinicaMedicamentosCual;
  }

  public void setClinicaMedicamentosCual(String clinicaMedicamentosCual) {
    this.clinicaMedicamentosCual = clinicaMedicamentosCual;
  }

  public String getClinicaCirugiaCual() {
    return clinicaCirugiaCual;
  }

  public void setClinicaCirugiaCual(String clinicaCirugiaCual) {
    this.clinicaCirugiaCual = clinicaCirugiaCual;
  }

  public String getClinicaDoloresCual() {
    return clinicaDoloresCual;
  }

  public void setClinicaDoloresCual(String clinicaDoloresCual) {
    this.clinicaDoloresCual = clinicaDoloresCual;
  }

  public String getClinicaRecomendacionesCual() {
    return clinicaRecomendacionesCual;
  }

  public void setClinicaRecomendacionesCual(String clinicaRecomendacionesCual) {
    this.clinicaRecomendacionesCual = clinicaRecomendacionesCual;
  }

  @Override
  public String toString() {
    return "Usuario{" +
            "ID='" + usuarioID + '\'' +
            ", Nombre='" + nombre + '\'' +
            ", Apellido='" + apellido + '\'' +
            ", Usuario='" + userName + '\'' +
            ", estado='" + estado + '\'' +
            ", fecha='" + nacimiento + '\'' +
            ", Celular='" + celular + '\'' +
            ", Radio='" + radio + '\'' +
            '}';
  }

}

