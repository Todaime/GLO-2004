/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Commun;

import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;

/**
 *
 * @author Trahanqc
 */
public class Point3D {
    private double positionX;
    private double positionY;
    private double positionZ;
    
    public Point3D() {
        this.positionX = VALEUR_ZERO;
        this.positionY = VALEUR_ZERO;
        this.positionZ = VALEUR_ZERO;
    }
    
    public Point3D(double positionX, double positionY, double positionZ) {
        this.positionX = positionX;
        this.positionY = positionY * -1;  //Remarque: On doit faire ça car le plan cartésien est à l'envers.  Sinon, les paquets ne seront pas dans le bon ordre d'affichage dans l'exportation 3D
        this.positionZ = positionZ;
    }
    
    public double obtenirX() {
        return this.positionX;
    }
    
    public void fixerX(double positionX) {
        this.positionX = positionX;
    }
    
    public double obtenirY() {
        return this.positionY;
    }
    
    public void fixerY(double positionY) {
        this.positionY = positionY;
    }
    
    public double obtenirZ() {
        return this.positionZ;
    }
    
    public void fixerZ(double positionZ) {
        this.positionZ = positionZ;
    }
}
