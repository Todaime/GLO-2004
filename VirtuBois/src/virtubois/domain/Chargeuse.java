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

import static virtubois.domain.Commun.Constantes.ANGLE_COMPLET_CERCLE;
import static virtubois.domain.Commun.Constantes.ANGLE_DEPLACEMENT_CHARGEUSE;
import static virtubois.domain.Commun.Constantes.ANGLE_MOITIER_CERCLE;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;
import static virtubois.domain.Commun.Constantes.VITESSE_DEPLACEMENT_CHARGEUSE;

/**
 *
 * @author maximeprieur
 */
public class Chargeuse extends ObjetDeLaCours implements Serializable {

    private BrasChargeuse paireDeBras;
    private List<Integer> indicesPaquetsPortes;
    public enum ModeChargeuse {
        PAQUET_PORTE,
        VIDE
    }
    public ModeChargeuse modeChargeuseActuel;
    private final double LONGUEUR_BRAS_CHARGEUSE_INITIALE = 2;
    private final double LARGEUR_BRAS_CHARGEUSE_INITIALE = 2;

    public Chargeuse() {
        super();
        this.modeChargeuseActuel = ModeChargeuse.VIDE;
        this.indicesPaquetsPortes = new ArrayList<>();
        this.paireDeBras = new BrasChargeuse(this.positionX,
                                             this.positionY,
                                             this.angle,
                                             this.longueur,
                                             VALEUR_ZERO,
                                             LONGUEUR_BRAS_CHARGEUSE_INITIALE,
                                             LARGEUR_BRAS_CHARGEUSE_INITIALE);
    }

    public Chargeuse(Chargeuse chargeuse) {
        super(chargeuse.positionX,
              chargeuse.positionY,
              chargeuse.positionZ,
              chargeuse.angle,
              chargeuse.etage,
              chargeuse.largeur,
              chargeuse.longueur);
        this.paireDeBras = new BrasChargeuse(this.positionX,
                                             this.positionY,
                                             this.angle,
                                             this.longueur,
                                             chargeuse.paireDeBras.obtenirHauteur(),
                                             chargeuse.paireDeBras.obtenirLongueur(),
                                             chargeuse.paireDeBras.obtenirLargeur());
        this.indicesPaquetsPortes = new ArrayList<>();
        this.modeChargeuseActuel = chargeuse.modeChargeuseActuel;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass()) {
           return false;
        }
        return ((Chargeuse)other).positionX == this.positionX &&
                ((Chargeuse)other).positionY == this.positionY &&
                ((Chargeuse)other).angle == this.angle;
    }

    public BrasChargeuse obtenirPaireDeBras() {
        return this.paireDeBras;
    }

    public double obtenirHauteurBras() {
        return paireDeBras.positionZ;
    }
    
    public ModeChargeuse obtenirModeChargeuse() {
        return this.modeChargeuseActuel;
    }
    
    public void fixerModeChargeuse(ModeChargeuse modeChargeuse) {
        this.modeChargeuseActuel = modeChargeuse;
    }
    
    public void modifierPositionX(double positionX) {
        this.fixerPositionX(positionX);
        this.paireDeBras.fixerPositionDepuisChargeuse(this.angle, positionX, this.positionY, this.longueur);
    }
    
    public void modifierPositionY(double positionY) {
        this.fixerPositionY(positionY);
        this.paireDeBras.fixerPositionDepuisChargeuse(this.angle, this.positionX, positionY, this.longueur);
    }
    
    public void modifierLongueur(double longueur) {
        this.fixerLongueur(longueur);
        this.paireDeBras.fixerPositionDepuisChargeuse(this.angle, this.positionX, this.positionY, longueur);
    }
    
    public void modifierLargeur(double largeur) {
        this.fixerLargeur(largeur);
    }
    
    public void modifierAngle(double angle) {
        this.fixerAngle(angle);
        this.paireDeBras.fixerPositionDepuisChargeuse(angle, this.positionX, this.positionY, this.longueur);
        this.paireDeBras.fixerAngle(angle);
    }
    
    public void modifierHauteurBras(double hauteur) {
        this.paireDeBras.fixerHauteur(hauteur);
    }
    
    public void modifierLongueurBras(double longueurBras) {
        this.paireDeBras.fixerLongueur(longueurBras);
        this.paireDeBras.fixerPositionDepuisChargeuse(this.angle, this.positionX, this.positionY, this.longueur);
    }
    
    public void modifierLargeurBras(double largeurBras) {
        this.paireDeBras.fixerLargeur(largeurBras);
    }

    private List<Integer> MAJIndicesPaquetsSurBras(double augmentation,List<Paquet> paquets) {
    
        // Segments representant les cotés du paquet
        Line2D.Double coteHaut = new Line2D.Double(paireDeBras.obtenirCoinSuperieurGaucheX(), paireDeBras.obtenirCoinSuperieurGaucheY(),
                                                   paireDeBras.obtenirCoinSuperieurDroitX(), paireDeBras.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(paireDeBras.obtenirCoinSuperieurDroitX(), paireDeBras.obtenirCoinSuperieurDroitY(),
                                                    paireDeBras.obtenirCoinInferieurDroitX(), paireDeBras.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(paireDeBras.obtenirCoinInferieurDroitX(), paireDeBras.obtenirCoinInferieurDroitY(),
                                                  paireDeBras.obtenirCoinInferieurGaucheX(), paireDeBras.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(paireDeBras.obtenirCoinInferieurGaucheX(), paireDeBras.obtenirCoinInferieurGaucheY(),
                                                     paireDeBras.obtenirCoinSuperieurGaucheX(), paireDeBras.obtenirCoinSuperieurGaucheY());

        List<Integer> indicesPaquetsSurBras = new ArrayList<>();

        for (int i = 0; i < paquets.size(); i++) {
            Paquet autrePaquet = paquets.get(i);

            if (paireDeBras.positionZ == autrePaquet.positionZ || (paireDeBras.positionZ < autrePaquet.positionZ && (paireDeBras.positionZ+augmentation) > autrePaquet.positionZ)) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinSuperieurGaucheX(), paireDeBras.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinSuperieurDroitX(), paireDeBras.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinInferieurDroitX(), paireDeBras.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinInferieurGaucheX(), paireDeBras.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.positionX, paireDeBras.positionY))  {
                    if (!indicesPaquetsSurBras.contains(i)) {
                        indicesPaquetsSurBras.add(i);
                    }
                    
                    List<Integer> indicePaquetsAuDessusDuPaquet = autrePaquet.obtenirPaquetsEnContactAuDessus(paquets);
                    for (int j = 0 ; j<indicePaquetsAuDessusDuPaquet.size();j++){
                        if (!indicesPaquetsSurBras.contains(indicePaquetsAuDessusDuPaquet.get(j))){
                            indicesPaquetsSurBras.add(indicePaquetsAuDessusDuPaquet.get(j));
                        }
                    }
                    
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet)){
                    if (!indicesPaquetsSurBras.contains(i)) {
                        indicesPaquetsSurBras.add(i);
                    }
                    
                    indicesPaquetsSurBras.add(i);
                    List<Integer> indicePaquetsAuDessusDuPaquet = autrePaquet.obtenirPaquetsEnContactAuDessus(paquets);
                    for (int j = 0 ; j<indicePaquetsAuDessusDuPaquet.size();j++){
                        if (!indicesPaquetsSurBras.contains(indicePaquetsAuDessusDuPaquet.get(j))){
                            indicesPaquetsSurBras.add(indicePaquetsAuDessusDuPaquet.get(j));
                        }
                    }
                }
            }
        }

        return indicesPaquetsSurBras;
    }

    private void augmenterHauteurPaquetsPortes(double augmentation, List<Paquet> paquets){
        for (Integer indicePaquetPorte : indicesPaquetsPortes) {
            Paquet unPaquet = paquets.get(indicePaquetPorte);
            unPaquet.fixerPositionZ(unPaquet.obtenirPositionZ() + augmentation);
        }
    }

    private void diminuerHauteurPaquetsPortes(double diminution, List<Paquet> paquets){
        this.indicesPaquetsPortes = obtenirListTrieParHauteur(indicesPaquetsPortes, paquets);
        List<Integer> indicesPaquetsPortesTries = new ArrayList<>();
        
        for (Integer indice : indicesPaquetsPortes) {
            indicesPaquetsPortesTries.add(indice);
        }
        
        for (Integer indicePaquetPorte : indicesPaquetsPortesTries) {
            Paquet unPaquet = paquets.get(indicePaquetPorte);
            double ecart = unPaquet.ecartPaquetEnDessous(diminution, paquets,indicesPaquetsPortes);
            
            if (ecart == -1) {
                unPaquet.fixerPositionZ(unPaquet.obtenirPositionZ() - diminution);
            }
            else {
                unPaquet.fixerPositionZ(unPaquet.obtenirPositionZ() - ecart);
                this.indicesPaquetsPortes.remove(indicePaquetPorte);    
            }
        }
    }

    public void reculer(List<Paquet> paquets) {
        deplacerChargeuse(paquets,
                          this.obtenirPositionX() - (VITESSE_DEPLACEMENT_CHARGEUSE / this.obtenirMultiplier()*Math.cos(Math.toRadians(this.obtenirAngle()))),
                          this.obtenirPositionY() - (VITESSE_DEPLACEMENT_CHARGEUSE / this.obtenirMultiplier()*Math.sin(Math.toRadians(this.obtenirAngle()))));
    }

    public void avancer(List<Paquet> paquets) {
        deplacerChargeuse(paquets,
                          this.obtenirPositionX() + (VITESSE_DEPLACEMENT_CHARGEUSE / this.obtenirMultiplier()*Math.cos(Math.toRadians(this.obtenirAngle()))),
                          this.obtenirPositionY() + (VITESSE_DEPLACEMENT_CHARGEUSE / this.obtenirMultiplier()*Math.sin(Math.toRadians(this.obtenirAngle()))));
    }
    
    private void deplacerChargeuse(List<Paquet> paquets, double positionX, double positionY) {
        double anciennePositionX = this.obtenirPositionX();
        double anciennePositionY = this.obtenirPositionY();

        this.fixerPositionX(positionX);
        this.fixerPositionY(positionY);

        double decalageX = this.obtenirPositionX() - anciennePositionX;
        double decalageY = this.obtenirPositionY() - anciennePositionY;

        deplacementHorizontalPaquetsPortes(decalageX, decalageY, paquets);
        this.paireDeBras.majPosition(this.obtenirPositionX(), this.obtenirPositionY(), this.obtenirAngle(),longueur,largeur);
    }

    public void tournerADroite(List<Paquet> paquets) {
        this.fixerAngle(this.obtenirAngle() + ANGLE_DEPLACEMENT_CHARGEUSE);
        
        if (this.obtenirAngle() > ANGLE_COMPLET_CERCLE) {
            this.fixerAngle(this.obtenirAngle() % ANGLE_COMPLET_CERCLE);
        }

        this.paireDeBras.majPosition(this.obtenirPositionX(), this.obtenirPositionY(), this.obtenirAngle(),longueur,largeur);
        deplacementRotationPaquetsPortes(ANGLE_DEPLACEMENT_CHARGEUSE, paquets);
    }

    public void tournerAGauche(List<Paquet> paquets) {
        this.fixerAngle(this.obtenirAngle() - ANGLE_DEPLACEMENT_CHARGEUSE);
        
        if (this.obtenirAngle() < VALEUR_ZERO) {
            this.fixerAngle(this.obtenirAngle() + ANGLE_COMPLET_CERCLE);
        }

        this.paireDeBras.majPosition(this.obtenirPositionX(), this.obtenirPositionY(), this.obtenirAngle(),longueur,largeur);
        deplacementRotationPaquetsPortes(-ANGLE_DEPLACEMENT_CHARGEUSE,paquets);
    }

    public void leverBras(double augmentation, List<Paquet> paquets) {
        this.indicesPaquetsPortes = MAJIndicesPaquetsSurBras(augmentation, paquets);

        if (!indicesPaquetsPortes.isEmpty()) {
            this.fixerModeChargeuse(ModeChargeuse.PAQUET_PORTE);
            augmenterHauteurPaquetsPortes(augmentation, paquets);
        }
        
        this.paireDeBras.monterBras(augmentation);
    }

    public void descendreBras(double diminution, List<Paquet> paquets) {
        if (this.obtenirHauteurBras() != VALEUR_ZERO){
            if (this.obtenirHauteurBras() - diminution > VALEUR_ZERO) {
                double ecartPaquetSousBras = ecartPaquetSousBras(diminution, paquets);
                
                if (ecartPaquetSousBras == -1) {
                    diminuerHauteurPaquetsPortes(diminution, paquets);
                    this.paireDeBras.descendreBras(diminution);
                }
                else {
                    diminuerHauteurPaquetsPortes(ecartPaquetSousBras, paquets);
                    this.paireDeBras.descendreBras(ecartPaquetSousBras);
                    this.indicesPaquetsPortes.clear();
                }
            } else {
                diminuerHauteurPaquetsPortes(this.obtenirHauteurBras(), paquets);
                this.indicesPaquetsPortes.clear();
                this.fixerModeChargeuse(ModeChargeuse.VIDE);
                this.paireDeBras.descendreBras(this.obtenirHauteurBras());
            }
        }
        
        if (indicesPaquetsPortes.isEmpty()){
            this.fixerModeChargeuse(ModeChargeuse.VIDE);
        }
    }

    private void deplacementHorizontalPaquetsPortes(double decalageX, double decalageY, List<Paquet> paquets) {
        for (Integer indicePaquetPorte : this.indicesPaquetsPortes){
            Paquet paquet = paquets.get(indicePaquetPorte);
            paquet.fixerPositionX(paquet.obtenirPositionX() + decalageX);
            paquet.fixerPositionY(paquet.obtenirPositionY() + decalageY);
        }
    }

    private void deplacementRotationPaquetsPortes(int angleRotation, List<Paquet> paquets) {
        for (Integer indicePaquetPorte : this.indicesPaquetsPortes){
            Paquet paquet = paquets.get(indicePaquetPorte);

            double pointX = (double)(paquet.obtenirPositionX() - this.obtenirPositionX());
            double pointY = (double)(paquet.obtenirPositionY() - this.obtenirPositionY());
            double theta = -Math.PI*angleRotation / ANGLE_MOITIER_CERCLE;
            
            double pointXTemporaire = pointX;
            double pointYTemporaire = pointY;

            pointX = pointXTemporaire*Math.cos(theta) + pointYTemporaire*Math.sin(theta);
            pointY = -pointXTemporaire*Math.sin(theta) + pointYTemporaire*Math.cos(theta);

            paquet.fixerPositionX(pointX + this.obtenirPositionX());
            paquet.fixerPositionY(pointY + this.obtenirPositionY());
            paquet.fixerAngle(paquet.obtenirAngle() + angleRotation);
        }
    }

    public List<Integer> obtenirIndicesPaquetsPortes() {
        return indicesPaquetsPortes;
    }
    
    public double ecartPaquetSousBras(double diminution,List<Paquet> paquets){
        double ecartPaquetSousBras = -1;
        
        // Segments representant les cotés du paquet
        Line2D.Double coteHaut = new Line2D.Double(paireDeBras.obtenirCoinSuperieurGaucheX(), paireDeBras.obtenirCoinSuperieurGaucheY(),
                                                   paireDeBras.obtenirCoinSuperieurDroitX(), paireDeBras.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(paireDeBras.obtenirCoinSuperieurDroitX(), paireDeBras.obtenirCoinSuperieurDroitY(),
                                                    paireDeBras.obtenirCoinInferieurDroitX(), paireDeBras.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(paireDeBras.obtenirCoinInferieurDroitX(), paireDeBras.obtenirCoinInferieurDroitY(),
                                                  paireDeBras.obtenirCoinInferieurGaucheX(), paireDeBras.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(paireDeBras.obtenirCoinInferieurGaucheX(), paireDeBras.obtenirCoinInferieurGaucheY(),
                                                     paireDeBras.obtenirCoinSuperieurGaucheX(), paireDeBras.obtenirCoinSuperieurGaucheY());

        for (int i = 0; i < paquets.size(); i++) {
            Paquet autrePaquet = paquets.get(i);

            if ((paireDeBras.obtenirPositionZ() >= autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur()) && ((paireDeBras.obtenirPositionZ()-diminution)<= autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur())) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinSuperieurGaucheX(), paireDeBras.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinSuperieurDroitX(), paireDeBras.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinInferieurDroitX(), paireDeBras.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.obtenirCoinInferieurGaucheX(), paireDeBras.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(paireDeBras.positionX, paireDeBras.positionY))  {
                    if (ecartPaquetSousBras == -1 || (paireDeBras.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur()))<ecartPaquetSousBras) {
                        ecartPaquetSousBras = paireDeBras.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur()) ;
                    }
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet)){
                    if (ecartPaquetSousBras == -1 || (paireDeBras.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur())) < ecartPaquetSousBras) {
                        ecartPaquetSousBras = paireDeBras.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur()) ;
                    }
                }
            }
        }

        return ecartPaquetSousBras;
        
    }
    
    public List<Integer> obtenirListTrieParHauteur(List<Integer> ListeIndiceATrier, List<Paquet> paquets) {
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
    
    public boolean validerLargeurBrasAvecLargeurChargeuse(double largeurBrasChargeuse, double largeurChargeuse) {
        return largeurBrasChargeuse > largeurChargeuse;
    }
}
