package com.subtrack.entity;

import java.time.LocalDate;


public class AbonnementAvecEngagement extends Abonnement {

    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService, double montantMensuel, LocalDate dateDebut,
            LocalDate dateFin,  int dureeEngagementMois) {
        super(nomService, montantMensuel, dateDebut, dateFin);
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