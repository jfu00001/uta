import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSlider;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu {

	protected JFrame frame;
	protected JComboBox option_update;
	protected JComboBox option_type;
	protected JSlider num_of_clients;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblNewLabel = new JLabel("Update Type");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setBounds(144, 0, 136, 46);

		option_update = new JComboBox(new String[] { "Immediate", "Deferred" });
		option_update.setFont(new Font("Tahoma", Font.PLAIN, 20));
		option_update.setBounds(144, 57, 136, 26);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(option_update);

		JLabel label = new JLabel("Update Type");
		label.setFont(new Font("Tahoma", Font.PLAIN, 24));
		label.setBounds(144, 94, 136, 46);
		frame.getContentPane().add(label);

		option_type = new JComboBox(new Object[] {});
		option_type.setModel(
				new DefaultComboBoxModel(new String[] { "Wound and Wait", "Wait and Die", "Cautious Waiting" }));
		option_type.setFont(new Font("Tahoma", Font.PLAIN, 20));
		option_type.setBounds(121, 151, 187, 26);
		frame.getContentPane().add(option_type);

		JLabel lblNumberOfClients = new JLabel("Number of Clients");
		lblNumberOfClients.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNumberOfClients.setBounds(120, 188, 224, 46);
		frame.getContentPane().add(lblNumberOfClients);

		num_of_clients = new JSlider(JSlider.HORIZONTAL, 1, 6, 3);
		num_of_clients.setMajorTickSpacing(1);
		num_of_clients.setMinorTickSpacing(1);
		num_of_clients.setPaintTicks(true);
		num_of_clients.setPaintLabels(true);
		num_of_clients.setFont(new Font("Tahoma", Font.PLAIN, 12));
		num_of_clients.setBounds(120, 229, 200, 63);
		frame.getContentPane().add(num_of_clients);

		JButton btnNewButton = new JButton("START");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doButtonAction();
			}
		});
		btnNewButton.setBounds(130, 303, 178, 67);
		frame.getContentPane().add(btnNewButton);
	}

	public int doButtonAction() {
		// to be used from child;
		return 0;
	}

}
