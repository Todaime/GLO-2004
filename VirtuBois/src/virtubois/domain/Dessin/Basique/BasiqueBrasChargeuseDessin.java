package virtubois.domain.Dessin.Basique;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import virtubois.domain.BrasChargeuse;
import virtubois.domain.Chargeuse;
import virtubois.domain.Dessin.BrasChargeuseDessin;
import virtubois.domain.Dessin.IMultiplicateur;

import static virtubois.domain.Commun.Constantes.COULEUR_BRAS;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author maximeprieur
 */
public class BasiqueBrasChargeuseDessin implements BrasChargeuseDessin
{
    @Override
    public void dessinerBrasChargeuse(Graphics g, Chargeuse chargeuse, IMultiplicateur multiplicateur, double xOrigine, double yOrigine) {
        BrasChargeuse bras = chargeuse.obtenirPaireDeBras();
        double pointX = multiplicateur.obtenirMultiplicateur()*(bras.obtenirPositionX() - xOrigine);
        double pointY = multiplicateur.obtenirMultiplicateur()*(bras.obtenirPositionY() - yOrigine);

        AffineTransform oldXForm = ((Graphics2D) g).getTransform();
        g.setColor(COULEUR_BRAS);
        AffineTransform identity = new AffineTransform();
        identity.translate(pointX, pointY);
        identity.rotate(Math.toRadians(bras.obtenirAngle()));
        ((Graphics2D) g).setTransform(identity);
        ((Graphics2D) g).fill(bras.shape1(multiplicateur));
        ((Graphics2D) g).fill(bras.shape2(multiplicateur));
        ((Graphics2D) g).setTransform(oldXForm);

    }
}