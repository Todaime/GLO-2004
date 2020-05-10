/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import virtubois.gui.MainWindow;

import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;
import virtubois.domain.Commun.Point3D;

/**
 *
 * @author Trahanqc
 */
public class SerialisationFichier3D {
    final String ENTETE_FICHIER = "solid VirtuBoisEquipe4";
    final String ENTETE_TRIANGLE = "\n  facet normal";
    final String DEBUT_BOUCLE = "\n    outer loop";
    final String VERTEX = "\n      vertex";
    final String FIN_BOUCLE = "\n    endloop";
    final String FIN_TRIANGLE = "\n  endfacet";
    final String FIN_FICHIER = "\nendsolid";
    
    private BufferedWriter constructeurFichier;
    private final MainWindow fenetrePrincipale;
    private final VirtuBoisControleur controleur;
        
    public SerialisationFichier3D(MainWindow fenetrePrincipale, VirtuBoisControleur controleur) {
        this.constructeurFichier = null;
        this.fenetrePrincipale = fenetrePrincipale;
        this.controleur = controleur;
    }

    public void exporter() {
        if (this.controleur.obtenirPaquets().size() > VALEUR_ZERO) {
            JFileChooser fenetreSauvegarde = new JFileChooser();
            fenetreSauvegarde.setDialogTitle("Exporter le plan de la cours en 3D");

            fenetreSauvegarde.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fenetreSauvegarde.setSelectedFile(new File("virtubois-equipe4.stl"));
            fenetreSauvegarde.setFileFilter(new FileNameExtensionFilter("Standard Tessellation Language (*.stl)", "stl"));

            //Remaque: On désactive l'option "Tous les fichiers"
            fenetreSauvegarde.setAcceptAllFileFilterUsed(false);

            if (fenetreSauvegarde.showSaveDialog(this.fenetrePrincipale) == JFileChooser.APPROVE_OPTION) {
                this.exporterDansFichier3D(fenetreSauvegarde.getSelectedFile().getPath());
            } else {
                //TODO - MEDIUM: Afficher un message d'erreur
                System.out.println("No Selection");
            }
        }
        else {
            //TODO - MEDIUM: Afficher un message d'erreur
            System.out.println("Aucun paquets à exporter");
        }
    }

    private void exporterDansFichier3D(String cheminEnregistrement) {
        try (BufferedWriter constructeur = new BufferedWriter(new FileWriter(cheminEnregistrement))) {
            this.fixerConstructeurFichier(constructeur);
            this.ecrireEnteteFichier();
            this.ecrireEnsemblePaquet();
            this.ecrireFinFichier();
            
            this.constructeurFichier.flush();
            this.constructeurFichier.close();
        }
        catch (IOException e) {
            //TODO - HIGH: Il faut afficher un message lorsqu'on ne peut pas sauvegarder
            e.printStackTrace();
        }
    }
    
    private void ecrireEnteteFichier() throws IOException {
        this.constructeurFichier.write(ENTETE_FICHIER);
    }
    
    private void ecrireEnsemblePaquet() throws IOException {
        for (Paquet paquet : this.controleur.obtenirPaquets()) {
            this.ecrirePaquet(paquet);
        }
    }
    
    private void ecrirePaquet(Paquet paquet) throws IOException {
        double hauteurPaquet = paquet.obtenirPositionZ() + paquet.obtenirHauteur();
        
        this.ecrireFaceGauche(paquet, hauteurPaquet);
        this.ecrireFaceArriere(paquet, hauteurPaquet);
        this.ecrireFaceHaut(paquet, hauteurPaquet);
        this.ecrireFaceFace(paquet, hauteurPaquet);
        this.ecrireFaceBas(paquet);
        this.ecrireFaceDroite(paquet, hauteurPaquet);
    }
    
    private void ecrireFaceGauche(Paquet paquet, double hauteurPaquet) throws IOException {
        Point3D pointHautGauche = new Point3D(paquet.obtenirCoinSuperieurGaucheX(),
                                              paquet.obtenirCoinSuperieurGaucheY(),
                                              hauteurPaquet);
        Point3D pointBasGauche = new Point3D(paquet.obtenirCoinSuperieurGaucheX(),
                                             paquet.obtenirCoinSuperieurGaucheY(),
                                             paquet.obtenirPositionZ());
        Point3D pointHautDroit = new Point3D(paquet.obtenirCoinInferieurGaucheX(),
                                             paquet.obtenirCoinInferieurGaucheY(),
                                             hauteurPaquet);
        Point3D pointBasDroit = new Point3D(paquet.obtenirCoinInferieurGaucheX(),
                                            paquet.obtenirCoinInferieurGaucheY(),
                                            paquet.obtenirPositionZ());
        
        this.ecrireTriangle(pointBasGauche, pointBasDroit, pointHautGauche);
        this.ecrireTriangle(pointHautDroit, pointHautGauche, pointBasDroit);
    }
    
    private void ecrireFaceArriere(Paquet paquet, double hauteurPaquet) throws IOException {
        Point3D pointHautGauche = new Point3D(paquet.obtenirCoinSuperieurDroitX(),
                                              paquet.obtenirCoinSuperieurDroitY(),
                                              hauteurPaquet);
        Point3D pointBasGauche = new Point3D(paquet.obtenirCoinSuperieurDroitX(),
                                             paquet.obtenirCoinSuperieurDroitY(),
                                             paquet.obtenirPositionZ());
        Point3D pointHautDroit = new Point3D(paquet.obtenirCoinSuperieurGaucheX(),
                                             paquet.obtenirCoinSuperieurGaucheY(),
                                             hauteurPaquet);
        Point3D pointBasDroit = new Point3D(paquet.obtenirCoinSuperieurGaucheX(),
                                            paquet.obtenirCoinSuperieurGaucheY(),
                                            paquet.obtenirPositionZ());
        
        this.ecrireTriangle(pointBasGauche, pointBasDroit, pointHautGauche);
        this.ecrireTriangle(pointHautDroit, pointHautGauche, pointBasDroit);
    }
    
    private void ecrireFaceHaut(Paquet paquet, double hauteurPaquet) throws IOException {
        Point3D pointHautGauche = new Point3D(paquet.obtenirCoinSuperieurGaucheX(),
                                              paquet.obtenirCoinSuperieurGaucheY(),
                                              hauteurPaquet);
        Point3D pointBasGauche = new Point3D(paquet.obtenirCoinInferieurGaucheX(),
                                             paquet.obtenirCoinInferieurGaucheY(),
                                             hauteurPaquet);
        Point3D pointHautDroit = new Point3D(paquet.obtenirCoinSuperieurDroitX(),
                                             paquet.obtenirCoinSuperieurDroitY(),
                                             hauteurPaquet);
        Point3D pointBasDroit = new Point3D(paquet.obtenirCoinInferieurDroitX(),
                                            paquet.obtenirCoinInferieurDroitY(),
                                            hauteurPaquet);
        
        this.ecrireTriangle(pointBasGauche, pointBasDroit, pointHautGauche);
        this.ecrireTriangle(pointHautDroit, pointHautGauche, pointBasDroit);
    }
    
    private void ecrireFaceFace(Paquet paquet, double hauteurPaquet) throws IOException {
        Point3D pointHautGauche = new Point3D(paquet.obtenirCoinInferieurGaucheX(),
                                              paquet.obtenirCoinInferieurGaucheY(),
                                              hauteurPaquet);
        Point3D pointBasGauche = new Point3D(paquet.obtenirCoinInferieurGaucheX(),
                                             paquet.obtenirCoinInferieurGaucheY(),
                                             paquet.obtenirPositionZ());
        Point3D pointHautDroit = new Point3D(paquet.obtenirCoinInferieurDroitX(),
                                             paquet.obtenirCoinInferieurDroitY(),
                                             hauteurPaquet);
        Point3D pointBasDroit = new Point3D(paquet.obtenirCoinInferieurDroitX(),
                                            paquet.obtenirCoinInferieurDroitY(),
                                            paquet.obtenirPositionZ());
        
        this.ecrireTriangle(pointBasGauche, pointBasDroit, pointHautGauche);
        this.ecrireTriangle(pointHautDroit, pointHautGauche, pointBasDroit);
    }
    
    private void ecrireFaceBas(Paquet paquet) throws IOException {
        Point3D pointHautGauche = new Point3D(paquet.obtenirCoinSuperieurGaucheX(),
                                              paquet.obtenirCoinSuperieurGaucheY(),
                                              paquet.obtenirPositionZ());
        Point3D pointBasGauche = new Point3D(paquet.obtenirCoinInferieurGaucheX(),
                                             paquet.obtenirCoinInferieurGaucheY(),
                                             paquet.obtenirPositionZ());
        Point3D pointHautDroit = new Point3D(paquet.obtenirCoinSuperieurDroitX(),
                                             paquet.obtenirCoinSuperieurDroitY(),
                                             paquet.obtenirPositionZ());
        Point3D pointBasDroit = new Point3D(paquet.obtenirCoinInferieurDroitX(),
                                            paquet.obtenirCoinInferieurDroitY(),
                                            paquet.obtenirPositionZ());
        
        this.ecrireTriangle(pointBasGauche, pointBasDroit, pointHautGauche);
        this.ecrireTriangle(pointHautDroit, pointHautGauche, pointBasDroit);
    }
    
    private void ecrireFaceDroite(Paquet paquet, double hauteurPaquet) throws IOException {
        Point3D pointHautGauche = new Point3D(paquet.obtenirCoinInferieurDroitX(),
                                              paquet.obtenirCoinInferieurDroitY(),
                                              hauteurPaquet);
        Point3D pointBasGauche = new Point3D(paquet.obtenirCoinInferieurDroitX(),
                                             paquet.obtenirCoinInferieurDroitY(),
                                             paquet.obtenirPositionZ());
        Point3D pointHautDroit = new Point3D(paquet.obtenirCoinSuperieurDroitX(),
                                             paquet.obtenirCoinSuperieurDroitY(),
                                             hauteurPaquet);
        Point3D pointBasDroit = new Point3D(paquet.obtenirCoinSuperieurDroitX(),
                                            paquet.obtenirCoinSuperieurDroitY(),
                                            paquet.obtenirPositionZ());
        
        this.ecrireTriangle(pointBasGauche, pointBasDroit, pointHautGauche);
        this.ecrireTriangle(pointHautDroit, pointHautGauche, pointBasDroit);
    }
    
    private void ecrireTriangle(Point3D point1, Point3D point2, Point3D point3) throws IOException {
        Point3D normaleTriangle = this.obtenirNormaleTriangle(point1, point2, point3);
        
        this.constructeurFichier.write(ENTETE_TRIANGLE + " " + normaleTriangle.obtenirX() + " " + normaleTriangle.obtenirY() + " " + normaleTriangle.obtenirZ());
        this.constructeurFichier.write(DEBUT_BOUCLE);
        this.constructeurFichier.write(VERTEX + " " + this.formaterDecimal(point1.obtenirX()) + " " + this.formaterDecimal(point1.obtenirY()) + " " + this.formaterDecimal(point1.obtenirZ()));
        this.constructeurFichier.write(VERTEX + " " + this.formaterDecimal(point2.obtenirX()) + " " + this.formaterDecimal(point2.obtenirY()) + " " + this.formaterDecimal(point2.obtenirZ()));
        this.constructeurFichier.write(VERTEX + " " + this.formaterDecimal(point3.obtenirX()) + " " + this.formaterDecimal(point3.obtenirY()) + " " + this.formaterDecimal(point3.obtenirZ()));
        this.constructeurFichier.write(FIN_BOUCLE);
        this.constructeurFichier.write(FIN_TRIANGLE);
    }
       
    private String formaterDecimal(double nombre) {
        return String.format("%.6f", nombre);
    }
    
    private Point3D obtenirNormaleTriangle(Point3D point1, Point3D point2, Point3D point3) {
        Point3D normaleTriangle = new Point3D();
        Point3D pointU = new Point3D();
        Point3D pointV = new Point3D();
        
        pointU.fixerX(point2.obtenirX() - point1.obtenirX());
        pointU.fixerY(point2.obtenirY() - point1.obtenirY());
        pointU.fixerZ(point2.obtenirZ() - point1.obtenirZ());
        
        pointV.fixerX(point3.obtenirX() - point1.obtenirX());
        pointV.fixerY(point3.obtenirY() - point1.obtenirY());
        pointV.fixerZ(point3.obtenirZ() - point1.obtenirZ());
        
        normaleTriangle.fixerX(pointU.obtenirY()*pointV.obtenirZ() - pointU.obtenirZ()*pointV.obtenirY());
        normaleTriangle.fixerY(pointU.obtenirZ()*pointV.obtenirX() - pointU.obtenirX()*pointV.obtenirZ());
        normaleTriangle.fixerZ(pointU.obtenirX()*pointV.obtenirY() - pointU.obtenirY()*pointV.obtenirX());
        
        return normaleTriangle;
    }
    
    private void ecrireFinFichier() throws IOException {
        this.constructeurFichier.write(FIN_FICHIER);
    }
    
    private void fixerConstructeurFichier(BufferedWriter constructeurFichier) {
        this.constructeurFichier = constructeurFichier;
    }
}
