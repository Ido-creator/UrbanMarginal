package controleur;
import vue.Arene;
import vue.EntreeJeu;
import vue.ChoixJoueur;
public class Controle {

	private EntreeJeu frmEntreeJeu;
	private Arene frmArene;
	private ChoixJoueur frmChoixjoueur;
	/**
	 * Constructeur
	 */
	private Controle() {
		this.frmEntreeJeu = new EntreeJeu(this);
		this.frmEntreeJeu.setVisible(true);
	}
	public void btnStart_clic() {
		this.frmArene = new Arene();
		this.frmArene.setVisible(true);
		this.frmEntreeJeu.dispose();
	}
	public void btnConnect_clic() {
		this.frmChoixjoueur = new ChoixJoueur(this);
		this.frmChoixjoueur.setVisible(true);
		this.frmEntreeJeu.dispose();
	}
	
	/**
	 * Méthode d'entrée dans l'application
	 * @param args non utilisé
	 */
	public static void main(String[] args) {
		new Controle();
		}

}
