
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JButton;

class ClientTCP extends Thread {

	String inputfile;
	String name;
	int id;
	Socket clientSocket;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	String sentence = null;
	String modifiedSentence;
	BufferedReader inFromUser;
	int sleep = 0;
	ArrayList<String> array = new ArrayList<String>(100);
	int arraylength = 0;
	int line = 0;
	boolean notactive=false;
	boolean end = false;
	JButton action;

	ClientTCP(String in, String na, int i, JButton action) throws FileNotFoundException {
		inputfile = in;
		name = na;
		id = i;
		inFromUser = new BufferedReader(new FileReader(inputfile));
		this.action=action;
	}

	// Every time the button is clicked, open new socket connection and send a
	// sentence
	public int doButtonAction() {
		// Must be implement at child
		if (sleep == 0 && !end) {
			try {
				if(action.getText().contains("END"))end=true;
				clientSocket = new Socket("localhost", 6789);
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				sentence = array.get(line);
				outToServer.writeBytes(sentence + '\n');
				modifiedSentence = inFromServer.readLine();
				System.out.println("FROM SERVER: " + modifiedSentence);
				clientSocket.close();

				if (modifiedSentence.equals("KILL")){
					return 0;
				}
				if (!modifiedSentence.equals("WAIT") && !modifiedSentence.equals("RESTART"))
					line++;
				else if (!modifiedSentence.equals("RESTART")){
					sleep = 3;
				}
				else {
					line=0;
				}

				if (line == arraylength) {
					return 0;
				} else {
					action.setText(array.get(line));
				}
			} catch (Exception e) {
				System.out.println("Error at Button at Transaction " + id);
				return 1;
			}
		}
		else sleep--;
		return 1;
	}

	// Once transaction is initialized, a button appears
	@Override
	public void run() {
		try {
			inFromUser = new BufferedReader(new FileReader(inputfile));
			do {
				sentence = inFromUser.readLine();
				array.add("T"+this.id+" "+sentence);
				arraylength++;
			} while (!sentence.contains("END"));
			inFromUser.close();
			action.setText(array.get(0));
		} catch (Exception e) {
			System.out.println("Error at Transaction " + id);
		}

	}
}