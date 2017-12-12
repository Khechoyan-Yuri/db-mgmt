import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


import com.twilio.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;



public class DatabaseServerGUI {
	static JButton Start = new JButton("Start");
	static JButton Stop = new JButton("Stop");
	static JFrame myFrame = new JFrame("Server Console");
	static JPanel panel = new JPanel(new GridLayout(4,1));
	static JTextArea Log = new JTextArea(10, 5);
	static JTextField Console = new JTextField();
	static JScrollPane pane;
	static int Started = 0;
	static int ConsolePath = 0;
	static Connection conn = null;
	
	static String loginName = null;
	static String loginPassword = null;
	static int loginStatus = 0;
	static String AdminPass = null;
	static int AdminAccess = 0;
	public static final String ACCOUNT_SID = "AC60297adad31d7ad9b1d624e9ab4e6798";
	public static final String AUTH_TOKEN = "ac74af457ad365b03dfd85f3b2d01b35";
	
	static Timer timer;
	static int MasterTimerCounter = 9600000;
	
	static ArrayList <String> Passwords = new ArrayList<String>();
	
	/* To add:
	 * -Weave the admin password into the normal command system sorta like this:
	 * 		if (adminPassword != 1) {
	 * 			cant use these commands
	 * 		}
	 * 		commands usable without admin password
	 * -real-time display of the database
	 * -logout system
	 */
	
	
	public static void main(String[] args) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		myFrame.setSize(900,900);
		myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Start.setPreferredSize(new Dimension(500, 200));
		Stop.setPreferredSize(new Dimension(500, 200));
	    panel.add(Start);
	    panel.add(Stop);
		myFrame.setContentPane(panel);
		myFrame.pack();
		panel.add(Log);
    	pane = new JScrollPane(Log);
		pane.setPreferredSize(new Dimension(900, 900));
		myFrame.add(pane, BorderLayout.CENTER);
		panel.add(Console);
		Log.append("---System Log---" + "\n");
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			System.out.println(dtf.format(now));
		Log.append("System initialized at timestamp:   " + GetTimestamp() + "\n");
		myFrame.setVisible(true);
		Start.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	if (Started == 1) {
		    		Log.append("System is already running.\n");
		    	}
		    	else { 
			    	try {
						String serverName = "localhost:3306"; //default port for XAMPP MySQL is 3306. If errors: change.
					    String mydatabase = "project_db";
					    String url = "jdbc:mysql://" + serverName + "/" + mydatabase; 
	
					    String username = "root";
					    String password = "";
					    conn = DriverManager.getConnection(url, username, password);
					    System.out.println("---Successfully established connection with SQL database---");
							Log.append("System started at timestamp:   " + GetTimestamp() + "\n");
					    // Do something with the Connection
					    
					    try {
					      String queryEMPID = "SELECT EMPID FROM employee";
					      Statement st = conn.createStatement();
					      ResultSet rs = st.executeQuery(queryEMPID);
					      while (rs.next()) {
					          String IDreturned = rs.getString("EMPID");
					          String CharacterPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!#$@%&*()^";
						        StringBuilder TwoChars = new StringBuilder();
						        Random rnd = new Random();
						        while (TwoChars.length() < 2) {
						            int index = (int) (rnd.nextFloat() * CharacterPool.length());
						            TwoChars.append(CharacterPool.charAt(index));
						        }
						      String saltStr = TwoChars.toString();
					          String Patchwork = IDreturned.substring(0, 2) + "--" + saltStr;
					          Passwords.add(Patchwork);
					          System.out.println(Patchwork);
					      }
					      try { 
					    	  String FnameTemp = null;
					    	  String LnameTemp = null;
					    	  int i = 0;
						      String query = "SELECT fname,lname FROM employee";
						      Statement statement = conn.createStatement();
						      ResultSet resultset = statement.executeQuery(query);
						      while (resultset.next()) {
						         FnameTemp = resultset.getString("fname");
						         LnameTemp = resultset.getString("lname");
						         System.out.println("First Name: " + FnameTemp + "     Last Name: " + LnameTemp + "    Password for the day: " + Passwords.get(i));
						         i++;
						      }
						      statement.close();
						    }
						    catch (Exception q) {
						      System.err.println("Got an exception! ");
						      System.err.println(q.getMessage());
						    }
				          String PassCharacterPool = "ABCDEFGHJKMNOPQRSTUVWXYZ1234567890!#$@%&*^";
					      StringBuilder Builder = new StringBuilder();
					        Random rnd = new Random();
					        while (Builder.length() < 4) {
					            int index = (int) (rnd.nextFloat() * PassCharacterPool.length());
					            Builder.append(PassCharacterPool.charAt(index));
					        }
					      String Assembler = Builder.toString();
					      AdminPass = Assembler;
					      JOptionPane.showMessageDialog(null, "        Admin password for the day: " + AdminPass + "\n !!!Close this window after memorizing the password!!!", "Admin Password", JOptionPane.INFORMATION_MESSAGE);
					      st.close();
					    }
					    catch (Exception q) {
					      System.err.println("Got an exception! ");
					      System.err.println(q.getMessage());
					    }
					} catch (SQLException ex) {
					    // handle any errors
					    System.out.println("SQLException: " + ex.getMessage());
					    System.out.println("SQLState: " + ex.getSQLState());
					    System.out.println("VendorError: " + ex.getErrorCode());
					}
		    	}
		    	Started = 1;
		    	LoginStart();
		    	//the program
		    }            
		});
		Stop.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	//variable check/adjustment
		    	//to stop the system, they need some sort of admin password.
		    	if (Started == 1) {
			    	if (loginStatus == 1) {
			    		if (AdminAccess == 1) {
			    			int ShutdownReply = JOptionPane.showConfirmDialog(null, "Are you sure you want to shut the system down?", "Shutdown Warning",  JOptionPane.YES_NO_OPTION);
			    			if (ShutdownReply == JOptionPane.YES_OPTION) {
			    				try {
			    					Log.append("System shutdown at timestamp >>>" + GetTimestamp() + "<<<  by user \"" + loginName + "\"\n");
						    		System.out.println("Connection to SQl database closed");
									conn.close();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
						    	Started = 0;
			    			}
			    			if (ShutdownReply == JOptionPane.NO_OPTION) {
			    				System.out.println("Shutdown sequence aborted.");
			    			}
					    	
			    		}
			    		else {
			    			Console.setText("You need admin access to shut the program down.");
			    		}
			    	}
			    	else {
		    			Console.setText("You must be logged in & have admin access to use this command.");
		    		}
			    }
		    	else {
	    			Console.setText("System is currently offline.");
	    		}
		    }
		});
		Console.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent e) {
		            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
		            	try {
							Robot r = new Robot();
			            	Console.setText(""); /*this will erase the current text within the console input area.
			            						   Sort of a "garbage collector" idea.*/
			            	 r.keyPress(KeyEvent.VK_BACK_SPACE);
		            	} catch (AWTException e1) {
							e1.printStackTrace();
						}
		            }
		            if(e.getKeyCode() == KeyEvent.VK_ENTER && Started == 1) {
		                try {
							ConsoleHandler(Console.getText());
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		            else if (e.getKeyCode() == KeyEvent.VK_ENTER && Started != 1) {
		            	Console.setText("Please press the \"Start\" button before entering commands.");
		            }
	        }
	    });
	}
	
	@SuppressWarnings("deprecation")
	public static void ConsoleHandler(String Input) throws SQLException {
		if (Input.equals("about") || Input.equals("About")) {
    		if (loginStatus == 1) {
    				
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
		if (Input.equals("admin") || Input.equals("Admin")) {
			if (loginStatus == 1) {
				String AdminPasswordInput = JOptionPane.showInputDialog(null, "Admin Password: ");
			    if (AdminPasswordInput.equals(AdminPass)) {
			    	JOptionPane.showMessageDialog(null,"Password match. Temporary admin permissions granted.\nNote: admin status is lost upon logout.");
			    	AdminAccess = 1;
			    }
			    else {
			    	JOptionPane.showMessageDialog(null,"Password mismatch: Temporary admin permissions denied.\nContact system administrator for help.","Password failure",JOptionPane.ERROR_MESSAGE);
			    }
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
		if (Input.equals("changeTicket") || Input.equals("changeticket") || Input.equals("Changeticket") || Input.equals("ChangeTicket")) {
			if (loginStatus == 1) {
				if (AdminAccess == 1) {
					JFrame PassFrame = new JFrame("Change Ticket");//myFrame
        			JPanel PassPanel = new JPanel(new GridLayout(4,2));
        			JPanel MasterPanel = new JPanel(new GridLayout(2,1));
        			JPanel OptionsPanel = new JPanel(new GridLayout(1,2));
        			JButton Okay = new JButton("Okay");
        			JButton Cancel = new JButton("Cancel");
        			OptionsPanel.add(Okay);
        			OptionsPanel.add(Cancel);
	        			JLabel label01 = new JLabel("I want to");
	        			String[] WantTo = {"Add","Drop","Switch","Resolve"};
	        			JComboBox WantToList = new JComboBox(WantTo);
	        			JComboBox ticketForEmployeeList;
	        			JComboBox WithEmployeeList;
	        			JComboBox ForCustomerList;
	        			
	        			JLabel label02 = new JLabel("a ticket for employee ");
	        			
	        			JLabel label03 = new JLabel(" with (ignored if using DROP,ADD,or RESOLVE) ");
	        			
	        			
	        			JLabel label04 = new JLabel("for customer (ignored if using DROP, SWITCH, OR RESOLVE)  ");
	        			//in english: "I want to Switch a ticket for employee Steven Olson with John McQuaide"
	        			
        		    String query = "SELECT fname,lname FROM employee";
  			        Statement st = conn.createStatement();
  			        ResultSet rs = st.executeQuery(query);
  			        ArrayList <String> TempNameHolder = new ArrayList <String>();
  			        while (rs.next()) {
  			            String FnameReturned = rs.getString("fname");
  			            String LnameReturned = rs.getString("lname");
  			            String PatchworkName = FnameReturned + "," + LnameReturned;
  			            TempNameHolder.add(PatchworkName);
  			        }
  			        
  			        
        		    String queryCust = "SELECT Fname,Lname FROM customer";
  			        Statement stCust = conn.createStatement();
  			        ResultSet rsCust = st.executeQuery(queryCust);
  			        ArrayList <String> TempCustHolder = new ArrayList <String>();
  			        while (rsCust.next()) {
  			            String FnameReturnedCust = rsCust.getString("Fname");
  			            String LnameReturnedCust = rsCust.getString("Lname");
  			            String PatchworkName = FnameReturnedCust + "," + LnameReturnedCust;
  			            TempCustHolder.add(PatchworkName);
  			        }
	  			    String [] ticketForEmployee = TempNameHolder.toArray(new String[TempNameHolder.size()]);
	  			    String [] ticketWithEmployee = TempNameHolder.toArray(new String[TempNameHolder.size() + 1]);
	  			    String [] ticketForCustomer = TempCustHolder.toArray(new String[TempCustHolder.size() + 1]);
	  			    ticketWithEmployee[ticketWithEmployee.length - 1] = "--NULL--";
	  			    ticketForEmployeeList = new JComboBox(ticketForEmployee);
	  			    WithEmployeeList = new JComboBox(ticketWithEmployee);
	  			    ForCustomerList = new JComboBox(ticketForCustomer);
	  			    WithEmployeeList.setSelectedIndex(ticketWithEmployee.length - 1);
	  			    MasterPanel.add(PassPanel);
	  			    MasterPanel.add(OptionsPanel);
        			PassFrame.setContentPane(MasterPanel);
        			PassFrame.pack();
        			PassPanel.add(label01);
        			PassPanel.add(WantToList);
        			PassPanel.add(label02);
            		PassPanel.add(ticketForEmployeeList);
            		PassPanel.add(label03);
            		PassPanel.add(WithEmployeeList);
            		PassPanel.add(label04);
            		PassPanel.add(ForCustomerList);
        			PassFrame.setVisible(true);
        			Okay.addActionListener(new ActionListener() {
            		    public void actionPerformed(ActionEvent f) {
            		    	String choice01 = (String) WantToList.getSelectedItem();
            		    	String choice02 = (String) ticketForEmployeeList.getSelectedItem();
            		    	String choice03 = (String) WithEmployeeList.getSelectedItem();
            		    	String choice04 = (String) ForCustomerList.getSelectedItem();
            		    	if (choice01.equals("Add")) {
            		    		try {
            		    		List<String> commaSplit = Arrays.asList(choice04.split(","));
            		    		String query001 = "SELECT TicketID FROM customer WHERE Fname=\"" + commaSplit.get(0) + "\" AND Lname = \"" + commaSplit.get(1) + "\";";
              			        Statement st001 = conn.createStatement();
              			        ResultSet rs001 = st001.executeQuery(query001);
              			        List<String> commaSplitEmployee = Arrays.asList(choice02.split(","));
              			        String RStoString = "null";
              			        while (rs001.next()) {
              			        	RStoString = rs001.getString("TicketID");
              			        }
              			        System.out.println(commaSplitEmployee.get(0));
              			        System.out.println(commaSplitEmployee.get(1));
              			        System.out.println(RStoString);
              			        String TheoryQuery = "select child.id from child left join parent on (child.parent_id=parent.id) where child.id is not null and parent.id is null;";
            		    		String query002 = "UPDATE employee SET ticket_assigned =\"" + RStoString + "\" WHERE fname=\"" + commaSplitEmployee.get(0) + "\" AND lname =\"" + commaSplitEmployee.get(1) + "\";";
              			        String query005 = "SET GLOBAL unique_checks = 0;";
            		    		String query003 = "SET GLOBAL FOREIGN_KEY_CHECKS=0;";
              			        String query004 = "SET GLOBAL FOREIGN_KEY_CHECKS=1;";
              			        String query006 = "SET GLOBAL unique_checks = 1;";
              			        System.out.println("QUERY003:__" + query003);
              			        System.out.println("QUERY002:__" + query002);
              			        System.out.println("QUERY004:__" + query004);
            		    		Statement st002;
            		    		Statement st003;
            		    		Statement st004;
            		    		Statement st005;
            		    		Statement st006;
            		    		
            		    		/*
            		    		 select child.id from child left join parent on (child.parent_id=parent.id) where child.id is not null and parent.id is null;
            		    		 */
            		    		
            		    		
            		    		/*
            		    		when changing ticket for employee A to person A where employee B has person A, it works just fine. 
								after that, switching works just fine.

								when changing ticket for employee A to person C where employee B has person B, it doesn't work.

            		    		 */
								try {
									st003 = conn.createStatement();
									st003.execute(query003);
									st003.close();
									st005 = conn.createStatement();
									st005.execute(query005);
									st005.close();
									st002 = conn.createStatement();
									st002.executeUpdate(query002);
									st002.close();
									st006 = conn.createStatement();
									st006.execute(query006);
									st006.close();
									st004 = conn.createStatement();
									st004.execute(query004);
									st004.close();
								} catch (SQLException e) {
									e.printStackTrace();
								}
            		    		}catch (SQLException e1) {
            		    			e1.printStackTrace();
            		    		}
              			        
            		    	}
            		    	if (choice01.equals("Drop")) {
            		    		//choice03 null
            		    	}
            		    	if (choice01.equals("Switch")) {
            		    		
            		    	}
            		    	if (choice01.equals("Resolve")) {
            		    		//choice03 null
            		    	}
            		    }            
            		});
        			Cancel.addActionListener(new ActionListener() {
            		    public void actionPerformed(ActionEvent f) {
            		    	PassFrame.dispose();
            		    }            
            		});
	    		}
	    		else {
	    			Console.setText("You need administrator permission to use this command");
	    		}	
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}	
    	}
		if (Input.equals("Check-Out") || Input.equals("Check-out") || Input.equals("check-out") || Input.equals("checkout") || Input.equals("CheckOut")) {
			if (loginStatus == 1) {
				
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
		if (Input.equals("current") || Input.equals("Current")) {
			if (loginName.equals("--null--")) {
				Console.setText("Nobody is currently logged in");
			}
			else {
				Console.setText(loginName + " is currently logged in.");
			}
    	}
		if (Input.equals("cycle") || Input.equals("Cycle")) {
			if (loginStatus == 1) {
				if (AdminAccess == 1) {
	    			//normal command
	    		}
	    		else {
	    			Console.setText("You need administrator permission to use this command");
	    		}
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
		if (Input.equals("Device") || Input.equals("device") || Input.equals("Dev") || Input.equals("dev")) {
			if (loginStatus == 1) {
				
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
		if (Input.equals("displayQueue") || Input.equals("displayqueue") || Input.equals("DisplayQueue") || Input.equals("Displayqueue") || Input.equals("Queue") || Input.equals("queue") || Input.equals("ShowQueue")) {
			if (loginStatus == 1) {
				RealtimeMasterClock();
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
		if (Input.equals("easyModify") || Input.equals("easymodify") || Input.equals("EasyModify") || Input.equals("Easymodify")) {
			if (loginStatus == 1) {
				if (AdminAccess == 1) {
	    			//normal command
	    		}
	    		else {
	    			Console.setText("You need administrator permission to use this command");
	    		}
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("Help") || Input.equals("help")) {
    		Console.setText("See log for help options");
    		Log.append("-----------------------------------------------" + "\n");
    		Log.append("\"about\" to display information about this program" + "\n");
    		Log.append("\"admin\" to enter admin password for current user for higher level permissions." + "\n");
    		Log.append("\"changeTicket\" to change an employee's assigned ticket" + "\n");  /*this will NOT change the value of the ticket, but allocate 
			 																				  it somewhere else.*/
    		Log.append("\"check-out\" to check a customer out of the queue" + "\n");
    		Log.append("\"current\" to see who's currently logged in." + "\n");
    		Log.append("\"cycle\" to manually push the queue forward by 1" + "\n");		/*might need to integrate this with another 
    																						  programming language somehow.*/
    		Log.append("\"device\" to bring up the device panel" + "\n");					//separate panel just like Modify
    		Log.append("\"displayQueue\" to display a real-time panel of the current queue." + "\n");         /*will be difficult to to implement and 
																												program. my initial thoughts about how I 
																												will make this: run a thread.wait command 
																												every maybe 5-10 seconds that will 
																												pull all data, cell by cell, from the 
																												database. If it determines something is 
																												different/has changed since last update, 
																												it'll re-paint the panel and basically
																												update it to display the most recent
																												information.*/
    		Log.append("\"easyModify\" to directly change a value in the database without SQL" + "\n"); 	  //translating "dumb" input into SQL commands.
    		Log.append("\"help\" for a list of commands" + "\n");
    		Log.append("\"history\" to bring up the system's history panel" + "\n");		/*logs every command made, by who it was made, and the time 
    																						  at which it was executed (whenever somebody hits "enter" 
    																						  in the console")*/
    		Log.append("\"login\" to log into the system (required for system use)." + "\n");
    		Log.append("\"logout\" to log out of the system (required for system use)." + "\n");
    		Log.append("\"manualEnter\" to manually enter a full customer entry into the database." + "\n");  /*this command would be best used AFTER 
																												displayQueue. Also note that displayQueue 
																												should remain open until manually closed and 
																												be updated real-time like the display GUI 
																												in the other part of the project.*/
    		Log.append("\"modify\" to directly change a value in the SQL database" + "\n"); /*bring up separate window where the SQL command in its 
    																						  entirety is typed.*/
    		Log.append("\"passwords\" to bring up a table of today's employee passwords" + "\n");
    		Log.append("\"reason\" to find a customer's visit reason." + "\n");
    		Log.append("\"remove\" to completely remove a user from the database (highly unadvised)" + "\n"); //delete their entire row in the database.
    		Log.append("\"search\" to search and display an entry in the database" + "\n"); /*result displayed in log. Have user specify the customer's 
    																						  name and/or phone number*/
    		Log.append("\"text\" to text a user's cell phone number" + "\n");  				/*error handling for if the user's phone number is left empty. 
    																						  Allow user to put in custom message to be delivered to the
    																						  customer/client.*/
    		
    		Log.append("-----------------------------------------------" + "\n");
    		/* Smart idea for handling "dumb" input:
    		 * Small panel with a few things:
    		 * 		1) drop down menu 
    		 * 		2) the WHERE entries. CAN HAVE MULTIPLE WHERE ENTRIES.
    		 * 
    		 * I estimate that the most used conditions will be fname and phone number. phone number should be adaptable to both:
    		 * 		    314-724-3639
    		 * 				AND
    		 * 			3147243639
    		 */
    	}
    	if (Input.equals("History") || Input.equals("history") || Input.equals("Hist") || Input.equals("hist")) {
    		if (loginStatus == 1) {
    			if (AdminAccess == 1) {
        			//normal command
        		}
        		else {
        			Console.setText("You need administrator permission to use this command");
        		}
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("login") || Input.equals("Login")) {
    		  int loginIterator = 0;
    		  JTextField FNameField = new JTextField(25);
    		  JTextField LNameField = new JTextField(25);
    	      JPasswordField PassField = new JPasswordField(5);

    	      String checkAgainstFname = null;
    	      String checkAgainstLname = null;
    	      
    	      JPanel myPanel = new JPanel();
    	      myPanel.add(new JLabel("First Name: "));
    	      myPanel.add(FNameField);
    	      myPanel.add(Box.createHorizontalStrut(10));
    	      myPanel.add(new JLabel("Last Name: "));
    	      myPanel.add(LNameField);
    	      myPanel.add(Box.createHorizontalStrut(10)); // a spacer
    	      myPanel.add(new JLabel("Password: "));
    	      myPanel.add(PassField);

    	      int result = JOptionPane.showConfirmDialog(null, myPanel, 
    	               "Please enter your login information", JOptionPane.OK_CANCEL_OPTION);
    	      if (result == JOptionPane.OK_OPTION) {
    	         System.out.println("Fname: " + FNameField.getText());
    	         System.out.println("Lname: " + LNameField.getText());
    	         System.out.println("Password: " + PassField.getText());
    	         checkAgainstFname = FNameField.getText();
    	         checkAgainstLname = LNameField.getText();
    	      }
    	      
    	      try {
			      String query = "SELECT fname,lname FROM employee";
			      Statement st = conn.createStatement();
			      ResultSet rs = st.executeQuery(query);
			      while (rs.next()) {
			          String FnameReturned = rs.getString("fname");
			          String LnameReturned = rs.getString("lname");
			          System.out.print("fname:  " + FnameReturned + "      lname: " + LnameReturned + "\n");
			          if (FnameReturned.equals(checkAgainstFname) && LnameReturned.equals(checkAgainstLname)) {
			        	  break; // match found!
			          }
			          loginIterator++;
			      }
			      st.close();
			    }
			    catch (Exception q) {
			      System.err.println("Got an exception! ");
			      System.err.println(q.getMessage());
			    }
    	      String PassFromGenerator = Passwords.get(loginIterator);
    	      if (PassFromGenerator.equals(PassField.getText())) {
    	    	  System.out.println("Login success! Welcome back, " + FNameField.getText() + "!");
    	    	  loginStatus = 1;
    	    	  loginName = FNameField.getText();
    	    	  //login success!
    	      }
    	      else {
    	    	  System.out.println("Login failed. Your password or username is incorrect.");
    	    	  //send error message to user. Maybe put a "3 failures lockout. contact system manager/admin" lock feature in here for security reasons?
    	      }
    	}
    	if (Input.equals("logout") || Input.equals("Logout")) {
    		if (loginStatus == 0) {
    			JOptionPane.showMessageDialog(null,"There is currently nobody logged in.");
    		}
    		else {
    			JOptionPane.showMessageDialog(null,loginName + " has been logged out.");
        		loginName = "--null--";
        		loginPassword = "";
        		AdminAccess = 0;
        		loginStatus = 0;
    		}
    	}
    	if (Input.equals("manualEnter") || Input.equals("manualenter") || Input.equals("ManualEnter") || Input.equals("Manualenter") || Input.equals("manuelEnter") || Input.equals("menualEnter")) {
    		if (loginStatus == 1) {
    			if (AdminAccess == 1) {
    				JFrame ManualFrame = new JFrame("SQL Entry");
    				JPanel ManualPanel = new JPanel(new GridLayout(2,1));
    				JPanel ManualOptions = new JPanel(new GridLayout(1,2));
    				JScrollPane ManualScrollPane;
    				JTextArea ManualArea = new JTextArea(10, 5);
    			
    				JButton Submit = new JButton("Submit");
    				JButton Cancel = new JButton("Cancel");
    				
    				ManualFrame.setContentPane(ManualPanel);
    				ManualFrame.pack();
    				ManualPanel.add(ManualArea);
    				ManualOptions.add(Submit);
    				ManualOptions.add(Cancel);
    				ManualScrollPane = new JScrollPane(ManualArea);
    				ManualPanel.setPreferredSize(new Dimension(900, 900));
    				ManualPanel.add(ManualScrollPane, BorderLayout.CENTER);
    				ManualPanel.add(ManualOptions);
    				ManualFrame.setVisible(true);
    				
    				
    				Submit.addActionListener(new ActionListener() {
            		    public void actionPerformed(ActionEvent f) {
            		    	try {
            				      String query = ManualArea.getText();
            				      Statement st = conn.createStatement();
            				      ResultSet rs = st.executeQuery(query);
            				      while (rs.next()) {
            				          String FnameReturned = rs.getString("fname");
            				      }
            				      st.close();
            				    }
            				    catch (Exception q) {
            				      System.err.println("Got an exception! ");
            				      System.err.println(q.getMessage());
            				    }
            		    }            
            		});
    				Cancel.addActionListener(new ActionListener() {
            		    public void actionPerformed(ActionEvent f) {
            		    	
            		    }            
            		});
        			//normal command
        		}
        		else {
        			Console.setText("You need administrator permission to use this command");
        		}
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("modify") || Input.equals("Modify")) {
    		if (loginStatus == 1) {
    			if (AdminAccess == 1) {
        			//normal command
        		}
        		else {
        			Console.setText("You need administrator permission to use this command");
        		}
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("passwords") || Input.equals("Passwords") || Input.equals("password") || Input.equals("Password")) {
    		if (loginStatus == 1) {
    			if (AdminAccess == 1) {
        			JFrame PassFrame = new JFrame("Password Table");//myFrame
        			JPanel PassPanel = new JPanel(new GridLayout(1,1));				//panel
        			JScrollPane PassPane;							//pane
        			JTextArea PassScroller = new JTextArea(10, 5);	//log

        			PassFrame.setContentPane(PassPanel);
        			PassFrame.pack();
        			PassPanel.add(PassScroller);
        	    	PassPane = new JScrollPane(PassScroller);
        			PassPane.setPreferredSize(new Dimension(400, 400));
        			PassFrame.add(PassPane, BorderLayout.CENTER);
        			PassFrame.setVisible(true);
        			int i = 0;
        			String query = "SELECT fname,lname FROM employee";
				    Statement statement = conn.createStatement();
				    ResultSet resultset = statement.executeQuery(query);
				    while (resultset.next()) {
				    	PassScroller.append("First: " + resultset.getString("fname") + "     Last: " + resultset.getString("lname") + "     Password: " + Passwords.get(i) + "\n");
				        i++;
				    }
				      statement.close();
        		}
        		else {
        			Console.setText("You need administrator permission to use this command");
        		}
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("reason") || Input.equals("Reason") || Input.equals("visitReason") || Input.equals("visitreason") || Input.equals("VisitReason")) {
    		if (loginStatus == 1) {
				
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("remove") || Input.equals("Remove")) {
    		if (loginStatus == 1) {
    			if (AdminAccess == 1) {
        			//normal command
        		}
        		else {
        			Console.setText("You need administrator permission to use this command");
        		}
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("search") || Input.equals("Search")) {
    		if (loginStatus == 1) {
    			/*JFrame PassFrame = new JFrame("Password Table");//myFrame
    			JPanel PassPanel = new JPanel(new GridLayout(1,1));				//panel
    			JScrollPane PassPane;							//pane
    			JTextArea PassScroller = new JTextArea(10, 5);	//log

    			PassFrame.setContentPane(PassPanel);
    			PassFrame.pack();
    			PassPanel.add(PassScroller);
    	    	PassPane = new JScrollPane(PassScroller);
    			PassPane.setPreferredSize(new Dimension(400, 400));
    			PassFrame.add(PassPane, BorderLayout.CENTER);
    			PassFrame.setVisible(true);
    			int i = 0;
    			String query = "SELECT fname,lname FROM employee";
			    Statement statement = conn.createStatement();
			    ResultSet resultset = statement.executeQuery(query);
			    while (resultset.next()) {
			    	PassScroller.append("First: " + resultset.getString("fname") + "     Last: " + resultset.getString("lname") + "     Password: " + Passwords.get(i) + "\n");
			        i++;
			    }
			      statement.close();*/
    			
    			ArrayList <String> TableNames = new ArrayList <String>();
    			ArrayList <String> FindNames = new ArrayList <String>();
    			ArrayList <String> WhereNames = new ArrayList <String>();
    			int numOfTables = 0;
    			DatabaseMetaData md = conn.getMetaData();
    			ResultSet rs = md.getTables(null, null, "%", null);
    			while (rs.next()) {
    			 TableNames.add(rs.getString(3));
    			 numOfTables++;
    			}
    			JFrame SearchFrame = new JFrame("Search Database");
    			JPanel SearchPanel = new JPanel(new GridLayout(4,2));
    			JPanel MasterPanel = new JPanel(new GridLayout(2,1));
    			JPanel OptionsPanel = new JPanel(new GridLayout(1,2));
    			JButton Submit = new JButton("Submit");
    			JButton Cancel = new JButton("Cancel");
    			OptionsPanel.add(Submit);
    			OptionsPanel.add(Cancel);
        		JLabel label01 = new JLabel("FROM table ");
        		JLabel label02 = new JLabel("FIND ");
        		JLabel label03 = new JLabel("WHERE ");
        		JLabel label04 = new JLabel("is ");
        		JTextField isInput = new JTextField();
    			JComboBox TableNamesList = new JComboBox(TableNames.toArray());
    			
    			try { //NEED A CASE FOR employee fname and lname and CUSTOMER fname and lname
    				int r = 0;
    				while (r<numOfTables) {
			        	String SearchQuery01 = "SELECT * FROM " + TableNames.get(r);
					    Statement st = conn.createStatement();
					    ResultSet resS = st.executeQuery(SearchQuery01);
					    ResultSetMetaData meta = resS.getMetaData(); 
		    		      Integer columncount = meta.getColumnCount();
		    		      int count = 1 ;
		    		      while(count<=columncount){
		    		         FindNames.add(meta.getColumnLabel(count));
		    		         WhereNames.add(meta.getColumnLabel(count));
		    		         count++;
		    		      }
		    		      r++;
    				}
		        	} catch (SQLException e) {
						e.printStackTrace();
					}
    			JComboBox FindList= new JComboBox(FindNames.toArray());
    			JComboBox WhereList= new JComboBox(FindNames.toArray());
    			MasterPanel.add(SearchPanel);
    			SearchPanel.add(label01);
    			SearchPanel.add(TableNamesList);
    			SearchPanel.add(label02);
    			SearchPanel.add(FindList);
    			SearchPanel.add(label03);
    			SearchPanel.add(WhereList);
    			SearchPanel.add(label04);
    			SearchPanel.add(isInput);
    			MasterPanel.add(OptionsPanel);
    		    SearchFrame.setContentPane(MasterPanel);
    		    SearchFrame.setSize(500,500);
    		    SearchFrame.setVisible(true);
    		    Submit.addActionListener(new ActionListener() {
        		    public void actionPerformed(ActionEvent f) {
        		    	String SearchQuery01 = "SELECT " + FindList.getSelectedItem() + " FROM " + TableNamesList.getSelectedItem()+ " WHERE " + WhereList.getSelectedItem() + " = \"" + isInput.getText() + "\";";
        			    Statement st;
        			    System.out.println(SearchQuery01);
						try {
							st = conn.createStatement();
							ResultSet ReturnSet = st.executeQuery(SearchQuery01);
							JFrame SearchTempFrame = new JFrame("Results");
			    			JPanel SearchTempPanel = new JPanel(new GridLayout(1,1));
			    			JScrollPane SearchTempPane;
			    			JTextArea SearchTempScroller = new JTextArea(10, 5);
			    			SearchTempFrame.setContentPane(SearchTempPanel);
			    			SearchTempFrame.pack();
			    			SearchTempPanel.add(SearchTempScroller);
			    			SearchTempPane = new JScrollPane(SearchTempScroller);
			    			SearchTempPane.setPreferredSize(new Dimension(400, 400));
			    			SearchTempFrame.add(SearchTempPane, BorderLayout.CENTER);
			    			SearchTempFrame.setVisible(true);
			    			ResultSetMetaData rsmd = ReturnSet.getMetaData();
					    	int columnsNumber = rsmd.getColumnCount();
						    	while (ReturnSet.next()) {
						    	    for (int i = 1; i <= columnsNumber; i++) {
						    	        if (i > 1) System.out.print(",  ");
						    	        String columnValue = ReturnSet.getString(i);
						    	        System.out.println(columnValue);
						    	        SearchTempScroller.append(columnValue);
						    	    }
						    	    SearchTempScroller.append("\n");
						    	}	
						} catch (SQLException e) {
							e.printStackTrace();
						}
        			   
        		    }            
        		});
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	if (Input.equals("text") || Input.equals("Text")) {
    		if (loginStatus == 1) {
    			JFrame TextFrame = new JFrame("Text a Customer");
    			JPanel MasterTextPanel = new JPanel(new GridLayout(2,1));
				JPanel TextPanel = new JPanel(new GridLayout(2,1));
				JPanel TextOptions = new JPanel(new GridLayout(1,2));
				JScrollPane TextScrollPane;
				
				JPanel PassPanel = new JPanel(new GridLayout(4,2));
    			JPanel MasterPanel = new JPanel(new GridLayout(2,1));
    			JPanel OptionsPanel = new JPanel(new GridLayout(1,2));
				JTextArea TextArea = new JTextArea(10, 5);
			
				JButton Submit = new JButton("Submit");
				JButton Cancel = new JButton("Cancel");
				
				TextFrame.setContentPane(TextPanel);
				TextFrame.pack();
				JComboBox DropdownForCustomers;
				String queryText = "SELECT Fname,Lname FROM customer";
			        Statement stText = conn.createStatement();
			        ResultSet rsText = stText.executeQuery(queryText);
			        ArrayList <String> TempTextHolder = new ArrayList <String>();
			        while (rsText.next()) {
			            String FnameReturnedText = rsText.getString("Fname");
			            String LnameReturnedText = rsText.getString("Lname");
			            String PatchworkName = FnameReturnedText + "," + LnameReturnedText;
			            TempTextHolder.add(PatchworkName);
			        }
			    String [] CustomerArrayConversion = TempTextHolder.toArray(new String[TempTextHolder.size()]);
			    DropdownForCustomers = new JComboBox(CustomerArrayConversion);
			    MasterTextPanel.add(DropdownForCustomers);
				TextOptions.add(Submit);
				TextOptions.add(Cancel);
				TextScrollPane = new JScrollPane(TextArea);
				TextPanel.setPreferredSize(new Dimension(900, 900));
				MasterTextPanel.add(TextScrollPane, BorderLayout.CENTER);
				TextPanel.add(MasterTextPanel);
				TextPanel.add(TextOptions);
				TextFrame.setVisible(true);
				
				
				Submit.addActionListener(new ActionListener() {
        		    public void actionPerformed(ActionEvent f) {
        		    	try {
        		    		  String CustomerNameChoice = (String) DropdownForCustomers.getSelectedItem();
        		    		  List<String> commaSplit = Arrays.asList(CustomerNameChoice.split(","));
        				      String query = "SELECT CellNum FROM customer WHERE Fname =\"" + commaSplit.get(0) + "\" AND Lname =\"" + commaSplit.get(1) + "\";"/*TextArea.getText()*/;
        				      Statement st = conn.createStatement();
        				      System.out.println("QUERY: " + query);
        				      ResultSet rs = st.executeQuery(query);
        				      while (rs.next()) {
        				          String CellReturned = rs.getString("CellNum");
        				          System.out.println("CELL RETURNED: " + CellReturned);
        				          Message message = Message.creator(new PhoneNumber/*To:*/("+1" + CellReturned),new PhoneNumber/*From:*/("+13142309301"),/*Message:*/TextArea.getText()).create();
        				          System.out.println(message.getSid());
        				      }
        				      st.close();
        				    }
        				    catch (Exception q) {
        				      System.err.println("Got an exception! ");
        				      System.err.println(q.getMessage());
        				    }
        		    }            
        		});
				Cancel.addActionListener(new ActionListener() {
        		    public void actionPerformed(ActionEvent f) {
        		    	
        		    }            
        		});
    		}
    		else {
    			Console.setText("You need to be logged in to use this command.");
    		}
    	}
    	/* Below all of the normal commands should be error handling for mis-spelling.
    	 * like "if ((Input.first3_letters.equals("mod") || Input.first3_letters.equals("mud")) && input.invalid == 1 && path != PathForModifyNUMBER) {
    	 * 			console.setText("did you mean "modify"?");
    	 * 		}"
    	 * 
    	 */
    }
	
	public static void SQLhandler(/*input & arguments*/) {
		System.out.println("SQL with search reached.");
		
		/* SQL JDBC stuff:
		 * -----------------------------------------------------------------------------------------------
		 * Download: 	https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.44.tar.gz
		 * Setup help:  http://www.javahelps.com/2015/08/add-mysql-jdbc-driver-to-eclipse.html
		 * -----------------------------------------------------------------------------------------------
		 */
		
	}
	public static void GUItree () { //handles all input from console to determine what GUI to display and all the events for said GUIs
		
	}
	public static void LoginStart() {
		int loginIterator = 0;
		  JTextField FNameField = new JTextField(25);
		  JTextField LNameField = new JTextField(25);
	      JPasswordField PassField = new JPasswordField(5);

	      String checkAgainstFname = null;
	      String checkAgainstLname = null;
	      
	      JPanel myPanel = new JPanel();
	      myPanel.add(new JLabel("First Name: "));
	      myPanel.add(FNameField);
	      myPanel.add(Box.createHorizontalStrut(10));
	      myPanel.add(new JLabel("Last Name: "));
	      myPanel.add(LNameField);
	      myPanel.add(Box.createHorizontalStrut(10)); // a spacer
	      myPanel.add(new JLabel("Password: "));
	      myPanel.add(PassField);

	      int result = JOptionPane.showConfirmDialog(null, myPanel,"Please enter your login information", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	         System.out.println("Fname: " + FNameField.getText());
	         System.out.println("Lname: " + LNameField.getText());
	         System.out.println("Password: " + PassField.getText());
	         checkAgainstFname = FNameField.getText();
	         checkAgainstLname = LNameField.getText();
	      }
	      
	      try {
	    	  if (checkAgainstFname.equals("") || checkAgainstLname.equals("")) {
	    		  //user entered nothing to try and crash system.
	    	  }
	    	  else {
			      String query = "SELECT fname,lname FROM employee";
			      Statement st = conn.createStatement();
			      ResultSet rs = st.executeQuery(query);
			      while (rs.next()) {
			          String FnameReturned = rs.getString("fname");
			          String LnameReturned = rs.getString("lname");
			          System.out.print("fname:  " + FnameReturned + "      lname: " + LnameReturned + "\n");
			          if (FnameReturned.equals(checkAgainstFname) && LnameReturned.equals(checkAgainstLname)) {
			        	  break; // match found!
			          }
			          loginIterator++;
			      }
			      System.out.println("Row Position info is located in: " + loginIterator);
			      st.close();
	    	  }
		    }
		    catch (Exception q) {
		      System.err.println("Got an exception! ");
		      System.err.println(q.getMessage());
		    }
	      String PassFromGenerator = Passwords.get(loginIterator);
	      if (PassFromGenerator.equals(PassField.getText())) {
	    	  System.out.println("Login success! Welcome back, " + FNameField.getText() + "!");
	    	  loginStatus = 1;
	    	  loginName = FNameField.getText();
	    	  //login success!
	      }
	      else {
	    	  System.out.println("Login failed. Your password or username is incorrect.");
	    	  loginStatus = 0;
	    	  //send error message to user. Maybe put a "3 failures lockout. contact system manager/admin" lock feature in here for security reasons?
	      }
	}
	public static String GetTimestamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
	public static void RealtimeMasterClock() throws SQLException {
		JFrame QueueFrame = new JFrame("Customer Queue");
		JPanel QueuePanel = new JPanel(new GridLayout(1,1));				//panel
		JScrollPane QueuePane;
		JTextArea QueueScroller = new JTextArea(10, 5);

		QueueFrame.setContentPane(QueuePanel);
		QueueFrame.pack();
		QueuePanel.add(QueueScroller);
		QueuePane = new JScrollPane(QueueScroller);
		QueuePane.setPreferredSize(new Dimension(400, 400));
		QueueFrame.add(QueuePane, BorderLayout.CENTER);
		QueueFrame.setVisible(true);
		TimerTask timerTask = new RealtimeQueueManager(timer,QueueScroller,MasterTimerCounter,conn); //CLASS TO EXECUTE
        timer = new Timer(false);
        timer.scheduleAtFixedRate(timerTask, 0, 9000);
	}
	
}