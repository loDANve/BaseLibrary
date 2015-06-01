package com.wtm.library.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final public class ThreadPoolManager {
	private volatile static ThreadPoolManager instance = null;
	private static ExecutorService service = null;

	private ThreadPoolManager() {
		service = Executors.newCachedThreadPool();
	}

	public static ThreadPoolManager getInstance() {
		if (instance == null) {
			synchronized (ThreadPoolManager.class) {
				if (instance == null) {
					instance = new ThreadPoolManager();
				}
			}
		}
		return instance;
	}

	public void addTask(Runnable run) {
		service.execute(run);
	}

	public Future<?> submitTask(Runnable run) {
		return service.submit(run);
	}

	public void shutDownThreadPoll() {
		service.shutdown();
	}
}
