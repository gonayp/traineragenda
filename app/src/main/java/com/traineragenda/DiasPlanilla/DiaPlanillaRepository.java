package com.traineragenda.DiasPlanilla;

import com.traineragenda.Compromisos.Compromiso;

import java.security.Signature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Repositorio ficticio de leads
 */
public class DiaPlanillaRepository {
    private static DiaPlanillaRepository repository = new DiaPlanillaRepository();
    private List<DiaPlanilla> leads = new ArrayList<DiaPlanilla>();

    public static DiaPlanillaRepository getInstance() {
        return repository;
    }

    private DiaPlanillaRepository() {
    }

    public void addDiaPlanilla(DiaPlanilla p_dia){

        saveDiaPlanilla(new DiaPlanilla(p_dia.getDia_id(),p_dia.getPlanilla_id(),p_dia.getDia(),p_dia.getDistancia(),p_dia.getEntrenamiento(),p_dia.getTipo(),p_dia.getTiempo(),p_dia.getPace(),p_dia.getTerminado()));
    }

    public void deleteDiaPlanilla (){
        deleteLeads();
    }

    private void saveDiaPlanilla(DiaPlanilla lead) {
        leads.add(lead);
    }
    private void deleteLeads() {
        leads.clear();
    }

    public List<DiaPlanilla> getLeads() {
        return leads;
    }
}