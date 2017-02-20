package org.benmobile.analysis.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum TaskExecutor {
	INSTANCE;
	
	private ExecutorService executor = Executors.newFixedThreadPool(4);
	
	public void execute(Runnable r){
		executor.submit(r);
	}
}
