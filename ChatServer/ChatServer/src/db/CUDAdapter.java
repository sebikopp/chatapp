package db;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import security.PWHasher;
import model.UserAddSuccess;

public class CUDAdapter {
	
	private DBController dbc;
	private Connection con;
	private static final String ADD_USER = "Insert into user (nickname, password) values (?,?);";
	private PWHasher pwh;
//	private static final String CHK_UNAME_EXIST = "Select count (*) from user where nickname = ?;";
	public CUDAdapter(){
		dbc = DBController.getInstance();
		try {
			con = dbc.getConnection();
			pwh = new PWHasher();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * Hinzuf�gen eines Nutzers nach Nutzernamen und Passwort
	 * Die �berpr�fung, ob der Username bereits existiert und ob das PW stark genug ist, muss stattgefunden haben!
	 * Das PW jedoch soll noch nicht gehashed sein!
	 * @return
	 */
	public boolean addUser(String desiredNickname, String pw){
		try {
			PreparedStatement pst = con.prepareStatement(ADD_USER);
			pst.setString(1, desiredNickname);
			pst.setString(2, pwh.createHash(pw));
			pst.execute();
			con.commit();
			return true;
		} catch (Exception e) {
			// TODO Rollbackbehandlung schreiben
			e.printStackTrace();
			
			return false;
		}
		
	}
}
