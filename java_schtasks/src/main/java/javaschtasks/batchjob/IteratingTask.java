package javaschtasks.batchjob;

import org.apache.log4j.Logger;

import javafx.concurrent.Task;

public class IteratingTask extends Task<Integer> {
	private static Logger logger = Logger.getLogger(JobSubmitGui.class.getName());
    private final int totalIterations;
    private Taskobj atask = null;
    
    public IteratingTask(Taskobj task) { 
    	this.atask = task;
    	this.totalIterations = 3000;
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
        StringBuffer returncodes = wintask.runataskl(this.atask);
        updateMessage(returncodes.toString());
        logger.debug("shell task returned: " + returncodes.toString());
        return iterations;
    }
}