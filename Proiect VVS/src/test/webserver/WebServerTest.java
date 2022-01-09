package test.webserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.webserver.WebServer;

public class WebServerTest {
	
	@BeforeAll
	static void setup() { 
		WebServer.setBasePath("TestFiles");
	}
	
	
	private static WebServer ws = new WebServer();
	
	
	
	@Test
	public void grabPathFromLineTest() {
		assertEquals("/caine", ws.grabPathFromLine("GET /caine HTTP..."));
	}
	
	@Test
	public void grabContentTest() {
		 File file = new File("TestFiles/america");
		 if(file.exists())
			 file.delete();
		 if(!file.exists())
		 {
//			 assertThrows(FileNotFoundException, ws.grabContent(file));
			 try {
				new File(file.getParent()).mkdirs();
				file.createNewFile();
				assertEquals("",ws.grabContent(file));
				FileWriter fw = new FileWriter(file);
				fw.append("c");
				fw.flush();
				fw.close();
				assertEquals("c",ws.grabContent(file));
				file.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("can't create files");
			}
		 }
	}
	
	@Test
	public void findFileTest() {

		 File file1 = new File("TestFiles/america");
		 File file2 = new File("TestFiles/africa");
		 File file3 = new File("TestFiles/idk/asia.html");
		 try {
			 new File(file3.getParent()).mkdirs();
			 file1.createNewFile();
			 file2.createNewFile();
			 file3.createNewFile();
			 assertNull(ws.findFile("asdfgh"));
			 assertNotNull(ws.findFile("america"));
			 assertNotNull(ws.findFile("asia.html"));
			 assertNotNull(ws.findFile("idk/asia.html"));
			 file1.delete();
			 file2.delete();
			 file3.delete();
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 } 
	}
	
	@Test
	public void decodeTest() {
		String testString = "%20%21%22";
		assertEquals(" !\"",ws.decode(testString));
	}
	
	@Test
	public void addHeadersTest() {
		String testString = "america";
		assertEquals("HTTP/1.1 200 OK\r\n"
				+ "Content-Length: 7\r\n"
				+ "\r\n"
				+ "america\r\n"
				+ "\r\n"
				+ "",ws.addHeaders(testString));
	}
}