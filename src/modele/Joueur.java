package modele;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.net.URL;

import controleur.Global;
/**
 * Gestion des joueurs
 *
 */
public class Joueur extends Objet {
	
	private JLabel message;
	/**
	 * pseudo saisi
	 */
	private String pseudo ;
	/**
	 * n° correspondant au personnage (avatar) pour le fichier correspondant
	 */
	
	public String getPseudo() {
		return pseudo;
	}
	
	private int numPerso ; 
	/**
	 * instance de JeuServeur pour communiquer avec lui
	 */
	private JeuServeur jeuServeur ;
	/**
	 * numéro d'�tape dans l'animation (de la marche, touché ou mort)
	 */
	private int etape ;
	public int getEtape() {
		return etape;
	}
	/**
	 * la boule du joueur
	 */
	private Boule boule ;
	/**
	* vie restante du joueur
	*/
	private int vie ; 
	/**
	* tourné vers la gauche (0) ou vers la droite (1)
	*/
	private int orientation ;
	public int getOrientation() {
		return orientation;
	}
	
	/**
	 * Constructeur
	 */
	public Joueur(JeuServeur jeuServeur) {
		this.jeuServeur = jeuServeur;
		vie = Global.MAXVIE;
		etape = 1;
		orientation = Global.DROITE;
		
	}

	/**
	 * Initialisation d'un joueur (pseudo et numéro, calcul de la 1ère position, affichage, création de la boule)
	 */
	public void initPerso(String pseudo, int numPerso,Collection<Joueur> lesJoueurs,ArrayList<Mur> lesMurs) {
		this.pseudo = pseudo;
		this.numPerso = numPerso;
		System.out.println("joueur "+pseudo+" - num perso "+numPerso+" créé");
		boule = new Boule(jeuServeur);
		jLabel = new JLabel();
		message = new JLabel();
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setFont(new Font("Dialog", Font.PLAIN, 8));
		premierePosition(lesJoueurs, lesMurs);
		jeuServeur.ajoutLabelJeuArene(boule.getJLabel());
		jeuServeur.ajoutLabelJeuArene(jLabel);
		jeuServeur.ajoutLabelJeuArene(message);
		affiche("marche",this.etape);
		
		
		
	}

	/**
	 * Calcul de la première position aléatoire du joueur (sans chevaucher un autre joueur ou un mur)
	 */
	private void premierePosition(Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) {
		jLabel.setBounds(0, 0, Global.LARGEURPERSO, Global.HAUTEURPERSO);
		do {
			   posX = (int) Math.round(Math.random() * (Global.LARGEURARENE - Global.LARGEURPERSO)) ;
			   posY = (int) Math.round(Math.random() * (Global.HAUTEURARENE - Global.HAUTEURPERSO - Global.HAUTEURMESSAGE)) ;
			}while(this.toucheCollectionObjets((Collection)lesJoueurs) != null || this.toucheCollectionObjets((Collection)lesMurs) != null);
	}
	
	/**
	 * Affiche le personnage et son message
	 */
	public void affiche(String etat, int etape) {
		super.jLabel.setBounds(posX, posY, Global.LARGEURPERSO, Global.HAUTEURPERSO);
		String chemin = Global.CHEMINPERSONNAGES + Global.PERSO + this.numPerso + etat + etape + "d" + this.orientation + Global.EXTFICHIERPERSO;
		URL resource = getClass().getClassLoader().getResource(chemin);
		super.jLabel.setIcon(new ImageIcon(resource));
		this.message.setBounds(posX-Global.MARGEMESSAGE, posY+Global.HAUTEURPERSO, Global.LARGEURPERSO+Global.MARGEMESSAGE, Global.HAUTEURMESSAGE);
		this.message.setText(pseudo+" : "+vie);
		jeuServeur.envoiJeuATous();
	}

	/**
	 * Gère une action reçue et qu'il faut afficher (déplacement, tire de boule...)
	 */
	public void action(Integer action, Collection LesJoueurs, ArrayList lesMurs ) {
		if (!this.estMort()) {
			switch (action) {
			case KeyEvent.VK_LEFT:
				orientation = Global.GAUCHE;
				deplace(posX,action, -Global.PAS,Global.LARGEURARENE-Global.LARGEURPERSO,LesJoueurs, lesMurs);
			    break;
			case KeyEvent.VK_RIGHT:
				orientation = Global.DROITE;
				deplace(posX,action,Global.PAS,Global.LARGEURARENE-Global.LARGEURPERSO,LesJoueurs, lesMurs);
			    break;
			case KeyEvent.VK_UP:
				deplace(posY,action,-Global.PAS,Global.HAUTEURARENE-Global.HAUTEURPERSO,LesJoueurs, lesMurs);
			    break;
			case KeyEvent.VK_DOWN:
				deplace(posY,action,Global.PAS,Global.HAUTEURARENE-Global.HAUTEURPERSO,LesJoueurs, lesMurs);
			    break;
			case KeyEvent.VK_SPACE:
				if (!this.boule.getJLabel().isVisible()) {
					this.boule.tireBoule(this, lesMurs);
				}
				break;
	
			}
			affiche(Global.MARCHE,this.etape);
		}
	}

	/**
	 * Gère le déplacement du personnage 
	 * @param position position de départ
	 * @param action gauche, droite, haut ou bas
	 * @param lepas valeur de déplacement (positif ou négatif)
	 * @param max valeur à ne pas dépasser
	 * @param lesJoueurs collection de joueurs pour éviter les collisions
	 * @param lesMurs collection de murs pour éviter les collisions
	 * @return nouvelle position
	 */
	private int deplace(int position, int action, int lepas, int max, Collection<Joueur> lesJoueurs, ArrayList<Mur> lesMurs) { 
		int ancpos = position ;
		position += lepas ;
		position = Math.max(position, 0) ;
		position = Math.min(position,  max) ;
		if (action==KeyEvent.VK_LEFT || action==KeyEvent.VK_RIGHT) {
			posX = position ;
		}else{
			posY = position ;
		}
		// controle s'il y a collision, dans ce cas, le personnage reste sur place
		if (this.toucheCollectionObjets((Collection)lesJoueurs) != null || this.toucheCollectionObjets((Collection)lesMurs) != null) {
			position = ancpos ;
			if (action == KeyEvent.VK_LEFT || action == KeyEvent.VK_RIGHT) {
	            posX = ancpos;
	        } else {
	            posY = ancpos;
	        }
		}
		// passe à l'étape suivante de l'animation de la marche
		etape = (etape % Global.NBETAPESMARCHE) + 1 ;
		return position ;
	}

	/**
	 * Gain de points de vie après avoir touché un joueur
	 */
	public void gainVie() {
		vie += Global.GAIN;
		affiche(Global.MARCHE, getEtape());
	}
	
	/**
	 * Perte de points de vie après avoir été touché 
	 */
	public void perteVie() {
		vie -= Global.PERTE;
		if (vie <0 ) {vie = 0;}
		affiche(Global.MARCHE, getEtape());
	}

	/**
	 * vrai si la vie est à 0
	 * @return true si vie = 0
	 */
	public Boolean estMort() {
		return vie == 0;
	}
	
	/**
	 * Le joueur se déconnecte et disparait
	 */
	public void departJoueur() {
		if (jLabel != null) {
			jLabel.setVisible(false);
			message.setVisible(false);
			boule.jLabel.setVisible(false);
			jeuServeur.envoiJeuATous();
		}
	}
	
}
