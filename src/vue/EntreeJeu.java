package vue;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import controleur.Controle;

public class EntreeJeu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtlp;
	private Controle controle;
	/**
	 * Create the frame.
	 */
	public EntreeJeu(Controle controle) {
		this.controle = controle;
		setTitle("UrbanMarginal");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 302, 159);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Start a server :");
		lblNewLabel.setBounds(10, 10, 134, 12);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Connect an existing server :");
		lblNewLabel_1.setBounds(10, 32, 134, 12);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("IP server :");
		lblNewLabel_2.setBounds(10, 58, 57, 12);
		contentPane.add(lblNewLabel_2);
			
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controle.evenementEntreeJeu("serveur");
			}
		});
		btnStart.setBounds(194, 6, 84, 20);
		contentPane.add(btnStart);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controle.evenementEntreeJeu(getTxtlp().getText());
			}
		});
		btnConnect.setBounds(194, 54, 84, 20);
		contentPane.add(btnConnect);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(194, 92, 84, 20);
		contentPane.add(btnExit);
		
		setTxtlp(new JTextField());
		getTxtlp().setText("127.0.0.1");
		getTxtlp().setBounds(63, 54, 121, 18);
		contentPane.add(getTxtlp());
		getTxtlp().setColumns(10);

	}
	public JTextField getTxtlp() {
		return txtlp;
	}
	public void setTxtlp(JTextField txtlp) {
		this.txtlp = txtlp;
	}
}
