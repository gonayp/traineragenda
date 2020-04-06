package com.traineragenda.Compromisos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Repositorio ficticio de leads
 */
public class CompromisoRepository {
    private static CompromisoRepository repository = new CompromisoRepository();
    private HashMap<String, Compromiso> leads = new HashMap<>();

    public static CompromisoRepository getInstance() {
        return repository;
    }

    private CompromisoRepository() {
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

    public void addCompromiso(Compromiso p_compromiso){

        saveCompromiso(new Compromiso(p_compromiso.getCompromisoID(),p_compromiso.getTitle(),p_compromiso.getParticipantes(), p_compromiso.getFecha(),p_compromiso.getLatitud(),p_compromiso.getLongitud(), p_compromiso.getOwner(),  p_compromiso.getImage(), p_compromiso.getEstado(),p_compromiso.getEntrenador()));
    }

    public void deleteCompromisos (){
        deleteLeads();
    }

    private void saveCompromiso(Compromiso lead) {
        leads.put(lead.getId(), lead);
    }
    private void deleteLeads() {
        leads.clear();
    }

    public List<Compromiso> getLeads() {
        return new ArrayList<>(leads.values());
    }
}