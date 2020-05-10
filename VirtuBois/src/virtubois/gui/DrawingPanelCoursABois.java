/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtubois.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import virtubois.domain.Dessin.ChargeuseDessin;
import virtubois.domain.Paquet;
import virtubois.domain.Dessin.DessinateurVirtuBois;
import virtubois.domain.Dessin.PaquetDessin;
import javax.swing.JPanel;
import virtubois.domain.Dessin.Basique.BasiqueDessinPaquet;
import virtubois.domain.Dessin.Basique.BasiqueChargeuseDessin;
import virtubois.domain.Dessin.Basique.BasiqueGrilleDessin;
import virtubois.domain.Dessin.GrilleDessin;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import virtubois.domain.Chargeuse;
import virtubois.domain.Chargeuse.ModeChargeuse;
import virtubois.gui.MainWindow.ModeApplication;
import virtubois.domain.Dessin.IMultiplicateur;
import virtubois.domain.Dessin.Multiplicateur;

import static virtubois.domain.Commun.Constantes.MULTIPLICATEUR_PAR_DEFAULT;
import static virtubois.domain.Commun.Constantes.NOUVEAU_PAQUET_ANGLE;
import static virtubois.domain.Commun.Constantes.NOUVEAU_PAQUET_HAUTEUR;
import static virtubois.domain.Commun.Constantes.NOUVEAU_PAQUET_LARGEUR;
import static virtubois.domain.Commun.Constantes.NOUVEAU_PAQUET_LONGUEUR;
import static virtubois.domain.Commun.Constantes.NOUVEAU_PAQUET_PRODUIT;
import static virtubois.domain.Commun.Constantes.POSITION_ORIGINE_X;
import static virtubois.domain.Commun.Constantes.POSITION_ORIGINE_Y;
import static virtubois.domain.Commun.Constantes.VALEUR_UN;
import static virtubois.domain.Commun.Constantes.VALEUR_UN_CENTIEME;
import static virtubois.domain.Commun.Constantes.VALEUR_UN_DIXIEME;
import static virtubois.domain.Commun.Constantes.VALEUR_UN_MILLION;
import static virtubois.domain.Commun.Constantes.VALEUR_ZERO;

public class DrawingPanelCoursABois extends JPanel
        implements MouseWheelListener
{
    final int HAUTEUR_COURS_A_BOIS = this.getHeight(); //TODO - MEDIUM: Est-ce vraiment une bonne idée considérant qu'on peut resize la fenêtre?
    final int LARGEUR_COURS_A_BOIS = this.getWidth(); //TODO - MEDIUM: Est-ce vraiment une bonne idée considérant qu'on peut resize la fenêtre?

    private double XOrigine;
    private double YOrigine;
    private ChargeuseDessin chargeuseDessin;
    private GrilleDessin grilleDessin;
    private MainWindow mainWindow;
    private IMultiplicateur multiplicateur;
    private PaquetDessin paquetDessin;
    double coordXMapSouris;
    double coordYMapSouris;
    private boolean modeGrilleMagnetique;
    private Timer startTimer;
    private final double multiplicateurGrilleMagnetique = 1;
    
    public DrawingPanelCoursABois() {
        //Note: Semble nécessaire pour faire l'affiche graphique.  À voir si l'on doit le conserver ou non.
    }

    public DrawingPanelCoursABois(MainWindow mainWindow) {
        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        this.modeGrilleMagnetique = false;
        this.mainWindow = mainWindow;
        this.chargeuseDessin = new BasiqueChargeuseDessin();
        this.grilleDessin = new BasiqueGrilleDessin();
        this.multiplicateur = new Multiplicateur(MULTIPLICATEUR_PAR_DEFAULT);
        this.paquetDessin = new BasiqueDessinPaquet();
        this.XOrigine = POSITION_ORIGINE_X;
        this.YOrigine = POSITION_ORIGINE_Y;
        this.startTimer = new Timer(10, (ActionEvent e) -> {
            repaint();
        });
        this.startTimer.start();
    }

    public void init() {
        initiateBindingKeyStroke();
        initialiserInterface();
        initialiserZoomMoletteSouris();
    }

    private void fixerCoordonneeXMapSouris(double coordonneeXMapSouris) {
        this.coordXMapSouris = coordonneeXMapSouris;
    }
    
    private void fixerCoordonneeYMapSouris(double coordonneYMapSouris) {
        this.coordYMapSouris = coordonneYMapSouris;
    }
    
    private double obtenirXOrigine() {
        return this.XOrigine;
    }
    
    private void fixerXOrigine(double xOrigine) {
        this.XOrigine = xOrigine;
    }
    
    private double obtenirYOrigine() {
        return this.YOrigine;
    }
    
    private void fixerYOrigine(double yOrigine) {
        this.YOrigine = yOrigine;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.mainWindow != null) {
            super.paintComponent(g);
            DessinateurVirtuBois virtuBoisDessinateur = new DessinateurVirtuBois(this.mainWindow.controleur,
                                                                                 this.paquetDessin,
                                                                                 this.chargeuseDessin,
                                                                                 this.grilleDessin);
            virtuBoisDessinateur.draw(g,
                                      getWidth(),
                                      getHeight(),
                                      this.multiplicateur,
                                      this.XOrigine,
                                      this.YOrigine);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        double multiplicateurInitial = multiplicateur.obtenirMultiplicateur();
        double coordXMap = coordXMapSouris;
        double coordYMap = coordYMapSouris;

        this.changerMultiplicateur(notches);
        this.fixerXOrigine(coordXMap-(coordXMap-XOrigine)*multiplicateurInitial/multiplicateur.obtenirMultiplicateur());
        this.fixerYOrigine(coordYMap-(coordYMap-YOrigine)*multiplicateurInitial/multiplicateur.obtenirMultiplicateur());
        repaint();
    }

    public void changerMultiplicateur(int notches) {
        if (this.multiplicateur.obtenirMultiplicateur() + ((double)(notches)) > VALEUR_UN_DIXIEME) {
            this.multiplicateur.fixerMultiplicateur(this.multiplicateur.obtenirMultiplicateur()+((double)(notches)));
        }
        else {
            
            if (this.multiplicateur.obtenirMultiplicateur() + ((double)(notches))*VALEUR_UN_CENTIEME > VALEUR_ZERO) {
            this.multiplicateur.fixerMultiplicateur(this.multiplicateur.obtenirMultiplicateur()+((double)(notches)*VALEUR_UN_CENTIEME));
            }
        }
        this.mainWindow.controleur.fixerFacteurZoom(this.multiplicateur.obtenirMultiplicateur());
    }

    public void deplacerMapHaut() {
        this.fixerYOrigine(this.obtenirYOrigine() - (getHeight() * VALEUR_UN_DIXIEME / multiplicateur.obtenirMultiplicateur()));
        repaint();
    }

    public void deplacerMapBas() {
        this.fixerYOrigine(this.obtenirYOrigine() + (getHeight() * VALEUR_UN_DIXIEME / multiplicateur.obtenirMultiplicateur()));
        repaint();
    }

    public void deplacerMapDroite() {
        this.fixerXOrigine(this.obtenirXOrigine() + (getWidth() * VALEUR_UN_DIXIEME / multiplicateur.obtenirMultiplicateur()));
        repaint();
    }

    public void deplacerMapGauche() {
        this.fixerXOrigine(this.obtenirXOrigine() - (getWidth() * VALEUR_UN_DIXIEME / multiplicateur.obtenirMultiplicateur()));
        repaint();
    }

    public void changerModeGrilleMagnetique() {
        modeGrilleMagnetique = !modeGrilleMagnetique;
    }

    public Point coordSourisDansLaCour(MouseEvent me) {
        return new Point((int) Math.round(obtenirCoordonneeXDansPlanSelonMultiplicateur(me.getX())),
                         (int) Math.round(obtenirCoordonneeYDansPlanSelonMultiplicateur(me.getY())));
    }

    private void initialiserInterface() {
        this.setSize(LARGEUR_COURS_A_BOIS, HAUTEUR_COURS_A_BOIS);
        setPreferredSize(new Dimension(HAUTEUR_COURS_A_BOIS, LARGEUR_COURS_A_BOIS));
        setBorder(BorderFactory.createEmptyBorder(VALEUR_ZERO, VALEUR_ZERO, VALEUR_ZERO, VALEUR_ZERO));
        setVisible(true);
    }

    private void initialiserZoomMoletteSouris() {
        this.addMouseWheelListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (mainWindow.controleur.obtenirEtatDeplacement()){
                    mainWindow.controleur.fixerEtatDeplacement();
                }
                double xSouris = obtenirCoordonneeDecimalXDansPlanSelonMultiplicateur(me.getX());
                double ySouris = obtenirCoordonneeDecimalYDansPlanSelonMultiplicateur(me.getY());
                boolean paquetTouche = false;
                int etage = VALEUR_ZERO;
                int indicePaquetSousClic = -1; //TODO - LOW: Ce n'est pas considéré comme une bonne pratique de mettre des valeurs à -1
                List<Integer>  indicesPaquetsTouches = new ArrayList<>();
                MainWindow.ModeApplication modeApplication = mainWindow.obtenirModeApplication();

                for (int i = 0; i < mainWindow.controleur.obtenirPaquets().size(); i++) {
                    if (mainWindow.controleur.obtenirPaquets().get(i).verificationPointDansSurface(xSouris, ySouris)) {
                        paquetTouche = true;
                        indicesPaquetsTouches.add(i);

                        if (etage <= mainWindow.controleur.obtenirPaquets().get(i).obtenirEtage()) {
                            etage = mainWindow.controleur.obtenirPaquets().get(i).obtenirEtage();
                            indicePaquetSousClic = i;
                        }
                    }
                }

                if (modeApplication == MainWindow.ModeApplication.AJOUT) {
                    ajouterNouveauPaquet(xSouris, ySouris);
                }
                else if(modeApplication == MainWindow.ModeApplication.SELECTION || modeApplication == MainWindow.ModeApplication.AFFICHAGE_PAQUET
                        && mainWindow.controleur.obtenirChargeuse().obtenirModeChargeuse() != Chargeuse.ModeChargeuse.PAQUET_PORTE) {
                    //TODO - MEDIUM: Cette section comment à être grosse, il faudrait découper en plus petites méthodes
                    mainWindow.controleur.fixerIndicesPaquetsSelectionnes(indicesPaquetsTouches);

                    if (paquetTouche) {
                        mainWindow.fixerModeApplication(MainWindow.ModeApplication.AFFICHAGE_PAQUET);
                        mainWindow.controleur.fixerIndicePaquetSelectionne(indicePaquetSousClic);
                        mainWindow.afficherInformationPaquetSelectionne();
                        mainWindow.obtenirSupprimerBouton().setVisible(true);
                    } else {
                        mainWindow.modeAucunPaquetSelectionne();
                        mainWindow.controleur.fixerIndicePaquetSelectionne(-1);
                    }
                }
                
                repaint();
                mainWindow.repaintDrawingPannelPile();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                startTimer.stop();
                fixerCoordonneeXMapSouris(obtenirCoordonneeDecimalXDansPlanSelonMultiplicateur(me.getX()));
                fixerCoordonneeYMapSouris(obtenirCoordonneeDecimalYDansPlanSelonMultiplicateur(me.getY()));
                mainWindow.actualiserPositionSouris(coordXMapSouris, coordYMapSouris);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                if (mainWindow.controleur.obtenirChargeuse().modeChargeuseActuel != ModeChargeuse.PAQUET_PORTE
                        && SwingUtilities.isRightMouseButton(evt)
                        && mainWindow.obtenirModeApplication() == ModeApplication.AFFICHAGE_PAQUET) {
                    int indice = mainWindow.controleur.obtenirIndicePaquetSelectionne();

                    //TODO - MEDIUM: Mauvaise pratique de faire des validations sur des -1
                    if (indice != -1) {
                        if (mainWindow.controleur.paquetEstAuDessusPile(indice)) {
                            Paquet paquet = mainWindow.controleur.obtenirPaquets().get(indice);

                            if (modeGrilleMagnetique) {
                                deplacementParGrilleMagnetique(evt, paquet);
                            }
                            else {
                                mettreAJourPaquetDeplacement(evt, paquet);
                            }
                        }
                    }
                }
                repaint();
                mainWindow.repaintDrawingPannelPile();
            }
        });
    }

    private void deplacementParGrilleMagnetique(MouseEvent event, Paquet paquet) {
        Paquet paquetDeplacementSouris = paquet;
        
        double anciennePositionX = paquet.obtenirPositionX();
        double anciennePositionY = paquet.obtenirPositionY();
        
        double posX = obtenirCoordonneeDecimalXDansPlanSelonMultiplicateur(event.getX());
        double X_ARRONDI_AU_QUART = Math.round(posX/this.multiplicateurGrilleMagnetique) * this.multiplicateurGrilleMagnetique;
        double posY = obtenirCoordonneeDecimalYDansPlanSelonMultiplicateur(event.getY());
        double Y_ARRONDI_AU_QUART = Math.round(posY/this.multiplicateurGrilleMagnetique) * this.multiplicateurGrilleMagnetique;
        
        paquetDeplacementSouris.fixerPositionX(X_ARRONDI_AU_QUART);
        paquetDeplacementSouris.fixerPositionY(Y_ARRONDI_AU_QUART);
        
        if (mainWindow.controleur.chargeuseTouchePaquet(mainWindow.controleur.obtenirChargeuse())) {
            paquetDeplacementSouris.fixerPositionX(anciennePositionX);
            paquetDeplacementSouris.fixerPositionY(anciennePositionY);
        } else {
            this.mettreAJourPaquetPourDeplacement(paquet);
        }
    }

    private void mettreAJourPaquetDeplacement(MouseEvent event, Paquet paquet) {
        mainWindow.actualiserPositionSouris(obtenirCoordonneeDecimalXDansPlanSelonMultiplicateur(event.getX()),
                                            obtenirCoordonneeDecimalYDansPlanSelonMultiplicateur(event.getY()));
        double anciennePositionX = paquet.obtenirPositionX();
        double anciennePositionY = paquet.obtenirPositionY();
        
        paquet.fixerPositionX(obtenirCoordonneeDecimalXDansPlanSelonMultiplicateur(event.getX()));
        paquet.fixerPositionY(obtenirCoordonneeDecimalYDansPlanSelonMultiplicateur(event.getY()));
        
        if (mainWindow.controleur.chargeuseTouchePaquet(mainWindow.controleur.obtenirChargeuse())) {
            paquet.fixerPositionX(anciennePositionX);
            paquet.fixerPositionY(anciennePositionY);
        }
        else {
            this.mettreAJourPaquetPourDeplacement(paquet);
        }
    }
    
    private void mettreAJourPaquetPourDeplacement(Paquet paquet) {
        List<Paquet> paquets = mainWindow.controleur.obtenirPaquets();
        paquet.fixerEtage(paquet.obtenirEtagePaquet(paquets));
        paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(paquets));

        mettreAJourPaquetDeplacementDansInterface(paquet);
    }

    private void mettreAJourPaquetDeplacementDansInterface(Paquet paquet) {
        mainWindow.obtenirPaquetXTextbox().setText(String.valueOf(paquet.obtenirPositionX()));
        mainWindow.obtenirPaquetYTextbox().setText(String.valueOf(paquet.obtenirPositionY()));
        mainWindow.obtenirPaquetEtageTextbox().setText(String.valueOf(paquet.obtenirEtage()));
        mainWindow.obtenirPaquetZTextbox().setText(String.valueOf(paquet.obtenirPositionZ()));
    }

    private double obtenirCoordonneeDecimalXDansPlanSelonMultiplicateur(double coordonneeX) {
        return (double) Math.round(obtenirCoordonneeXDansPlanSelonMultiplicateur(coordonneeX) * VALEUR_UN_MILLION) / VALEUR_UN_MILLION;
    }

    private double obtenirCoordonneeDecimalYDansPlanSelonMultiplicateur(double coordonneeY) {
        return (double) Math.round(obtenirCoordonneeYDansPlanSelonMultiplicateur(coordonneeY) * VALEUR_UN_MILLION) / VALEUR_UN_MILLION;
    }

    private double obtenirCoordonneeXDansPlanSelonMultiplicateur(double coordonneeX) {
        return coordonneeX/multiplicateur.obtenirMultiplicateur() + XOrigine;
    }

    private double obtenirCoordonneeYDansPlanSelonMultiplicateur(double coordonneeY) {
        return coordonneeY/multiplicateur.obtenirMultiplicateur() + YOrigine;
    }

    public void gestionBoutonZoom(int notches) {
        double multiplicateurInitial = multiplicateur.obtenirMultiplicateur();
        double coordXMap = getWidth()/(2*multiplicateurInitial)+XOrigine;
        double coordYMap = getHeight()/(2*multiplicateurInitial)+YOrigine;

        this.changerMultiplicateur(notches);
        this.fixerXOrigine(coordXMap-(coordXMap-XOrigine)*multiplicateurInitial/multiplicateur.obtenirMultiplicateur());
        this.fixerYOrigine(coordYMap-(coordYMap-YOrigine)*multiplicateurInitial/multiplicateur.obtenirMultiplicateur());
        repaint();
    }
    
    public void initiateBindingKeyStroke() {
        bindKeyStroke(this, JPanel.WHEN_IN_FOCUSED_WINDOW, "move.left", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, VALEUR_ZERO, true), actionTournerAGauche(this));
        bindKeyStroke(this, JPanel.WHEN_IN_FOCUSED_WINDOW, "move.right", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, VALEUR_ZERO, true), actionTournerADroite(this));
        bindKeyStroke(this, JPanel.WHEN_IN_FOCUSED_WINDOW, "move.down", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, VALEUR_ZERO, true), actionReculer(this));
        bindKeyStroke(this, JPanel.WHEN_IN_FOCUSED_WINDOW, "move.up", KeyStroke.getKeyStroke(KeyEvent.VK_UP, VALEUR_ZERO, true), actionAvancer(this));

        bindKeyStroke(this, JPanel.WHEN_IN_FOCUSED_WINDOW, "changerHauteur.haut", KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, VALEUR_ZERO, true), leverBras(this));
        bindKeyStroke(this, JPanel.WHEN_IN_FOCUSED_WINDOW, "changerHauteur.bas", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, VALEUR_ZERO, true), baisserBras(this));
        
        bindKeyStroke(this, JPanel.WHEN_IN_FOCUSED_WINDOW, "marcheAutomatique", KeyStroke.getKeyStroke(KeyEvent.VK_UP, java.awt.event.InputEvent.CTRL_DOWN_MASK, true), marcheAutomatique(this));
    }

    public Action leverBras(JPanel frame) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controleur.gestionLeverBras(VALEUR_UN);
                repaint();
                mainWindow.changerInformationChargeuse();
                mainWindow.afficherInformationPaquetSelectionne();
            }
        };
    }

    public Action baisserBras(JPanel frame) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controleur.gestionBaisserBras(VALEUR_UN);
                repaint();
                mainWindow.changerInformationChargeuse();
                mainWindow.afficherInformationPaquetSelectionne();
            }
        };
    }

    public Action actionTournerAGauche(JPanel frame) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controleur.gestionTournerAGauche();
                repaint();
                mainWindow.changerInformationChargeuse();
                mainWindow.afficherInformationPaquetSelectionne();
            }
        };
    }

    public Action actionTournerADroite(JPanel frame) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controleur.gestionTournerADroite();
               repaint();
               mainWindow.changerInformationChargeuse();
               mainWindow.afficherInformationPaquetSelectionne();
            }
        };
    }

    public Action actionAvancer(JPanel frame) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controleur.gestionAvancer();
                repaint();
                mainWindow.changerInformationChargeuse();
                mainWindow.afficherInformationPaquetSelectionne();
            }
        };
    }

    public Action actionReculer(JPanel frame){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controleur.gestionReculer();
                repaint();
                mainWindow.changerInformationChargeuse();
                mainWindow.afficherInformationPaquetSelectionne();
            }
        };
    }
    
    public Action marcheAutomatique(JPanel frame) {
                return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.ajouterAncienneVersion();
                int timerDelay = 100;
                Timer t = new Timer(timerDelay, new ActionListener(){
                  public void actionPerformed(ActionEvent e) {
                    if (mainWindow.controleur.obtenirEtatDeplacement()) { 
                        mainWindow.changerInformationChargeuse();
                        mainWindow.afficherInformationPaquetSelectionne();
                        repaint();
                        if (mainWindow.controleur.gestionAvancer()) {
                            mainWindow.controleur.fixerEtatDeplacement();
                        }
                    }
                  }
                });
                t.start();
                mainWindow.controleur.fixerEtatDeplacement();
                
            };
        };
    }
            
    protected void bindKeyStroke(JComponent frame, int condition, String name, KeyStroke keyStroke, Action action) {
        InputMap im = frame.getInputMap(condition);
        ActionMap am = frame.getActionMap();

        im.put(keyStroke, name);
        am.put(name, action);
    }
    
    private void ajouterNouveauPaquet(double xSouris, double ySouris) {
        if (mainWindow.validerEmplacementNouveauPaquet(xSouris, ySouris)) {
            List<Paquet> paquets = mainWindow.controleur.obtenirPaquets();
            Paquet paquet;
            
            if (mainWindow.controleur.obtenirIndicePaquetSelectionne() != -1) {
                Paquet paquetSource = mainWindow.controleur.obtenirPaquetSelectionne();
                paquet = new Paquet(xSouris,
                                    ySouris,
                                    Paquet.creerNouveauCodeBarre(paquets),
                                    paquetSource.obtenirProduit(),
                                    paquetSource.obtenirLargeur(),
                                    paquetSource.obtenirLongueur(),
                                    paquetSource.obtenirAngle(),
                                    paquetSource.obtenirHauteur());
            }
            else {
                paquet = new Paquet(xSouris,
                                    ySouris,
                                    Paquet.creerNouveauCodeBarre(paquets),
                                    NOUVEAU_PAQUET_PRODUIT,
                                    NOUVEAU_PAQUET_LARGEUR,
                                    NOUVEAU_PAQUET_LONGUEUR,
                                    NOUVEAU_PAQUET_ANGLE,
                                    NOUVEAU_PAQUET_HAUTEUR);
            }
            
            paquet.fixerEtage(paquet.obtenirEtagePaquet(paquets));
            paquet.fixerPositionZ(paquet.obtenirPositionZPaquet(paquets));
            mainWindow.ajouterAncienneVersion();
            mainWindow.controleur.ajouterPaquet(paquet);
            mainWindow.controleur.fixerIndicePaquetSelectionne(mainWindow.controleur.obtenirPaquets().size() - 1);
            mainWindow.afficherInfosNouveauPaquet(paquet);
            mainWindow.obtenirSupprimerBouton().setVisible(true);
        }
    }

}