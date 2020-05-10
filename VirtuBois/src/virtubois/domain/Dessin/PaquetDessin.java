/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin;

import java.awt.Graphics;
import virtubois.domain.Paquet;

/**
 *
 * @author pcMasterRace
 */
public interface PaquetDessin {

    public void dessinerPaquet(Graphics g, 
                               Paquet paquet, 
                               IMultiplicateur multiplicateur, 
                               double xOrigine, 
                               double yOrigine);
    
    public void dessinerPaquetSelectionne(Graphics g,
                                          Paquet paquet,
                                          IMultiplicateur multiplicateur,
                                          double xOrigine,
                                          double yOrigine);
    
}