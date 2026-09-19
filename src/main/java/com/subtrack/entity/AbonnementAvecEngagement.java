package com.subtrack.entity;

import java.time.LocalDate;

import com.subtrack.entity.enums.StatutAbonnement;

public class AbonnementAvecEngagement extends Abonnement {

    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String id, String nomService, double montantMensuel, LocalDate dateDebut,
            LocalDate dateFin, StatutAbonnement statut, int dureeEngagementMois) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
        this.dureeEngagementMois = dureeEngagementMois;
                
    }

    public int getDureeEngagementMois()
    {
        return dureeEngagementMois;
    }
    @Override 
    public String getTypeAbonnement() {
        return "abonnment avec engagement";
    }
}