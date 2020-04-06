package com.traineragenda.Actividades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Repositorio ficticio de leads
 */
public class ActividadesRepository {
  private static ActividadesRepository repository = new ActividadesRepository();
  private HashMap<String, Actividad> leads = new HashMap<>();

  public static ActividadesRepository getInstance() {
    return repository;
  }

  private ActividadesRepository() {
//        saveLead(new Actividad("Alexander Pierrot", "CEO", "Insures S.O.", R.drawable.lead_photo_1));
//        saveLead(new Actividad("Carlos Lopez", "Asistente", "Hospital Blue", R.drawable.lead_photo_2));
//        saveLead(new Actividad("Sara Bonz", "Directora de Marketing", "Electrical Parts ltd", R.drawable.lead_photo_3));
//        saveLead(new Actividad("Liliana Clarence", "Diseñadora de Producto", "Creativa App", R.drawable.lead_photo_4));
//        saveLead(new Actividad("Benito Peralta", "Supervisor de Ventas", "Neumáticos Press", R.drawable.lead_photo_1));
//        saveLead(new Actividad("Juan Jaramillo", "CEO", "Banco Nacional", R.drawable.lead_photo_2));
//        saveLead(new Actividad("Christian Steps", "CTO", "Cooperativa Verde", R.drawable.lead_photo_3));
//        saveLead(new Actividad("Alexa Giraldo", "Lead Programmer", "Frutisofy", R.drawable.lead_photo_4));
//        saveLead(new Actividad("Linda Murillo", "Directora de Marketing", "Seguros Boliver", R.drawable.lead_photo_1));
//        saveLead(new Actividad("Lizeth Astrada", "CEO", "Concesionario Motolox", R.drawable.lead_photo_2));
  }

  public void addActividad(Actividad p_actividad){
    saveLead(new Actividad(p_actividad.getName(), p_actividad.getTitle(), p_actividad.getCompany(), p_actividad.getDescription(), p_actividad.getOwner(), p_actividad.getTipo(), p_actividad.getImage()));
  }

  public void deleteActividades (){
    deleteLeads();
  }

  private void saveLead(Actividad lead) {
    leads.put(lead.getId(), lead);
  }
  private void deleteLeads() {
    leads.clear();
  }

  public List<Actividad> getLeads() {
    return new ArrayList<>(leads.values());
  }
}