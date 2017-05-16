
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.ScrollPane;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ServerInterface {
	private boolean ran=false;
	private int option_update;
	private int option_type;
	private int num_of_clients = 0;
	private JFrame frame;
	private JButton clientActions[] = new JButton[6];
	private ClientTCP[] clients;
	private int initCl = 0;
	private JLabel lblLogLabel = new JLabel("<html></html>", SwingConstants.CENTER);
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 * 
	 * @param num_of_clients
	 * @param option_type
	 * @param option_update
	 */
	public ServerInterface(int option_update, int option_type, int num_of_clients) {
		this.option_update=option_update;
		this.option_type=option_type;
		this.num_of_clients = num_of_clients;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new JFrame();
					frame.setVisible(true);
					initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setBounds(100, 100, 458, 860);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Server Panel
		JLabel lblServer = new JLabel(" SERVER");
		lblServer.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblServer.setBounds(0, 0, 152, 53);
		frame.getContentPane().add(lblServer);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
		panel.setBounds(0, 64, 442, 148);
		frame.getContentPane().add(panel);

		JButton btnCheckpoint = new JButton("CHECKPOINT");
		JButton btnCrash = new JButton("CRASH");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(170)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnCrash, GroupLayout.PREFERRED_SIZE, 108,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnCheckpoint))
								.addContainerGap(171, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING,
								gl_panel.createSequentialGroup().addGap(12)
										.addComponent(btnCheckpoint, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnCrash,
												GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
				.addGap(23)));
		panel.setLayout(gl_panel);

		// Clien Panel
		JLabel lblClient = new JLabel(" CLIENTS");
		lblClient.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblClient.setBounds(0, 223, 152, 53);
		frame.getContentPane().add(lblClient);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
		panel_1.setBounds(0, 287, 442, 292);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		for (int ch = 0; ch < 1; ch++) {
			if (num_of_clients < 1)
				break;
			JPanel panel_2 = new JPanel();
			panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
			panel_2.setBounds(10, 18, 126, 115);
			panel_1.add(panel_2);
			JLabel lblClient_1 = new JLabel("Client 1");

			JButton btnNewButton = new JButton("Next Action");
			btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 8));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// clients[0].doButtonAction();
				}
			});
			clientActions[0] = btnNewButton;
			GroupLayout gl_panel_2 = new GroupLayout(panel_2);
			gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel_2.createSequentialGroup()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel_2.createSequentialGroup().addGap(39).addComponent(lblClient_1))
									.addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(
											btnNewButton, GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE)))
					.addContainerGap()));
			gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel_2.createSequentialGroup().addGap(5).addComponent(lblClient_1).addGap(18)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(22)));
			panel_2.setLayout(gl_panel_2);

			if (num_of_clients < 2)
				break;
			JPanel panel_3 = new JPanel();
			panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
			panel_3.setBounds(157, 18, 126, 115);
			panel_1.add(panel_3);

			JLabel lblClient_2 = new JLabel("Client 2");

			JButton button = new JButton("Next Action");
			button.setFont(new Font("Tahoma", Font.PLAIN, 8));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// button
				}
			});
			clientActions[1] = button;
			GroupLayout gl_panel_3 = new GroupLayout(panel_3);
			gl_panel_3.setHorizontalGroup(
					gl_panel_3.createParallelGroup(Alignment.LEADING).addGap(0, 126, Short.MAX_VALUE)
							.addGroup(gl_panel_3.createSequentialGroup()
									.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_panel_3.createSequentialGroup().addGap(39)
													.addComponent(lblClient_2))
							.addGroup(gl_panel_3.createSequentialGroup().addContainerGap().addComponent(button,
									GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE))).addContainerGap()));
			gl_panel_3
					.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGap(0, 115, Short.MAX_VALUE)
							.addGroup(gl_panel_3.createSequentialGroup().addGap(5).addComponent(lblClient_2).addGap(18)
									.addComponent(button, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
									.addGap(22)));
			panel_3.setLayout(gl_panel_3);

			if (num_of_clients < 3)
				break;
			JPanel panel_4 = new JPanel();
			panel_4.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
			panel_4.setBounds(306, 18, 126, 115);
			panel_1.add(panel_4);

			JLabel lblClient_3 = new JLabel("Client 3");

			JButton button_1 = new JButton("Next Action");
			button_1.setFont(new Font("Tahoma", Font.PLAIN, 8));
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			clientActions[2] = button_1;
			GroupLayout gl_panel_4 = new GroupLayout(panel_4);
			gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
					.addGap(0, 126, Short.MAX_VALUE)
					.addGroup(gl_panel_4.createSequentialGroup()
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel_4.createSequentialGroup().addGap(39).addComponent(lblClient_3))
									.addGroup(gl_panel_4.createSequentialGroup().addContainerGap()
											.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE)))
							.addContainerGap()));
			gl_panel_4
					.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGap(0, 115, Short.MAX_VALUE)
							.addGroup(gl_panel_4.createSequentialGroup().addGap(5).addComponent(lblClient_3).addGap(18)
									.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
									.addGap(22)));
			panel_4.setLayout(gl_panel_4);

			if (num_of_clients < 4)
				break;
			JPanel panel_6 = new JPanel();
			panel_6.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
			panel_6.setBounds(10, 133, 126, 115);
			panel_1.add(panel_6);

			JLabel lblClient_4 = new JLabel("Client 4");

			JButton button_3 = new JButton("Next Action");
			button_3.setFont(new Font("Tahoma", Font.PLAIN, 8));
			button_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			clientActions[3] = button_3;
			GroupLayout gl_panel_6 = new GroupLayout(panel_6);
			gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
					.addGap(0, 126, Short.MAX_VALUE)
					.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel_6.createSequentialGroup().addGap(39).addComponent(lblClient_4))
									.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
											.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE)))
							.addContainerGap()));
			gl_panel_6
					.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGap(0, 115, Short.MAX_VALUE)
							.addGroup(gl_panel_6.createSequentialGroup().addGap(5).addComponent(lblClient_4).addGap(18)
									.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
									.addGap(22)));
			panel_6.setLayout(gl_panel_6);

			if (num_of_clients < 5)
				break;
			JPanel panel_5 = new JPanel();
			panel_5.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
			panel_5.setBounds(157, 133, 126, 115);
			panel_1.add(panel_5);

			JLabel lblClient_5 = new JLabel("Client 5");

			JButton button_2 = new JButton("Next Action");
			button_2.setFont(new Font("Tahoma", Font.PLAIN, 8));
			button_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			clientActions[4] = button_2;
			GroupLayout gl_panel_5 = new GroupLayout(panel_5);
			gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
					.addGap(0, 126, Short.MAX_VALUE).addGap(0, 126, Short.MAX_VALUE)
					.addGroup(gl_panel_5.createSequentialGroup()
							.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel_5.createSequentialGroup().addGap(39).addComponent(lblClient_5))
									.addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
											.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE)))
							.addContainerGap()));
			gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
					.addGap(0, 115, Short.MAX_VALUE).addGap(0, 115, Short.MAX_VALUE)
					.addGroup(gl_panel_5.createSequentialGroup().addGap(5).addComponent(lblClient_5).addGap(18)
							.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(22)));
			panel_5.setLayout(gl_panel_5);

			if (num_of_clients < 6)
				break;
			JPanel panel_7 = new JPanel();
			panel_7.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
			panel_7.setBounds(306, 133, 126, 115);
			panel_1.add(panel_7);

			JLabel lblClient_6 = new JLabel("Client 6");

			JButton button_4 = new JButton("Next Action");
			button_4.setFont(new Font("Tahoma", Font.PLAIN, 8));
			button_4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			clientActions[5] = button_4;
			GroupLayout gl_panel_7 = new GroupLayout(panel_7);
			gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
					.addGap(0, 126, Short.MAX_VALUE).addGap(0, 126, Short.MAX_VALUE)
					.addGroup(gl_panel_7.createSequentialGroup()
							.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel_7.createSequentialGroup().addGap(39).addComponent(lblClient_6))
									.addGroup(gl_panel_7.createSequentialGroup().addContainerGap()
											.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE)))
							.addContainerGap()));
			gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
					.addGap(0, 115, Short.MAX_VALUE).addGap(0, 115, Short.MAX_VALUE)
					.addGroup(gl_panel_7.createSequentialGroup().addGap(5).addComponent(lblClient_6).addGap(18)
							.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(22)));
			panel_7.setLayout(gl_panel_7);

		}
		JButton btnRandom = new JButton("RANDOM");
		btnRandom.setBounds(343, 259, 89, 23);
		panel_1.add(btnRandom);
		JLabel lblLog = new JLabel(" LOG");
		lblLog.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblLog.setBounds(0, 590, 152, 53);
		frame.getContentPane().add(lblLog);

		// LOG PANEL

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, null, null, null));
		panel_8.setBounds(0, 644, 442, 167);
		frame.getContentPane().add(panel_8);
		panel_8.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 442, 167);
		panel_8.add(scrollPane);
		scrollPane.setViewportView(lblLogLabel);
		
		
		
		//server-client
		ServerTCP t1 = new ServerTCP("Server", option_update, option_type, num_of_clients, lblLogLabel);
		t1.setDaemon(true);
		t1.start();
		btnCheckpoint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t1.doButtonActionCheckpoint();
			}
		});
		btnCrash.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t1.doButtonActionCrash();
			}
		});
		

		String inputfile;
		String name;
		clients = new ClientTCP[num_of_clients];
		for (int i = 0; i < num_of_clients; i++) {
			inputfile = "inputs/input" + (i + 1) + ".txt";
			name = "Client" + (i + 1);
			try {
				clients[i] = new ClientTCP(inputfile, name, (i + 1), clientActions[i]);
				int currButton = initCl;
				initCl++;
				clientActions[i].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						clients[currButton].doButtonAction();
					}
				});
				;
				clients[i].start();
			} catch (Exception e) {
				System.out.println("There is no file with name " + inputfile);
			}

		}
		btnRandom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ran=!ran;
			}
		});
		
		new Thread(){
			public void run(){
				while(true){
					if(ran){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						clientActions[(int)(Math.random()*num_of_clients)].doClick();
						try {
							Thread.sleep(1300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

	}
}
