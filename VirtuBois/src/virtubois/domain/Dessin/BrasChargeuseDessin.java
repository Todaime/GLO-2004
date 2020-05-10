/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin;

import java.awt.Graphics;
import virtubois.domain.Chargeuse;

/**
 *
 * @author maximeprieur
 */
public interface BrasChargeuseDessin  {
    
    public void dessinerBrasChargeuse(Graphics g, Chargeuse chargeuse, IMultiplicateur multiplicateur,double xOrigine,double yOrigine);
}

