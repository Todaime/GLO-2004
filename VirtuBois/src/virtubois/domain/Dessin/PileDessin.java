/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin;

import java.awt.Graphics;
import virtubois.domain.VirtuBoisControleur;

/**
 *
 * @author maximeprieur
 */
public interface PileDessin {

    public void dessinerPaquet(VirtuBoisControleur controleur, Graphics g, int width, int height, double multiplicateur);
    
}
