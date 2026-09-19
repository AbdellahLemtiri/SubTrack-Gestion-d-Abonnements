package com.subtrack.dao.impl;

import com.subtrack.dao.PaiementDAO;
import com.subtrack.entity.Paiement;
import com.subtrack.entity.enums.StatutPaiement;

import java.util.*;
import java.util.stream.Collectors;

public class PaiementDAOImpl implements PaiementDAO {

    private final Map<String, Paiement> storage = new HashMap<>();

    @Override
    public Paiement create(Paiement paiement) {
        if (paiement == null) {
            throw new IllegalArgumentException("Le paiement ne peut pas être null.");
        }
        storage.put(paiement.getIdPaiement(), paiement);
        return paiement;
    }

    @Override
    public Optional<Paiement> findById(String idPaiement) {
        return Optional.ofNullable(storage.get(idPaiement));
    }

    @Override
    public List<Paiement> findByAbonnement(String idAbonnement) {
        return storage.values().stream()
                .filter(p -> p.getIdAbonnement().equals(idAbonnement))
                .collect(Collectors.toList());
    }

    @Override
    public List<Paiement> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean update(Paiement paiement) {
        if (paiement == null || !storage.containsKey(paiement.getIdPaiement())) {
            return false;
        }
        storage.put(paiement.getIdPaiement(), paiement);
        return true;
    }

    @Override
    public boolean delete(String idPaiement) {
        return storage.remove(idPaiement) != null;
    }


    @Override
    public List<Paiement> findUnpaidByAbonnement(String idAbonnement) {
        return storage.values().stream().filter(p -> p.getIdAbonnement().equals(idAbonnement))
                .filter(p -> p.getStatut() != StatutPaiement.PAYE).collect(Collectors.toList());
    }

    
    @Override
    public List<Paiement> findLastPayments(int limit) {
        return storage.values().stream()
                .sorted(Comparator.comparing(Paiement::getDateEcheance).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}