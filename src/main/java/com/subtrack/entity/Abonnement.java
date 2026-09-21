package com.subtrack.entity;

import com.subtrack.entity.enums.StatutAbonnement;  

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public abstract class Abonnement {
    protected String id;
    protected String nomService;
    protected double montantMensuel;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected StatutAbonnement statut;

    public Abonnement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin) {
        this.id = UUID.randomUUID().toString();
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = StatutAbonnement.ACTIVE;
    }

    public Abonnement(String id, String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatutAbonnement statut) {
        this.id = id;
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public abstract String getTypeAbonnement();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public double getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(double montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutAbonnement getStatut() {
        return statut;
    }

    public void setStatut(StatutAbonnement statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abonnement that = (Abonnement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ID: " + id + 
               "  Service: " + nomService + 
               "  Type: " + getTypeAbonnement() + 
               "  Montant: " + String.format("%.2f", montantMensuel) + " MAD" +
               "  Début: " + dateDebut + 
               "  Fin: " + (dateFin != null ? dateFin : "Indéfinie") + 
               "  Statut: " + statut;
    }
}