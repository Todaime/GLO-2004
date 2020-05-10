/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin.Basique;

import java.awt.Graphics;
import virtubois.domain.Dessin.GrilleDessin;
import virtubois.domain.Dessin.IMultiplicateur;

import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;
import static virtubois.domain.Commun.Constantes.COULEUR_GRILLE;
/**
 *
 * @author maximeprieur
 */
public class BasiqueGrilleDessin implements GrilleDessin
{
    @Override
    public void dessinerGrille(Graphics g2, int width, int height, IMultiplicateur multiplicateur, double xOrigine, double yOrigine) {
        g2.setColor(COULEUR_GRILLE);
        double pointX = multiplicateur.obtenirMultiplicateur()*(xOrigine);
        double pointY = multiplicateur.obtenirMultiplicateur()*(yOrigine);

        double largeurCarre = multiplicateur.obtenirMultiplicateur();
        double offSetX = -pointX%(-largeurCarre);
        double offSetY = -pointY%(-largeurCarre);
        
        for (double i =  offSetX; i < width ; i += largeurCarre){
            g2.drawLine((int)i, VALEUR_ZERO, (int)i, height);
        }
        
        for (double i = offSetY; i < height; i += largeurCarre){
            g2.drawLine(VALEUR_ZERO, (int)i, width, (int)i);
        }
    }
}
