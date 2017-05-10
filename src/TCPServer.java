
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import Interface.Clientinterface;
import Interface.LogfileView;
import Interface.Serverinterface;

class TCPServer extends Serverinterface implements Runnable {
	// class TCPServer extends Serverinterface implements Runnable {
	String name;
	int option_update;// 1.Immediate 2.Deferred
	int option_type;// 1.Wound and wait 2.Wait And Die 3.Cautious Waiting
	int c = 0;
	int numofclient = 11;

	int[] lock_commit = new int[numofclient];
	int[] commited = new int[numofclient];
	boolean[] compens = new boolean[numofclient];
	int[] earliestLSN = new int[numofclient];
	int[] timestamp = new int[numofclient];
	boolean[] waitgraph = new boolean[numofclient];
	boolean[] shrinking_phase = new boolean[numofclient];
	int checkpointInstr=10;

	int TS = 0;
	int logline = 1;
	int num_of_clients;
	static ArrayList<Item> items = new ArrayList<Item>(26);
	File log_file;
	String Logfile = "";
	LogfileView frame = new LogfileView(Logfile);


	public TCPServer(String na, int opt_up, int opt_ty, int num_oc) {
		super(na);
		name = na;
		option_update = opt_up;
		option_type = opt_ty;
		num_of_clients = num_oc;
	}

	//Writes sentence to log and to server interface
	private void write_to_log(String clientSentence) throws IOException {
		if (frame.isVisible()) {
			frame.setVisible(false);
		}

		FileWriter logfw = new FileWriter(log_file.getAbsoluteFile(), true);
		logfw.write(clientSentence);
		logfw.write("\n");
		logfw.close();
		// for interface
		Logfile += logline + ") " + clientSentence + "<br>";
		frame = new LogfileView(Logfile);
		frame.setLocation(0, Clientinterface.client_windowY + server_windowY);
		frame.setVisible(true);

	}

	//Gets the transaction to abort, eg "T1", "T2", etc....
	public void abort_trans(String comm) throws IOException {
		int trans_num = Integer.parseInt(comm.substring(1));
		int start_line = earliestLSN[trans_num];
		if (start_line == 0) {
			return;
		}
		Scanner undo = new Scanner(log_file);
		int i = 1;
		while (i != start_line) {
			i++;
			undo.nextLine();
		}
	
		while (i < logline) {
			String command = undo.nextLine();
			String[] comm2 = command.split(" ");
			
			if (comm2[1].equals("WRITE") && comm2[0].contains(""+trans_num)) {
				Item item = items.get((int) (Character.toLowerCase(comm2[3].charAt(0)) - 'a'));
				if (item.touched == false) {
					item.touched = true;
					String temp = comm2[2] + "/";
					File valuefile = new File(temp + comm2[3] + ".txt");
					FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
					valuefw.write(comm2[5]);
					valuefw.close();
				}
			}
			i++;
		}
		undo.close();
		Item item;
		for (int k = 0; k < 26; k++) {
			item = items.get(k);
			item.touched = false;
		}
	}

	public void checkpoint(boolean log_flag) throws IOException{
		int loglinecheck = logline;
		if (log_flag == false)
			loglinecheck--;
		int trans_num;
		int start_line = loglinecheck;
		for (int i = 0; i < 10; i++) {// number of
			// transactions
			if (commited[i] == 1) {
				if (earliestLSN[i] < start_line) {
					start_line = earliestLSN[i];
				}
			}
		}
		
		Scanner redo = new Scanner(log_file);
		int i = 1;
		while (i < start_line) {
			i++;
			redo.nextLine();
		}
		while (i <= loglinecheck) {
			String command = redo.nextLine();
			String[] comm2 = command.split(" ");
			trans_num = Integer.parseInt(comm2[0].substring(1));
			if (comm2[1].equals("WRITE")) {
				if (commited[trans_num] == 1) {
					String temp = comm2[2] + "/";
					File valuefile = new File(temp + comm2[3] + ".txt");
					FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
					valuefw.write(comm2[4]);
					valuefw.close();
				}
			}
			i++;
		}
		redo.close();
		for (int k = 0; k < 10; k++)
			commited[k] = 0;
		System.out.println("FLUSHED");
	}

	//System crash and recovery
	@Override
	public int doButtonActionCrash() {
		try {
			write_to_log("T0 CRASH");
			logline++;

			if (option_update == 1){//Immediate
				for(int i=1;i<numofclient;i++){
					if(commited[i]==0){
						String comm="T"+i;
						abort_trans(comm);
					}
				}
			}else if (option_update == 2){//Differed
				checkpoint(false);
			}

			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}


	@Override
	public int doButtonActionCheckpoint() {
		try {
			write_to_log("T0 CHECKPOINT");
			checkpoint(true);
			logline++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	//In case of RESTARTing function
	public void free_locks(int id) {
		for (int i = 0; i < items.size(); i++) {
			Item temp = items.get(i);
			if (temp.list.contains(id)) {
				temp.list.remove((Integer) id);
				if (temp.status == 2) {
					temp.status = 0;
				} else if (temp.status == 1) {
					if (temp.list.size() == 0) {
						temp.status = 0;
					}
				}
			}
		}
	}

	// return 0 for Continue,
	// 1 for wait,
	// 2 for restart
	// i is the demanding transaction timestamp and j the holders timestamp
	public int waitType(int i, int j, int n, int t, Item item) throws IOException {
		System.out.println(
				"T" + t + " with " + i + " timestamp " + " try to catch " + "T" + n + " with " + j + " timestamp ");
		// recourse earliest,trans earliest
		if (option_type == 1) {// 1.Wound and wait 2.Wait And Die 3.Cautious
			if (i == j) {
				System.out.println("Return Ok");
				return 0;
			} // Waiting
			if (i > j) {
				System.out.println("Return wait");
				return 1;
			}
			String trans = "T" + n;
			if (option_update == 1)
				abort_trans(trans);
			write_to_log(trans + " ABORT");
			free_locks(n);
			compens[n] = true;
			logline++;
			item.list.remove((Integer) n);
			System.out.println("Return Ok");
			return 0;
		} else if (option_type == 2) {// 1.Wound and wait 2.Wait And Die
			if (i == j) {
				System.out.println("Return Ok");
				return 0;
			} // 3.Cautious Waiting
			if (i < j) {
				System.out.println("Return wait");
				return 1;
			}

			String trans = "T" + t;
			if (option_update == 1)
				abort_trans(trans);
			logline++;
			write_to_log(trans + " ABORT");
			compens[t] = true;
			free_locks(t);
			item.list.remove((Integer) t);
			System.out.println("Return restart");
			return 2;
		} else if (option_type == 3) {// 1.Wound and wait 2.Wait And Die

			if (i == j) {
				System.out.println("Return Ok");
				return 0;
			} // 3.Cautious Waiting

			if (!waitgraph[t]) {
				if (waitgraph[n]) {
					String trans = "T" + t;
					if (option_update == 1)
						abort_trans(trans);
					logline++;
					write_to_log(trans + " ABORT");
					compens[t] = true;
					free_locks(t);
					item.list.remove((Integer) t);
					System.out.println("Return restart");
					return 2;

				} else {
					waitgraph[t] = true;
					System.out.println("Return wait");
					return 1;
				}

			} else {
				System.out.println("Return wait");
				return 1;
			}

		}

		return 0;
	}


	@SuppressWarnings("null")
	@Override
	public void run() {
		log_file = new File("log.txt");
		try {
			log_file.delete();
			log_file.createNewFile();
			for (int i = 0; i < 26; i++) {
				items.add(new Item((char) ('a' + i)));
				try {
					File valuefile = null;
					if (i < 10)
						valuefile = new File("FILE1/" + ((char)('A' + i)) + ".txt");
					else if (i<20)
						valuefile = new File("FILE2/" + ((char)('A' + i)) + ".txt");
					else
						valuefile = new File("FILE3/" + ((char)('A' + i)) + ".txt");
					valuefile.delete();
					valuefile.createNewFile();
					FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
					valuefw.write("0");
					valuefw.close();
				} catch (Exception e) {
					System.out.println("Error creating .txt file");
				}

			}

			setLocation(0, Clientinterface.client_windowY);
			// to be shown down of the Clients
			setVisible(true);


			String clientSentence;
			String serverResponse = "";
			@SuppressWarnings("resource")
			ServerSocket welcomeSocket = new ServerSocket(6789);

			while (true) {
				System.out.println("Log will write the next entry at line : " + logline);
				boolean log_flag = false;
	
				Socket connectionSocket = welcomeSocket.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);// T1 +
				String[] comm = clientSentence.split(" ");
				int transaction_num = Integer.parseInt(comm[0].substring(1));
				if (compens[transaction_num]) {
					serverResponse = "RESTART";
					compens[transaction_num] = false;
				} else {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					if (option_update == 1) {
						/// IMMEDIATE UPDATE
						// step1
						if (timestamp[transaction_num] == 0){
							if (!comm[1].equals("BEGIN")) {
								num_of_clients--;
							//	System.out.println(num_of_clients);
								serverResponse = "KILL";
								outToClient.writeBytes(serverResponse + '\n');
								continue;
							}
							else{
								timestamp[transaction_num] = ++TS;
							}

						}
						if (comm[1].equals("END")) {
							num_of_clients--;
							write_to_log(clientSentence);
							serverResponse = "OK";
							log_flag = true;
						}
						else if (comm[1].equals("BEGIN")) {
							earliestLSN[transaction_num] = logline;
							write_to_log(clientSentence);
							serverResponse = "OK";
							log_flag = true;
						}
						else if (comm[1].equals("COMMIT")) {
							commited[transaction_num] = 1;
							lock_commit[transaction_num] = 1;
							write_to_log(clientSentence);
							serverResponse = "OK";
							log_flag = true;
						} else if (comm[1].equals("ABORT")) {
							commited[transaction_num] = 2;
							lock_commit[transaction_num] = 1;
							// UNDO AT ABORT
							write_to_log(clientSentence);
							abort_trans(comm[0]);
							log_flag = true;
						} else if (comm[1].equals("READLOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println("YOU ARE NOT ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[3].charAt(0)) - 'a'));
								if (item.status != 2) {
									item.status = 1;
									item.list.add(transaction_num);
									waitgraph[transaction_num] = false;
									serverResponse = "OK";
								} else {
									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										item.list.add(transaction_num);
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";
									}

								}
							}

						} else if (comm[1].equals("WRITELOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU ARE NOT ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[3].charAt(0)) - 'a'));
								if (item.status != 2) {
									if (item.status == 0 || (item.status == 1 && item.list.size() == 1
											&& item.list.contains(transaction_num))) {
										item.status = 2;
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										if (!item.list.contains(transaction_num))
											item.list.add(transaction_num);
									} else {

										for (int inter = 0; inter < item.list.size(); inter++) {

											int t = item.list.get(inter);
											int i = timestamp[transaction_num];
											int j = timestamp[t];
											int temp = waitType(i, j, t, transaction_num, item);
											if (temp == 0) {
												waitgraph[transaction_num] = false;
												serverResponse = "OK";
											} else if (temp == 1) {
												serverResponse = "WAIT";
												break;

											} else if (temp == 2) {
												serverResponse = "RESTART";
												break;

											}
										}
									}
								} else {
									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										serverResponse = "OK";
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";

									}
								}
							}
							
						} else if (comm[1].equals("DELETELOCK")){
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU ARE NOT ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								boolean breaklock = false;
								//Lock ALL items needed
								int start; int end;
								if (comm[2].equals("FILE1")){start = 0; end = 10;}
								else if (comm[2].equals("FILE2")){start = 10; end = 20;}
								else {start = 20; end = 26;}
								for (int k = start; k<end; k++) {
									Item item = items.get(k);
									if (item.status != 2) {
										if (item.status == 0 || (item.status == 1 && item.list.size() == 1
												&& item.list.contains(transaction_num))) {
											item.status = 2;
											waitgraph[transaction_num] = false;
											serverResponse = "OK";
											if (!item.list.contains(transaction_num))
												item.list.add(transaction_num);
										} else {

											for (int inter = 0; inter < item.list.size(); inter++) {

												int t = item.list.get(inter);
												int i = timestamp[transaction_num];
												int j = timestamp[t];
												int temp = waitType(i, j, t, transaction_num, item);
												if (temp == 0) {
													waitgraph[transaction_num] = false;
													serverResponse = "OK";
												} else if (temp == 1) {
													serverResponse = "WAIT";
													breaklock = true;
													break;

												} else if (temp == 2) {
													serverResponse = "RESTART";
													breaklock = true;
													break;

												}
											}
											if (breaklock) break;
										}
									} else {
										int i = timestamp[transaction_num];
										int n = item.list.get(0);
										int j = timestamp[n];
										int temp = waitType(i, j, n, transaction_num, item);
										if (temp == 0) {
											serverResponse = "OK";
										} else if (temp == 1) {
											serverResponse = "WAIT";
											break;
										} else if (temp == 2) {
											serverResponse = "RESTART";
											break;

										}
									}
								}//end of loop
							}
						}
						else if (comm[1].equals("UNLOCK")) {
							shrinking_phase[transaction_num] = true;
							Item item = items.get((int) (Character.toLowerCase(comm[3].charAt(0)) - 'a'));
							if (item.status == 2 && item.list.size() == 1 && item.list.contains(transaction_num)) {
								if (lock_commit[transaction_num] == 0) {
									System.out.println("YOU ARE NOT ALLOWED TO UNLOCK A WRITE-LOCK BEFORE COMMIT");
									System.exit(0);
								} else {
									item.status = 0;
									item.list.remove((Integer) transaction_num);
								}
							} else if (item.status == 1 && item.list.size() == 1
									&& item.list.contains(transaction_num)) {
								item.status = 0;
								item.list.remove((Integer) transaction_num);
							} else {
								item.list.remove((Integer) transaction_num);
							}
							serverResponse = "OK";
						}else if (comm[1].equals("DELETEUNLOCK")) {
							shrinking_phase[transaction_num] = true;
							int start; int end;
							if (comm[2].equals("FILE1")){start = 0; end = 10;}
							else if (comm[2].equals("FILE2")){start = 10; end = 20;}
							else {start = 20; end = 26;}
							for (int k = start; k<end; k++) {
								Item item = items.get(k);
								if (item.status == 2 && item.list.size() == 1 && item.list.contains(transaction_num)) {
									if (lock_commit[transaction_num] == 0) {
										System.out.println("YOU ARE NOT ALLOWED TO UNLOCK A WRITE-LOCK BEFORE COMMIT");
										System.exit(0);
									} else {
										item.status = 0;
										item.list.remove((Integer) transaction_num);
									}
								}
								serverResponse = "OK";
							}
						} else if(comm[1].equals("DELETE")){
							int start; int end;
							if (comm[2].equals("FILE1")){start = 0; end = 10;}
							else if (comm[2].equals("FILE2")){start = 10; end = 20;}
							else {start = 20; end = 26;}
							for (int k = start; k<end; k++) {
								String temp2 = comm[2] + "/";
								Character x = (char)('A' + k);
								File valuefile = new File(temp2 + x.toString() + ".txt");
								Scanner valuefw1 = new Scanner(valuefile);
								String temp = valuefw1.next();
								valuefw1.close();
								FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
								valuefw.write("0");
								valuefw.close();
								valuefw1.close();
								serverResponse = "OK";
								write_to_log("T" + transaction_num + " WRITE " + comm[2] + " " + x.toString() + " 0 " + temp);
								log_flag = false;
								logline++;
							}
							
							//READ OR WRITE
						} else {
							String temp2 = comm[2] + "/";
							File valuefile = new File(temp2 + comm[3] + ".txt");
							// READ COMMAND
							if (comm[1].equals("READ")) {
								Scanner valuefw = new Scanner(valuefile);
								String temp = valuefw.next();

								valuefw.close();
								// STELOUME TO VALUE PISW STON CLIENT

								serverResponse = temp;

								write_to_log(clientSentence);
								log_flag = true;
								// WRITE COMMAND
							} else if (comm[1].equals("WRITE")) {
								Scanner valuefw1 = new Scanner(valuefile);
								String temp = valuefw1.next();
								valuefw1.close();
								FileWriter valuefw = new FileWriter(valuefile.getAbsoluteFile());
								valuefw.write(comm[4]);
								valuefw.close();
								valuefw1.close();

								// STELOUME TO VALUE PISW STON CLIENT
								serverResponse = "OK";
								write_to_log(clientSentence + " " + temp);
								log_flag = true;
							} else {
								Exception e = null;
								throw e;
							}
						}

						// if the first line is readlock dont count
						if (log_flag) {// if Unlock or lock dont change the line
							// if Unlock or lock dont change the line
							logline++;
						}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					} else if (option_update == 2) {

						/// DEFERRED UPDATE
						// step1

						if (timestamp[transaction_num] == 0){

							if (!comm[1].equals("BEGIN")) {
								num_of_clients--;
								serverResponse = "KILL";
								outToClient.writeBytes(serverResponse + '\n');
								continue;
							}
							else{
								timestamp[transaction_num] = ++TS;
							}

						}

						if (comm[1].equals("END")) {
							num_of_clients--;
							write_to_log(clientSentence);
							serverResponse = "OK";
							log_flag = true;
						}
						else if (comm[1].equals("BEGIN")) {
							earliestLSN[transaction_num] = logline;
							write_to_log(clientSentence);
							serverResponse = "OK";
							log_flag = true;
						}

						else if (comm[1].equals("COMMIT")) {
							commited[transaction_num] = 1;
							lock_commit[transaction_num] = 1;
							write_to_log(clientSentence);
							serverResponse = "OK";
							log_flag = true;
						} else if (comm[1].equals("ABORT")) {
							commited[transaction_num] = 2;
							lock_commit[transaction_num] = 1;
							write_to_log(clientSentence);
							serverResponse = "OK";
							log_flag = true;
						} else if (comm[1].equals("READLOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU ARE NOT ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[3].charAt(0)) - 'a'));
								if (item.status != 2) {
									item.status = 1;
									item.list.add(transaction_num);
									waitgraph[transaction_num] = false;
									serverResponse = "OK";
								} else {
									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										item.list.add(transaction_num);
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";

									}
								}
							}

						} else if (comm[1].equals("WRITELOCK")) {
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU ARE NOT ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								Item item = items.get((int) (Character.toLowerCase(comm[3].charAt(0)) - 'a'));
								if (item.status != 2) {
									if (item.status == 0 || (item.status == 1 && item.list.size() == 1
											&& item.list.contains(transaction_num))) {
										item.status = 2;
										waitgraph[transaction_num] = false;
										serverResponse = "OK";
										if (!item.list.contains(transaction_num))
											item.list.add(transaction_num);
									} else {

										for (int inter = 0; inter < item.list.size(); inter++) {

											int t = item.list.get(inter);
											int i = timestamp[transaction_num];
											int j = timestamp[t];
											int temp = waitType(i, j, t, transaction_num, item);
											if (temp == 0) {
												waitgraph[transaction_num] = false;
												serverResponse = "OK";
											} else if (temp == 1) {
												serverResponse = "WAIT";
												break;

											} else if (temp == 2) {
												serverResponse = "RESTART";
												break;

											}
										}

									}
								} else {

									int i = timestamp[transaction_num];
									int n = item.list.get(0);
									int j = timestamp[n];
									int temp = waitType(i, j, n, transaction_num, item);
									if (temp == 0) {
										serverResponse = "OK";
									} else if (temp == 1) {
										serverResponse = "WAIT";
									} else if (temp == 2) {
										serverResponse = "RESTART";

									}

								}
							}
						} else if (comm[1].equals("DELETELOCK")){
							if (shrinking_phase[transaction_num] == true) {
								System.out.println(
										"YOU ARE NOT ALLOWED TO LOCK ANYTHING AFTER UNLOCKING AT LEAST ONE ITEM");
								System.exit(0);
							} else {
								boolean breaklock = false;
								//Lock ALL items needed
								int start; int end;
								if (comm[2].equals("FILE1")){start = 0; end = 10;}
								else if (comm[2].equals("FILE2")){start = 10; end = 20;}
								else {start = 20; end = 26;}
								for (int k = start; k<end; k++) {
									Item item = items.get(k);
									if (item.status != 2) {
										if (item.status == 0 || (item.status == 1 && item.list.size() == 1
												&& item.list.contains(transaction_num))) {
											item.status = 2;
											waitgraph[transaction_num] = false;
											serverResponse = "OK";
											if (!item.list.contains(transaction_num))
												item.list.add(transaction_num);
										} else {

											for (int inter = 0; inter < item.list.size(); inter++) {

												int t = item.list.get(inter);
												int i = timestamp[transaction_num];
												int j = timestamp[t];
												int temp = waitType(i, j, t, transaction_num, item);
												if (temp == 0) {
													waitgraph[transaction_num] = false;
													serverResponse = "OK";
												} else if (temp == 1) {
													serverResponse = "WAIT";
													breaklock = true;
													break;

												} else if (temp == 2) {
													serverResponse = "RESTART";
													breaklock = true;
													break;

												}
											}
											if (breaklock) break;
										}
									} else {

										int i = timestamp[transaction_num];
										int n = item.list.get(0);
										int j = timestamp[n];
										int temp = waitType(i, j, n, transaction_num, item);
										if (temp == 0) {
											serverResponse = "OK";
										} else if (temp == 1) {
											serverResponse = "WAIT";
											break;
										} else if (temp == 2) {
											serverResponse = "RESTART";
											break;

										}

									}
								}//end of loop
							}

						} else if (comm[1].equals("UNLOCK")) {
							shrinking_phase[transaction_num] = true;
							Item item = items.get((int) (Character.toLowerCase(comm[3].charAt(0)) - 'a'));
							if (item.status == 2 && item.list.size() == 1 && item.list.contains(transaction_num)) {
								if (lock_commit[transaction_num] == 0) {
									System.out.println("YOU ARE NOT ALLOWED TO UNLOCK A WRITE-LOCK BEFORE COMMIT");
									System.exit(0);
								} else {
									item.status = 0;
									item.list.remove((Integer) transaction_num);
								}
							} else if (item.status == 1 && item.list.size() == 1
									&& item.list.contains(transaction_num)) {
								item.status = 0;
								item.list.remove((Integer) transaction_num);
							} else {
								item.list.remove((Integer) transaction_num);
							}
							serverResponse = "OK";
						} else if (comm[1].equals("DELETEUNLOCK")) {
							shrinking_phase[transaction_num] = true;
							int start; int end;
							if (comm[2].equals("FILE1")){start = 0; end = 10;}
							else if (comm[2].equals("FILE2")){start = 10; end = 20;}
							else {start = 20; end = 26;}
							for (int k = start; k<end; k++) {
							Item item = items.get(k);
							if (item.status == 2 && item.list.size() == 1 && item.list.contains(transaction_num)) {
								if (lock_commit[transaction_num] == 0) {
									System.out.println("YOU ARE NOT ALLOWED TO UNLOCK A WRITE-LOCK BEFORE COMMIT");
									System.exit(0);
								} else {
									item.status = 0;
									item.list.remove((Integer) transaction_num);
								}
							} else if (item.status == 1 && item.list.size() == 1
									&& item.list.contains(transaction_num)) {
								item.status = 0;
								item.list.remove((Integer) transaction_num);
							} else {
								item.list.remove((Integer) transaction_num);
							}
							serverResponse = "OK";
							}
						} else if (comm[1].equals("DELETE")){
							int start; int end;
							if (comm[2].equals("FILE1")){start = 0; end = 10;}
							else if (comm[2].equals("FILE2")){start = 10; end = 20;}
							else {start = 20; end = 26;}
							for (int k = start; k<end; k++) {
								Character x = (char)('A' + k);
								write_to_log("T" + transaction_num + " WRITE " + comm[2] + " " + x.toString() + " 0");
								if (logline % 10 == 0 || num_of_clients == 0) {
									checkpoint (true);
								}
								logline++;
							}
							log_flag = false;
						} else {
							String temp2 = comm[2] + "/";
							File valuefile = new File(temp2 + comm[3] + ".txt");
							// READ COMMAND
							if (comm[1].equals("READ")) {
								Scanner valuefw = new Scanner(valuefile);
								String temp = valuefw.next();

								valuefw.close();
								// STELOUME TO VALUE PISW STON CLIENT

								serverResponse = temp;

								write_to_log(clientSentence);
								log_flag = true;
								// WRITE COMMAND
							} else if (comm[1].equals("WRITE")) {
								// STELOUME TO VALUE PISW STON CLIENT
								serverResponse = "OK";
								write_to_log(clientSentence);
								log_flag = true;
							} else {
								Exception e = null;
								throw e;
							}
						}
						// CHECKPOINT PHASE
						if ((logline % 10 == 0 || num_of_clients == 0) && log_flag) {
							checkpoint (log_flag);
						}


						if (log_flag) {// if Unlock or lock dont change the line
							logline++;
						}
					}
				}
				outToClient.writeBytes(serverResponse + '\n');

			}
		} catch (IOException e1) {
			System.out.println("Provlima ston server");
			e1.printStackTrace();
		} catch (Exception e1) {

			System.err.println("Wrong Input");
			e1.printStackTrace();
		}
	}
}