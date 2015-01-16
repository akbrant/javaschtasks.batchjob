package javaschtasks.batchjob;

import javafx.concurrent.Task;

public class IteratingTask extends Task<Integer> {
    private final int totalIterations;

    public IteratingTask(int totalIterations) {
        this.totalIterations = totalIterations;
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
        StringBuffer returncodes = wintask.linuxfortune(null);
        updateMessage(returncodes.toString());
        return iterations;
    }
}