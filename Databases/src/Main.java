

public class Main extends Menu {

	Thread[] clients =null;

	@Override
	public int doButtonAction() {

		String inputfile;
		String name;

		switch (option_update.getSelectedIndex()+1) {
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

		switch (option_type.getSelectedIndex()+1) {
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

		
		ServerInterface si=new ServerInterface(option_update.getSelectedIndex()+1, option_type.getSelectedIndex()+1,num_of_clients.getValue());
		this.frame.dispose();
		
		return 0;
	}

	public static void main(String[] args) {
		Main o = new Main();
		o.frame.setVisible(true);
	}
}
