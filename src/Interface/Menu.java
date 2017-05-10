package Interface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JSlider;

public class Menu extends JFrame {

	private JPanel contentPane;
	private ButtonGroup bgUpdate = new ButtonGroup();		//for radio Buttons
	private ButtonGroup bgType = new ButtonGroup();		//for radio Buttons
	protected int option_update=1;//1.Immediate 2.Deferred 
	protected int option_type=1;//1.Wound and wait 2.Wait And Die 3.Cautious Waiting

	//for Client Labeling
	int Client_MIN = 1;//minimum
	int Client_MAX = 10;//maximum
	int Client_INIT = 3; //initial State
	int Client_Step = 1; //step to Change in Label

	protected int num_of_clients = Client_INIT;//num of clients


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 351);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		//Type group buttons
		JRadioButton rdbtnImmediateUpdate = new JRadioButton("Immediate Update");//opt 1
		rdbtnImmediateUpdate.setSelected(true);//is selected
		JRadioButton rdbtnDeferredUpdate = new JRadioButton("Deferred Update");//opt 2

		rdbtnImmediateUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option_update=1;
				//System.out.println("Option Update is "+option_update);
			}
		});

		rdbtnDeferredUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option_update=2;
				//System.out.println("Option Update is "+option_update);
			}
		});

		//for grouping Updates (radio Buttons)
		bgUpdate.add(rdbtnImmediateUpdate);
		bgUpdate.add(rdbtnDeferredUpdate);

		//Type radio buttons
		JRadioButton rdbtnWoundAndWait = new JRadioButton("Wound and wait");//opt 1
		rdbtnWoundAndWait.setSelected(true);//is Selected at start
		JRadioButton rdbtnWaitAndDie = new JRadioButton("Wait and die");//opt 2
		JRadioButton rdbtnCautiousWaiting = new JRadioButton("Cautious Waiting");//opt 3

		rdbtnWoundAndWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option_type=1;
				//System.out.println("Option Type is "+option_type=1;);
			}
		});

		rdbtnWaitAndDie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option_type=2;
				//System.out.println("Option Type is "+option_type=1;);
			}
		});

		rdbtnCautiousWaiting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option_type=3;
				//System.out.println("Option Type is "+option_type=1;);
			}
		});

		//grouping Type (radio Buttons)
		bgType.add(rdbtnWoundAndWait);
		bgType.add(rdbtnWaitAndDie);
		bgType.add(rdbtnCautiousWaiting);

		//Button to Start Server
		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(doButtonAction()==0){
					//implementation at Child
					dispose();
				}
			}
		});

		//for slider to Choose Number of clients
		JSlider slider = new JSlider(JSlider.HORIZONTAL,Client_MIN, Client_MAX, Client_INIT);

		slider.addChangeListener(new SliderListener());	//listener
		slider.setMajorTickSpacing(Client_Step);
		slider.setMinorTickSpacing(Client_Step);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		//	slider.set

		//Labeling in the Interface
		JLabel lblUpdateType = new JLabel("Update Type");
		lblUpdateType.setFont(new Font("Times New Roman", Font.PLAIN, 20));

		JLabel lblDeadlockPreventionType = new JLabel("Deadlock Type");
		lblDeadlockPreventionType.setFont(new Font("Times New Roman", Font.PLAIN, 20));

		JLabel label = new JLabel("Number of Clients");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 20));

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(23)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_contentPane.createSequentialGroup()
														.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
																.addComponent(lblUpdateType, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
																.addComponent(rdbtnImmediateUpdate)
																.addComponent(rdbtnDeferredUpdate, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
														.addGap(53)
														.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
																.addComponent(lblDeadlockPreventionType, GroupLayout.PREFERRED_SIZE, 151, Short.MAX_VALUE)
																.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
																		.addComponent(rdbtnCautiousWaiting, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
																		.addGap(26))
																.addComponent(rdbtnWaitAndDie, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
																.addComponent(rdbtnWoundAndWait, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)))
												.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(label, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(119)
										.addComponent(btnStartServer)))
						.addContainerGap())
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUpdateType, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDeadlockPreventionType, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(rdbtnImmediateUpdate)
								.addComponent(rdbtnWoundAndWait))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(rdbtnDeferredUpdate)
								.addComponent(rdbtnWaitAndDie))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(rdbtnCautiousWaiting)
						.addPreferredGap(ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
										.addComponent(btnStartServer)
										.addGap(22))
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
										.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(65))))
				);
		contentPane.setLayout(gl_contentPane);

	}

	public int doButtonAction() {
		//Must be implement at child
		System.out.println("You should implement this Function");
		return 0;
	}

	//for Event of slider
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			//assigning the Num of clients
			num_of_clients=(int)source.getValue();

		}
	}
}
