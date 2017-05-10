package Interface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Clientinterface extends JFrame {

	private JPanel contentPane;
	protected String nextInstr="";
	protected JLabel lblNextInstruction = new JLabel("Next Instruction");
	static public int client_windowX=280;
	public static int client_windowY=160;


	public Clientinterface(String name) {
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, client_windowX, client_windowY);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnSendInstr = new JButton("Send Next Instruction");
		btnSendInstr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(doButtonAction()==0){
					dispose();
				}
			}
		});
		btnSendInstr.setBounds(43, 59, 178, 42);
		contentPane.add(btnSendInstr);
		lblNextInstruction.setBounds(43, 13, 137, 16);
		contentPane.add(lblNextInstruction);
	}

	public int doButtonAction() {
		//Must be implement at child
		System.out.println("You should implement this Function");
		return 0;
	}
}
