package com.subtrack.entity;
import java.time.LocalDate;
import com.subtrack.entity.enums.StatutAbonnement;
public class AbonnementSansEngagement extends Abonnement
{
    public AbonnementSansEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatutAbonnement statut) 
    {
        super(nomService, montantMensuel,dateDebut,dateFin,statut);
    }

    @Override 
    public String getTypeAbonnement()
    {
        return "abonment sans engagement";
    }
}