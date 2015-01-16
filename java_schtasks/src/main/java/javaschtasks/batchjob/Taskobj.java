package javaschtasks.batchjob;

public class Taskobj {

	String Author ="";
	String Descrip ="";
	String UserID ="";
	String Command ="";
	String Args ="";
	String WorkDir ="";
	String Filename ="";
	
	
	public Taskobj(String author, String descrip, String userID,
			String command, String args, String workDir, String filename) {
		super();
		Author = author;
		Descrip = descrip;
		UserID = userID;
		Command = command;
		Args = args;
		WorkDir = workDir;
		Filename = filename;
	}






	public static void main(String[] args) {
	
	}

	



	public Taskobj() {
		
	}



	public Taskobj(Taskobj task) {
		Author = task.getAuthor();
		Descrip = task.getDescrip();
		UserID = task.getUserID();
		Command = task.getCommand();
		Args = task.getArgs();
		WorkDir = task.getWorkDir();
		Filename = task.getFilename();
	}


	public void clear() {
		 Author ="";
		 Descrip ="";
		 UserID ="";
		 Command ="";
		 Args ="";
		 WorkDir ="";
		 Filename = "";
		
	}

	@Override
	public String toString() {
		return "Taskobj [Author=" + Author + ", Descrip=" + Descrip
				+ ", UserID=" + UserID + ", Command=" + Command + ", Args="
				+ Args + ", WorkDir=" + WorkDir + ", Filename=" + Filename
				+ "]";
	}



	public String getAuthor() {
		return Author;
	}


	public void setAuthor(String author) {
		Author = author;
	}


	public String getDescrip() {
		return Descrip;
	}


	public void setDescrip(String descrip) {
		Descrip = descrip;
	}


	public String getUserID() {
		return UserID;
	}


	public void setUserID(String userID) {
		UserID = userID;
	}


	public String getCommand() {
		return Command;
	}


	public void setCommand(String command) {
		Command = command;
	}


	public String getArgs() {
		return Args;
	}


	public void setArgs(String args) {
		Args = args;
	}


	public String getWorkDir() {
		return WorkDir;
	}


	public void setWorkDir(String workDir) {
		WorkDir = workDir;
	}
	
	public String getFilename() {
		return Filename;
	}



	public void setFilename(String filename) {
		Filename = filename;
	}

	

}
