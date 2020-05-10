/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin;

import java.awt.BasicStroke;
import virtubois.domain.VirtuBoisControleur;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import virtubois.domain.Paquet;

import static virtubois.domain.Commun.Constantes.COULEUR_BLANCHE;
import static virtubois.domain.Commun.Constantes.GROSSEUR_STROKE;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;

/**
 *
 * @author maximeprieur
 */
public class DessinateurVirtuBois
{
    private final VirtuBoisControleur controleur;
    private final PaquetDessin paquetDessin;
    private final ChargeuseDessin chargeuseDessin;
    private final GrilleDessin grilleDessin;

    public DessinateurVirtuBois(VirtuBoisControleur controleur,
                                PaquetDessin paquetDessin,
                                ChargeuseDessin chargeuseDessin,
                                GrilleDessin grilleDessin) {
        this.controleur = controleur;
        this.grilleDessin = grilleDessin;
        this.chargeuseDessin = chargeuseDessin;
        this.paquetDessin = paquetDessin;
    }

    public void draw(Graphics g,
                     int width,
                     int height,
                     IMultiplicateur multiplicateur,
                     double xOrigine,
                     double yOrigine) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(GROSSEUR_STROKE));
        g2.setColor(COULEUR_BLANCHE);
        g2.fillRect(VALEUR_ZERO, VALEUR_ZERO, width, height);
        grilleDessin.dessinerGrille(g,
                                    width,
                                    height,
                                    multiplicateur,
                                    xOrigine,
                                    yOrigine);
        
        List<Integer> indicesATrier = new ArrayList<>();

        for (int i = 0; i < controleur.obtenirPaquets().size(); i++) {
            indicesATrier.add(i);
        }

        indicesATrier = controleur.obtenirListTrieParHauteur(indicesATrier);
        List<Paquet> lesPaquet = controleur.obtenirPaquets();
        boolean chargeuseDessinee = false;
        double hauteurBras = controleur.obtenirChargeuse().obtenirHauteurBras();
        
        for (int i = 0; i < indicesATrier.size(); i++) {
            if (hauteurBras <= lesPaquet.get(indicesATrier.get(i)).obtenirPositionZ() && chargeuseDessinee == false) {
                chargeuseDessinee = true;
                chargeuseDessin.dessinerChargeuse(g2,
                                          controleur.obtenirChargeuse(),
                                          multiplicateur,
                                          xOrigine,
                                          yOrigine);
            }
            if (indicesATrier.get(i) == controleur.obtenirIndicePaquetSelectionne()) {
                paquetDessin.dessinerPaquetSelectionne(g2,
                                                       lesPaquet.get(indicesATrier.get(i)),
                                                       multiplicateur,
                                                       xOrigine,
                                                       yOrigine);
            }
            else {
                paquetDessin.dessinerPaquet(g2,
                                            lesPaquet.get(indicesATrier.get(i)),
                                            multiplicateur,
                                            xOrigine,
                                            yOrigine);
            }
        }
        if (chargeuseDessinee == false) {
                            chargeuseDessin.dessinerChargeuse(g2,
                                          controleur.obtenirChargeuse(),
                                          multiplicateur,
                                          xOrigine,
                                          yOrigine);
        }
        
    }

}

