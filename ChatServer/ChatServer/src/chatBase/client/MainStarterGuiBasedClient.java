package chatBase.client;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainStarterGuiBasedClient {

	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.err
					.println("ach gottla, es gibt kein Bier auf Hawaii und keinen Windows Look And Feel Krams");
			e.printStackTrace();
		}
		// BEGIN MAIN
		ClientGuiViewSwing gui = new ClientGuiViewSwing();
		gui.setVisible(true);
		
		// END MAIN
	}

}
