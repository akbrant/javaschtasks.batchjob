package javaschtasks.batchjob;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.controlsfx.dialog.Dialogs;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class JobSubmitGui extends Application implements Initializable{
	private static Logger logger = Logger.getLogger(JobSubmitGui.class.getName());
	
	
	private String defaultfolder = "C:\\Users\\user\\git\\sasbatch\\sampleFiles";
	public static String defaultuser = "auser";
	public static String defaultserverip = "xxx.xxx.xxx.xxx";
	public static String defaultservername = "servermachinename";
	public static String defaultpass = "auserpass";

	
	private Stage primaryStage;
	private Desktop desktop = Desktop.getDesktop();

	@FXML private TableView<Taskobj> tasktable;
	private ObservableList<Taskobj> tsakList = FXCollections.observableArrayList();
	
	@FXML	private Button Scanbut;
	@FXML	private TextField goinput;

	
	@FXML	private TextArea teskdesc;
	
	
	
	
	@FXML	private TableColumn<Taskobj, String> taskArgs;
	@FXML	private TableColumn<Taskobj, String> taskCommand;
	@FXML	private TableColumn<Taskobj, String> taskname;
	@FXML	private TableColumn<Taskobj, String> taskuserid;
	@FXML	private TableColumn<Taskobj, String> taskTriggers;
	@FXML	private TabPane tabpane;
	@FXML	private Tab ppltab;
	@FXML	private Button setFolderbutt;
	@FXML	private TextField statusField;

	@FXML	private ProgressBar possbar;
	
	private void populatetaskTable(List<Taskobj>  tasks ) {			
		
		//cast to javafx list 
		tsakList = FXCollections.observableList(tasks);		
		
		tasktable.getItems().clear();
		//tasktable.setStyle(style);     
		tasktable.setItems(tsakList);
		
		taskname.setCellValueFactory(new PropertyValueFactory<Taskobj, String>("Filename"));
		taskuserid.setCellValueFactory(new PropertyValueFactory<Taskobj, String>("UserID"));
		taskArgs.setCellValueFactory(new PropertyValueFactory<Taskobj, String>("Args"));
		

	
		tasktable.getColumns().setAll(taskname, taskuserid, taskArgs) ;
	}
	
	@FXML
	void newfortune(ActionEvent event){
		IteratingTask task = new IteratingTask(new Taskobj());
	     
	     possbar.progressProperty().bind(task.progressProperty());
	     //to change text
	     //teskdesc.textProperty().bind(task.messageProperty());
	     teskdesc.textProperty().addListener(new ChangeListener(){
			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub
				 System.out.println(" has changed!");
			}	    	 
	     });
	     
	     task.messageProperty().addListener(new ChangeListener(){
			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				SimpleStringProperty ii = (SimpleStringProperty) arg0;
				teskdesc.setText(ii.getValue() + "\n");
				System.out.println( ii.getValue() + " " + ii.getName());
				
			}
	    	 
	     });
	     Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
		
	}
	
	@FXML
	void openfoldercho(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		
		//Set to user directory or go to default if cannot access
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if(!userDirectory.canRead()) {
		    userDirectory = new File("c:/");
		}
		directoryChooser.setInitialDirectory(userDirectory);
		
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
         
        if(selectedDirectory == null){
        	logger.debug("No Directory selected");
        }else{
        	logger.debug(selectedDirectory.getAbsolutePath());
        	defaultfolder = selectedDirectory.getAbsolutePath();
        }
        //auto rescan 
    	try {
			handleScan(event);
		} catch (Exception e) {
			statusField.setText(e.getLocalizedMessage());
			return;
		}
        this.setProValues();
        statusField.setText("Job Folder set to: " +  this.defaultfolder);
	}

	
	@FXML
	void handleScan(ActionEvent event) throws Exception {
		File testfolder = new File(defaultfolder);
		if (!testfolder.exists()){
			throw new Exception(this.defaultfolder + " Folder does not exisit, please set new XML task folder. ");
		}			
		List<File> allfiles = ReadXMLFile.listFilesForFolder(new File(defaultfolder));
		List<File> goodxmljobs = new ArrayList<File>();
		List<Taskobj> alltaskobj =  new ArrayList<Taskobj>();
		
		for (final File fileEntry : allfiles) {
	        if(ReadXMLFile.readxmlString(fileEntry.getAbsolutePath(), null)){	        	
	        	logger.debug("\n" + fileEntry.getAbsolutePath() + "\n");
	        	goodxmljobs.add(fileEntry);
	        	ReadXMLFile.task.setFilename(fileEntry.getAbsolutePath());
	        	alltaskobj.add(new Taskobj(ReadXMLFile.task));
	        	ReadXMLFile.task.clear();
	        } 
	    }
		
		populatetaskTable(alltaskobj);
		

	}
	
	

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		setupCasecontextmnu();
		setuprowChanges();
		
		try {
			getPropValues();
			handleScan(null);
		} catch (IOException e) {
			statusField.setText(e.getLocalizedMessage());
			logger.error(e);
			return;
		} catch (Exception e) {
			statusField.setText(e.getLocalizedMessage());
			logger.error(e);
			return;
		}
		teskdesc.setText("Job Folder set to: " +  this.defaultfolder);
		
	
	     
		//initializeAccelerators();
	}

	
	private void setupCasecontextmnu() {
		ContextMenu menu = new ContextMenu();
		tasktable.setContextMenu(menu);

		tasktable.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if (arg0.getClickCount() == 1) { //look for clicks, in same spot (x,y) on table
				
				}
			}
		});

		final MenuItem addViewPdf = new MenuItem("Run");
		menu.getItems().add(addViewPdf);
		addViewPdf.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				final Taskobj task = tasktable.getSelectionModel().getSelectedItem();
				
				
				IteratingTask ittask = new IteratingTask(task);
				   possbar.progressProperty().bind(ittask.progressProperty());
				     //to change text
				     //teskdesc.textProperty().bind(task.messageProperty());
				     teskdesc.textProperty().addListener(new ChangeListener(){
						@Override
						public void changed(ObservableValue arg0, Object arg1, Object arg2) {
							logger.debug(" has changed!");
						}	    	 
				     });
				     
				     ittask.messageProperty().addListener(new ChangeListener(){
						@Override
						public void changed(ObservableValue arg0, Object arg1, Object arg2) {
							SimpleStringProperty ii = (SimpleStringProperty) arg0;
							File ranfile = new File(task.Filename);
							statusField.setText(ranfile.getName() + ": "+  ii.getValue());
							logger.debug( ii.getValue() + ": " + ii.getName());
							
						}
				    	 
				     });
				    Thread th = new Thread(ittask);
			        th.setDaemon(true);
			        th.start();
				
				//dame lovely'
				//run the shit here 
				//wintask.runataskl(task);
				//wintask.deltask1(task);
				
				//DecisionsApp.this.logger.debug(com.getApplicantname() + com.getPdffilename());
				//PopupActionsJavaFx.LanchPdfreader(com, DecisionsApp.this);
			}
		});
		

		final MenuItem addEditItem = new MenuItem("Edit");
		menu.getItems().add(addEditItem);
		addEditItem.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				Taskobj task  = tasktable.getSelectionModel().getSelectedItem();
				boolean issas = false;
				List<String>  args = ReadXMLFile.parseSASargs(task.getArgs());
				String actualparms = "";
				if(args.size() == 5) { //should be a sasjob then
					//2 is -SYSPARMS
					//3 should be actual parms
					issas = true;
					String sysparms = args.get(2);
					actualparms = args.get(3);					
				} else {
					actualparms = task.getArgs();
				}

				Optional<String> response = Dialogs.create()
						.owner(primaryStage)
						.title("Edit Arguments")
						.masthead("You can modify Arguments:")
						.message("Args:")
						.showTextInput(actualparms);

				// One way to get the response value.
				File file = new File(task.getFilename());;
				if (response.isPresent() && issas) {
					logger.debug("Your args: " + response.get());
					args.set(3, response.get());
					
					if(file.exists()){
						//Desktop.getDesktop().open(file);
						if(actualparms.length() > 0) {
							//rebuild sas aurgs
							String wholesasargs ="";
							int pos = 0;
							for (String arg: args){
								if(pos++ < 4) {
									wholesasargs += arg + "\"";
								} else {
									wholesasargs += arg;
								}								
								logger.debug(wholesasargs);
							}

							ReadXMLFile.readxmlString(file.getAbsolutePath(), wholesasargs);	
						}								
					}

				} else if (response.isPresent()) {
					ReadXMLFile.readxmlString(file.getAbsolutePath(), response.get());
				}
				//rescan on edit
				try {
					handleScan(event);
				} catch (Exception e) {
					statusField.setText(e.getLocalizedMessage());				
				}

			}
		});		

	}


	private void setuprowChanges() {
		
		tasktable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB) {
                 
                } else {
                	Taskobj task = tasktable.getSelectionModel().getSelectedItem();
                	teskdesc.setText(task.getDescrip());

       					
                }
            }
        });
		
		tasktable.setOnMousePressed(new EventHandler<MouseEvent>() {
		    @Override 
		    public void handle(MouseEvent event) {
		        if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
		        	Taskobj task = tasktable.getSelectionModel().getSelectedItem();
	              	teskdesc.setText(task.getDescrip());
		        }
		    }
		});		
	}
	

	@Override
	public void start(final  Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/BatchJobeTabs.fxml"));
		final FileChooser fileChooser = new FileChooser();
		Scene scene = new Scene(root);

		stage.setTitle("Jobs Submit");
		stage.setScene(scene);
		stage.setResizable(false);
		
		
		
        this.primaryStage = stage;
		stage.show();

	}


	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	
	public void getPropValues() throws IOException {
		 
		String result = "";
		Properties prop = new Properties();
		String propFileName = "jobsubmit.properties";
 
		InputStream inputStream =  new FileInputStream("jobsubmit.properties");
 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
 
		// get the property value and print it out
		defaultfolder = prop.getProperty("defaultfolder");
 
		defaultuser = prop.getProperty("defaultuser");
		defaultserverip = prop.getProperty("defaultserverip");
		defaultservername = prop.getProperty("defaultservername");
		defaultpass = prop.getProperty("defaultpass");
		
			
		
		logger.debug("default folder from prob file: " + defaultfolder);
	}
	
	public void setProValues(){
		Properties prop = new Properties();
		OutputStream output = null;
	 
		try {
	 
			output = new FileOutputStream("jobsubmit.properties");
	 
			// set the properties value
			prop.setProperty("defaultfolder", this.defaultfolder);
			prop.setProperty("defaultuser", this.defaultuser);
			prop.setProperty("defaultserverip", this.defaultserverip);
			prop.setProperty("defaultservername", this.defaultservername);
			prop.setProperty("defaultpass", this.defaultpass);
			
			// save properties to project root folder
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
		
		
	}

}
