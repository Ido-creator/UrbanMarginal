package modele;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controleur.Global;
/**
 * Gestion des murs
 *
 */
public class Mur extends Objet {

	/**
	 * Constructeur
	 */
	public Mur() {
		posX = (int) Math.round(Math.random() * (Global.LARGEURARENE - Global.LARGEURMUR));
		posY = (int) Math.round(Math.random() * (Global.HAUTEURARENE - Global.HAUTEURMUR));
		jLabel = new JLabel("");
		jLabel.setBounds(posX, posY, Global.LARGEURMUR, Global.HAUTEURMUR);
		String chemin = Global.CHEMINMUR;
		URL resource = getClass().getClassLoader().getResource(chemin);
		jLabel.setIcon(new ImageIcon(resource));
	}

}
