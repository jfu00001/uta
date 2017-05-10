package Interface;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LogfileView extends JFrame {

	private JPanel contentPane;

	public LogfileView(String t) {
		setTitle("LOGFILE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		contentPane = new JPanel();
 		contentPane.setBorder(new EmptyBorder(100, 100, 100, 100));
 		setBounds(100, 100, 246, 279);
 		setContentPane(contentPane);
 		contentPane.setLayout(null);
 		
 		JScrollPane scrollPane = new JScrollPane();
 		scrollPane.setBounds(12, 13, 204, 208);
 		contentPane.add(scrollPane);
 		JLabel lblLogLabel = new JLabel("<html>"+t+"</html>", SwingConstants.CENTER);
 	scrollPane.setViewportView(lblLogLabel);

	}

}
