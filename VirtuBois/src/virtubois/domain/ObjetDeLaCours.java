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

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import virtubois.domain.Dessin.IMultiplicateur;
import virtubois.domain.Dessin.Multiplicateur;

import static virtubois.domain.Commun.Constantes.ANGLE_MOITIER_CERCLE;
import static virtubois.domain.Commun.Constantes.ANGLE_QUART_CERCLE;
import static virtubois.domain.Commun.Constantes.ANGLE_TROIS_QUART_CERCLE;
import static virtubois.domain.Commun.Constantes.VALEUR_UN;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;

public class ObjetDeLaCours implements   Serializable 
{
    public JFrame frame;
    public ObjetDeLaCours Contenu;
    
    protected double positionX;
    protected double positionY;
    protected double positionZ;
    protected int etage;
    protected double hauteur;
    protected double largeur;
    protected double longueur;
    protected double angle;
    public IMultiplicateur multiplier;
    int[] polygonXs;
    int[] polygonYs;
    public Shape shape = new Polygon(polygonXs(), polygonYs(), polygonXs().length);
    double x = 50.0;
    double y = 50.0;

    protected VirtuBoisControleur controleur;

    public ObjetDeLaCours() {
        this.positionX = VALEUR_ZERO;
        this.positionY = VALEUR_ZERO;
        this.positionZ = VALEUR_ZERO;
        this.etage = VALEUR_ZERO;
        this.hauteur = VALEUR_ZERO;
        this.largeur = 2;
        this.longueur = 2;
        this.angle = VALEUR_ZERO;
    }
    
    // constructeur pour undo redo
    public ObjetDeLaCours(double positionX, double positionY, double positionZ, double angle, int etage, double largeur, double longueur) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.angle = angle;
        this.etage = etage;
        this.largeur = largeur;
        this.longueur = longueur;
    }
    
    public ObjetDeLaCours(Point mousePoint) {
        this.x = mousePoint.x;
        this.y = mousePoint.y;
    }
    
    public double obtenirPositionX() {
        return this.positionX;
    }
    
    //TODO - HIGH: Cette méthode devrait être protected
    public void fixerPositionX(double positionX) {
        this.positionX = positionX;
    }
    
    public double obtenirPositionY() {
        return this.positionY;
    }
    
    //TODO - HIGH: Cette méthode devrait être protected
    public void fixerPositionY(double positionY) {
        this.positionY = positionY;
    }
    
    public double obtenirPositionZ() {
        return this.positionZ;
    }
    
    //TODO - HIGH: Cette méthode devrait être protected
    public void fixerPositionZ(double positionZ) {
        this.positionZ = positionZ;
    }
    
    public int obtenirEtage() {
        return this.etage;
    }
    
    //TODO - HIGH: Cette méthode devrait être protected
    public void fixerEtage(int etage) {
        this.etage = etage;
    }
    
    public double obtenirHauteur() {
        return this.hauteur;
    }
    
    protected void fixerHauteur(double hauteur) {
        this.hauteur = hauteur;
    }
    
    public double obtenirLargeur() {
        return this.largeur;
    }
    
    public void fixerLargeur(double largeur) {
        this.largeur = largeur;
    }
    
    public double obtenirLongueur() {
        return this.longueur;
    }
    
    public void fixerLongueur(double longueur) {
        this.longueur = longueur;
    }
    
    public double obtenirAngle() {
        return this.angle;
    }
    
    public void fixerAngle(double angle) {
        this.angle = angle;
    }
    
    public boolean contains(Point test) {
        Point offset = new Point((int)this.obtenirPositionX(), (int)this.obtenirPositionY());
        Point[] lesPoints = points(offset);
        int i;
        int j;
        boolean result = false;

        for (i = 0, j = lesPoints.length - 1; i < lesPoints.length; j = i++) {
            //TODO - MEDIUM: Simplifier cette validation ou la mettre dans un méthode à part
            if ((lesPoints[i].y > test.y) != (lesPoints[j].y > test.y) &&
                (test.x < (lesPoints[j].x - lesPoints[i].x) * (test.y - lesPoints[i].y) / (lesPoints[j].y-lesPoints[i].y) + lesPoints[i].x)) {
                result = !result;
            }
        }

        return result;
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public int[] polygonXs() {
        return new int[] {
            (int)Math.round(-(this.obtenirLongueur()/2) * this.obtenirMultiplier()),
            (int)Math.round(this.obtenirLongueur()/2 * this.obtenirMultiplier()),
            (int)Math.round(this.obtenirLongueur()/2 * this.obtenirMultiplier()),
            (int)Math.round(-(this.obtenirLongueur()/2) * this.obtenirMultiplier())
        };
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public int[] polygonYs() {
        return new int[] {
            (int)Math.round(-(this.obtenirLargeur()/2) * this.obtenirMultiplier()),
            (int)Math.round(-(this.obtenirLargeur()/2) * this.obtenirMultiplier()),
            (int)Math.round(this.obtenirLargeur()/2* this.obtenirMultiplier()),
            (int)Math.round(this.obtenirLargeur()/2 * this.obtenirMultiplier())
        };
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public Shape shape(IMultiplicateur multiplicateur){
        this.multiplier = multiplicateur;
        return new Polygon(polygonXs(), polygonYs(), polygonXs().length);
    }

    public Point deplacementPourGraphique() {
        this.multiplier.fixerMultiplicateur(obtenirMultiplier());

        double pointX = obtenirMultiplier() * this.obtenirPositionX();
        double pointY = obtenirMultiplier() * this.obtenirPositionY();

        AffineTransform identity = new AffineTransform();
        identity.translate(pointX, pointY);
        identity.rotate(this.obtenirThetaRotation());

        //TODO - HIGH: Ce bout de code est présent dans cette classe.  Mettre dans une méthode centralisée
        if (this.Contenu != null) {
            this.Contenu.fixerPositionX((int)Math.round(this.obtenirPositionX() + this.obtenirLongueur()/2 * this.obtenirMultiplier() * this.obtenirCosinusRadian(this.obtenirAngle())));
            this.Contenu.fixerPositionY((int)Math.round(this.obtenirPositionY() + this.obtenirLargeur()/2 * this.obtenirMultiplier() * this.obtenirSinusRadian(this.obtenirAngle())));
            this.Contenu.fixerAngle(this.obtenirAngle());
        }

        return new Point((int)pointX, (int)pointY);
    }

    protected double obtenirSinusRadian(double angle) {
        if (angle - ANGLE_QUART_CERCLE < VALEUR_ZERO) {
           return Math.sin(Math.toRadians(angle + ANGLE_TROIS_QUART_CERCLE));
        }
        
        return Math.sin(Math.toRadians(angle - ANGLE_QUART_CERCLE));
    }

    protected double obtenirCosinusRadian(double angle) {
        if (angle - ANGLE_QUART_CERCLE < VALEUR_ZERO) {
           return Math.cos(Math.toRadians(angle + ANGLE_TROIS_QUART_CERCLE));
        }

        return Math.cos(Math.toRadians(angle - ANGLE_QUART_CERCLE));
    }

    protected double obtenirMultiplier() {
        if (controleur == null) {
            if(this.multiplier == null) {
                this.multiplier = new Multiplicateur(VALEUR_UN);
            }
            return this.multiplier.obtenirMultiplicateur();
        } else {
            return controleur.obtenirFacteurDeZoom();
        }
    }

    private double obtenirThetaRotation() {
        //TODO - HIGH: Ce bout de code est présent dans cette classe.  Mettre dans une méthode centralisée
        if (this.Contenu != null) {
            this.Contenu.fixerPositionX((int)Math.round(this.obtenirPositionX() + this.obtenirLongueur() * this.obtenirMultiplier() * this.obtenirCosinusRadian(this.obtenirAngle())));
            this.Contenu.fixerPositionY((int)Math.round(this.obtenirPositionY() + this.obtenirLargeur() * this.obtenirMultiplier() * this.obtenirSinusRadian(this.obtenirAngle())));
            this.Contenu.fixerAngle(this.obtenirAngle());
        }

        return Math.toRadians(this.angle);
    }
    
    //TODO - MEDIUM: Nom pas assez détaillé
    private Point[] points(Point offset) {
        Point[] tousLesPoints = new Point[polygonXs().length];

        for (int i = 0; i < polygonXs().length; i++) {
            tousLesPoints[i] = new Point(polygonXs()[i] + offset.x,
                                         polygonYs()[i] + offset.y);
        }

        return tousLesPoints;
    }

    public double obtenirCoinSuperieurGaucheX() {
        double xSupGauche = this.obtenirPositionX() - this.obtenirLongueur()/2.0;
        double ySupGauche = this.obtenirPositionY() - this.obtenirLargeur()/2.0;
        return this.obtenirCoinAvecAngleX(xSupGauche, ySupGauche);
    }

    public double obtenirCoinSuperieurGaucheY() {
        double xSupGauche = this.obtenirPositionX() - this.obtenirLongueur()/2.0;
        double ySupGauche = this.obtenirPositionY() - this.obtenirLargeur()/2.0;
        return this.obtenirCoinAvecAngleY(xSupGauche, ySupGauche);
    }

    public double obtenirCoinSuperieurDroitX() {
        double xSupDroit = this.obtenirPositionX() + this.obtenirLongueur()/2.0;
        double ySupDroit = this.obtenirPositionY() - this.obtenirLargeur()/2.0;
        return this.obtenirCoinAvecAngleX(xSupDroit, ySupDroit);
    }

    public double obtenirCoinSuperieurDroitY() {
        double xSupDroit = this.obtenirPositionX() + this.obtenirLongueur()/2.0;
        double ySupDroit = this.obtenirPositionY() - this.obtenirLargeur()/2.0;
        return this.obtenirCoinAvecAngleY(xSupDroit, ySupDroit);
    }

    public double obtenirCoinInferieurDroitX() {
        double xInfDroit = this.obtenirPositionX() + this.obtenirLongueur()/2;
        double yInfDroit = this.obtenirPositionY() + this.obtenirLargeur()/2;
        return this.obtenirCoinAvecAngleX(xInfDroit, yInfDroit);
    }

    public double obtenirCoinInferieurDroitY() {
        double xInfDroit = this.obtenirPositionX() + this.obtenirLongueur()/2.0;
        double yInfDroit = this.obtenirPositionY() + this.obtenirLargeur()/2.0;
        return this.obtenirCoinAvecAngleY(xInfDroit, yInfDroit);
    }

    public double obtenirCoinInferieurGaucheX() {
        double xInfGauche = this.obtenirPositionX() - this.obtenirLongueur()/2.0;
        double yInfGauche = this.obtenirPositionY() + this.obtenirLargeur()/2.0;
        return this.obtenirCoinAvecAngleX(xInfGauche, yInfGauche);
    }

    public double obtenirCoinInferieurGaucheY() {
        double xInfGauche = this.obtenirPositionX() - this.obtenirLongueur()/2.0;
        double yInfGauche = this.obtenirPositionY() + this.obtenirLargeur()/2.0;
        return this.obtenirCoinAvecAngleY(xInfGauche, yInfGauche);
    }

    public double obtenirCoinAvecAngleX(double coordonneeX, double coordonneeY) {
        double xcentre = coordonneeX - this.obtenirPositionX();
        double ycentre = coordonneeY - this.obtenirPositionY();
        double theta = Math.PI*this.obtenirAngle() / ANGLE_MOITIER_CERCLE;
        return (xcentre*Math.cos(-theta) + ycentre*Math.sin(-theta)) + this.obtenirPositionX();
    }

    public double obtenirCoinAvecAngleY(double coordonneeX, double coordonneeY) {
        double xcentre = coordonneeX - this.obtenirPositionX();
        double ycentre = coordonneeY - this.obtenirPositionY();
        double theta = Math.PI*this.obtenirAngle() / ANGLE_MOITIER_CERCLE;
        return (-xcentre*Math.sin(-theta) + ycentre*Math.cos(-theta)) + this.obtenirPositionY();
    }

   public boolean verificationPointDansSurface(double xPoint, double yPoint) {
       //TODO - MEDIUM: Cette logique est présente ailleurs, il faudrait mettre dans un helper
        double pointX = (double)(xPoint - this.obtenirPositionX());
        double pointY = (double)(yPoint - this.obtenirPositionY());
        double theta = Math.PI*this.obtenirAngle() / ANGLE_MOITIER_CERCLE;

        double pointXTemporaire = pointX;
        double pointYTemporaire = pointY;

        pointX = pointXTemporaire*Math.cos(theta) + pointYTemporaire*Math.sin(theta);
        pointY = -pointXTemporaire*Math.sin(theta) + pointYTemporaire*Math.cos(theta);

        boolean appartient = true;

        if (pointX >= this.obtenirLongueur()/2 || pointX <= -this.obtenirLongueur()/2) {
            appartient = false;
        }

        if (pointY >= this.obtenirLargeur()/2 || pointY <= -this.obtenirLargeur()/2) {
            appartient = false;
        }

        return appartient;
    }
}


