/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain;

import java.awt.Polygon;
import java.awt.Shape;
import java.io.Serializable;
import static virtubois.domain.Commun.Constantes.ANGLE_MOITIER_CERCLE;
import virtubois.domain.Dessin.IMultiplicateur;

import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;

/**
 *
 * @author maximeprieur
 */
public class BrasChargeuse extends ObjetDeLaCours implements Serializable
{
    public BrasChargeuse(double posXChargeuse,
                         double posYChargeuse,
                         double angleChargeuse,
                         double longueurChargeuse,
                         double hauteurBrasChargeuse,
                         double longueurBrasChargeuse,
                         double largeurBrasChargeuse) {
        super();
        this.etage = VALEUR_ZERO;
        this.longueur = longueurBrasChargeuse;
        this.largeur  = largeurBrasChargeuse;
        this.hauteur = hauteurBrasChargeuse;
        this.angle = angleChargeuse;
        this.fixerPositionDepuisChargeuse(angleChargeuse, posXChargeuse, posYChargeuse, longueurChargeuse);
    }

    public void descendreBras(double diminution) {
        this.fixerPositionZ(this.obtenirPositionZ() - diminution);
    }

    public void monterBras(double augmentation) {
        this.fixerPositionZ(this.obtenirPositionZ() + augmentation);
    }

    public void majPosition(double posXChargeuse, double posYChargeuse, double angleChargeuse, double longeurChargeuse, double largeurChargeuse) {
        this.fixerPositionDepuisChargeuse(angleChargeuse, posXChargeuse, posYChargeuse, longeurChargeuse);
        this.fixerAngle(angleChargeuse);
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public int[] polygonYs1() {
        return new int[] {
            (int)Math.round((-this.obtenirLargeur()/8) * this.obtenirMultiplier()),
            (int)Math.round(-this.obtenirLargeur()/8 * this.obtenirMultiplier()),
            (int)Math.round(-this.obtenirLargeur()/2 * this.obtenirMultiplier()),
            (int)Math.round((-this.obtenirLargeur()/2) * this.obtenirMultiplier())
        };
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public int[] polygonYs2() {
        return new int[] {
            (int)Math.round((this.obtenirLargeur()/2) * this.obtenirMultiplier()),
            (int)Math.round(this.obtenirLargeur()/2 * this.obtenirMultiplier()),
            (int)Math.round(this.obtenirLargeur()/8 * this.obtenirMultiplier()),
            (int)Math.round((this.obtenirLargeur()/8) * this.obtenirMultiplier())
        };
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public int[] polygonXs() {
        return new int[] {
            (int)Math.round(-(this.obtenirLongueur()/2) * this.obtenirMultiplier()),
            (int)Math.round((this.obtenirLongueur()/2) * this.obtenirMultiplier()),
            (int)Math.round(this.obtenirLongueur()/2 * this.obtenirMultiplier()),
            (int)Math.round(-this.obtenirLongueur()/2 * this.obtenirMultiplier())
        };
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public Shape shape1(IMultiplicateur multiplicateur){
        this.multiplier = multiplicateur;
        return new Polygon(polygonXs(),polygonYs1(), polygonYs1().length);
    }

    //TODO - MEDIUM: Nom pas assez détaillé
    public Shape shape2(IMultiplicateur multiplicateur){
        this.multiplier = multiplicateur;
        return new Polygon(polygonXs(), polygonYs2(), polygonYs2().length);
    }

    public void fixerPositionDepuisChargeuse(double angleChargeuse, double posXChargeuse, double posYChargeuse, double longueurChargeuse) {
        double theta = -Math.PI*angleChargeuse / ANGLE_MOITIER_CERCLE;
        double ecart = this.longueur/2 + longueurChargeuse/2;
        double pointX = ecart;
        
        positionX = pointX * Math.cos(theta) + posXChargeuse;
        positionY = -pointX * Math.sin(theta) + posYChargeuse;
    }

}
