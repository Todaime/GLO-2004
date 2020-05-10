/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain;

/**
 *
 * @author pcMasterRace
 */
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static virtubois.domain.Commun.Constantes.VALEUR_UN;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;

public class Paquet extends ObjetDeLaCours implements Serializable
{
    private String codeBarre;
    private Date dateProduction;
    private String produit;

    public Paquet() {
        super();
        this.codeBarre = "";
        this.dateProduction = new Date();
        this.produit = "";
    }

    // Constructeur copie pour undo Redoo
    public Paquet(Paquet paquet) {
        super(paquet.positionX,
              paquet.positionY,
              paquet.positionZ,
              paquet.angle,
              paquet.etage,
              paquet.largeur,
              paquet.longueur);
        this.codeBarre = paquet.obtenirCodeBarre();
        this.dateProduction = paquet.dateProduction;
        this.produit = paquet.produit;
        this.hauteur = paquet.hauteur;
    }

    public Paquet(double positionX, double positionY, String codeBarre, String produit, double largeur,
                  double longueur, double angle, double hauteur) {
        super();
        this.codeBarre = codeBarre;
        this.dateProduction = new Date();
        this.produit = produit;
        this.hauteur =  hauteur;
        this.positionX = positionX;
        this.positionY = positionY;
        this.largeur = largeur;
        this.longueur = longueur;
        this.angle = angle;
    }
    
    public String obtenirCodeBarre() {
        return this.codeBarre;
    }

    public Date obtenirDateProduction() {
        return this.dateProduction;
    }
    
    public void fixerDateProduction(Date dateProduction) {
        this.dateProduction = dateProduction;
    }

    public String obtenirProduit() {
        return this.produit;
    }
    
    public void fixerProduit(String produit) {
        this.produit = produit;
    }
    
    public List<Integer> obtenirPaquetsEnContactAuDessus(List<Paquet> paquets){
        
        List<Integer> indicesPaquetAuContactDessus = new ArrayList<Integer>();

        Line2D.Double coteHaut = new Line2D.Double(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY(),
                                                   this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY(),
                                                    this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY(),
                                                  this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY(),
                                                     this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY());

        for (int i = 0; i < paquets.size(); i++) {
            Paquet autrePaquet = paquets.get(i);
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (!autrePaquet.obtenirCodeBarre().equals(this.obtenirCodeBarre()) &&  this.positionZ + this.hauteur == autrePaquet.positionZ) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.positionX, this.positionY) || 
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY()) ||
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY()) ||
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY()) ||
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY()) ||
                        this.verificationPointDansSurface(autrePaquet.positionX, autrePaquet.positionY)) {
                    indicesPaquetAuContactDessus.add(i);
                    List<Integer> indicesRecurence = autrePaquet.obtenirPaquetsEnContactAuDessus(paquets);
                    
                    for (int j = 0; j < indicesRecurence.size(); j++){
                        indicesPaquetAuContactDessus.add(indicesRecurence.get(j));
                    }                    
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    indicesPaquetAuContactDessus.add(i);
                    List<Integer> indicesRecurence = autrePaquet.obtenirPaquetsEnContactAuDessus(paquets);
                    
                    for (int j = 0; j < indicesRecurence.size(); j++) {
                        indicesPaquetAuContactDessus.add(indicesRecurence.get(j));
                    }
                }
            }
        }
        return indicesPaquetAuContactDessus;
    }
    
    public double ecartPaquetEnDessous(double diminution, List<Paquet> paquets,List<Integer> paquetsPortes) {
        double ecartPaquetSousBras = -1;
        
        // Segments representant les cotés du paquet
        Line2D.Double coteHaut = new Line2D.Double(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY(),
                                                   this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY(),
                                                    this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY(),
                                                  this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY(),
                                                     this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY());

        for (int i = 0; i < paquets.size(); i++) {
                Paquet autrePaquet = paquets.get(i);

            if ((this.obtenirPositionZ() >= autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur()) && ((this.obtenirPositionZ()-diminution) < autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur())
                    && !paquetsPortes.contains(i)) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.positionX, this.positionY))  {
                    if (ecartPaquetSousBras == -1 || (this.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur())) < ecartPaquetSousBras) {
                        ecartPaquetSousBras = this.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur()) ;
                    }
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet)){
                    if (ecartPaquetSousBras == -1 || (this.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur())) < ecartPaquetSousBras) {
                        ecartPaquetSousBras = this.obtenirPositionZ() - (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur()) ;
                    }
                }
            }
        }

        return ecartPaquetSousBras;
    }
    
    public static String creerNouveauCodeBarre(List<Paquet> paquets) {
        boolean estPresent;
        long nombreAleatoire;
        
        do {
            estPresent = false;
            nombreAleatoire = 10000000 + (int)(Math.random() * 90000000);
            for (Paquet paquet : paquets) {
                String codeBarrePaquetTest = paquet.obtenirCodeBarre();

                if (codeBarrePaquetTest != null && Long.toString(nombreAleatoire).equals(paquet.obtenirCodeBarre())) {
                    estPresent = true;
                    break;
                }
            }
        } while (estPresent == true);
        
        return Long.toString(nombreAleatoire);
    }
    
    public int obtenirEtagePaquet(List<Paquet> paquets) {
        int etagePaquet = VALEUR_ZERO;

        Line2D.Double coteHaut = new Line2D.Double(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY(),
                                                   this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY(),
                                                    this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY(),
                                                  this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY(),
                                                     this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (!autrePaquet.obtenirCodeBarre().equals(this.obtenirCodeBarre()) && etagePaquet <= autrePaquet.obtenirEtage()) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.positionX, this.positionY) || 
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY()) ||
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY()) ||
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY()) ||
                        this.verificationPointDansSurface(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY()) ||
                        this.verificationPointDansSurface(autrePaquet.positionX, autrePaquet.positionY)) {
                    etagePaquet = autrePaquet.obtenirEtage() + 1;
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    etagePaquet = autrePaquet.obtenirEtage() + 1;
                }
            }
        }

        return etagePaquet;
    }
    
    public double obtenirPositionZPaquet(List<Paquet> paquets) {
        double positionZPaquet = VALEUR_ZERO;

        Line2D.Double coteHaut = new Line2D.Double(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY(),
                                                   this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY(),
                                                    this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY(),
                                                  this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY(),
                                                     this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur() >= positionZPaquet && !this.obtenirCodeBarre().equals(autrePaquet.obtenirCodeBarre())) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirPositionX(), this.obtenirPositionY())) {
                    positionZPaquet = autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur();
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    positionZPaquet = autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur();
                }
            }
        }

        return positionZPaquet;
    }
    
    public void modifierPositionZPaquetsDansPile(List<Paquet> paquets, double positionZPaquetModifie, double ancienneHauteurPaquet, double nouvelleHauteurPaquet) {
        double positionZPaquet = positionZPaquetModifie;
        double hauteurPaquetDessous = nouvelleHauteurPaquet;
        
        Line2D.Double coteHaut = new Line2D.Double(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY(),
                                                   this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY(),
                                                    this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY(),
                                                  this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY(),
                                                     this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur() >= positionZPaquetModifie + ancienneHauteurPaquet && !this.obtenirCodeBarre().equals(autrePaquet.obtenirCodeBarre())) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirPositionX(), this.obtenirPositionY())) {
                    positionZPaquet = (autrePaquet.obtenirPositionZ() == positionZPaquetModifie + ancienneHauteurPaquet) ? positionZPaquetModifie + nouvelleHauteurPaquet : positionZPaquet + hauteurPaquetDessous;
                    hauteurPaquetDessous = autrePaquet.obtenirHauteur();
                    autrePaquet.fixerPositionZ(positionZPaquet);
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    positionZPaquet = (autrePaquet.obtenirPositionZ() == positionZPaquetModifie + ancienneHauteurPaquet) ? positionZPaquetModifie + nouvelleHauteurPaquet : positionZPaquet + hauteurPaquetDessous;
                    hauteurPaquetDessous = autrePaquet.obtenirHauteur();
                    autrePaquet.fixerPositionZ(positionZPaquet);
                }
            }
        }
    }
    
    public String obtenirCodeBarrePaquetSuperieur(List<Paquet> paquets) {
        String codeBarrePaquetSuperieur = "";
        
        Line2D.Double coteHaut = new Line2D.Double(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY(),
                                                   this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY());
        Line2D.Double coteDroit = new Line2D.Double(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY(),
                                                    this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY());
        Line2D.Double coteBas = new Line2D.Double(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY(),
                                                  this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY());
        Line2D.Double coteGauche = new Line2D.Double(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY(),
                                                     this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY());

        for (Paquet autrePaquet : paquets) {
            // On traite un paquet different ayant une hauteur superieure ou égale à la hauteur actuelle
            if (autrePaquet.obtenirPositionZ() == this.obtenirPositionZ() + this.obtenirHauteur() && !this.obtenirCodeBarre().equals(autrePaquet.obtenirCodeBarre())) {
                Line2D.Double coteHaut_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY(),
                                                                       autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY());
                Line2D.Double coteDroit_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinSuperieurDroitX(), autrePaquet.obtenirCoinSuperieurDroitY(),
                                                                        autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY());
                Line2D.Double coteBas_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurDroitX(), autrePaquet.obtenirCoinInferieurDroitY(),
                                                                      autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY());
                Line2D.Double coteGauche_autrePaquet = new Line2D.Double(autrePaquet.obtenirCoinInferieurGaucheX(), autrePaquet.obtenirCoinInferieurGaucheY(),
                                                                         autrePaquet.obtenirCoinSuperieurGaucheX(), autrePaquet.obtenirCoinSuperieurGaucheY());

                // On regarde si un des sommets ou le centre, se trouve dans l'autre paquet
                if (autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurGaucheX(), this.obtenirCoinSuperieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinSuperieurDroitX(), this.obtenirCoinSuperieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurDroitX(), this.obtenirCoinInferieurDroitY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirCoinInferieurGaucheX(), this.obtenirCoinInferieurGaucheY()) ||
                        autrePaquet.verificationPointDansSurface(this.obtenirPositionX(), this.obtenirPositionY())) {
                    codeBarrePaquetSuperieur = autrePaquet.obtenirCodeBarre();
                }
                // Sinon on regarde si les cotes se croisent
                else if (coteHaut.intersectsLine(coteHaut_autrePaquet) || coteHaut.intersectsLine(coteDroit_autrePaquet) || coteHaut.intersectsLine(coteBas_autrePaquet) || coteHaut.intersectsLine(coteGauche_autrePaquet) ||
                         coteDroit.intersectsLine(coteHaut_autrePaquet) || coteDroit.intersectsLine(coteDroit_autrePaquet) || coteDroit.intersectsLine(coteBas_autrePaquet) || coteDroit.intersectsLine(coteGauche_autrePaquet) ||
                         coteBas.intersectsLine(coteHaut_autrePaquet) || coteBas.intersectsLine(coteDroit_autrePaquet) || coteBas.intersectsLine(coteBas_autrePaquet) || coteBas.intersectsLine(coteGauche_autrePaquet) ||
                         coteGauche.intersectsLine(coteHaut_autrePaquet) || coteGauche.intersectsLine(coteDroit_autrePaquet) || coteGauche.intersectsLine(coteBas_autrePaquet) || coteGauche.intersectsLine(coteGauche_autrePaquet))
                {
                    codeBarrePaquetSuperieur = autrePaquet.obtenirCodeBarre();
                }
            }
        }
        
        return codeBarrePaquetSuperieur;
    }
    
    public void modifierPaquetsDansPileApresDeplacement(List<Paquet> paquets, List<Integer> indicePaquetsAuDessus) {
        double positionZPaquet = this.obtenirPositionZ();
        int etagePaquet = this.obtenirEtage();
        
        for (Integer indicePaquet : indicePaquetsAuDessus) {
            Paquet autrePaquet = paquets.get(indicePaquet);
            
            autrePaquet.fixerPositionZ(positionZPaquet);
            autrePaquet.fixerEtage(etagePaquet);
            
            positionZPaquet = autrePaquet.obtenirPositionZ() + autrePaquet.obtenirHauteur();
            etagePaquet = autrePaquet.obtenirEtage() + VALEUR_UN;
        }
    }
}
