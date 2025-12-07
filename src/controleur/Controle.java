package controleur;
import vue.Arene;
import vue.EntreeJeu;
import vue.ChoixJoueur;
import outils.connexion.*;
import modele.Jeu;
import modele.JeuServeur;
import modele.JeuClient;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controleur.Global;

public class Controle implements AsyncResponse{
	
	private EntreeJeu frmEntreeJeu;
	private Arene frmArene;
	private ChoixJoueur frmChoixjoueur;
	private ServeurSocket serveur;
	private ClientSocket client;
	private Jeu leJeu;
	
	/**
	 * Constructeur
	 */
	private Controle() {
		this.frmEntreeJeu = new EntreeJeu(this);
		this.frmEntreeJeu.setVisible(true);
	}
	
	/**
	 * Méthode d'entrée dans l'application
	 * @param args non utilisé
	 */
	public static void main(String[] args) {
		new Controle();
		}
	/**
	 * 
	 */
	public void evenementEntreeJeu(String info) {
		if (Global.SERVEUR.equals(info)) {
			this.serveur = new ServeurSocket(this, Global.PORT);
			this.leJeu = new JeuServeur(this);
			this.frmArene = new Arene(this, Global.SERVEUR);
			JeuServeur jeuServeur = (JeuServeur) leJeu;
			jeuServeur.constructionMurs();
			this.frmArene.setVisible(true);
			this.frmEntreeJeu.dispose();
		}	
		else {
			this.client = new ClientSocket (this, info, Global.PORT);		
			}
	}
	
	public void evenementChoixJoueur(String pseudo, int numPerso) {
		this.frmArene.setVisible(true);
		this.frmChoixjoueur.dispose();
		JeuClient jeuClient = (JeuClient) leJeu;
		String info = Global.PSEUDO+ Global.SEPARATEUR + pseudo + Global.SEPARATEUR + numPerso;
		jeuClient.envoi(info);
	}

	public void evenementJeuServeur(String ordre, Object info) {
		switch(ordre) {
			case Global.ORDREAJOUTMUR:
				frmArene.ajoutMur(info);
				break;
			case Global.AJOUTPANELMURS:
				leJeu.envoi((Connection) info, frmArene.getJpnMur());
				break;
			case Global.AJOUTLABELJEU:
				frmArene.ajoutJLabelJeu((JLabel) info);
				break;
			case Global.MODIFPANELJEU:
				leJeu.envoi((Connection) info, frmArene.getJpnJeu());
				break;
			case Global.AJOUTCHAT:
				frmArene.ajoutTChat((String) info);
				((JeuServeur)this.leJeu).envoi(this.frmArene.getTxtChat());
				break;
		}
	}
	
	public void evenementJeuClient(String ordre, Object info) {
		switch(ordre) {
			case Global.AJOUTPANELMURS:
				frmArene.setJpnMur((JPanel)info);
				break;
			case Global.MODIFPANELJEU:
				frmArene.setJpnJeu((JPanel)info);
				break;
			case Global.MODIFTCHAT:
				frmArene.setTxtChat((String)info);
				break;
			case Global.JOUSON:
				frmArene.jouSon((Integer)info);
				break;
		}
	}
	
	public void evenementArene(Object message) {
		if (message instanceof String) {
			((JeuClient)this.leJeu).envoi(Global.MESSAGECHAT+Global.SEPARATEUR+ message);
		}
		if (message instanceof Integer) {
			((JeuClient)this.leJeu).envoi(Global.ACTION+Global.SEPARATEUR+ message);
		}

	}
	@Override
	public void reception(Connection connection, String ordre, Object info) {
		// TODO Auto-generated method stub
		switch(ordre) {
		case "connexion":
			if (!(this.leJeu instanceof JeuServeur)) {
				this.frmArene = new Arene(this, Global.CLIENT);
				this.frmArene.setVisible(false);
				this.frmChoixjoueur = new ChoixJoueur(this);
				this.frmChoixjoueur.setVisible(true);
				this.frmEntreeJeu.dispose();
				this.leJeu = new JeuClient(this);
				leJeu.connexion(connection);
			}
			else {
				leJeu.connexion(connection);
			}
			break;
		case "reception":
			leJeu.reception(connection, info);
			break;
		case "deconnexion":
			leJeu.deconnexion(connection);
			break;
		default:	
		}
	}
	
	public void envoi(Connection connection, Object objet) {
		connection.envoi(objet);
	}
}
