package javaschtasks.batchjob;

import org.apache.log4j.Logger;

import javafx.concurrent.Task;

public class IteratingTask extends Task<Integer> {
	private static Logger logger = Logger.getLogger(JobSubmitGui.class.getName());
    private final int totalIterations;
    private Taskobj atask = null;
    private String operation; 
    
    public IteratingTask(Taskobj task, String opper) { 
    	this.atask = task;
    	this.totalIterations = 3000;
    	this.operation = opper;
    }

    @Override protected Integer call() throws Exception {
        int iterations = 0;
        for (iterations = 0; iterations < totalIterations; iterations++) {
            if (isCancelled()) {
                updateMessage("Cancelled");
                break;
            }
            //updateMessage("Iteration " + iterations);
            updateProgress(iterations, totalIterations);
            //System.out.println("Iteration " + iterations);
            
        }
        StringBuffer returncodes = null;
        if (this.operation.toLowerCase().equals("run")){
        	returncodes = wintask.runataskl(this.atask);
        } else if (this.operation.toLowerCase().equals("del")){
        	returncodes = wintask.deltask1(this.atask);
        } else {
        	throw new Exception("Not valid operation for task, shoule be run or del");
        }
        
        updateMessage(returncodes.toString());
        logger.debug("shell task returned: " + returncodes.toString());
        return iterations;
    }
}