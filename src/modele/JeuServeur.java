package modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JLabel;

import controleur.Controle;
import controleur.Global;
import outils.connexion.Connection;

/**
 * Gestion du jeu côté serveur
 *
 */
public class JeuServeur extends Jeu {
	
	
	/**
	 * Collection de murs
	 */
	private ArrayList<Mur> lesMurs = new ArrayList<Mur>() ;
	/**
	 * Collection de joueurs
	 */
	private Hashtable<Connection, Joueur> LesJoueurs = new Hashtable<Connection, Joueur>();
	public Collection<Joueur> getLesJoueurs() {
		return LesJoueurs.values();
	}
	
	/**
	 * Constructeur
	 */
	public JeuServeur(Controle controle) {
		super(controle);
	}
	
	@Override
	public void connexion(Connection connection) {
		Joueur joueur = new Joueur(this);
		LesJoueurs.put(connection, joueur );
	}

	@Override
	public void reception(Connection connection, Object objet) {
		String[] message = ((String)objet).split(Global.SEPARATEUR);
		switch(message[0]) {
		case Global.PSEUDO :
			this.controle.evenementJeuServeur(Global.AJOUTPANELMURS,connection);
			String pseudo = message[1];
			Integer numPerso = Integer.parseInt(message[2]);
			this.LesJoueurs.get(connection).initPerso(pseudo, numPerso, this.LesJoueurs.values(),this.lesMurs);
			String messageArrive = Global.ETOILES+pseudo+Global.MESSAGECONNNECTION+Global.ETOILES;
            this.controle.evenementJeuServeur(Global.AJOUTCHAT, messageArrive);
			break;
		case Global.MESSAGECHAT:
            String phrase = message[1];
            Joueur joueur = this.LesJoueurs.get(connection);
            if (joueur != null) {
                String ligneChat = joueur.getPseudo() + Global.LIENCHAT + phrase;
                this.controle.evenementJeuServeur(Global.AJOUTCHAT, ligneChat);
            }
			break;
		case Global.ACTION:
			Integer action = Integer.parseInt(message[1]);
			this.LesJoueurs.get(connection).action(action, this.LesJoueurs.values(), this.lesMurs);
			break;
		}
		
	}
	
	@Override
	public void deconnexion(Connection connection) {
		Joueur joueur = LesJoueurs.get(connection);
		joueur.departJoueur();
		LesJoueurs.remove(connection);
	}

	/**
	 * Envoi d'une information vers tous les clients
	 * fais appel plusieurs fois à l'envoi de la classe Jeu
	 */
	public void envoi(Object info) {
		for (Connection connection : this.LesJoueurs.keySet()) {
			this.envoi(connection, info);
		}
	}

	/**
	 * Génération des murs
	 */
	public void constructionMurs() {
		for (int i = 0; i < Global.NNBMURS; i++) {
			lesMurs.add(new Mur());
			this.controle.evenementJeuServeur(Global.ORDREAJOUTMUR, lesMurs.get(lesMurs.size()-1).getJLabel());
		}	
	}
	
	public void ajoutLabelJeuArene(JLabel jLabel) {
		this.controle.evenementJeuServeur(Global.AJOUTLABELJEU, jLabel);
	}
	
	public void envoiJeuATous() {
		for(Connection connection : this.LesJoueurs.keySet()) {
			   this.controle.evenementJeuServeur(Global.MODIFPANELJEU, connection);
			}
	}
}
