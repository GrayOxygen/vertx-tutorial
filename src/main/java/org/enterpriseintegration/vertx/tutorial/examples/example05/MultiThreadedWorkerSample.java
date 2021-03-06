package org.enterpriseintegration.vertx.tutorial.examples.example05;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

/**
 * Example 05 - MultiThreadedWorker: Deploy a multi-threaded worker verticle
 */
public class MultiThreadedWorkerSample extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		
		vertx.deployVerticle("org.enterpriseintegration.vertx.tutorial.examples.example05.MultiThreadedWorkerSample",
				// Instantiate a DeploymentOptions by setting an explicit number
				// of instances and enabling worker code, multiThreaded mode but
				// without having to set the number of instances
				new DeploymentOptions().setWorker(true).setMultiThreaded(true), res -> {
					if (res.succeeded()) {
						System.out.println("Multi-threaded Worker verticle deployed");

						// Send messages on the event bus
						EventBus eventBus = vertx.eventBus();
						eventBus.publish("event", "event01");
						eventBus.send("event", "event02");
						eventBus.send("event", "event03");

					} else {
						System.out.println("Error while deploying a verticle: " + res.cause().getMessage());
					}
				});
	}

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("Creating an instance with PID " + Thread.currentThread().getId());

		EventBus eventBus = vertx.eventBus();

		// Consume messages on the event bus
		eventBus.consumer("event", message -> {
			System.out.println("PID " + Thread.currentThread().getId() + " received " + message.body());
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
		});

		startFuture.complete();
	}
}