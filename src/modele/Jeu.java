package modele;

import controleur.Controle;
import outils.connexion.*;

/**
 * Informations et méthodes communes aux jeux client et serveur
 *
 */
public abstract class Jeu {

	public Jeu(Controle controle) {
        this.controle = controle;
    }
	/**
	 * Réception d'une connexion (pour communiquer avec un ordinateur distant)
	 */
	public abstract void connexion(Connection connection) ;
	
	/**
	 * Réception d'une information provenant de l'ordinateur distant
	 */
	public abstract void reception(Connection connection, Object objet) ;
	
	/**
	 * Déconnexion de l'ordinateur distant
	 */
	public abstract void deconnexion(Connection connection) ;
	
	/**
	 * Envoi d'une information vers un ordinateur distant
	 */
	public void envoi(Connection connection, Object objet) {
		controle.envoi(connection, objet);
	}
	
	protected Controle controle;
	
}
