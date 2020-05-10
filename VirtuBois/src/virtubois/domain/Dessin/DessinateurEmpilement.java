/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import virtubois.domain.Paquet;
import virtubois.domain.VirtuBoisControleur;

import static virtubois.domain.Commun.Constantes.COULEUR_LIGNE;
import static virtubois.domain.Commun.Constantes.COULEUR_BLANCHE;
import static virtubois.domain.Commun.Constantes.COULEUR_PAQUET;
import static virtubois.domain.Commun.Constantes.COULEUR_PAQUET_SELECTIONNE;
import static virtubois.domain.Commun.Constantes.GROSSEUR_STROKE;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;

/**
 *
 * @author maximeprieur
 */
public class DessinateurEmpilement
{
    private final VirtuBoisControleur controleur;

    public DessinateurEmpilement(VirtuBoisControleur controleur, PileDessin pileDessin) {
        this.controleur = controleur;
    }

    //TODO - MEDIUM: Cette méthode pourrait être refactorer.  Une méthode pour le if, une autre pour le else
    public void draw(Graphics g, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(GROSSEUR_STROKE));
        List<Integer> indicesPaquetsSelectionnes = this.controleur.obtenirIndicesPaquetsSelectionnes();
        g2.setColor(COULEUR_BLANCHE);
        g2.fillRect(VALEUR_ZERO, VALEUR_ZERO, width, height);

        if (!indicesPaquetsSelectionnes.isEmpty()) {
            List<Integer> listeIndicePaquetsSelectionnes = this.controleur.obtenirIndicesPaquetsSelectionnes();
            List<Integer> listeIndicesTrieParHauteur = this.controleur.obtenirListTrieParHauteur(listeIndicePaquetsSelectionnes);
            List<Paquet> paquets = this.controleur.obtenirPaquets();

            int indicePaquetAuDessus = listeIndicesTrieParHauteur.get(listeIndicesTrieParHauteur.size()-1);
            double coordZPile = paquets.get(indicePaquetAuDessus).obtenirPositionZ() + paquets.get(indicePaquetAuDessus).obtenirHauteur();            
                
            g2.setColor(COULEUR_PAQUET);
            // Calcul de xMin / xMax
            double xMin = Double.MAX_VALUE;
            double xMax =-Double.MAX_VALUE;
            List<Double> xMinPaquets = new ArrayList<>();
            List<Double> xMaxPaquets = new ArrayList<>();

            // On obtient le x Min et Max global + xMin et Max de tous les paquets de la pile
            for (int i = 0; i < listeIndicesTrieParHauteur.size(); i++) {
                Paquet paquet = paquets.get(listeIndicesTrieParHauteur.get(i));
                xMinPaquets.add(Double.MAX_VALUE);
                xMaxPaquets.add(-Double.MAX_VALUE);

                double x = paquet.obtenirCoinSuperieurGaucheX();
                if ( x > xMaxPaquets.get(i)){
                    xMaxPaquets.set(i,x);
                }
                if ( x < xMinPaquets.get(i)){
                    xMinPaquets.set(i,x);
                }

                x = paquet.obtenirCoinInferieurGaucheX();
                if ( x > xMaxPaquets.get(i)){
                    xMaxPaquets.set(i,x);
                }
                if ( x < xMinPaquets.get(i)){
                    xMinPaquets.set(i,x);
                }

                x = paquet.obtenirCoinInferieurDroitX();
                if ( x > xMaxPaquets.get(i)){
                    xMaxPaquets.set(i,x);
                }
                if ( x < xMinPaquets.get(i)){
                    xMinPaquets.set(i,x);
                }

                x = paquet.obtenirCoinSuperieurDroitX();
                if ( x > xMaxPaquets.get(i)){
                    xMaxPaquets.set(i,x);
                }
                if ( x < xMinPaquets.get(i)){
                    xMinPaquets.set(i,x);
                }

                if (xMinPaquets.get(i)< xMin) {
                    xMin = xMinPaquets.get(i);
                }

                if (xMaxPaquets.get(i)> xMax) {
                    xMax = xMaxPaquets.get(i);
                }
            }

            int indexPaquetSelectionne = VALEUR_ZERO;
            double valeurScaling;
            if (coordZPile > xMax-xMin){
                valeurScaling = coordZPile;
            } else {
                valeurScaling = xMax - xMin;
            }
            for (int i = 0 ; i <listeIndicesTrieParHauteur.size() ; i++) {
                int bordGauche = (int)(width*(xMinPaquets.get(i)-xMin)/valeurScaling);
                int bordDroit = (int)(width*(xMaxPaquets.get(i)-xMinPaquets.get(i))/valeurScaling);
                int hauteurPaquet = (int)(height*paquets.get(listeIndicesTrieParHauteur.get(i)).obtenirHauteur()/valeurScaling);
                int coordZPaquet = (int)(height*paquets.get(listeIndicesTrieParHauteur.get(i)).obtenirPositionZ()/valeurScaling);
                g2.fillRect(bordGauche, height - coordZPaquet -hauteurPaquet, bordDroit, hauteurPaquet);
                g2.setColor(COULEUR_LIGNE);
                g2.drawLine(bordGauche, height - coordZPaquet - hauteurPaquet, bordGauche, height - coordZPaquet);
                g2.drawLine(bordDroit+bordGauche, height - coordZPaquet - hauteurPaquet, bordDroit+bordGauche, height - coordZPaquet);
                g2.drawLine(bordGauche, height - coordZPaquet - hauteurPaquet, bordGauche+bordDroit, height - coordZPaquet - hauteurPaquet);
                g2.drawLine(bordGauche, height - coordZPaquet, bordGauche+bordDroit, height - coordZPaquet);
                g2.setColor(COULEUR_PAQUET);
                if (listeIndicesTrieParHauteur.get(i) == this.controleur.obtenirIndicePaquetSelectionne()) {
                    indexPaquetSelectionne = i;
                }
            }

            int indiceDuPaquet = listeIndicesTrieParHauteur.get(indexPaquetSelectionne);
            int hauteurPaquetSelectionne = (int)(height*paquets.get(indiceDuPaquet).obtenirHauteur()/valeurScaling);

            g2.setColor(COULEUR_PAQUET_SELECTIONNE);

            int bordGauche = (int)(width*(xMinPaquets.get(indexPaquetSelectionne)-xMin)/valeurScaling);
            int bordDroit = (int)(width*(xMaxPaquets.get(indexPaquetSelectionne)-xMinPaquets.get(indexPaquetSelectionne))/valeurScaling);
            int hauteurPaquet = (int)(height*paquets.get(indiceDuPaquet).obtenirHauteur()/valeurScaling);
            int coordZPaquet = (int)(height*paquets.get(indiceDuPaquet).obtenirPositionZ()/valeurScaling);
            g2.fillRect( bordGauche, height - coordZPaquet-hauteurPaquetSelectionne, bordDroit, hauteurPaquetSelectionne);
            g2.setColor(COULEUR_LIGNE);
            g2.drawLine(bordGauche, height - coordZPaquet - hauteurPaquet, bordGauche, height - coordZPaquet);
            g2.drawLine(bordDroit+bordGauche, height - coordZPaquet - hauteurPaquet, bordDroit+bordGauche, height - coordZPaquet);
            g2.drawLine(bordGauche, height - coordZPaquet - hauteurPaquet, bordGauche+bordDroit, height - coordZPaquet - hauteurPaquet);
            g2.drawLine(bordGauche, height - coordZPaquet, bordGauche+bordDroit, height - coordZPaquet);
            
        }
    }
}
