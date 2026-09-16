package com.subtrack.entity;

import com.subtrack.entity.enums.StatutPaiement;
import com.subtrack.entity.enums.TypePaiement;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Paiement {
    private String idPaiement;
    private String idAbonnement;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private TypePaiement typePaiement;
    private StatutPaiement statut;

    public Paiement(String idAbonnement, LocalDate dateEcheance, LocalDate datePaiement, TypePaiement typePaiement,
            StatutPaiement statut) {
        this.idPaiement = UUID.randomUUID().toString();
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statut = statut;
    }

    public Paiement(String idPaiement, String idAbonnement, LocalDate dateEcheance, LocalDate datePaiement,
            TypePaiement typePaiement, StatutPaiement statut) {
        this.idPaiement = idPaiement;
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statut = statut;
    }

    public String getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(String idPaiement) {
        this.idPaiement = idPaiement;
    }

    public String getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(String idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public TypePaiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Paiement paiement = (Paiement) o;
        return Objects.equals(idPaiement, paiement.idPaiement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPaiement);
    }

    @Override
    public String toString() {
        String result = "ID: " + idPaiement + " | AboID: " + idAbonnement + " | Échéance: " + dateEcheance
                + " | Statut: " + statut;
        if (datePaiement != null) {
            result += " | Payé le: " + datePaiement;
        }
        if (typePaiement != null) {
            result += " | Mode: " + typePaiement;
        }
        return result;
    }
}