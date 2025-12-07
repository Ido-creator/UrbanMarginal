package modele;

import java.net.URL;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controleur.Global;
/**
 * Gestion de la boule
 *
 */
public class Boule extends Objet implements Runnable {

    private Collection<Mur> lesMurs;
    private Joueur attaquant;   
	/**
	 * instance de JeuServeur pour la communication
	 */
	private JeuServeur jeuServeur ;
	
	/**
	 * Constructeur
	 */
	public Boule(JeuServeur jeuServeur) {
	    this.jeuServeur = jeuServeur;
		super.jLabel = new JLabel();
		super.jLabel.setVisible(false);
		super.jLabel.setBounds(0,0,Global.LARGEURBOULE,Global.HAUTEURBOULE);
		URL resource = getClass().getClassLoader().getResource(Global.CHEMINBOULES);
		super.jLabel.setIcon(new ImageIcon(resource));
	}
	
	/**
	 * Tire d'une boule
	 */
	public void tireBoule(Joueur attaquant, Collection<Mur> lesMurs) {
		this.lesMurs = lesMurs;
		this.attaquant = attaquant;
		posY = attaquant.getPosY() + Global.HAUTEURPERSO / 4;
		if (attaquant.getOrientation() == Global.GAUCHE) {
			posX =attaquant.getPosX() - Global.LARGEURBOULE - 1;
		}else {posX = attaquant.getPosX() + Global.LARGEURPERSO + 1;}
		new Thread(this).start();
	}
	private int lePas;
	private Collection<Joueur> lesJoueurs;
	private Joueur victime;
	private void pause(long milli, int nano) {
		try {
			Thread.sleep(milli, nano);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		jeuServeur.envoi(Global.FIGHT);
		attaquant.affiche(Global.MARCHE, Global.POSITIONTIR);
		super.jLabel.setVisible(true);
		Joueur victime = null;
		if (attaquant.getOrientation() == Global.GAUCHE) {
			lePas = -Global.PAS;
		}
		else {lePas = Global.PAS;}
		do {
			posX += lePas;
			super.jLabel.setBounds(posX,posY,Global.LARGEURBOULE,Global.HAUTEURBOULE);
			jeuServeur.envoiJeuATous();
			lesJoueurs = jeuServeur.getLesJoueurs();
			victime = (Joueur)toucheCollectionObjets(lesJoueurs);
			pause(Global.MILLI/4,Global.NANO);
		}while(posX>=0 && posX<=Global.LARGEURARENE && this.toucheCollectionObjets(lesMurs)==null && victime==null);
		if(victime != null && !victime.estMort()) {
			jeuServeur.envoi(Global.HURT);
			victime.perteVie();
			attaquant.gainVie();
			for (int i = 0; i < Global.ANIMATIONTOUCHE; i++) {
				victime.affiche(Global.TOUCHE, i%2+1);
				pause(Global.MILLI,Global.NANO);
				
			}
			if (victime.estMort()) {
				jeuServeur.envoi(Global.DEATH);
				for (int i = 0; i < Global.ANIMATIONMORT; i++) {
					victime.affiche(Global.MORT, i%2+1);
					pause(Global.MILLI,Global.NANO);
				}
			}
			else {
				victime.affiche(Global.MARCHE,victime.getEtape());
			}
			
		}
		super.jLabel.setVisible(false);
		jeuServeur.envoiJeuATous();		
	}
	
}
