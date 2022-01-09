package main.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class WebServer extends Thread {
	
	protected Socket clientSocket;
	
	private static String basePath = "TestSite";

	private static boolean maintenance;
	
	private static String maintenancePath = "maintenance.html";
	
	public static void toggleMaintenance() {
		maintenance = !maintenance;
	}
	
	public static void setMaintenancePath(String path) {
		maintenancePath = path;
	}
	
	public static void setBasePath(String path) {
		basePath = path;
	}

	public WebServer(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}
	
	//test constructor
	public WebServer() {}

	public void run() {
		System.out.println("New Communication Thread Started");
		String relativePath;
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
//				System.out.println("Server: " + inputLine);
//				out.println(inputLine);
				if(inputLine.startsWith("GET"))
				{
					 relativePath=grabPathFromLine(inputLine);
					 
					 File file = findFile(maintenance?maintenancePath:relativePath);
					 if(file!=null)
					 {
						 try{
							 
							 String fileContent = grabContent(file);
							 
							 String contentWithHeaders = addHeaders(fileContent);
							 
							 clientSocket.getOutputStream().write(contentWithHeaders.getBytes());
						 }
						 catch(FileNotFoundException e ) {
//							 System.out.println("File not found");
						 }
						 catch(IOException e) {
							 System.out.println("Can't read file");
						 }
					 }
					 else
					 {
						 String contentWithHeaders = addHeaders("<div align=center style='margin-top:50vh;font-size:4vh'>File not found</div>");
						 clientSocket.getOutputStream().write(contentWithHeaders.getBytes());
					 }
				}

				if (inputLine.trim().equals(""))
					break;
			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}

	public String grabPathFromLine(String pathLine) {
		String relativePath = pathLine.substring(4);
		relativePath = relativePath.substring(0,relativePath.indexOf(' '));
//		System.out.println(relativePath);
		return decode(relativePath);
	}
	
	public String grabContent(File file) throws IOException {
		 BufferedReader br = new BufferedReader(new FileReader(file));
		 String st;
		 String content = "";
		 while ((st = br.readLine()) != null)
	            content = content + st;
//	     System.out.println(content);  
	     br.close();
	     return content;
	}
	
	public String addHeaders(String fileContent) {
		final String CRLF = "\r\n";
		String response = "HTTP/1.1 200 OK" + CRLF  + 
                "Content-Length: " + fileContent.getBytes().length + CRLF + 
                CRLF + fileContent + CRLF + CRLF;
		return response;
	}
	
	public String decode(String value) {
	    try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
	
	public File findFile(String relativePath) {
		File fileToReturn;
		fileToReturn = new File(basePath + relativePath);
		if(!fileToReturn.exists())
		{
			Optional<Path> foundFile;
			try {
				foundFile = Files.walk(Paths.get(basePath))
						.filter(f->f.toString().replace('\\', '/').endsWith(relativePath))
						.findAny();
				if(foundFile.isPresent())
					fileToReturn = foundFile.get().toFile();
				else 
					fileToReturn=null;
			} catch (IOException e) {
				System.out.println("Error trying to look for file.");
				e.printStackTrace();
			}
		}
		return fileToReturn;
	}
}