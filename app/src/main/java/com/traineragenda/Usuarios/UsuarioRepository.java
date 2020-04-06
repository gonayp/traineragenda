package com.traineragenda.Usuarios;

import com.traineragenda.Compromisos.Compromiso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Repositorio ficticio de leads
 */
public class UsuarioRepository {
    private static UsuarioRepository repository = new UsuarioRepository();
    private HashMap<String, Usuario> leads = new HashMap<>();

    public static UsuarioRepository getInstance() {
        return repository;
    }

    private UsuarioRepository() {

    }

    public void addUsuario(Usuario p_usuario){

        saveUsuario(new Usuario(p_usuario.getUsuarioID(),p_usuario.getAge(),p_usuario.getGender(), p_usuario.getUserName(),p_usuario.getNombre(),p_usuario.getApellido(),
                p_usuario.getCelular(),  p_usuario.getNacimiento(), p_usuario.getAlta(),p_usuario.getTipo(),p_usuario.getLatitud(),p_usuario.getLongitud(),p_usuario.getRadio(),
                p_usuario.getPlanillas(),p_usuario.getUlt_planilla(), p_usuario.getPeso(),p_usuario.getAltura(),p_usuario.getCorridaCuantoTiempo(),p_usuario.getCorridaCantidad(),p_usuario.getCorridaMotivacion(),p_usuario.getCorridaVecesSemana(),
                p_usuario.getCorridaDistancia(),p_usuario.getCorridaRitmo(),p_usuario.getCorridaLugar(),p_usuario.getCorridaTurno(),p_usuario.getClinicaColesterol(),p_usuario.getClinicaCardiopatia(),
                p_usuario.getClinicaHistoricoCardiaco(),p_usuario.getClinicaHipertenso(),p_usuario.getClinicaDiabetico(),p_usuario.getClinicaPulmones(),p_usuario.getClinicaMedicamentos(),p_usuario.getClinicaCirugia(),
                p_usuario.getClinicaDolores(),p_usuario.getClinicaRecomendaciones(),p_usuario.getClinicaColesterolCual(),p_usuario.getClinicaCardiopatiaCual(),p_usuario.getClinicaHistoricoCardiacoCual(),
                p_usuario.getClinicaHipertensoCual(),p_usuario.getClinicaDiabeticoCual(),p_usuario.getClinicaPulmonesCual(),p_usuario.getClinicaMedicamentosCual(),p_usuario.getClinicaCirugiaCual(),
                p_usuario.getClinicaDoloresCual(),p_usuario.getClinicaRecomendacionesCual()));
    }

    public void deleteUsuarios (){
        deleteLeads();
    }

    private void saveUsuario(Usuario lead) {
        leads.put(lead.getUsuarioID(), lead);
    }
    private void deleteLeads() {
        leads.clear();
    }

    public List<Usuario> getLeads() {
        return new ArrayList<>(leads.values());
    }
}