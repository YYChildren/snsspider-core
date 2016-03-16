package com.mingchao.snsspider.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.task.ExeRunnableTask;
import com.mingchao.snsspider.task.Task;

public class BaseTaskExcutor implements TaskExcutor {
	protected Log log = LogFactory.getLog(this.getClass());
	public static final int DEFALT_SIZE = 1;
	private String groupName;
	private ThreadGroup threadGroup;
	private ThreadPoolExecutor threadPool;
	private int semaphore;

	public BaseTaskExcutor() {
		this(DEFALT_SIZE);
	}
	
	public BaseTaskExcutor(int poolSize) {
		this.semaphore = poolSize;
		
		groupName = this.getClass().getSimpleName();
		threadGroup = new ThreadGroup(groupName);
		ThreadFactory threadFactory = new DefaultThreadFactory(threadGroup);
		threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(
				poolSize, threadFactory);
		threadPool.prestartAllCoreThreads();
	}

	@Override
	public synchronized void execute(Task task) throws InterruptedException {
		if(--semaphore < 0){//申请资源
			wait();
		}
		threadPool.execute(new ExeRunnableTask(task, this));//该线程最终会执行下面的 after()函数
	}

	@Override
	public synchronized void after() {
		if(semaphore++ < 0){//释放资源
			notify();
		}
	}

	@Override
	public void close() {
		threadPool.shutdown();
		log.warn(this.getClass().getSimpleName() + " Stopped");
	}
}

class DefaultThreadFactory implements ThreadFactory {
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	DefaultThreadFactory(ThreadGroup threadGroup) {
		group = threadGroup;
		namePrefix = threadGroup.getName() + "-worker-";
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix
				+ threadNumber.getAndIncrement(), 0);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}
}
