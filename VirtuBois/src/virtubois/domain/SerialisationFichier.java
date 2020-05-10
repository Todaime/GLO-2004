/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import virtubois.gui.MainWindow;

/**
 *
 * @author julie
 */
public class SerialisationFichier {
    
    private final MainWindow fenetrePrincipale;
    private final VirtuBoisControleur controleur;
    
    public SerialisationFichier() {
        this.fenetrePrincipale = null;
        this.controleur = null;
    }
    
    public SerialisationFichier(MainWindow fenetrePrincipale, VirtuBoisControleur controleur) {
        this.fenetrePrincipale = fenetrePrincipale;
        this.controleur = controleur;
    }
    
    public void sauvegarderControleur() {
        JFileChooser fenetreSauvegarde = new JFileChooser();
        fenetreSauvegarde.setDialogTitle("Sauvegarder le plan de la cours");
        fenetreSauvegarde.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        fenetreSauvegarde.setSelectedFile(new File("virtubois-equipe4.txt"));
        fenetreSauvegarde.setFileFilter(new FileNameExtensionFilter("Normal text file (*.txt)", "txt"));

        //Remaque: On désactive l'option "Tous les fichiers"
        fenetreSauvegarde.setAcceptAllFileFilterUsed(false);
        
        if (fenetreSauvegarde.showSaveDialog(this.fenetrePrincipale) == JFileChooser.APPROVE_OPTION) {
            this.sauvegarderControleurDansFichier(fenetreSauvegarde.getSelectedFile().getPath());
        } else {
            //TODO - MEDIUM: Afficher un message d'erreur
            System.out.println("No Selection");
        }
    }
    
    public VirtuBoisControleur chargerControleur() {
        JFileChooser fenetreSelectionFichier = new JFileChooser();
        fenetreSelectionFichier.setDialogTitle("Ouvrir un fichier");
        
        if (fenetreSelectionFichier.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fichier = fenetreSelectionFichier.getSelectedFile();
            return this.chargerControleurSelonFichier(fichier.getPath());
        } else {
            //TODO - MEDIUM: Afficher un message d'erreur
            System.out.println("File access cancelled by user.");
        }
        
        return new VirtuBoisControleur();
    }
    
    private void sauvegarderControleurDansFichier(String cheminEnregistrement) {
        try {
            //TODO - HIGH: Utiliser des meilleurs noms de variables;
            FileOutputStream fichier = new FileOutputStream(cheminEnregistrement);
            ObjectOutputStream objetEcriture = new ObjectOutputStream(fichier);
            objetEcriture.writeObject(this.controleur);
            objetEcriture.close();
        } catch (IOException e) {
            //TODO - HIGH: Il faut afficher un message lorsqu'on ne peut pas sauvegarder
            e.printStackTrace();
        }
    }

    private VirtuBoisControleur chargerControleurSelonFichier(String cheminFichier) {
        try {
            FileInputStream fichier = new FileInputStream(cheminFichier);
            ObjectInputStream objetLecture = new ObjectInputStream(fichier);
            VirtuBoisControleur result = (VirtuBoisControleur) objetLecture.readObject();
            objetLecture.close();

            return result;
        } catch (IOException | ClassNotFoundException e) {
            //TODO - HIGH: Il faut afficher un message lorsqu'on ne peut pas ouvrir un fichier
            e.printStackTrace();
        }
        
        return new VirtuBoisControleur();
    }

}
