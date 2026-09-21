package com.subtrack.service;

import java.time.LocalDate;
import java.util.List;


import com.subtrack.entity.Abonnement;

public interface AbonnementService {

    Abonnement creerAbonnementAvecEngagement(String nomService, double montantMensuel, LocalDate dateDebut,
            int dureeEngagementMois);

    Abonnement creerAbonnementSansEngagement(String nomService, double montantMensuel, LocalDate dateDebut);

    Abonnement modifierAbonnement(Abonnement abonnement);

    boolean supprimerAbonnement(String id);

    boolean resilierAbonnement(String id);

    Abonnement trouverParId(String id);

    List<Abonnement> listerTous();

    List<Abonnement> listerActifs();

    List<Abonnement> listerParType(String type);

    void genererEcheances(String idAbonnement);
}