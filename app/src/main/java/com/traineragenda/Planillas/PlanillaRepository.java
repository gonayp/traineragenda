package com.traineragenda.Planillas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Repositorio ficticio de leads
 */
public class PlanillaRepository {
    private static PlanillaRepository repository = new PlanillaRepository();
    private HashMap<Long, Planilla> leads = new HashMap<>();

    public static PlanillaRepository getInstance() {
        return repository;
    }

    private PlanillaRepository() {

    }

    public void addPlanilla(Planilla p_planilla){

        savePlanilla(new Planilla(p_planilla.getPlanilla_id(),p_planilla.getUsuario_id(),p_planilla.getFecha(),p_planilla.getTerminado(),p_planilla.getDatosAptitud(),
                p_planilla.getDatosObjetivo(),p_planilla.getDatosModalidad(),p_planilla.getDatosPeriodicidad(),p_planilla.getDatosPeriodo(),p_planilla.getDatosFrecuencia(),
                p_planilla.getDatosCooper(),p_planilla.getDatosFCmax(),p_planilla.getDatosPeso(),p_planilla.getDatosIMC(),p_planilla.getDatosBrazo(),p_planilla.getDatosCintura(),
                p_planilla.getDatosAbdominal(),p_planilla.getDatosQuadril(),p_planilla.getDatosCoxa(),p_planilla.getDatosKilometros(),p_planilla.getDatosPaceM(),p_planilla.getDatosPaceS(),
                p_planilla.getDatosTiempoM(),p_planilla.getDatosTiempoS()));
    }

    public void deletePlanilla (){
        deleteLeads();
    }

    private void savePlanilla(Planilla lead) {
        leads.put(lead.getPlanilla_id(), lead);
    }
    private void deleteLeads() {
        leads.clear();
    }

    public List<Planilla> getLeads() {
        return new ArrayList<>(leads.values());
    }
}