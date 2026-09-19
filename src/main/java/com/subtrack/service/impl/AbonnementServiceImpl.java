package com.subtrack.service.impl;

import com.subtrack.dao.AbonnementDAO;
import com.subtrack.dao.PaiementDAO;
import com.subtrack.dao.impl.PaiementDAOImpl;
import com.subtrack.entity.Abonnement;
import com.subtrack.exception.BusinessValidationException;
import com.subtrack.exception.EntityNotFoundException;
import com.subtrack.exception.ValidationException;
import com.subtrack.service.AbonnementService;
import com.subtrack.entity.enums.TypePaiement;
import com.subtrack.entity.AbonnementAvecEngagement;
import com.subtrack.entity.AbonnementSansEngagement;

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
        abonnementDAO.creat(abonnement);
        genererEcheances(abonnement.getId());
        return abonnement;
    }

    @Override

    public void genererEcheances(String idAbonnement) throws EntityNotFoundException {

        Abonnement abonnement = abonnementDAO.findById(idAbonnement)
                .orElseThrow(() -> new EntityNotFoundException("Abonnement introuvable  " + idAbonnement));
        int nbrMois = 1;
        if (abonnement instanceof AbonnementAvecEngagement) {
            nbrMois = ((AbonnementAvecEngagement) abonnement).getDureeEngagementMois();
        }

        for (int m : nbrMois) {
            
        }
    }

}