package com.subtrack;

import com.subtrack.dao.AbonnementDAO;
import com.subtrack.dao.PaiementDAO;
import com.subtrack.dao.impl.AbonnementDAOImpl;
import com.subtrack.dao.impl.PaiementDAOImpl;
import com.subtrack.service.AbonnementService;
import com.subtrack.service.PaiementService;
import com.subtrack.service.impl.AbonnementServiceImpl;
import com.subtrack.service.impl.PaiementServiceImpl;
import com.subtrack.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        
        AbonnementDAO abonnementDAO = new AbonnementDAOImpl();
        PaiementDAO paiementDAO = new PaiementDAOImpl();

        AbonnementService abonnementService = new AbonnementServiceImpl(abonnementDAO, paiementDAO);
        PaiementService paiementService = new PaiementServiceImpl(paiementDAO, abonnementDAO);

        ConsoleApp app = new ConsoleApp(abonnementService, paiementService);
        app.start();
    }
}