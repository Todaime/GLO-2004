/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static virtubois.domain.Commun.Constantes.VALEUR_UN;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;
/**
 *
 * @author maximeprieur
 */
public class VirtuBoisControleur implements Serializable
{
    private Chargeuse chargeuse;
    private double facteurDeZoom;
    private List<Paquet> paquets;
    private int indicePaquetSelectionne;
    private List<Integer> indicePaquetsSelectionnes;
    private boolean etatDeplacementChargeuse;
    
    public VirtuBoisControleur() {
        this.indicePaquetSelectionne = -1;
        this.chargeuse = new Chargeuse();
        this.facteurDeZoom = VALEUR_UN;
        this.paquets = new ArrayList<>();
        this.indicePaquetsSelectionnes = new ArrayList<>();
    }

    // Contructeur copie pour undo redo
    public VirtuBoisControleur(VirtuBoisControleur controleurActuel) {
        this.chargeuse = new Chargeuse(controleurActuel.chargeuse);
        this.facteurDeZoom = controleurActuel.facteurDeZoom;
        this.paquets = new ArrayList<>();
        this.indicePaquetSelectionne = controleurActuel.indicePaquetSelectionne;
        List<Paquet> lesPaquetsActuels = controleurActuel.obtenirPaquets();

        for (int i = 0; i < lesPaquetsActuels.size(); i++) {
            paquets.add(new Paquet(lesPaquetsActuels.get(i)));
        }

        this.indicePaquetsSelectionnes = new ArrayList<>();
        
        if (this.indicePaquetSelectionne != -1) {
            this.indicePaquetsSelectionnes.add(this.indicePaquetSelectionne);
        }
    }

    public List<Paquet> obtenirPaquets() {
        return paquets;
    }

    public Chargeuse obtenirChargeuse() {
        return chargeuse;
    }

    public double obtenirFacteurDeZoom() {
       return facteurDeZoom;
    }

    public void fixerIndicePaquetSelectionne(int indice) {
        indicePaquetSelectionne = indice;
    }

    public int obtenirIndicePaquetSelectionne() {
        return indicePaquetSelectionne;
    }

    public void fixerIndicesPaquetsSelectionnes(List<Integer> indices) {
        this.indicePaquetsSelectionnes = indices;
    }

    public List<Integer> obtenirIndicesPaquetsSelectionnes() {
        return indicePaquetsSelectionnes;
    }

    public void ajouterPaquet(Paquet paquet) {
        this.paquets.add(paquet);
    }
    
    public Paquet obtenirPaquetSelectionne() {
        return this.paquets.get(this.indicePaquetSelectionne);
    }
    
    public void modifierHauteurPaquet(double hauteurPaquet) {
        if (this.paquetEstSelectionne()) {
            Paquet paquet = this.obtenirPaquetSelectionne();
            double ancienneHauteurPaquet = paquet.obtenirHauteur();
            
            paquet.fixerHauteur(hauteurPaquet);
            paquet.modifierPositionZPaquetsDansPile(this.paquets, paquet.obtenirPositionZ(), ancienneHauteurPaquet, hauteurPaquet);
        }
    }
    
    public void modifierProduitPaquet(String produitPaquet) {
        if (this.paquetEstSelectionne()) {
            this.obtenirPaquetSelectionne().fixerProduit(produitPaquet);
        }
    }
    
    public void modifierAnglePaquet(double anglePaquet) {
        if (this.paquetEstSelectionne()) {
            Paquet paquet = this.obtenirPaquetSelectionne();
            Paquet ancienPaquet = new Paquet(paquet);
            List<Integer> indicePaquetsAuDessus = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
            String codeBarrePaquetSuperieur = paquet.obtenirCodeBarrePaquetSuperieur(this.paquets);
            
            paquet.fixerAngle(anglePaquet);
            
            if (indicePaquetsAuDessus.size() > VALEUR_ZERO) {
                List<Integer> indicePaquetsAuDessusApresModification = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
                
                if (indicePaquetsAuDessusApresModification.size() > VALEUR_ZERO) {
                    String codeBarreNouveauPaquetSuperieur = this.paquets.get(indicePaquetsAuDessusApresModification.get(VALEUR_ZERO)).obtenirCodeBarre();
                    
                    if (!codeBarrePaquetSuperieur.equals(codeBarreNouveauPaquetSuperieur)) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                        
                        if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                            ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                        }
                    }
                    else if (codeBarrePaquetSuperieur.equals("") && codeBarreNouveauPaquetSuperieur.equals("")) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                    }
                }
                else {
                    paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                    paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));

                    if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                        ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                    }
                }
            }
            else {
                paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
            }
        }
    }
    
    public void modifierLongueurPaquet(double longueurPaquet) {
        if (this.paquetEstSelectionne()) {
            Paquet paquet = this.obtenirPaquetSelectionne();
            Paquet ancienPaquet = new Paquet(paquet);
            List<Integer> indicePaquetsAuDessus = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
            String codeBarrePaquetSuperieur = paquet.obtenirCodeBarrePaquetSuperieur(this.paquets);
            
            paquet.fixerLongueur(longueurPaquet);
            
            if (indicePaquetsAuDessus.size() > VALEUR_ZERO) {
                List<Integer> indicePaquetsAuDessusApresModification = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
                
                if (indicePaquetsAuDessusApresModification.size() > VALEUR_ZERO) {
                    String codeBarreNouveauPaquetSuperieur = this.paquets.get(indicePaquetsAuDessusApresModification.get(VALEUR_ZERO)).obtenirCodeBarre();
                    
                    if (!codeBarrePaquetSuperieur.equals(codeBarreNouveauPaquetSuperieur)) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                        
                        if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                            ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                        }
                    }
                    else if (codeBarrePaquetSuperieur.equals("") && codeBarreNouveauPaquetSuperieur.equals("")) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                    }
                }
                else {
                    paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                    paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));

                    if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                        ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                    }
                }
            }
            else {
                paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
            }
        }
    }
    
    public void modifierLargeurPaquet(double largeurPaquet) {
        if (this.paquetEstSelectionne()) {
            Paquet paquet = this.obtenirPaquetSelectionne();
            Paquet ancienPaquet = new Paquet(paquet);
            List<Integer> indicePaquetsAuDessus = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
            String codeBarrePaquetSuperieur = paquet.obtenirCodeBarrePaquetSuperieur(this.paquets);
            
            paquet.fixerLargeur(largeurPaquet);
            
            if (indicePaquetsAuDessus.size() > VALEUR_ZERO) {
                List<Integer> indicePaquetsAuDessusApresModification = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
                
                if (indicePaquetsAuDessusApresModification.size() > VALEUR_ZERO) {
                    String codeBarreNouveauPaquetSuperieur = this.paquets.get(indicePaquetsAuDessusApresModification.get(VALEUR_ZERO)).obtenirCodeBarre();
                    
                    if (!codeBarrePaquetSuperieur.equals(codeBarreNouveauPaquetSuperieur)) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                        
                        if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                            ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                        }
                    }
                    else if (codeBarrePaquetSuperieur.equals("") && codeBarreNouveauPaquetSuperieur.equals("")) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                    }
                }
                else {
                    paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                    paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));

                    if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                        ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                    }
                }
            }
            else {
                paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
            }
        }
    }
    
    public void modifierPositionXPaquet(double positionXPaquet) {
        if (this.paquetEstSelectionne()) {
            Paquet paquet = this.obtenirPaquetSelectionne();
            Paquet ancienPaquet = new Paquet(paquet);
            List<Integer> indicePaquetsAuDessus = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
            String codeBarrePaquetSuperieur = paquet.obtenirCodeBarrePaquetSuperieur(this.paquets);

            paquet.fixerPositionX(positionXPaquet);
            
            if (indicePaquetsAuDessus.size() > VALEUR_ZERO) {
                List<Integer> indicePaquetsAuDessusApresModification = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
                
                if (indicePaquetsAuDessusApresModification.size() > VALEUR_ZERO) {
                    String codeBarreNouveauPaquetSuperieur = this.paquets.get(indicePaquetsAuDessusApresModification.get(VALEUR_ZERO)).obtenirCodeBarre();
                    
                    if (!codeBarrePaquetSuperieur.equals(codeBarreNouveauPaquetSuperieur)) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                        
                        if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                            ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                        }
                    }
                    else if (codeBarrePaquetSuperieur.equals("") && codeBarreNouveauPaquetSuperieur.equals("")) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                    }
                }
                else {
                    paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                    paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));

                    if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                        ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                    }
                }
            }
            else {
                paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
            }
        }
    }
    
    public void modifierPositionYPaquet(double positionYPaquet) {
        if (this.paquetEstSelectionne()) {
            Paquet paquet = this.obtenirPaquetSelectionne();
            Paquet ancienPaquet = new Paquet(paquet);
            List<Integer> indicePaquetsAuDessus = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
            String codeBarrePaquetSuperieur = paquet.obtenirCodeBarrePaquetSuperieur(this.paquets);
            
            paquet.fixerPositionY(positionYPaquet);
            
            if (indicePaquetsAuDessus.size() > VALEUR_ZERO) {
                List<Integer> indicePaquetsAuDessusApresModification = paquet.obtenirPaquetsEnContactAuDessus(this.paquets);
                
                if (indicePaquetsAuDessusApresModification.size() > VALEUR_ZERO) {
                    String codeBarreNouveauPaquetSuperieur = this.paquets.get(indicePaquetsAuDessusApresModification.get(VALEUR_ZERO)).obtenirCodeBarre();
                    
                    if (!codeBarrePaquetSuperieur.equals(codeBarreNouveauPaquetSuperieur)) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                        
                        if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                            ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                        }
                    }
                    else if (codeBarrePaquetSuperieur.equals("") && codeBarreNouveauPaquetSuperieur.equals("")) {
                        paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
                    }
                }
                else {
                    paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                    paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));

                    if (paquetEstAuDessusPile(this.indicePaquetSelectionne)) {
                        ancienPaquet.modifierPaquetsDansPileApresDeplacement(this.paquets, indicePaquetsAuDessus);
                    }
                }
            }
            else {
                paquet.fixerEtage(paquet.obtenirEtagePaquet(this.paquets));
                paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(this.paquets));
            }
        }
    }
    
    public void modifierPositionXChargeuse(double positionXChargeuse) {
        this.chargeuse.modifierPositionX(positionXChargeuse);
    }
    
    public void modifierPositionYChargeuse(double positionYChargeuse) {
        this.chargeuse.modifierPositionY(positionYChargeuse);
    }
    
    public void modifierLongueurChargeuse(double longueurChargeuse) {
        this.chargeuse.modifierLongueur(longueurChargeuse);
    }
    
    public void modifierLargeurChargeuse(double largeurChargeuse) {
        this.chargeuse.modifierLargeur(largeurChargeuse);
    }
    
    public void modifierAngleChargeuse(double angleChargeuse) {
        this.chargeuse.modifierAngle(angleChargeuse);
    }
    
    public void modifierHauteurBrasChargeuse(double hauteurBrasChargeuse) {
        this.chargeuse.modifierHauteurBras(hauteurBrasChargeuse);
    }
    
    public void modifierLongueurBrasChargeuse(double longueurBrasChargeuse) {
        this.chargeuse.modifierLongueurBras(longueurBrasChargeuse);
    }
    
    public void modifierLargeurBrasChargeuse(double largeurBrasChargeuse) {
        this.chargeuse.modifierLargeurBras(largeurBrasChargeuse);
    }
    
    public int obtenirEtagePaquetSelectionne() {
        return this.paquets.get(this.indicePaquetSelectionne).obtenirEtage();
    }
    
    public double obtenirPositionZPaquetSelectionne() {
        return this.paquets.get(this.indicePaquetSelectionne).obtenirPositionZ();
    }
    
    public void supprimerDernierPaquet() {
       paquets.remove(paquets.size()-1);
    }

    public boolean paquetEstAuDessusPile(int indice) {
        boolean estAuDessus = true;
        Paquet paquet = paquets.get(indice);
        int etage = paquet.obtenirEtage();

        // Segments representant les cotés du paquet
        Line2D.Double coteHaut = new Line2D.Double(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY(),
                                                   paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY(),
                                                    paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY(),
                                                  paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY(),
                                                     paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY());
        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (!autrePaquet.obtenirCodeBarre().equals(paquet.obtenirCodeBarre()) &&  etage <= autrePaquet.obtenirEtage()) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if ((autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.positionX, paquet.positionY)) && etage < autrePaquet.obtenirEtage()) {
                    estAuDessus = false;
                }
                // Sinon on regarde si les cotes se croisent
                else if ((coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                        && etage < autrePaquet.obtenirEtage())
                {
                    estAuDessus = false;
                }
            }
        }
        return estAuDessus;
    }

    public Paquet obtenirInformationPaquetAuDessous(Paquet paquet) {
        Paquet paquetAuDessous = paquet;
        int etage = VALEUR_ZERO;

        Line2D.Double coteHaut = new Line2D.Double(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY(),
                                                   paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY(),
                                                    paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY(),
                                                  paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY(),
                                                     paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (!autrePaquet.obtenirCodeBarre().equals(paquet.obtenirCodeBarre()) &&  etage <= autrePaquet.obtenirEtage()) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirPositionX(), paquet.obtenirPositionY())) {
                    paquetAuDessous = autrePaquet;
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    paquetAuDessous = autrePaquet;
                }
            }
        }

        return paquetAuDessous;
    }

    public void fixerPaquets(List<Paquet> paquets) {
        this.paquets = paquets;
    }

    public int obtenirNombrePaquetsPiles() {
        return indicePaquetsSelectionnes.size();
    }

    public double obtenirHauteurPile() {
        double hauteurDeLaPile = VALEUR_ZERO;
        if (!indicePaquetsSelectionnes.isEmpty()) {
            List<Integer> IndicePileTrieParHauteur = obtenirListTrieParHauteur(indicePaquetsSelectionnes);
            Paquet paquetDuDessus =  paquets.get(IndicePileTrieParHauteur.get(IndicePileTrieParHauteur.size()-1));
            hauteurDeLaPile = paquetDuDessus.obtenirPositionZ() + paquetDuDessus.obtenirHauteur();
        }

        return hauteurDeLaPile;
    }

    public void fixerChargeuse(Chargeuse chargeuse) {
        this.chargeuse = chargeuse;
    }

    public List<Integer> obtenirListTrieParHauteur(List<Integer> ListeIndiceATrier) {
        List<Integer> listeIndiceTrieParHauteur = ListeIndiceATrier;

        for (int i = listeIndiceTrieParHauteur.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                double hauteurPaquet = paquets.get(listeIndiceTrieParHauteur.get(j)).obtenirPositionZ();

                if (paquets.get(listeIndiceTrieParHauteur.get(j+1)).obtenirPositionZ() < hauteurPaquet ) {
                    int temp = listeIndiceTrieParHauteur.get(j+1);
                    listeIndiceTrieParHauteur.set(j+1, listeIndiceTrieParHauteur.get(j));
                    listeIndiceTrieParHauteur.set(j, temp);
                }
            }
        }

        return listeIndiceTrieParHauteur;
    }

    public void gestionSuppressionPaquet() {
        Paquet paquet = paquets.get(indicePaquetSelectionne);
        Paquet dernierPaquet = paquet;
        double positionZPaquet = VALEUR_ZERO;
        
        Line2D.Double coteHaut = new Line2D.Double(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY(),
                                                   paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY(),
                                                    paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY(),
                                                  paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY(),
                                                     paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure  à la hauteur actuelle
            if (!autrePaquet.obtenirCodeBarre().equals(paquet.obtenirCodeBarre()) &&  paquet.obtenirEtage() < autrePaquet.obtenirEtage()) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paquet.obtenirPositionX(), paquet.obtenirPositionY())) {
                    autrePaquet.fixerEtage(autrePaquet.obtenirEtage() - 1);
                    positionZPaquet = (dernierPaquet.obtenirCodeBarre().equals(paquet.obtenirCodeBarre())) ? paquet.obtenirPositionZ() : positionZPaquet + dernierPaquet.obtenirHauteur();
                    autrePaquet.fixerPositionZ(positionZPaquet);
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    autrePaquet.fixerEtage(autrePaquet.obtenirEtage() - 1);
                    positionZPaquet = (dernierPaquet.obtenirCodeBarre().equals(paquet.obtenirCodeBarre())) ? paquet.obtenirPositionZ() : positionZPaquet + dernierPaquet.obtenirHauteur();
                    autrePaquet.fixerPositionZ(positionZPaquet);
                }
            }
            
            dernierPaquet = autrePaquet;
        }
        
        this.paquets.remove(this.indicePaquetSelectionne);
        this.indicePaquetsSelectionnes = new ArrayList<>();
        this.indicePaquetSelectionne = -1;
    }

    public boolean chargeuseTouchePaquet(Chargeuse chargeuse) {
        boolean toucheUnPaquet = false;
        
        Line2D.Double coteHaut = new Line2D.Double(chargeuse.obtenirCoinSuperieurGaucheX(), chargeuse.obtenirCoinSuperieurGaucheY(),
                                                   chargeuse.obtenirCoinSuperieurDroitX(), chargeuse.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(chargeuse.obtenirCoinSuperieurDroitX(), chargeuse.obtenirCoinSuperieurDroitY(),
                                                    chargeuse.obtenirCoinInferieurDroitX(), chargeuse.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(chargeuse.obtenirCoinInferieurDroitX(), chargeuse.obtenirCoinInferieurDroitY(),
                                                  chargeuse.obtenirCoinInferieurGaucheX(), chargeuse.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(chargeuse.obtenirCoinInferieurGaucheX(), chargeuse.obtenirCoinInferieurGaucheY(),
                                                     chargeuse.obtenirCoinSuperieurGaucheX(), chargeuse.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                   autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
            Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                    autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
            Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                  autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
            Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                     autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

            // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
            if (autrePaquet.verificationPointDansSurface(chargeuse.obtenirCoinSuperieurGaucheX(), chargeuse.obtenirCoinSuperieurGaucheY()) ||
                    autrePaquet.verificationPointDansSurface(chargeuse.obtenirCoinSuperieurDroitX(), chargeuse.obtenirCoinSuperieurDroitY()) ||
                    autrePaquet.verificationPointDansSurface(chargeuse.obtenirCoinInferieurDroitX(), chargeuse.obtenirCoinInferieurDroitY()) ||
                    autrePaquet.verificationPointDansSurface(chargeuse.obtenirCoinInferieurGaucheX(), chargeuse.obtenirCoinInferieurGaucheY()) ||
                    autrePaquet.verificationPointDansSurface(chargeuse.obtenirPositionX(), chargeuse.obtenirPositionY())) {
                toucheUnPaquet = true;
            }
            // Sinon on regarde si les cotes se croisent
            else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                     coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                     coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                     coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
            {
                toucheUnPaquet = true;
            }
        }

        return toucheUnPaquet;
    }

    public void fixerFacteurZoom(double facteur) {
        this.facteurDeZoom = facteur;
    }
    
    public void gestionLeverBras(double augmentation) {
        chargeuse.leverBras(augmentation, paquets);
        if (chargeuse.obtenirModeChargeuse() == Chargeuse.ModeChargeuse.PAQUET_PORTE) {
            indicePaquetsSelectionnes = chargeuse.obtenirIndicesPaquetsPortes();
        }
    }
    
    public void gestionBaisserBras(double diminution) {
        chargeuse.descendreBras(diminution, paquets);
    }
    
    public void gestionTournerAGauche() {
        chargeuse.tournerAGauche(paquets);
        if (this.chargeuseTouchePaquet(this.chargeuse) || collisionPaquetTransporte() || brasCollisionPaquet()) {
            chargeuse.tournerADroite(paquets);
        }
    }
    
    public void gestionTournerADroite() {
        chargeuse.tournerADroite(paquets);
        if (this.chargeuseTouchePaquet(this.chargeuse) || collisionPaquetTransporte() || brasCollisionPaquet()) {
            chargeuse.tournerAGauche(paquets);
        }
    }
    
    public boolean gestionAvancer() {
        chargeuse.avancer(paquets);
        if (this.chargeuseTouchePaquet(this.chargeuse) || collisionPaquetTransporte() || brasCollisionPaquet()) {
            chargeuse.reculer(paquets);
            return true;
        }
        return false;
    }
    
    public void gestionReculer() {
        chargeuse.reculer(paquets);
        if (this.chargeuseTouchePaquet(this.chargeuse) || collisionPaquetTransporte() || brasCollisionPaquet()) {
            chargeuse.avancer(paquets);
        }
    }

    public void fixerEtatDeplacement() {
        this.etatDeplacementChargeuse = !etatDeplacementChargeuse;
    }
    
    public boolean obtenirEtatDeplacement() {
        return this.etatDeplacementChargeuse;
    }
     
    public boolean PaquetSurChargeuse(double posX,
                                      double posY,
                                      double longueur,
                                      double largeur,
                                      double angle) { 
        Paquet paquet = new Paquet();
        paquet.fixerPositionX(posX);
        paquet.fixerPositionY(posY);
        paquet.fixerLongueur(longueur);
        paquet.fixerLargeur(largeur);
        paquet.fixerAngle(angle);
       
        boolean nouveauPaquetSurChargeuse = false;
       
        Line2D.Double coteHaut = new Line2D.Double(chargeuse.obtenirCoinSuperieurGaucheX(), chargeuse.obtenirCoinSuperieurGaucheY(),
                                                   chargeuse.obtenirCoinSuperieurDroitX(), chargeuse.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(chargeuse.obtenirCoinSuperieurDroitX(), chargeuse.obtenirCoinSuperieurDroitY(),
                                                    chargeuse.obtenirCoinInferieurDroitX(), chargeuse.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(chargeuse.obtenirCoinInferieurDroitX(), chargeuse.obtenirCoinInferieurDroitY(),
                                                  chargeuse.obtenirCoinInferieurGaucheX(), chargeuse.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(chargeuse.obtenirCoinInferieurGaucheX(), chargeuse.obtenirCoinInferieurGaucheY(),
                                                     chargeuse.obtenirCoinSuperieurGaucheX(), chargeuse.obtenirCoinSuperieurGaucheY());

        Line2D.Double coteHaut_autrePaquet = new Line2D.Double(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY(),
                                                               paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit_autrePaquet = new Line2D.Double(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY(),
                                                                paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas_autrePaquet = new Line2D.Double(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY(),
                                                              paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche_autrePaquet = new Line2D.Double(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY(),
                                                                 paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY());

        // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
        if (paquet.verificationPointDansSurface(chargeuse.obtenirCoinSuperieurGaucheX(), chargeuse.obtenirCoinSuperieurGaucheY()) ||
                paquet.verificationPointDansSurface(chargeuse.obtenirCoinSuperieurDroitX(), chargeuse.obtenirCoinSuperieurDroitY()) ||
                paquet.verificationPointDansSurface(chargeuse.obtenirCoinInferieurDroitX(), chargeuse.obtenirCoinInferieurDroitY()) ||
                paquet.verificationPointDansSurface(chargeuse.obtenirCoinInferieurGaucheX(), chargeuse.obtenirCoinInferieurGaucheY()) ||
                paquet.verificationPointDansSurface(chargeuse.obtenirPositionX(), chargeuse.obtenirPositionY())) {
            nouveauPaquetSurChargeuse = true;
        }
        // Sinon on regarde si les cotes se croisent
        else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                 coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                 coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                 coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
        {
            nouveauPaquetSurChargeuse = true;
        }     
       
       return nouveauPaquetSurChargeuse;     
    }
     
    public boolean collisionPaquetTransporte() {
        List<Integer> indicesPaquetsTransportes = chargeuse.obtenirIndicesPaquetsPortes();
        boolean collision = false;
         
        for (int indice : indicesPaquetsTransportes) {
             
            Paquet paquet = paquets.get(indice);
             
            Line2D.Double coteHaut = new Line2D.Double(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY(),
                                                       paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY());
            Line2D.Double coteDroit = new Line2D.Double(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY(),
                                                        paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY());
            Line2D.Double coteBas = new Line2D.Double(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY(),
                                                      paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY());
            Line2D.Double coteGauche = new Line2D.Double(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY(),
                                                         paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY());

            for (Paquet autrePaquet : paquets) {
                // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
                if (!autrePaquet.obtenirCodeBarre().equals(paquet.obtenirCodeBarre()) &&
                   ((paquet.obtenirPositionZ() >= autrePaquet.obtenirPositionZ() && paquet.obtenirPositionZ() < (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur())) ||
                   (autrePaquet.obtenirPositionZ() >= paquet.obtenirPositionZ() && autrePaquet.obtenirPositionZ() < (paquet.obtenirPositionZ() + paquet.obtenirHauteur())))) {
                    Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                           autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                    Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                            autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                    Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                          autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                    Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                             autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                    // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                    if (autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurGaucheX(), paquet.obtenirCoinSuperieurGaucheY()) ||
                            autrePaquet.verificationPointDansSurface(paquet.obtenirCoinSuperieurDroitX(), paquet.obtenirCoinSuperieurDroitY()) ||
                            autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurDroitX(), paquet.obtenirCoinInferieurDroitY()) ||
                            autrePaquet.verificationPointDansSurface(paquet.obtenirCoinInferieurGaucheX(), paquet.obtenirCoinInferieurGaucheY()) ||
                            autrePaquet.verificationPointDansSurface(paquet.positionX, paquet.positionY) || 
                            paquet.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY()) ||
                            paquet.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY()) ||
                            paquet.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY()) ||
                            paquet.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY()) ||
                            paquet.verificationPointDansSurface(autrePaquet.positionX, autrePaquet.positionY)) {
                        return true;
                    }
                    // Sinon on regarde si les cotes se croisent
                    else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                             coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                             coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                             coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                    {
                        return true;
                    }
                }
            }
             
        }
        
        return collision;
    }
    
    public boolean brasCollisionPaquet() {
        ObjetDeLaCours bras = obtenirChargeuse().obtenirPaireDeBras();
        Line2D.Double coteHaut = new Line2D.Double(bras.obtenirCoinSuperieurGaucheX(), bras.obtenirCoinSuperieurGaucheY(),
                                                   bras.obtenirCoinSuperieurDroitX(), bras.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(bras.obtenirCoinSuperieurDroitX(), bras.obtenirCoinSuperieurDroitY(),
                                                    bras.obtenirCoinInferieurDroitX(), bras.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(bras.obtenirCoinInferieurDroitX(), bras.obtenirCoinInferieurDroitY(),
                                                  bras.obtenirCoinInferieurGaucheX(), bras.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(bras.obtenirCoinInferieurGaucheX(), bras.obtenirCoinInferieurGaucheY(),
                                                     bras.obtenirCoinSuperieurGaucheX(), bras.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (bras.obtenirPositionZ() > autrePaquet.obtenirPositionZ() && bras.obtenirPositionZ() < (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur())) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(bras.obtenirCoinSuperieurGaucheX(), bras.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(bras.obtenirCoinSuperieurDroitX(), bras.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(bras.obtenirCoinInferieurDroitX(), bras.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(bras.obtenirCoinInferieurGaucheX(), bras.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(bras.positionX, bras.positionY) || 
                        bras.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY()) ||
                        bras.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY()) ||
                        bras.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY()) ||
                        bras.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY()) ||
                        bras.verificationPointDansSurface(autrePaquet.positionX, autrePaquet.positionY)) {
                    return true;
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public Chargeuse creerChargeuseTemporaire(double positionXChargeuse,
                                              double positionYChargeuse,
                                              double angleChargeuse,
                                              double largeurChargeuse,
                                              double longueurChargeuse) {
        Chargeuse chargeuseTemporaire = new Chargeuse();
        chargeuseTemporaire.fixerPositionX(positionXChargeuse);
        chargeuseTemporaire.fixerPositionY(positionYChargeuse);
        chargeuseTemporaire.fixerAngle(angleChargeuse);
        chargeuseTemporaire.fixerLargeur(largeurChargeuse);
        chargeuseTemporaire.fixerLongueur(longueurChargeuse);
        
        return chargeuseTemporaire;
    }
    
    public boolean validerLargeurBrasAvecLargeurChargeuse(double largeurBrasChargeuse, double largeurChargeuse) {
        return this.chargeuse.validerLargeurBrasAvecLargeurChargeuse(largeurBrasChargeuse, largeurChargeuse);
    }
    
    public boolean controleurEstDansEtatInitial() {
        return this.paquets.size() == VALEUR_ZERO &&
               this.indicePaquetSelectionne == -1 &&
               this.indicePaquetsSelectionnes.size() == VALEUR_ZERO &&
               this.chargeuse.equals(new Chargeuse());
    }
    
    public void reinitialiserCoursABois() {
        this.paquets = new ArrayList<>();
        this.indicePaquetSelectionne = -1;
        this.indicePaquetsSelectionnes = new ArrayList<>();
        this.chargeuse = new Chargeuse();
    }
    
    private boolean paquetEstSelectionne() {
        return this.indicePaquetSelectionne != -1;
    }
}


