
import java.util.ArrayList;

public class Item {
	String name;
	int status; // 0=unlocked 1=readlock 2=writelock
	ArrayList<Integer> list = new ArrayList<Integer>(); //holder-transactions of item
	boolean touched; //for undo's

	public Item(char na){
		name = Character.toString(na);
		status = 0;
		touched = false;
	}
}

