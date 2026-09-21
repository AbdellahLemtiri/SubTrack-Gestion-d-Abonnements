package com.subtrack.entity;
import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement
{
    public AbonnementSansEngagement(String nomService, double montantMensuel, LocalDate dateDebut) 
    {
        super(nomService, montantMensuel,dateDebut,null);
    }

    @Override 
    public String getTypeAbonnement()
    {
        return "abonment sans engagement";
    }
}