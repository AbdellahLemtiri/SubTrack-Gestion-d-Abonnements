package com.subtrack.dao;

import com.subtrack.entity.Abonnement;
import java.util.Optional;

public innterface AbonnementDAO
 {

    Abonnement creat(Abonnement abonnement);
    Optional<Abonnement> findById(String id);
    List<Abonnement>findAll();
    boolean update(Abonnement abonnement);
    boolean delete(String id);
    List<Abonnement> findActiveSubscriptions();
    List<Abonnement> findByType(String type);
 }