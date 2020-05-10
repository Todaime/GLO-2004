/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin;

import java.io.Serializable;

import static virtubois.domain.Commun.Constantes.VALEUR_UN;

/**
 *
 * @author pcMasterRace
 */
public class Multiplicateur implements IMultiplicateur, Serializable {

    private double multiplicateurInterne;

    public Multiplicateur(double multiplicateur) {
        this.multiplicateurInterne = multiplicateur;
    }

   
    @Override
    public double Multiplicateur() {
        multiplicateurInterne = VALEUR_UN;
        return multiplicateurInterne;
    }

    @Override
    public double obtenirMultiplicateur() {
        return multiplicateurInterne;
    }

    @Override
    public double fixerMultiplicateur(double multiplicateur) {
        return this.multiplicateurInterne = multiplicateur;
    }
    
}
