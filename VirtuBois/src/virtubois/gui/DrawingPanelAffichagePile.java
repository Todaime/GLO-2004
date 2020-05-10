/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;
import virtubois.domain.Dessin.DessinateurEmpilement;
import virtubois.domain.Dessin.PileDessin;
import virtubois.domain.Paquet;


public class DrawingPanelAffichagePile extends javax.swing.JPanel
{
    private MainWindow mainWindow;
    private PileDessin pileDessin;

    public DrawingPanelAffichagePile() {
    }

    public DrawingPanelAffichagePile(MainWindow mainWindow) {
        super(new BorderLayout());
        this.mainWindow = mainWindow;
    }

    public void init() {
        initialiserSouris();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.mainWindow != null) {
            super.paintComponent(g);
            DessinateurEmpilement empilementDessinateur = new DessinateurEmpilement(this.mainWindow.controleur,
                                                                                    this.pileDessin);
            empilementDessinateur.draw(g, getWidth(), getHeight());
        }
    }

    private void initialiserSouris() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int nbPaquet = mainWindow.controleur.obtenirNombrePaquetsPiles();

                if (nbPaquet != VALEUR_ZERO) {
                    List<Paquet> paquets = mainWindow.controleur.obtenirPaquets();
                    List<Integer> listeIndicesTrieParHauteur = mainWindow.controleur.obtenirListTrieParHauteur(mainWindow.controleur.obtenirIndicesPaquetsSelectionnes());
                    double valeurScaling = obtenirValeurScalingVueEmpilement(paquets, listeIndicesTrieParHauteur);
                    
                    for (int i = 0 ; i < listeIndicesTrieParHauteur.size(); i++) {
                        int indice = listeIndicesTrieParHauteur.get(i);
                        Paquet paquet = paquets.get(indice);
                        
                        if (me.getY() <= obtenirBorneSuperieurPaquet(paquet, valeurScaling) && me.getY() >= obtenirBorneInferieurPaquet(paquet, valeurScaling)) {
                            mainWindow.controleur.fixerIndicePaquetSelectionne(listeIndicesTrieParHauteur.get(i));
                            mainWindow.afficherInformationPaquetSelectionne();
                            break;
                        }
                    }
                }
                repaint();
                mainWindow.repeindreCourABois();
            }
        });
    }
    
    private double obtenirValeurScalingVueEmpilement(List<Paquet> paquets, List<Integer> listeIndicesTrieParHauteur) {
        int indicePaquetAuDessus = listeIndicesTrieParHauteur.get(listeIndicesTrieParHauteur.size() - 1);
        double coordZPile = paquets.get(indicePaquetAuDessus).obtenirHauteur() + paquets.get(indicePaquetAuDessus).obtenirPositionZ();
        double xMin = Double.MAX_VALUE;
        double xMax = -Double.MAX_VALUE;
        List<Double> xMinPaquets = new ArrayList<>();
        List<Double> xMaxPaquets = new ArrayList<>();

        // On obtient le x Min et Max global + xMin et Max de tous les paquets de la pile
        for (int i = 0; i < listeIndicesTrieParHauteur.size(); i++) {
            Paquet paquet = paquets.get(listeIndicesTrieParHauteur.get(i));
            xMinPaquets.add(Double.MAX_VALUE);
            xMaxPaquets.add(-Double.MAX_VALUE);

            double x = paquet.obtenirCoinSuperieurGaucheX();
            if (x > xMaxPaquets.get(i)){
                xMaxPaquets.set(i, x);
            }
            if (x < xMinPaquets.get(i)){
                xMinPaquets.set(i, x);
            }

            x = paquet.obtenirCoinInferieurGaucheX();
            if (x > xMaxPaquets.get(i)){
                xMaxPaquets.set(i, x);
            }
            if (x < xMinPaquets.get(i)){
                xMinPaquets.set(i, x);
            }

            x = paquet.obtenirCoinInferieurDroitX();
            if (x > xMaxPaquets.get(i)){
                xMaxPaquets.set(i, x);
            }
            if (x < xMinPaquets.get(i)){
                xMinPaquets.set(i, x);
            }

            x = paquet.obtenirCoinSuperieurDroitX();
            if (x > xMaxPaquets.get(i)){
                xMaxPaquets.set(i, x);
            }
            if (x < xMinPaquets.get(i)){
                xMinPaquets.set(i, x);
            }

            if (xMinPaquets.get(i) < xMin) {
                xMin = xMinPaquets.get(i);
            }

            if (xMaxPaquets.get(i) > xMax) {
                xMax = xMaxPaquets.get(i);
            }
        }

        double valeurScaling;

        if (coordZPile > xMax-xMin){
            valeurScaling = coordZPile;
        } else {
            valeurScaling = xMax - xMin;
        }
        
        return valeurScaling;
    }
    
    private int obtenirBorneInferieurPaquet(Paquet paquet, double valeurScaling) {
        return (int)(getHeight() - this.obtenirCoordonneeZPaquet(paquet, valeurScaling) - this.obtenirHauteurPaquet(paquet, valeurScaling));
    }
    
    private int obtenirBorneSuperieurPaquet(Paquet paquet, double valeurScaling) {
        return (int)(getHeight() - this.obtenirCoordonneeZPaquet(paquet, valeurScaling));
    }
    
    private int obtenirHauteurPaquet(Paquet paquet, double valeurScaling) {
        return (int)(getHeight()*paquet.obtenirHauteur()/valeurScaling);
    }
    
    private int obtenirCoordonneeZPaquet(Paquet paquet, double valeurScaling) {
        return (int)(getHeight()*paquet.obtenirPositionZ()/valeurScaling);
    }
}