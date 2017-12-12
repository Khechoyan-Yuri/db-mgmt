import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RealtimeQueueManager extends TimerTask {

	static Timer timerGlobal;
	static JTextArea QueueScrollerGlobal;
	static int MasterTimerCounterGlobal;
	static Connection connGlobal;
	ArrayList <Integer> Position = new ArrayList<Integer>();
	ArrayList <String> OriginalQueue = new ArrayList<String>();
	
	public RealtimeQueueManager(Timer timer, JTextArea QueueScroller, int masterTimerCounter, Connection conn) throws SQLException { //i need timer, passScroller, masterTimerCounter, and conn.
		timerGlobal = timer;
		QueueScrollerGlobal = QueueScroller;
		MasterTimerCounterGlobal = masterTimerCounter;
		connGlobal = conn;
		String query = "SELECT Fname,Lname FROM customer";
	    Statement statement = connGlobal.createStatement();
	    ResultSet resultset = statement.executeQuery(query);
	    int i = 0;
	    while (resultset.next()) {
	    		String first = resultset.getString("Fname");
	    		String last = resultset.getString("Lname");
	    		String Combo = first + " " + last;
	    		OriginalQueue.add(Combo);
	    		Position.add(i);
		    	i++;
	    }
	    statement.close();
	}

	@Override
	public void run() {
		try {
			QueueScrollerGlobal.setText("");
			QueueScrollerGlobal.setText("Position \t First/Last \n");
			String query = "SELECT Fname,Lname FROM customer";
		    Statement statement = connGlobal.createStatement();
		    ResultSet resultset = statement.executeQuery(query);
		    int i = 0;
		    while (resultset.next()) {
		    	String first = resultset.getString("Fname");
	    		String last = resultset.getString("Lname");
	    		String Combo = first + " " + last;
	    		if (OriginalQueue.contains(Combo)) {
	    			QueueScrollerGlobal.append(Position.get(i).toString() + "\t " + OriginalQueue.get(i)+" \n");
	    		}
	    		else {
	    			OriginalQueue.add(Combo);
	    			Position.add((Position.size()));
	    		}
		    	i++;
		    }
		    MasterTimerCounterGlobal--;
		    if (MasterTimerCounterGlobal == 0) {
		    	timerGlobal.cancel();
		    }
		    statement.close();
		} catch (SQLException e) {
			QueueScrollerGlobal.append(e.getMessage());
		}
	}
}