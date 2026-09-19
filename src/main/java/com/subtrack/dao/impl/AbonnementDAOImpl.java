package com.subtrack.dao.impl;

import com.subtrack.dao.AbonnementDAO;
import com.subtrack.entity.Abonnement;
import com.subtrack.entity.enums.StatutAbonnement;

import java.util.*;
import java.util.stream.Collectors;

public class AbonnementDAOImpl implements AbonnementDAO {

    private final Map<String, Abonnement> storage = new HashMap<>();

    @Override
    public Abonnement create(Abonnement abonnement) {
        if (abonnement == null) {
            throw new IllegalArgumentException("L'abonnement ne peut pas être null.");
        }
        storage.put(abonnement.getId(), abonnement);
        return abonnement;
    }

    @Override
    public Optional<Abonnement> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Abonnement> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean update(Abonnement abonnement) {
        if (abonnement == null || !storage.containsKey(abonnement.getId())) {
            return false;
        }
        storage.put(abonnement.getId(), abonnement);
        return true;
    }

    @Override
    public boolean delete(String id) {
        if(storage.containsKey(id))
        {
            storage.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Abonnement> findActiveSubscriptions() {
        return storage.values().stream().filter(a -> a.getStatut() == StatutAbonnement.ACTIVE).collect(Collectors.toList());    
    }


    @Override
    public List<Abonnement> findByType(String type) {
        return storage.values().stream()
                .filter(a -> a.getTypeAbonnement().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }
}