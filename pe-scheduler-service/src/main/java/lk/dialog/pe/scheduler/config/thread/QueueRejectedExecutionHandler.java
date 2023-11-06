package lk.dialog.pe.scheduler.config.thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class QueueRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueRejectedExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            LOGGER.info("Threadpool Queue full , waiting on caller thread till queue is freed. activeThreads={}|maxThreads={}|currentQueueSize={}|completedTasks={}",executor.getActiveCount(),executor.getMaximumPoolSize(),executor.getQueue().size(),executor.getCompletedTaskCount());
            executor.getQueue().put(r);
        }
        catch (InterruptedException e) {
            throw new RejectedExecutionException("There was an unexpected InterruptedException whilst waiting to add a Runnable in the executor's blocking queue", e);
        }
    }
}
