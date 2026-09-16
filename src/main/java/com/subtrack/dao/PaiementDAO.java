package com.subtrack.dao;

import com.subtrack.entity.Paiement;

import java.util.List;
import java.util.Optional;

public interface PaiementDAO {
    Paiement create(Paiement paiement);
    Optional<Paiement> findById(String idPaiement);
    List<Paiement> findByAbonnement(String idAbonnement);
    List<Paiement> findAll();
    boolean update(Paiement paiement);
    boolean delete(String idPaiement);
    List<Paiement> findUnpaidByAbonnement(String idAbonnement);
    List<Paiement> findLastPayments(int limit);
}