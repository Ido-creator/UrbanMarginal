package modele;

import javax.swing.JPanel;

import controleur.Controle;
import controleur.Global;
import outils.connexion.Connection;

/**
 * Gestion du jeu côté client
 *
 */
public class JeuClient extends Jeu {
	
	private boolean mursOK = false;
	private Connection connectionServeur;
	/**
	 * Controleur
	 */
	public JeuClient(Controle controle) {
		super(controle);
	}
	
	@Override
	public void connexion(Connection connection) {
		connectionServeur = connection;
	}

	@Override
	public void reception(Connection connection, Object info) {
		if (info instanceof JPanel && !mursOK) {
			this.controle.evenementJeuClient(Global.AJOUTPANELMURS, info);
			mursOK = true;
		}
		else {
			if (info instanceof String) {
				this.controle.evenementJeuClient(Global.MODIFTCHAT, info);
			}
			else {
				if (info instanceof Integer) {
					controle.evenementJeuClient(Global.JOUSON, info);
				}
				else
				{
					this.controle.evenementJeuClient(Global.MODIFPANELJEU, info);
				}
			}	
		}
	}
	
	@Override
	public void deconnexion(Connection connection) {
		System.exit(0);
	}

	/**
	 * Envoi d'une information vers le serveur
	 * fais appel une fois à l'envoi dans la classe Jeu
	 */
	public void envoi(String info) {
		envoi(connectionServeur, info);
	}

}
