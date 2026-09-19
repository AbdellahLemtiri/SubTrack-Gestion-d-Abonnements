package com.subtrack.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.subtrack.dao.AbonnementDAO;
import com.subtrack.dao.PaiementDAO;
import com.subtrack.entity.Abonnement;
import com.subtrack.entity.AbonnementAvecEngagement;
import com.subtrack.entity.Paiement;
import com.subtrack.entity.enums.StatutAbonnement;
import com.subtrack.entity.enums.StatutPaiement;
import com.subtrack.exception.BusinessValidationException;
import com.subtrack.exception.EntityNotFoundException;
import com.subtrack.service.AbonnementService;

public class AbonnementServiceImpl implements AbonnementService {

    private final AbonnementDAO abonnementDAO;
    private final PaiementDAO paiementDAO;

    public AbonnementServiceImpl(AbonnementDAO abonnementDAO, PaiementDAO paiementDAO) {
        this.abonnementDAO = abonnementDAO;
        this.paiementDAO = paiementDAO;
    }

    @Override
    public Abonnement creerAbonnement(Abonnement abonnement) {
        if (abonnement == null) {
            throw new BusinessValidationException("L'abonnement ne peut pas être null.");
        }
        if (abonnement.getMontantMensuel() <= 0) {
            throw new BusinessValidationException("Le montant mensuel doit être supérieur à 0.");
        }
        if (abonnement.getNomService() == null || abonnement.getNomService().trim().isEmpty()) {
            throw new BusinessValidationException("Le nom du service ne peut pas être vide.");
        }
        if (abonnement.getDateDebut() == null) {
            abonnement.setDateDebut(LocalDate.now());
        }

        abonnementDAO.create(abonnement);
        genererEcheances(abonnement.getId());
        return abonnement;
    }

    @Override
    public Abonnement modifierAbonnement(Abonnement abonnement) {
        if (abonnement == null) {
            throw new BusinessValidationException("L'abonnement ne peut pas être null.");
        }
        if (abonnement.getId() == null || abonnement.getId().trim().isEmpty()) {
            throw new BusinessValidationException("L'ID de l'abonnement est obligatoire pour la modification.");
        }
        if (abonnement.getMontantMensuel() <= 0) {
            throw new BusinessValidationException("Le montant mensuel doit être supérieur à 0.");
        }
        if (abonnement.getNomService() == null || abonnement.getNomService().trim().isEmpty()) {
            throw new BusinessValidationException("Le nom du service ne peut pas être vide.");
        }

        Abonnement abnmt = abonnementDAO.findById(abonnement.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Abonnement introuvable avec l'ID: " + abonnement.getId()));

        abnmt.setNomService(abonnement.getNomService());
        abnmt.setMontantMensuel(abonnement.getMontantMensuel());

        abonnementDAO.update(abnmt);
        return abnmt;
    }

    @Override
    public boolean supprimerAbonnement(String id) {
        Abonnement abnmt = abonnementDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Abonnement introuvable avec l'ID: " + id));

        paiementDAO.findByAbonnement(abnmt.getId())
                .forEach(p -> paiementDAO.delete(p.getIdPaiement()));

        return abonnementDAO.delete(abnmt.getId());
    }

    @Override
    public List<Abonnement> listerTous() {
        return abonnementDAO.findAll();
    }

    @Override
    public Abonnement trouverParId(String id) {
        return abonnementDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Abonnement introuvable avec l'ID: " + id));
    }

    @Override
    public void genererEcheances(String idAbonnement) {
        Abonnement abonnement = abonnementDAO.findById(idAbonnement)
                .orElseThrow(() -> new EntityNotFoundException("Abonnement introuvable avec l'ID: " + idAbonnement));

        int nbrMois = 1;
        if (abonnement instanceof AbonnementAvecEngagement) {
            nbrMois = ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois();
        }

        for (int i = 0; i < nbrMois; i++) {
            LocalDate echeance = abonnement.getDateDebut().plusMonths(i);
            Paiement paiement = new Paiement(idAbonnement, echeance, null, null, StatutPaiement.NON_PAYE);
            paiementDAO.create(paiement);
        }
    }

    @Override
    public List<Abonnement> listerActifs() {
        return abonnementDAO.findActiveSubscriptions();
    }

    @Override
    public List<Abonnement> listerParType(String type) {
        return abonnementDAO.findByType(type);
    }

    @Override
    public boolean resilierAbonnement(String id) {
        Abonnement abnmt = abonnementDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Abonnement introuvable avec l'ID: " + id));

        if (abnmt.getStatut() == StatutAbonnement.RESILIE) {
            throw new BusinessValidationException("Cet abonnement est déjà résilié.");
        }

        LocalDate dateResiliation = LocalDate.now();
        abnmt.setStatut(StatutAbonnement.RESILIE);
        abnmt.setDateFin(dateResiliation);
        abonnementDAO.update(abnmt);

        // Nettoyage des échéances futures non réglées
        paiementDAO.findByAbonnement(id).stream()
                .filter(p -> p.getStatut() == StatutPaiement.NON_PAYE)
                .filter(p -> p.getDateEcheance().isAfter(dateResiliation))
                .forEach(p -> paiementDAO.delete(p.getIdPaiement()));

        return true;
    }
}