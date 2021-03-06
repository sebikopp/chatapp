package chatBase.srv;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.TreeMap;

import chatBase.model.ChatMessage;
import chatBase.model.ChatMessageMessage;
import chatBase.srv.adm.AdminShellStubImpl;
import de.root1.simon.Registry;
import de.root1.simon.Simon;

/**
 * Zentraler Chatserver, welcher die Einzelthreads und das Llgging verwaltet
 * @author Sebastian
 *
 */
public class Server {
	public static final int DEFAULT_PORT = 1500;
//	private int maxId;
	TreeMap<Long, ClientThread> map; // an ArrayList to keep the list of the Client
	private int port;// the port number to listen for connection
	private boolean goOn;// the boolean that will be turned of to stop the server
	private LoggingDaemon ld;
	private static Server instance;
	private AdminShellStubImpl adshellstub;
	public static Server getInstance(int port, PrintStream ps){
		if (Server.instance == null){
			Server.instance = new Server(port, ps);
		}
		return Server.instance;
	}
	public static Server getInstance(){
		return Server.instance;
	}
	private Server(int port, PrintStream ps) {
		this.port = port;
		map = new TreeMap<Long,ClientThread>();
		this.ld = new LoggingDaemon(ps);
		new Thread(() ->{ 
			try {
				new WerbeSender(this.port+2);
			} catch (Exception e) {return;}
		}).start();
		// CLI-Thread starten
		new Thread(()->{
			adshellstub = new AdminShellStubImpl(instance);
			try {
				Registry reg = Simon.createRegistry(port+AdminShellStubImpl.ADMIN_SHELL_PORT_OFFSET);
				reg.bind(AdminShellStubImpl.DEFAULT_BINDING, adshellstub);
				logMessage("AdminShell kann gestartet werden");
			} catch (Exception e) {
				logStackTrace(e);
				logMessage("Adminshell nicht anbindbar");
			}
		}).start();
	}
	/**
	 * Zentraler Durchlauf: je neuem Client wird ein neuer Thread er�ffnet
	 */
	public void doRun() {
		goOn = true;
		/* create socket server and wait for connection requests */
		try{
			ServerSocket serverSocket = new ServerSocket(port);
			while(goOn) {
				logMessage("Server ist bereit: Portnummer: " + port);
				Socket socket = serverSocket.accept();  	// accept connection
				long clntThrId = System.currentTimeMillis();
				if (!goOn) break;
				ClientThread ct = new ClientThread(this, socket, clntThrId); 
				map.put(clntThrId, ct);	
				map.forEach((Long l, ClientThread cthr) -> cthr.writeWhoIsIn()); // An alle beim login sagen, wer drin ist.
				ct.start();
			}
			serverSocket.close();
			for (Entry<Long, ClientThread> dd: map.entrySet()){
				try{
					dd.getValue().getsInput().close();
					dd.getValue().getsOutput().close();
					dd.getValue().getSocket().close();
				} catch(Exception exc) {
					logStackTrace(exc);
				} 
			}
		}
		catch (IOException e) {
            logStackTrace(e);
		} finally{
			System.exit(0);
		}
	}		
    /**
     * Loggt den Stacktrace eines Throwable-Objektes mit Zeitstempel in den im LoggingDaemon referenzierten
     * PrintStream
     * @param thr Zu protokollierendes Throwable-Objekt
     */
	public void logStackTrace(Throwable thr){
		ld.getPw().println(LocalDateTime.now() + ":" + thr.getMessage());
		thr.printStackTrace(ld.getPw());
	}
	/**
	 * Loggt eine allg. Lognachricht mit Zeitstempel in den im LoggingDaemon referenzierten
     * PrintStream
	 * @param msg Zu protokollierende Meldung (ohne Zeitstempel (der wird generiert))
	 */
	public void logMessage(String msg){
		ld.getPw().println(LocalDateTime.now().toString() + ": " + msg);
	}
	/**
	 * Stoppt den Server
	 */
	public void stop() {
		goOn = false;
	}
	/**
	 * Eine als String-Objekt vorhandene Nachricht an alle verbundenen Clients als normale
	 * Chat-Nachricht versenden
	 * Sollte das Senden bei einem Client nicht funktionieren, wird seine Verbindung getrennt
	 * @param message 
	 */
	synchronized void sendToAll(String message) {
		String messageWithDT;
		ld.getPw().println(messageWithDT = "\n"+LocalDateTime.now().toString() + " " + message);
		map.forEach((Long ii, ClientThread dd) ->{
			ChatMessageMessage msgx = new ChatMessageMessage(ChatMessage.MESSAGE, messageWithDT);
			boolean rc= dd.writeMsg(msgx);
			if (!rc){
				map.remove(ii);
				logMessage("Die Verbindung des Clients " + dd.getUsername() + " wurde geschlossen und entfernt.");
				map.forEach((Long l, ClientThread cthr) -> cthr.writeWhoIsIn());
			}
		});
	}
	/**
	 * Einen Client entfernen und die aktualisierte Liste aller verbundenen User an ihre Clients senden
	 * @param id Session-Key in der Map
	 */
	synchronized void remove(long id) {
		// scan the array list until we found the Id
		map.remove(id);
		map.forEach((Long l, ClientThread cthr) -> cthr.writeWhoIsIn()); // Beim Logout allen mitteilen, wer drin ist
	}
	public LoggingDaemon getLd() {
		return this.ld;
	}
}