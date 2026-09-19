package com.subtrack.service;

import java.util.List;

import com.subtrack.entity.Abonnement;
import com.subtrack.entity.Paiement;

public interface PaiementService {

    public Paiement enregistrerPaiement();
    public Paiement modifierPaiement();
    public boolean supprimerPaiement(String id);
    public List<Abonnement> detectionEmpayes();


    
}