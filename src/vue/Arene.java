package vue;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

import controleur.Controle;
import controleur.Global;
import modele.JeuServeur;
import modele.Objet;
import outils.son.Son;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Arene extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtSaisie;
	private JPanel jpnMur;
	private JPanel jpnJeu;
	private JTextArea txtChat;
	private Controle controle;
	private boolean client;
	private Son[] lesSon = new Son[Global.SON.length];
	public JPanel getJpnMur() {
		return this.jpnMur;
	}
	public void setJpnMur(JPanel panelRecu) {
	    this.jpnMur.add(panelRecu);
	    this.jpnMur.repaint();
	}
	public JPanel getJpnJeu() {
		return this.jpnJeu;
	}
	public void setJpnJeu(JPanel panelRecu) {
	    this.jpnJeu.removeAll();
		this.jpnJeu.add(panelRecu);
	    this.jpnJeu.repaint();
	    contentPane.requestFocus();
	}
	public String getTxtChat() {
		return txtChat.getText();
	}
	public void setTxtChat(String txtChat) {
	    this.txtChat.setText(txtChat);
	    this.txtChat.setCaretPosition(this.txtChat.getDocument().getLength());
	}

	private void contentPane_KeyPressed(KeyEvent e) {
		int touche = -1;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_SPACE:
				touche = e.getKeyCode();
		    break;
		default: touche = -1;
		}
		if (touche != -1) {
			controle.evenementArene(touche);
		}
	}
	
	public void jouSon(Integer numSon) {
		lesSon[numSon].play();
	}
	/**
	 * Create the frame.
	 */
	public Arene(Controle controle, String info) {
		this.controle = controle;
		setTitle("Arena");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setPreferredSize(new Dimension(800, 600 +25 + 140));
		this.pack();
		this.client = Global.CLIENT.equals(info);
		
		contentPane = new JPanel();		
		if(client) {
			contentPane.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					contentPane_KeyPressed(e);
				}
			});
		}
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		client = info == Global.CLIENT;
		/*
		 * Affichage des murs
		 */
		jpnJeu = new JPanel();
		jpnJeu.setBounds(0,0,Global.LARGEURARENE, Global.HAUTEURARENE);
		jpnJeu.setOpaque(false);
		jpnJeu.setLayout(null);
		contentPane.add(jpnJeu);		
		
		if (client) {
			for (int k=0 ; k<Global.SON.length ; k++) {
				lesSon[k] = new Son(getClass().getClassLoader().getResource(Global.SON[k])) ;
			}
		}
		/*
		 * Affichage des murs
		 */
		jpnMur = new JPanel();
		jpnMur.setBounds(0,0,Global.LARGEURARENE, Global.HAUTEURARENE);
		jpnMur.setOpaque(false);
		jpnMur.setLayout(null);
		contentPane.add(jpnMur);
		
		JLabel lblFond = new JLabel("");
		lblFond.setBounds(0, 0, 800, 600);
		contentPane.add(lblFond);
		String chemin = Global.FONDARENE;
		URL resource = getClass().getClassLoader().getResource(chemin);
		lblFond.setIcon(new ImageIcon(resource));
		
		if(client) {
			txtSaisie = new JTextField();
			txtSaisie.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					txtSaisie_keyPressed(e);
					contentPane.requestFocus();
				}
			});
			txtSaisie.setBounds(0, 600, 800, 25);
			contentPane.add(txtSaisie);
			txtSaisie.setColumns(10);
		}

		
		JScrollPane jspChat = new JScrollPane();
		jspChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jspChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jspChat.setBounds(0, 625, 800, 140);
		contentPane.add(jspChat);
		
		txtChat = new JTextArea();
		txtChat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(client) {
					contentPane_KeyPressed(e);
				}
			}
		});
		txtChat.setEditable(false);
		jspChat.setViewportView(txtChat);
	}
	
	public void ajoutMur(Object mur) {
		JLabel lblMur = (JLabel) mur;
	    jpnMur.add(lblMur);
	    jpnMur.repaint();
	}
	
	public void ajoutJLabelJeu(JLabel jLabel) {
	    jpnJeu.add(jLabel);
	    jpnJeu.repaint();
	}
	
	public void ajoutTChat(String message) {
		setTxtChat(getTxtChat()+message+Global.RETOURALALIGNE);
		this.txtChat.setCaretPosition(this.txtChat.getDocument().getLength());
	}
	
	public void txtSaisie_keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER && txtSaisie.getText() != "") {
			this.controle.evenementArene(txtSaisie.getText());
			txtSaisie.setText("");
		}
	}
}

