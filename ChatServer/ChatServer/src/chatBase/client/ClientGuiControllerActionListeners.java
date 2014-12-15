package chatBase.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chatBase.model.ChatMessage;
import chatBase.model.ChatMessageMessage;

public class ClientGuiControllerActionListeners {
	
	private ClientGuiViewSwing gui;
	public ActionListener actionListenerButtonLogout;
	public ActionListener actionListenerButtonLogin;
	public ActionListener actionListenerButtonSenden;
	public ActionListener actionListenerEinstellungenSichern;
	public ActionListener actionListenerEinstellungenLaden;
	
	public ClientGuiControllerActionListeners(ClientGuiViewSwing clientGuiView) {
		gui = clientGuiView;
	

	// Control Actionlisterners
		
	actionListenerEinstellungenSichern = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			File file =new File("config.cfg");
			 
    	    //if file doesnt exists, then create it
    	    if(!file.exists()){
    	    	try {
					file.createNewFile();
				} catch (IOException e) {
					// do nothing
					e.printStackTrace();
				}
    	    }
    	    
    	    try {
        	    List<String> aLines = new ArrayList<String>();
        	    aLines.add(gui.editClientNickname.getText().trim());
        	    aLines.add(gui.editServerIP.getText().trim());
        	    aLines.add(gui.editServerPort.getText().trim());
				gui.client.writeSmallTextFile(aLines, "config.cfg");
			} catch (IOException e) {
				// do nothing
				e.printStackTrace();
			};
			
		}
	};
	
	actionListenerEinstellungenLaden = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			File file =new File("config.cfg");
			 
    	    //if file doesnt exists, then create it
    	    if(!file.exists()){
    	    	try {
					file.createNewFile();
				} catch (IOException e) {
					// do nothing
					e.printStackTrace();
				}
    	    }
    	    
    	    List<String> aLines = new ArrayList<String>();
    	    
    	    try {
				aLines = (List<String>) gui.client.readSmallTextFile("config.cfg");
				gui.editClientNickname.setText(aLines.get(0));
				gui.editServerIP.setText(aLines.get(1));
				gui.editServerPort.setText(aLines.get(2));
				
			} catch (IOException e) {
				// do nothing
				e.printStackTrace();
			};
			
		}
	};
	
	actionListenerButtonLogin = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String nickname = gui.editClientNickname.getText().trim();
			String serverIP = gui.editServerIP.getText().trim();
			String serverPort = gui.editServerPort.getText().trim();
			int intServerPort = 1500; //standard
			try {
				intServerPort = Integer.parseInt(serverPort);
			}
			catch(Exception en) {
				//nix  
			}

			//genug Infos hierf�r:
			gui.client = new ClientController(serverIP, intServerPort, nickname, gui);
			gui.adCli.startAdv(serverIP, (intServerPort+2));

			if(gui.client.start()) {
				
			gui.memoChat.setText("");
			gui.istVerbunden = true;
			
			// zeug nach login deaktivieren, bzw aktivieren
			gui.buttonLogin.setEnabled(false);
			gui.buttonLogout.setEnabled(true);

			gui.editServerIP.setEditable(false);
			gui.editServerPort.setEditable(false);
			gui.editClientNickname.setEditable(false);}
		}
	}; 
	
	actionListenerButtonSenden = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (gui.istVerbunden){
	gui.client.sendMessage(new ChatMessageMessage(ChatMessage.MESSAGE, gui.memoChat.getText()));				
	gui.memoChat.setText("");
	gui.memoChat.requestFocusInWindow();}
			//else nix machen
		}
	}; 
	
	actionListenerButtonLogout = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {	
			gui.client.sendMessage(new ChatMessage(ChatMessage.LOGOUT));
			
			gui.buttonLogin.setEnabled(true);
			gui.buttonLogout.setEnabled(false);

			gui.editServerIP.setEditable(true);
			gui.editServerPort.setEditable(true);
			gui.editClientNickname.setEditable(true);
			
			gui.adCli.stopAdv();
		}
	}; 
	
}
}
