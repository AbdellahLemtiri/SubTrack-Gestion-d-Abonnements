package com.subtrack.service;

import java.time.LocalDate;

import com.subtrack.entity.Paiement;
import com.subtrack.entity.enums.StatutPaiement;
import com.subtrack.entity.enums.TypePaiement;

import java.util.List;
import java.util.Map;

public interface PaiementService {

    Paiement enregistrerPaiement(String idPaiement, LocalDate datePaiement, TypePaiement typePaiement);

    Paiement modifierPaiement(String idPaiement, LocalDate datePaiement, TypePaiement typePaiement,
            StatutPaiement statutPaiement);

    boolean supprimerPaiement(String id);

    Map<String, Object> detectionEmpayes();

    double totalEmpayes();

    List<Paiement> detecterImpayesParAbonnement(String idAbonnement);

    double calculerTotalImpayeParAbonnement(String idAbonnement);

    double calculerSommePayeeParAbonnement(String idAbonnement);

    List<Paiement> getDerniersPaiements(int limit);

    Map<String, Double> genererRapportMensuel();

    Map<Integer, Double> genererRapportAnnuel();

    List<Paiement> genererRapportImpayes();
}