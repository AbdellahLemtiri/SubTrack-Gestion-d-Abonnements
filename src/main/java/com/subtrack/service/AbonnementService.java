package com.subtrack.service;

import java.util.List;
import java.util.Optional;

import com.subtrack.entity.Abonnement;

public interface AbonnementService {

    Abonnement creerAbonnement(Abonnement abonnement);

    Abonnement modifierAbonnement(Abonnement abonnement);

    boolean supprimerAbonnement(String id);

    boolean resilierAbonnement(String id);

    Abonnement trouverParId(String id);

    List<Abonnement> listerTous();

    List<Abonnement> listerActifs();

    List<Abonnement> listerParType(String type);

    void genererEcheances(String idAbonnement);
}