/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Dessin.Basique;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import virtubois.domain.Dessin.ChargeuseDessin;

import static virtubois.domain.Commun.Constantes.COULEUR_CHARGEUSE;

/**
 *
 * @author maximeprieur
 */
import virtubois.domain.Chargeuse;
import virtubois.domain.Dessin.BrasChargeuseDessin;
import virtubois.domain.Dessin.IMultiplicateur;

public class BasiqueChargeuseDessin implements ChargeuseDessin
{
    private BrasChargeuseDessin brasChargeuseDessin;
    
    public BasiqueChargeuseDessin() {
        this.brasChargeuseDessin = new BasiqueBrasChargeuseDessin();
    }
    
    @Override
    public void dessinerChargeuse(Graphics g, Chargeuse chargeuse, IMultiplicateur multiplicateur, double xOrigine, double yOrigine) {
        double pointX = multiplicateur.obtenirMultiplicateur()*(chargeuse.obtenirPositionX() - xOrigine);
        double pointY = multiplicateur.obtenirMultiplicateur()*(chargeuse.obtenirPositionY() - yOrigine);
        
        AffineTransform oldXForm = ((Graphics2D) g).getTransform();
        g.setColor(COULEUR_CHARGEUSE);
        AffineTransform identity = new AffineTransform();
        identity.translate(pointX, pointY);
        identity.rotate(Math.toRadians(chargeuse.obtenirAngle()));
        ((Graphics2D) g).setTransform(identity);
        ((Graphics2D) g).fill(chargeuse.shape(multiplicateur));
        ((Graphics2D) g).setTransform(oldXForm);
        this.dessinerBrasChargeuse(g, chargeuse, multiplicateur, xOrigine, yOrigine);
    }
    
    private void dessinerBrasChargeuse(Graphics g, Chargeuse chargeuse, IMultiplicateur multiplicateur, double xOrigine, double yOrigine) {
        this.brasChargeuseDessin.dessinerBrasChargeuse(g, chargeuse, multiplicateur, xOrigine, yOrigine);
    }
}
