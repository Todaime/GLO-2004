/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin.Basique;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import virtubois.domain.Dessin.PaquetDessin;
import virtubois.domain.Paquet;
import java.awt.geom.AffineTransform;
import virtubois.domain.Dessin.IMultiplicateur;

import static virtubois.domain.Commun.Constantes.COULEUR_PAQUET;
import static virtubois.domain.Commun.Constantes.COULEUR_NOIR;
import static virtubois.domain.Commun.Constantes.COULEUR_PAQUET_SELECTIONNE;
import static virtubois.domain.Commun.Constantes.HAUTEUR_BORDURE;

/**
 *
 * @author maximeprieur
 */
public class BasiqueDessinPaquet implements PaquetDessin
{
    @Override
    public void dessinerPaquetSelectionne(Graphics g, Paquet paquet, IMultiplicateur multiplicateur, double xOrigine, double yOrigine) {
        this.dessinerPaquetDansPlan(g,
                                    paquet,
                                    multiplicateur,
                                    xOrigine,
                                    yOrigine,
                                    COULEUR_PAQUET_SELECTIONNE);
    }
    
    @Override
    public void dessinerPaquet(Graphics g, Paquet paquet, IMultiplicateur multiplicateur, double xOrigine, double yOrigine) {
        this.dessinerPaquetDansPlan(g,
                                    paquet,
                                    multiplicateur,
                                    xOrigine,
                                    yOrigine,
                                    COULEUR_PAQUET);
    }
    
    private void dessinerPaquetDansPlan(Graphics g, Paquet paquet, IMultiplicateur multiplicateur, double xOrigine, double yOrigine, Color couleurPaquet) {
        double pointX = multiplicateur.obtenirMultiplicateur()*(paquet.obtenirPositionX() - xOrigine);
        double pointY = multiplicateur.obtenirMultiplicateur()*(paquet.obtenirPositionY() - yOrigine);

        AffineTransform oldXForm = ((Graphics2D) g).getTransform();
        g.setColor(couleurPaquet);
        AffineTransform identity = new AffineTransform();
        identity.translate(pointX, pointY);
        identity.rotate(Math.toRadians(paquet.obtenirAngle()));
        ((Graphics2D) g).setTransform(identity);
        ((Graphics2D) g).fill(paquet.shape(multiplicateur));
        g.setColor(COULEUR_NOIR);
        ((Graphics2D) g).setStroke(new BasicStroke(HAUTEUR_BORDURE));
        ((Graphics2D) g).draw(paquet.shape(multiplicateur));
        ((Graphics2D) g).setTransform(oldXForm);
    }
}
