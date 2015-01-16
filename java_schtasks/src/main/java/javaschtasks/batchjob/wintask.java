package javaschtasks.batchjob;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

public class wintask {
	private static Logger logger = Logger.getLogger(wintask.class.getName());



	public static void main(String args[]) throws IOException, InterruptedException {
		StringBuffer sb = wintask.getrunoncexml();
		String myxml = sb.toString();
		ReadXMLFile.readxmlString(myxml, null);
		System.out.println(myxml);

	}
	
	
	

	public static StringBuffer stoutErrors(Process p) {
		BufferedReader br = null;
		try{
			// stdout
			StringBuffer ret = new StringBuffer();
			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				ret.append(line);
			}
			//stderr
			is = p.getErrorStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static StringBuffer runawinho() {
	
				List<String> commands = new ArrayList<String>();

				commands.add("schtasks.exe");
				commands.add("/run");
				commands.add("/S");
				commands.add(JobSubmitGui.defaultserver);
				commands.add("/U");
				commands.add(JobSubmitGui.defaultuser);
				commands.add("/P");
				commands.add(JobSubmitGui.defaultpass);
				commands.add("/TN");
				commands.add("FOLDERJOBS\\Testing777");
				
				logger.debug("Running shell command: " + commands);
				ProcessBuilder builder = new ProcessBuilder(commands);
				Process p;
				try {
					p = builder.start();
					StringBuffer sb = wintask.stoutErrors(p);
					p.waitFor();
					System.out.println(p.exitValue());
					return sb;
				} catch (IOException | InterruptedException e) {			
					e.printStackTrace();
				} 
				return null;
		
	}
	
	public static StringBuffer deltask1(Taskobj task) {
		List<String> commands = new ArrayList<String>();

		String taskname = wintask.getGoodTaskName(task);
		commands.add("schtasks.exe");
		commands.add("/S");
		commands.add(JobSubmitGui.defaultserver);
		commands.add("/U");
		commands.add(JobSubmitGui.defaultuser);
		commands.add("/P");
		commands.add(JobSubmitGui.defaultpass);
		commands.add("/TN");
		commands.add("FOLDERJOBS\\" + taskname);
		commands.add("/F");
		
		logger.debug("Running shell command: " + commands);
		ProcessBuilder builder = new ProcessBuilder(commands);
		Process p;
		try {
			p = builder.start();
			StringBuffer sb = wintask.stoutErrors(p);
			p.waitFor();
			System.out.println(p.exitValue());
			return sb;
		} catch (IOException | InterruptedException e) {			
			e.printStackTrace();
		} 
		return null;
		
	}
	
	public static StringBuffer runataskl(Taskobj task) {
	
		List<String> commands = new ArrayList<String>();
		String taskname = wintask.getGoodTaskName(task);
		commands.add("schtasks.exe");
		commands.add("/Create");
		commands.add("/S");
		commands.add(JobSubmitGui.defaultserver);
		commands.add("/U");
		commands.add(JobSubmitGui.defaultuser);
		commands.add("/P");
		commands.add(JobSubmitGui.defaultpass);
		commands.add("/XML");
		commands.add(task.getFilename());		
		commands.add("/TN");
		commands.add("FOLDERJOBS\\" + taskname);
		commands.add("/F");
		
		logger.debug("Running shell command: " + commands);
		ProcessBuilder builder = new ProcessBuilder(commands);
		Process p;
		try {
			p = builder.start();
			StringBuffer sb = wintask.stoutErrors(p);
			p.waitFor();
			System.out.println(p.exitValue());
			return sb;
		} catch (IOException | InterruptedException e) {			
			e.printStackTrace();
		} 
		return null;
		
		
	}
		
	private static String getGoodTaskName(Taskobj task) {
		
		File afile = new File(task.getFilename());
		String filenameshort = afile.getName();
		
		Calendar ca1 = Calendar.getInstance();
		int DAY_OF_YEAR = ca1.get(Calendar.DAY_OF_YEAR);
		String newtask = DAY_OF_YEAR + filenameshort;
		
		return newtask;
		
	}
		
	public static StringBuffer getrunoncexml() {
		List<String> commands = new ArrayList<String>();
		commands.add("schtasks.exe");
		commands.add("/query");
		commands.add("/S");
		commands.add(JobSubmitGui.defaultserver);
		commands.add("/U");
		commands.add(JobSubmitGui.defaultuser);
		commands.add("/P");
		commands.add(JobSubmitGui.defaultpass);
		commands.add("/TN");
		commands.add("FOLDERJOBS\\");
		commands.add("/XML");

		logger.debug("Running shell command: " + commands);
		ProcessBuilder builder = new ProcessBuilder(commands);
		Process p;
		try {
			p = builder.start();
			StringBuffer sb = wintask.stoutErrors(p);
			p.waitFor();
			System.out.println(p.exitValue());
			return sb;
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}

}
