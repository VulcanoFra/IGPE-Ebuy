package application.net.server;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainServer {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Server");
		frame.setSize(300,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JPanel().add(new JLabel("SERVER")));
		Server server = new Server();
		
		if(server.startServer()) {
			JOptionPane.showMessageDialog(null, "server has correctly started", "OK", JOptionPane.INFORMATION_MESSAGE);
			frame.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(null, "server has not started", "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
}
