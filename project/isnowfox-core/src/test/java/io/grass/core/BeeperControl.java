package io.grass.core;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.isnowfox.core.junit.BaseTest;

import static java.util.concurrent.TimeUnit.*;

public class BeeperControl extends BaseTest{
	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(5);

	public void test() throws InterruptedException {
		final Runnable beeper = new Runnable() {
			public void run() {
				System.out.println(System.currentTimeMillis());
			}
		};
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(
				beeper, 10, 10, SECONDS);
		
		scheduler.schedule(new Runnable() {
			public void run() {
				beeperHandle.cancel(true);
			}
		}, 40, SECONDS);
		Thread.sleep(10000*1000);
	}

}
