package db;

import java.sql.Connection;
import java.sql.SQLException;

import security.PWHasher;

public abstract class AbstractDBAdapter {

	static final int DEFAULT_ROLLBACK_TRIES = 5;
	protected DBController dbc;
	protected Connection con;
	protected PWHasher pwh;
	public AbstractDBAdapter(){
		dbc = DBController.getInstance();
		try {
			con = dbc.getConnection();
			pwh = new PWHasher();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected final void doRollback(){
		if (con != null)
			doRollback(DEFAULT_ROLLBACK_TRIES);
	}
	private final void doRollback(int tries){
		try {
			con.rollback();
		} catch (Exception exc){
			if (tries > 0)
				doRollback(tries-1);
			else System.err.println("FATAL Error: Rollback finally failed!");
		}
	}
	protected final void checkCon() throws SQLException{
		if (con == null || con.isClosed()){
			con = dbc.getConnection();
		}
	}
}
