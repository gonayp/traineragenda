package com.traineragenda.DiasEtapas;


import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio ficticio de leads
 */
public class DiaEtapaRepository {
    private static DiaEtapaRepository repository = new DiaEtapaRepository();
    private List<DiaEtapa> leads = new ArrayList<DiaEtapa>();

    public static DiaEtapaRepository getInstance() {
        return repository;
    }

    private DiaEtapaRepository() {
    }

    public void addDiaEtapa(DiaEtapa p_dia){

        saveDiaEtapa(new DiaEtapa(p_dia.getDia_id(),p_dia.getPlanilla_id(),p_dia.getEtapa_id(),p_dia.getDistancia(),p_dia.getTipo(),p_dia.getDuracion(),p_dia.getIntensidad(),p_dia.getZona(),p_dia.getFc(),p_dia.getVelocidad()));
    }

    public void deleteDiaEtapa (){
        deleteLeads();
    }

    private void saveDiaEtapa(DiaEtapa lead) {
        leads.add(lead);
    }
    private void deleteLeads() {
        leads.clear();
    }

    public List<DiaEtapa> getLeads() {
        return leads;
    }
}