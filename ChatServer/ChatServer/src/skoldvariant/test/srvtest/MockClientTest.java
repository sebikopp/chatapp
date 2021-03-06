package skoldvariant.test.srvtest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import skoldvariant.db.CUDAdapter;
import skoldvariant.db.RAdapter;
import skoldvariant.model.ChatMessage;
import skoldvariant.model.Conversation;
import skoldvariant.model.LoginRequest;
import skoldvariant.model.LoginResponse;
import skoldvariant.model.User;
import skoldvariant.servercore.MessageTypes;
import skoldvariant.servercore.ServerCenter;
public class MockClientTest {
	CUDAdapter cud;
	RAdapter rad;
	ServerCenter scnt;
	Gson gs;
	private int count;
	@Before
	public void pullUp(){
		rad = new RAdapter();
		cud = new CUDAdapter();
		scnt = ServerCenter.getInstance();
		gs = new Gson();
	}
	@Test
	public void test(){
		try{
			System.out.println(rad.getAllUsersforPrint());
		HashMap<String,String> uspw = new HashMap<String, String>();
		uspw.put("user1", "123");
		uspw.put("user2", "345");
		uspw.put("user3", "789");
		uspw.forEach((String ux, String px) -> cud.addUser(ux, px));
		HashMap<String,User> clntmap = new HashMap<String, User>();
		uspw.forEach((String usx, String xxx) -> clntmap.put(usx, rad.getUserByNickname(usx)));
		cud.addConv(Arrays.asList(clntmap.entrySet().stream().map(e -> e.getValue()).toArray(User[]::new)));
		uspw.forEach((String ux, String pwx) -> {
			LoginRequest lrq = new LoginRequest();
			lrq.setPw(pwx);
			lrq.setUsername(ux);
			lrq.setHost("localhost");
			lrq.setPort(4711);
			String resp = scnt.processJson(gs.toJson(lrq, LoginRequest.class), new Integer(MessageTypes.LOGIN_REQ).toString());
			System.out.println(rad.getAllUsersforPrint());
			System.err.println(resp);
//			System.exit(0);
			LoginResponse lrsp = gs.fromJson(resp, LoginResponse.class);
			String token = lrsp.getToken();
			User u = clntmap.remove(ux);
			clntmap.put(token, u);
			System.err.printf("User %s mit Token %s hinzugefügt\n", u.getNickname(), token);
		});
		count = 0;
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("logfile.txt"));
		} catch (FileNotFoundException e2) {
			pw = null;
			e2.printStackTrace();
		}
		for (Entry<String, User> entx: clntmap.entrySet()){
			Conversation conv = null;
			try {
				conv = rad.getConvsByUsr(entx.getValue().getNickname()).get(0);
			} catch (Exception e1) {
				e1.printStackTrace();
				fail(e1.getMessage());
			}
			ChatMessage cmg = new ChatMessage(UUID.randomUUID().toString(), entx.getValue(), conv, entx.getKey());
			String cmgs = scnt.processJson(gs.toJson(cmg, ChatMessage.class) , ""+MessageTypes.SUBMIT_MSG);
			if (cmgs.length() == 0) fail ("No response generated");
			try {
				pw.println(cmgs);
			} catch (Exception excptio){
				System.err.println(cmgs);
			}
			count++;
		}
		try {
			pw.close();
		} catch (Exception e1) {}
		System.err.println("Kurz vor Schluss");
		assertTrue(count == clntmap.size() && count != 0); // Wenn er bis hierhin noch ned gescheitert, sollts klappen
		} catch (Exception suexcept){
			StackTraceElement[] ste = suexcept.getStackTrace();
			suexcept.printStackTrace();
			Arrays.asList(ste).forEach(System.err::println);
			fail (suexcept.getMessage());
		}
	}
//	@After
//	public void afterTest(){
//		try {
//			File f = new File("./chatdb.mv.db");
//			f.delete();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
