/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin;

import java.awt.Graphics;

/**
 *
 * @author maximeprieur
 */
public interface GrilleDessin {
    public void dessinerGrille(Graphics g, int width, int height, IMultiplicateur multiplicateur, double xOrigine, double yOrigine);
}
