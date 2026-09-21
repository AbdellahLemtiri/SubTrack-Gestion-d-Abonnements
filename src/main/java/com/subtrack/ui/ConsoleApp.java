package com.subtrack.ui;

import com.subtrack.entity.Abonnement;

import com.subtrack.entity.Paiement;

import com.subtrack.entity.enums.TypePaiement;
import com.subtrack.exception.BusinessValidationException;
import com.subtrack.service.AbonnementService;
import com.subtrack.service.PaiementService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ConsoleApp {

    private final AbonnementService abonnementService;
    private final PaiementService paiementService;

    public ConsoleApp(AbonnementService abonnementService, PaiementService paiementService) {
        this.abonnementService = abonnementService;
        this.paiementService = paiementService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            ConsoleUtils.clearScreen();
            System.out.println("=================================================");
            System.out.println("                     SUBTRACK                    ");
            System.out.println("=================================================");
            System.out.println(" 1. - Gestion des Abonnements");
            System.out.println(" 2. - Gestion des Paiements & Échéances");
            System.out.println(" 3. - Rapports Financiers & Statistiques");
            System.out.println(" 0. - Quitter l'application");
            System.out.println("=================================================");

            int choice = ConsoleUtils.readInt("Votre choix : ");
            switch (choice) {
                case 1:
                    menuAbonnements();
                    break;
                case 2:
                    menuPaiements();
                    break;
                case 3:
                    menuRapports();
                    break;
                case 0:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Option non reconnue.");
                    ConsoleUtils.pause();
            }
        }
    }

    // =================  ABONNEMENTS ==========
    private void menuAbonnements() {
        boolean back = false;
        while (!back) {
            ConsoleUtils.clearScreen();
            System.out.println(" [ GESTION DES ABONNEMENTS ] ");
            System.out.println("1. Créer un abonnement");
            System.out.println("2. Consulter tous les abonnements");
            System.out.println("3. Modifier un abonnement");
            System.out.println("4. Résilier un abonnement");
            System.out.println("5. Supprimer un abonnement");
            System.out.println("0. Retour au menu principal");

            int choice = ConsoleUtils.readInt("Votre choix : ");
            try {
                switch (choice) {
                    case 1:
                        creerAbonnement();
                        break;
                    case 2:
                        listerAbonnements();
                        break;
                    case 3:
                        modifierAbonnement();
                        break;
                    case 4:
                        resilierAbonnement();
                        break;
                    case 5:
                        supprimerAbonnement();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("\n Erreur : " + e.getMessage());
            }
            if (!back)
                ConsoleUtils.pause();
        }
    }

    private void creerAbonnement() {
        System.out.println("\n Nouveau abonnement ");
        String nom = ConsoleUtils.readString("Nom du service  : ");
        double montant = ConsoleUtils.readDouble("Montant mensuel (MAD) : ");
        LocalDate dateDebut = ConsoleUtils.readDate("Date de début (YYYY-MM-DD)", true);

        System.out.println("Type d'engagement :");
        System.out.println("1. Avec engagement");
        System.out.println("2. Sans engagement");
        int type = ConsoleUtils.readInt("Choix : ");

        try {
            if (type == 1) {
                int mois = ConsoleUtils.readInt("Durée d'engagement (en mois) : ");
                abonnementService.creerAbonnementAvecEngagement(nom, montant, dateDebut, mois);
            } else {
                abonnementService.creerAbonnementSansEngagement(nom, montant, dateDebut);
            }
            System.out.println(" Abonnement créé avec succès avec génération des échéances !");
        } catch (BusinessValidationException e) {
            System.err.println(" Erreur: " + e.getMessage());
        }
    }

    private void listerAbonnements() {
        System.out.println("\n Liste de tous les abonnements ");
        List<Abonnement> list = abonnementService.listerTous();
        if (list.isEmpty()) {
            System.out.println("Aucun abonnement trouvé.");
            return;
        }
        list.forEach(System.out::println);
    }

    private void modifierAbonnement() {
        String id = ConsoleUtils.readString("ID de l'abonnement à modifier : ");
        Abonnement existing = abonnementService.trouverParId(id);
        System.out.println("Trouvé : " + existing.getNomService() + " (" + existing.getMontantMensuel() + " MAD)");

        String newNom = ConsoleUtils.readString("Nouveau nom (" + existing.getNomService() + ") : ");
        double newMontant = ConsoleUtils.readDouble("Nouveau montant mensuel : ");

        existing.setNomService(newNom.isEmpty() ? existing.getNomService() : newNom);
        existing.setMontantMensuel(newMontant);
        abonnementService.modifierAbonnement(existing);
        System.out.println(" Abonnement mis à jour !");
    }

    private void resilierAbonnement() {
        String id = ConsoleUtils.readString("ID de l'abonnement à résilier : ");
        abonnementService.resilierAbonnement(id);
        System.out.println(" Abonnement résilié et échéances futures nettoyées !");
    }

    private void supprimerAbonnement() {
        String id = ConsoleUtils.readString("ID de l'abonnement à supprimer définitivement : ");
        abonnementService.supprimerAbonnement(id);
        System.out.println(" Abonnement et échéances associées supprimés !");
    }

    // =================  PAIEMENTS =======
    private void menuPaiements() {
        boolean back = false;
        while (!back) {
            ConsoleUtils.clearScreen();
            System.out.println(" [ GESTION DES PAIEMENTS ] ");
            System.out.println("1. Enregistrer un règlement");
            System.out.println("2. Consulter les paiements d'un abonnement");
            System.out.println("3. Afficher les 5 derniers paiements");
            System.out.println("4. Consulter les impayés (abonnements avec engagement)");
            System.out.println("5. Afficher la somme totale payée pour un abonnement");
            System.out.println("6. Supprimer un paiement");
            System.out.println("0. Retour au menu principal");

            int choice = ConsoleUtils.readInt(" Votre choix : ");
            try {
                switch (choice) {
                    case 1:
                        enregistrerPaiement();
                        break;
                    case 2:
                        listerPaiementsAbonnement();
                        break;
                    case 3:
                        afficherDerniersPaiements();
                        break;
                    case 4:
                        afficherImpayesEngagement();
                        break;
                    case 5:
                        afficherSommePayee();
                        break;
                    case 6:
                        supprimerPaiement();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("\n Erreur : " + e.getMessage());
            }
            if (!back)
                ConsoleUtils.pause();
        }
    }

    private void enregistrerPaiement() {
        String idPaiement = ConsoleUtils.readString("ID de l'échéance/paiement : ");
        LocalDate datePaiement = ConsoleUtils.readDate("Date du règlement (YYYY-MM-DD)", true);

        System.out.println("Mode de règlement :");
        System.out.println("1. CARTE_BANCAIRE");
        System.out.println("2. VIREMENT");
        System.out.println("3. PRELEVEMENT_AUTOMATIQUE");
        System.out.println("4. ESPECES");
        int m = ConsoleUtils.readInt("Choix : ");

        TypePaiement type = TypePaiement.CARTE_BANCAIRE;
        if (m == 2)
            type = TypePaiement.VIREMENT;
        else if (m == 3)
            type = TypePaiement.PRELEVEMENT_AUTOMATIQUE;
        else if (m == 4)
            type = TypePaiement.ESPECES;

        paiementService.enregistrerPaiement(idPaiement, datePaiement, type);
        System.out.println(" Paiement validé et acquitté !");
    }

    private void listerPaiementsAbonnement() {
        String idAbo = ConsoleUtils.readString("ID de l'abonnement : ");
        List<Paiement> list = paiementService.listerParAbonnement(idAbo);
        if (list.isEmpty()) {
            System.out.println("Aucun paiement trouvé.");
            return;
        }
        list.forEach(System.out::println);
    }

    private void afficherDerniersPaiements() {
        System.out.println("\n Les 5 derniers règlements / échéances ");
        List<Paiement> list = paiementService.getDerniersPaiements(5);
        if (list.isEmpty()) {
            System.out.println("Aucun historique disponible.");
            return;
        }
        list.forEach(System.out::println);
    }

    private void afficherImpayesEngagement() {
        System.out.println("\n Retards & Impayés (Contrats avec Engagement) ");
        Map<String, Object> res = paiementService.detecterImpayesAvecEngagement();
        List<Paiement> liste = (List<Paiement>) res.get("paiements");
        double total = (double) res.get("total");

        if (liste.isEmpty()) {
            System.out.println("Aucun impayé constaté.");
            return;
        }
        liste.forEach(System.out::println);
        System.out.printf("\n Total impayé exigible : %.2f MAD\n", total);
    }

    private void afficherSommePayee() {
        String idAbo = ConsoleUtils.readString("ID de l'abonnement : ");
        double total = paiementService.calculerSommePayeeParAbonnement(idAbo);
        System.out.printf("Total réglé à ce jour pour cet abonnement : %.2f MAD\n", total);
    }

    private void supprimerPaiement() {
        String idP = ConsoleUtils.readString("ID du paiement : ");
        paiementService.supprimerPaiement(idP);
        System.out.println(" Paiement supprimé.");
    }

    // ===========  RAPPORTS ============
    private void menuRapports() {
        boolean back = false;
        while (!back) {
            ConsoleUtils.clearScreen();
            System.out.println("  RAPPORTS FINANCIERS & STATISTIQUE  ");
            System.out.println("1. Dépenses réelles par mois ");
            System.out.println("2. Dépenses réelles par année  ");
            System.out.println("3. Rapport global des impayés");
            System.out.println("0. Retour au menu principal");

            int choice = ConsoleUtils.readInt(" Votre choix : ");
            try {
                switch (choice) {
                    case 1:
                        System.out.println("\n Total Payé par Mois ");
                        paiementService.genererRapportMensuel()
                                .forEach((mois, somme) -> System.out.printf("Mois %s : %.2f MAD\n", mois, somme));
                        break;
                    case 2:
                        System.out.println("\n Total Payé par Année ");
                        paiementService.genererRapportAnnuel()
                                .forEach((annee, somme) -> System.out.printf("Année %d : %.2f MAD\n", annee, somme));
                        break;
                    case 3:
                        System.out.println("\n Liste globale de tous les impayés ");
                        List<Paiement> impayes = paiementService.genererRapportImpayes();
                        if (impayes.isEmpty())
                            System.out.println("Aucun impayé.");
                        else
                            impayes.forEach(System.out::println);
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println(" Erreur : " + e.getMessage());
            }
            if (!back)
                ConsoleUtils.pause();
        }
    }
}