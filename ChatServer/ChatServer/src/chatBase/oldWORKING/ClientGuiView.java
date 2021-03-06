package chatBase.oldWORKING;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


class ClientGuiView {

	// Frame Main
	JFrame frameMain;
	ClientGuiActionListeners clientguiAL;
	
	// Chat
	JTextArea memoVerlauf = new JTextArea();
	JTextArea memoChat = new JTextArea();
	JButton buttonSenden = new JButton("Senden");
	Client client;
	
	// Config
	JTextField editServerIP = new JTextField();
	JTextField editServerPort = new JTextField();
	JTextField editClientNickname = new JTextField();
	
	// Login
	JButton buttonLogin = new JButton("Anmeldung am Server");
	JButton buttonLogout = new JButton("Abmeldung vom Server");
    boolean istVerbunden = false;
	
	// Kontakte
	JList<String> listKontakte = new JList<String>();
	
	// Werbung (bisl Geld verdienen :-) )
	JTextArea memoWerbung = new JTextArea();
	
	// MenuBar
	 JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuEinstellungen = new JMenu("Einstellungen");
		menuBar.add(menuEinstellungen);
		JMenuItem menuItemEinstellungenSichern = new JMenuItem("Sichern");
		menuEinstellungen.add(menuItemEinstellungenSichern);
		JMenuItem menuItemEinstellungenLaden = new JMenuItem("Laden");
		menuEinstellungen.add(menuItemEinstellungenLaden);
		return menuBar;
	}
	 
	 void setFixSizeOfComponent(JComponent comp, int width, int height){
		 Dimension d = new Dimension(width, height);
		 comp.setSize(d);
		 comp.setMinimumSize(d);
		 comp.setMaximumSize(d);
		 comp.setPreferredSize(d);
	 }
	 
	 JPanel createChatPanel() {
		 JPanel panel = new JPanel();
		 panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		 setFixSizeOfComponent(panel, 500, 400);
		 
		 JPanel wrapPanel4Chat = new JPanel();
		 wrapPanel4Chat.setLayout(new BoxLayout(wrapPanel4Chat, BoxLayout.X_AXIS));
		 wrapPanel4Chat.add(new JScrollPane(memoChat));
		 setFixSizeOfComponent(memoChat, 400, 100);
		 wrapPanel4Chat.add(buttonSenden);
		 setFixSizeOfComponent(buttonSenden,100,100);
		 buttonSenden.addActionListener(clientguiAL.actionListenerButtonSenden);
		 
		 panel.add(new JScrollPane(memoVerlauf));
		 memoVerlauf.setEditable(false);
		 setFixSizeOfComponent(memoVerlauf, 500, 300);
		 panel.add(new JSeparator(SwingConstants.HORIZONTAL));
		 panel.add(wrapPanel4Chat);
		 setFixSizeOfComponent(wrapPanel4Chat,500,100);
		 
		 return panel;
	 }
	 
	 JPanel createConfigPanel(){
		 JPanel panelOuter = new JPanel();
		 panelOuter.setLayout(new BoxLayout(panelOuter, BoxLayout.X_AXIS));
		 setFixSizeOfComponent(panelOuter, 200, 400);
		 
		 JPanel panelInner = new JPanel();
		 panelInner.setLayout(new BoxLayout(panelInner, BoxLayout.Y_AXIS));
		 setFixSizeOfComponent(panelInner, 198, 398);
		 
		 JPanel panelEinstellungen = new JPanel();
		 panelEinstellungen.setLayout(new BoxLayout(panelEinstellungen, BoxLayout.Y_AXIS));
		 setFixSizeOfComponent(panelEinstellungen, 196, 160);
		 
		 
		 panelEinstellungen.add(new JLabel("Server IP:"));
		 panelEinstellungen.add(editServerIP);
		 panelEinstellungen.add(new JLabel("Server Port:"));
		 panelEinstellungen.add(editServerPort);
		 panelEinstellungen.add(new JLabel("Client Nickname:"));
		 panelEinstellungen.add(editClientNickname);
		 panelEinstellungen.add(Box.createRigidArea(new Dimension(0,2))); //bisl Abstand
		 panelEinstellungen.add(buttonLogin);
		 buttonLogin.addActionListener(clientguiAL.actionListenerButtonLogin);
		 setFixSizeOfComponent(buttonLogin, 194, 26);
		 panelEinstellungen.add(Box.createRigidArea(new Dimension(0,2))); //bisl Abstand
		 panelEinstellungen.add(buttonLogout);
		 buttonLogout.addActionListener(clientguiAL.actionListenerButtonLogout);
		 setFixSizeOfComponent(buttonLogout, 194, 26);
		 buttonLogout.setEnabled(false);
		 
		 
		 JPanel panelKontakte = new JPanel();
		 panelKontakte.setLayout(new BoxLayout(panelKontakte, BoxLayout.Y_AXIS));
		 setFixSizeOfComponent(panelKontakte, 196, 196);
		 panelKontakte.add(new JLabel("gerade online:"));
		 panelKontakte.add(new JScrollPane(listKontakte));
		 
		 panelInner.add(panelEinstellungen);
		 panelInner.add(Box.createRigidArea(new Dimension(0,40))); //bisl Abstand
		 panelInner.add(panelKontakte);
		 
		 panelOuter.add(panelInner);
		 panelOuter.add(new JSeparator(SwingConstants.VERTICAL));
		 return panelOuter;
	 }

	 JPanel createAdvertPanel(){
		 JPanel panel = new JPanel();
		 panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		 setFixSizeOfComponent(panel, 700, 100);
		 panel.add(memoWerbung);	 
		 memoWerbung.setEditable(false);
		 return panel;
	 }
	 
	// Frame Main
	 void createFrame() {
		frameMain = new JFrame("Chat-App pre-alpha");
		frameMain.setLayout(new BorderLayout());
		
		
		frameMain.setResizable(false);
		
		setStandard();
		
		
		// try {
			//sp�ter: frameMain.setIconImage(ImageIO.read(getClass().getResource("./resources/nestIcon.png")));
		// } catch (IOException e) {
			// shouldn't happen.
			// If this happens: No icon, which isn't a real problem
		//	e.printStackTrace();
		// }
		//frameMain.setSize(800, 600);
		//frameMain.setMaximumSize(new Dimension(800, 600));
		//frameMain.setMinimumSize(new Dimension(800, 600));
		// frame.setResizable(false);
		frameMain.setLocationRelativeTo(null);
		frameMain.setLocation(100, 50);
		// SwingUtilities.updateComponentTreeUI(frame);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frameMain.setJMenuBar(createMenuBar());
		frameMain.add(createConfigPanel(), BorderLayout.WEST);
		//frameMain.add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.CENTER);
		frameMain.add(createChatPanel(), BorderLayout.CENTER);
		frameMain.add(createAdvertPanel(), BorderLayout.SOUTH);

		frameMain.pack();
	}
	 
	public ClientGuiView() {
		super();
		clientguiAL = new ClientGuiActionListeners(this);
		createFrame();
	}

	// GUI-methodes
	public void setVisible(boolean visible) {
		frameMain.setVisible(visible);
	}

	public void dispose() {
		frameMain.dispose();
	}
	
	public void appendMemoVerlauf(String str) {
		memoVerlauf.append(str);
		memoVerlauf.setCaretPosition(memoVerlauf.getText().length() - 1);
	}
	
	public void verbindungsfehler() {
		buttonLogin.setEnabled(true);
		buttonLogout.setEnabled(false);
		setStandard();
		editServerIP.setEditable(true); //sab
		editServerPort.setEditable(true);	//sab
		
		//for (ActionListener al : buttonSenden.getActionListeners()){
		//	buttonSenden.removeActionListener(al);
		//}  //sab nicht n�tig, �ber istverbunden geregelt
		
		istVerbunden = false;
	}
	
	public void setStandard(){
		// sp�ter durch autoload von einstellungen ersetzen (Feature 3)
		editServerIP.setText("127.0.0.1");
		editServerPort.setText("1500");
		editClientNickname.setText("Unbekannt");
	}

}
