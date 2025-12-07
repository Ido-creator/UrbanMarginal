package vue;

import java.awt.Cursor;
import java.awt.Dimension;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

import controleur.Controle;
import controleur.Global;
import outils.son.Son;

public class ChoixJoueur extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtPseudo;
	private JLabel lblPersonnage;
	private Controle controle;
	private int numPerso = 1;
	private Son suivant;
	private Son precedent;
	private Son go;
	private Son welcome;
	
	/*
	 * Méthode pour afficher les personnages
	 */
	private void affichePerso() {
		String chemin = "personnages/perso"+numPerso+"marche1d1.gif";
		URL resource = getClass().getClassLoader().getResource(chemin);
		lblPersonnage.setIcon(new ImageIcon(resource));
	}
	/*
	 * Méthodes pour gerer l'aparance de la souris en fonction de sa position.
	 */
	private void sourisNormal() {
		contentPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	private void sourisDoigt() {
		contentPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	/**
	 * Create the frame.
	 */
	public ChoixJoueur(Controle controle) {
		this.controle = controle;
		setTitle("Choice\r\n");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setPreferredSize(new Dimension(400, 275));
		this.pack();
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		precedent = new Son(getClass().getClassLoader().getResource(Global.SONPRECEDENT));
		suivant = new Son(getClass().getClassLoader().getResource(Global.SONSUIVANT));
		welcome = new Son(getClass().getClassLoader().getResource(Global.SONWELCOME));
		go = new Son(getClass().getClassLoader().getResource(Global.SONGO));
		
		lblPersonnage = new JLabel("");
		lblPersonnage.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersonnage.setBounds(143, 113, 120, 123);
		contentPane.add(lblPersonnage);
	
		txtPseudo = new JTextField();
		txtPseudo.setText("essai");
		txtPseudo.setBounds(143, 246, 120, 19);
		contentPane.add(txtPseudo);
		txtPseudo.setColumns(10);
		
		JLabel lblPrecedent = new JLabel("");
		lblPrecedent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				numPerso -= 1;
				if (numPerso < 1) {
					numPerso = Global.MAXPERSO;
				}
				affichePerso();
				precedent.play();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormal();
			}
		});
		
		lblPrecedent.setBounds(60, 145, 44, 47);
		contentPane.add(lblPrecedent);
		
		JLabel lblSuivant = new JLabel("");
		lblSuivant.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				numPerso += 1;
				if (numPerso > Global.MAXPERSO) {
					numPerso = 1;
				}
				affichePerso();
				suivant.play();
			}
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormal();
			}
		});
		lblSuivant.setBounds(292, 145, 34, 40);
		contentPane.add(lblSuivant);
		
		JLabel lblGo = new JLabel("");
		lblGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtPseudo.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "La saisie du pseudo est obligatoire");
					txtPseudo.requestFocus();
				}
				else {
					controle.evenementChoixJoueur(txtPseudo.getText(), numPerso);
					go.play();
				}
				
			}
			public void mouseEntered(MouseEvent e) {
				sourisDoigt();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				sourisNormal();
			}
		});
		lblGo.setBounds(313, 202, 66, 63);
		contentPane.add(lblGo);
		
		JLabel lblFond = new JLabel("");
		lblFond.setBounds(0, 0, 400, 275);
		contentPane.add(lblFond);
		String chemin = Global.FONDCHOIX;
		URL resource = getClass().getClassLoader().getResource(chemin);
		lblFond.setIcon(new ImageIcon(resource));
		
		affichePerso();
		welcome.play();
	}
}
