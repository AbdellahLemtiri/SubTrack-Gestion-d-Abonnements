package com.subtrack.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.subtrack.dao.AbonnementDAO;
import com.subtrack.dao.PaiementDAO;
import com.subtrack.entity.Abonnement;
import com.subtrack.entity.AbonnementAvecEngagement;
import com.subtrack.entity.Paiement;
import com.subtrack.entity.enums.StatutPaiement;
import com.subtrack.entity.enums.TypePaiement;
import com.subtrack.exception.BusinessValidationException;
import com.subtrack.exception.EntityNotFoundException;
import com.subtrack.service.PaiementService;

public class PaiementServiceImpl implements PaiementService {

    private final PaiementDAO paiementDAO;
    private final AbonnementDAO abonnementDAO;

    public PaiementServiceImpl(PaiementDAO paiementDAO, AbonnementDAO abonnementDAO) {
        this.abonnementDAO = abonnementDAO;
        this.paiementDAO = paiementDAO;
    }

    @Override
    public Paiement enregistrerPaiement(String idPaiement, LocalDate datePaiement, TypePaiement typePaiement) {
        Paiement pmnt = paiementDAO.findById(idPaiement)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'ID: " + idPaiement));

        if (pmnt.getStatut() == StatutPaiement.PAYE) {
            throw new BusinessValidationException("Cette échéance a déjà été réglée.");
        }

        pmnt.setDatePaiement(datePaiement != null ? datePaiement : LocalDate.now());
        pmnt.setTypePaiement(typePaiement);
        pmnt.setStatut(StatutPaiement.PAYE);

        paiementDAO.update(pmnt);
        return pmnt;
    }

    @Override
    public Paiement modifierPaiement(String idPaiement, LocalDate datePaiement, TypePaiement typePaiement,
            StatutPaiement statutPaiement) {
        Paiement pmnt = paiementDAO.findById(idPaiement)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'ID: " + idPaiement));

        if (datePaiement != null)
            pmnt.setDatePaiement(datePaiement);
        if (typePaiement != null)
            pmnt.setTypePaiement(typePaiement);
        if (statutPaiement != null)
            pmnt.setStatut(statutPaiement);

        paiementDAO.update(pmnt);
        return pmnt;
    }

    @Override
    public boolean supprimerPaiement(String id) {
        Paiement pmnt = paiementDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'ID: " + id));
        return paiementDAO.delete(pmnt.getIdPaiement());
    }

    @Override
    public List<Paiement> detecterImpayes() {
        LocalDate today = LocalDate.now();
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() != StatutPaiement.PAYE)
                .filter(p -> p.getDateEcheance().isBefore(today))
                .peek(p -> {
                    if (p.getStatut() != StatutPaiement.EN_RETARD) {
                        p.setStatut(StatutPaiement.EN_RETARD);
                        paiementDAO.update(p);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> detecterImpayesAvecEngagement() {
        List<Paiement> paiements = detecterImpayes().stream()
                .filter(p -> abonnementDAO.findById(p.getIdAbonnement())
                        .map(ab -> ab instanceof AbonnementAvecEngagement)
                        .orElse(false))
                .collect(Collectors.toList());

        double total = paiements.stream()
                .mapToDouble(p -> abonnementDAO.findById(p.getIdAbonnement())
                        .map(Abonnement::getMontantMensuel)
                        .orElse(0.0))
                .sum();

        Map<String, Object> resultat = new HashMap<>();
        resultat.put("paiements", paiements);
        resultat.put("total", total);
        return resultat;
    }

    @Override
    public List<Paiement> detecterImpayesParAbonnement(String idAbonnement) {
        return detecterImpayes().stream()
                .filter(p -> p.getIdAbonnement().equals(idAbonnement))
                .collect(Collectors.toList());
    }

    @Override
    public double calculerTotalImpayeParAbonnement(String idAbonnement) {
        Abonnement abo = abonnementDAO.findById(idAbonnement)
                .orElseThrow(() -> new EntityNotFoundException("Abonnement introuvable"));

        long countImpayes = detecterImpayesParAbonnement(idAbonnement).size();
        return countImpayes * abo.getMontantMensuel();
    }

    @Override
    public double calculerSommePayeeParAbonnement(String idAbonnement) {
        Abonnement abo = abonnementDAO.findById(idAbonnement)
                .orElseThrow(() -> new EntityNotFoundException("Abonnement introuvable"));

        long nbPayes = paiementDAO.findByAbonnement(idAbonnement).stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .count();

        return nbPayes * abo.getMontantMensuel();
    }

    @Override
    public List<Paiement> getDerniersPaiements(int limit) {
        return paiementDAO.findLastPayments(limit);
    }

    @Override
    public Map<String, Double> genererRapportMensuel() {
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE && p.getDatePaiement() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getDatePaiement().getYear() + "-"
                                + String.format("%02d", p.getDatePaiement().getMonthValue()),
                        Collectors.summingDouble(p -> abonnementDAO.findById(p.getIdAbonnement())
                                .map(Abonnement::getMontantMensuel)
                                .orElse(0.0))));
    }

    @Override
    public Map<Integer, Double> genererRapportAnnuel() {
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE && p.getDatePaiement() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getDatePaiement().getYear(),
                        Collectors.summingDouble(p -> abonnementDAO.findById(p.getIdAbonnement())
                                .map(Abonnement::getMontantMensuel)
                                .orElse(0.0))));
    }

    @Override
    public List<Paiement> genererRapportImpayes() {
        return detecterImpayes().stream()
                .sorted(Comparator.comparing(Paiement::getDateEcheance))
                .collect(Collectors.toList());
    }
}