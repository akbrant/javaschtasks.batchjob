package javaschtasks.batchjob;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ReadXMLFile {
	
	private static Logger logger = Logger.getLogger(ReadXMLFile.class.getName());
	 //make task object and return it.
	static Taskobj task = new Taskobj();
	
	public static void main(String args[]) throws IOException, InterruptedException {
		
		
		//ReadXMLFile.listFilesForFolder(folder);
		ReadXMLFile.parseSASargs("-SYSIN \"X:\\CP\\sash\\sampleFiles\\R341.SAS\" -SYSPARM \"2055 2010\" -LOG G:\\PRINT -PRINT G:\\PRINT");
		//ReadXMLFile.readxmlString(null);
		//ReadXMLFile.parsetasksfromXML(null);
	}
	
	
	
	public static List<String> parseSASargs(String sasargs){
		List<String> allparms = new ArrayList<String>();
		logger.debug(sasargs.toUpperCase());
		logger.debug(sasargs.contains("-SYSPARM"));
		if(sasargs.toUpperCase().contains("-SYSPARM")){
			for (String retval: sasargs.split("\"")){
				logger.debug(retval);
				allparms.add(retval);
		      }
			
		}
		return allparms;
	}
	


	public static boolean readxmlString(String xmlstr, String newargs) {

		try {


			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File file = new File(xmlstr); 
			Document doc = dBuilder.parse(file);
		
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			if (doc.hasChildNodes()) {	
				task.setFilename(xmlstr);
				Taskobj atask = printNote(doc.getChildNodes(), newargs );	
				System.out.println(atask);
			}
			if(newargs != null && newargs.length() > 1) {
				testwrite(xmlstr, doc);  //rights out the new args
			}			
			return true;
			
		} catch (Exception e) {
			logger.error(e);
			System.out.println("Not parsable xml: " + xmlstr);
			//e.printStackTrace();
		}
		return false;
	}
	
	private static void testwrite(String xmlstr, Document doc) throws TransformerException, IOException{
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    //transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-16LE"); //almost works but is missing the byte order marker
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UnicodeLittle"); //holy crap we got it!
		DOMSource source = new DOMSource(doc);
		FileOutputStream result = new FileOutputStream(new File(xmlstr));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, new StreamResult(result));
		
		result.close();

	}

	private static Taskobj printNote(NodeList nodeList, String newargs) {

		
		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name and value
				//System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				//System.out.println("Node Value =" + tempNode.getTextContent());
				
				if(tempNode.getNodeName().equals("Author")) {
					task.setAuthor(tempNode.getTextContent());
				}
				if(tempNode.getNodeName().equals("Description")) {
					task.setDescrip(tempNode.getTextContent());
				}
				if(tempNode.getNodeName().equals("UserId")) {
					task.setUserID(tempNode.getTextContent());
				}
				if(tempNode.getNodeName().equals("Command")) {
					task.setCommand(tempNode.getTextContent());
				}
				if(tempNode.getNodeName().equals("Arguments")) {
					if(newargs != null && newargs.length() > 1) {
						tempNode.setTextContent(newargs);
					}
					task.setArgs(tempNode.getTextContent());
				}
				if(tempNode.getNodeName().equals("WorkingDirectory")) {
					task.setWorkDir(tempNode.getTextContent());
				}

				

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						Node node = nodeMap.item(i);
						//System.out.println("attr name : " + node.getNodeName());
						//System.out.println("attr value : " + node.getNodeValue());
						
						
					}

				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					printNote(tempNode.getChildNodes(), newargs);

				}

				//System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

			}

		}
		return task;
	}
	
	
	public static List<String> readFile(String filepath){

		List<String> allLines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath)))
		{
 
			String sCurrentLine;
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				allLines.add(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allLines; 
 
		
	}
	
	
	public static List<File> listFilesForFolder(final File folder) {
		List<File> allfiles = new ArrayList<File>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	
	            allfiles.add(fileEntry);
	           
	        }
	    }
		return allfiles;
	}


}


