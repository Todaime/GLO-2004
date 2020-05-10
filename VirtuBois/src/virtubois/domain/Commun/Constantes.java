/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain.Commun;

import java.awt.Color;

/**
 *
 * @author Trahanqc
 */
public final class Constantes {
    
    private Constantes() {
        //Remarque: On ne veut pas appelé directement cette classe
    }
    
    public final static double VALEUR_UN_DIXIEME = 0.1; //TODO - MEDIUM: trouver un meilleur nom pour cette constante
    public final static double VALEUR_UN_CENTIEME = 0.01; //TODO - MEDIUM: trouver un meilleur nom pour cette constante
    public final static int ANGLE_COMPLET_CERCLE = 360;
    public final static int ANGLE_DEPLACEMENT_CHARGEUSE = 4;
    public final static int ANGLE_MOITIER_CERCLE = 180;
    public final static int ANGLE_QUART_CERCLE = 90;
    public final static int ANGLE_TROIS_QUART_CERCLE = 270;
    public final static int GROSSEUR_STROKE = 5;
    public final static int HAUTEUR_BORDURE = 2;
    public final static int MULTIPLICATEUR_PAR_DEFAULT = 40;
    public final static int NOMBRE_COIN_RECTANGLE = 4;
    public final static int POSITION_ORIGINE_X = -20;
    public final static int POSITION_ORIGINE_Y = -13;
    public final static int TEMPS_RAFRAICHISSEMENT_APPLICATION = 100;
    public final static int VALEUR_DIX_MILLE = 10000; //TODO - MEDIUM: Trouver un meilleur nom pour cette constante
    public final static int VALEUR_UN = 1;
    public final static int VALEUR_UN_MILLION = 1000000;
    public final static int VALEUR_ZERO = 0;
    public final static double VITESSE_DEPLACEMENT_CHARGEUSE = 10.0;
    
    public final static String NOUVEAU_PAQUET_PRODUIT = "Chêne";
    public final static double NOUVEAU_PAQUET_LARGEUR = 2;
    public final static double NOUVEAU_PAQUET_LONGUEUR = 4;
    public final static double NOUVEAU_PAQUET_ANGLE = 0;
    public final static double NOUVEAU_PAQUET_HAUTEUR = 5;
    
    public final static Color COULEUR_BLANCHE = new Color(239, 239, 239);
    public final static Color COULEUR_BORDURE_CHAMP = new Color(189, 189, 189);
    public final static Color COULEUR_BORDURE_CHAMP_SELECTIONNE = new Color(48, 63, 159);
    public final static Color COULEUR_BOUTON = new Color(63,82,181);
    public final static Color COULEUR_BOUTON_SELECTIONNE = new Color(44, 55, 126);
    public final static Color COULEUR_BRAS = new Color(163, 163, 163);
    public final static Color COULEUR_CHAMP_ERREUR = new Color(244, 68, 54);
    public final static Color COULEUR_CHARGEUSE = new Color(255, 193, 7);
    public final static Color COULEUR_GRILLE = new Color(117, 117, 117);
    public final static Color COULEUR_LIBELLE = new Color(0, 0, 0);
    public final static Color COULEUR_LIGNE = new Color(0, 0, 0);
    public final static Color COULEUR_PAQUET = new Color(136, 66, 29);
    public final static Color COULEUR_PAQUET_SELECTIONNE = new Color(155, 86, 49);
    public final static Color COULEUR_NOIR = new Color(0, 0, 0);
}
