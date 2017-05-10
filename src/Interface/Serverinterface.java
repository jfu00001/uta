package Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Serverinterface extends JFrame{

	private JPanel contentPane;
	boolean TwoPhaseLocking = false;
	boolean deffered_writing = true;
	protected static int  server_windowX=328;
	protected static int  server_windowY=122;

	public Serverinterface(String name){
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(100, 100, 100, 100));
		setBounds(100, 100, server_windowX, server_windowY);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnCrash = new JButton("Crash");
		btnCrash.setBounds(12, 30, 141, 37);
		btnCrash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doButtonActionCrash();
			}
		});
		contentPane.add(btnCrash);

		JButton btnCheckpoint = new JButton("CheckPoint");
		btnCheckpoint.setBounds(175, 30, 127, 37);
		btnCheckpoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doButtonActionCheckpoint();
			}
		});
		contentPane.add(btnCheckpoint);



	}

	public int doButtonActionCrash() {
		return 0;
	}

	public int doButtonActionCheckpoint() {
		return 0;
	}


}
