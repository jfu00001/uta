

public class Main extends Menu {

	Thread[] clients =null;

	@Override
	public int doButtonAction() {

		String inputfile;
		String name;

		switch (option_update) {
		case 1: {

			System.out.println("The Update is  Immediate");
			break;
		}
		case 2: {

			System.out.println("The Update is  Deferred");
			break;
		}
		default: {
			System.out.println("ERROR.You didnt Choose");
		}
		}

		switch (option_type) {
		case 1: {

			System.out.println("The Type is  Wound and wait");
			break;
		}
		case 2: {

			System.out.println("The Type is  Wait And Die");
			break;
		}
		case 3: {

			System.out.println("The Type is  Cautious Waiting");
			break;
		}
		default: {
			System.out.println("ERROR.You didnt Choose");
		}

		}

		/*
		 * try if items[i].txt exist valuefilename=items[i].txt else
		 * valuefilename new
		 * 
		 */
		/*clients = new Thread[num_of_clients];
		int numofCl=0;
		for (int i = 0; i < num_of_clients; i++) {
			inputfile = "inputs/input" + (i + 1) + ".txt";
			name = "Client" + (i + 1);
			try {
				clients[i] = new Thread(new TCPClient(inputfile, name, (i + 1)));
				clients[i].start();
				numofCl++;
			} catch (Exception e) {
				System.out.println("There is no file with name " + inputfile);
			}

		}


		System.out.println("The Number of Clients are " + numofCl);
		Thread t1 = new Thread(new TCPServer("Server", option_update, option_type,numofCl));
		t1.setDaemon(true);
		t1.start();*/
		
		ServerInterface si=new ServerInterface(option_update, option_type,num_of_clients);
		
		
		return 0;
	}

	public static void main(String[] args) {
		Main o = new Main();
		o.setVisible(true);
	}
}
