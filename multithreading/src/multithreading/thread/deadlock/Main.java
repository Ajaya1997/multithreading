package multithreading.thread.deadlock;

import java.util.Random;

public class Main {
	
	public static class TrainA implements Runnable{
		private Intersection intersection;
		private Random random = new Random();
		
		public TrainA(Intersection intersection) {
			this.intersection = intersection;
		}
		@Override
		public void run() {
			while (true) {
				long sleepTime = random.nextInt(5);
				try {
					Thread.sleep(sleepTime);
				} catch (Exception e) {
				}
				intersection.takeRoadA();
			}
			
		}
	}
	
	public static class TrainB implements Runnable{
		private Intersection intersection;
		private Random random = new Random();
		
		public TrainB(Intersection intersection) {
			this.intersection = intersection;
		}
		@Override
		public void run() {
			while (true) {
				long sleepTime = random.nextInt(5);
				try {
					Thread.sleep(sleepTime);
				} catch (Exception e) {
				}
				intersection.takeRoadB();
			}
			
		}
	}
	
	public static class Intersection{
		private Object roadA = new Object();
		private Object roadB = new Object();
		
		public void takeRoadA() {
			synchronized (roadA) {
				System.out.println("RoadA is locked by thread"+ Thread.currentThread().getName());
				synchronized (roadB) {
					System.out.println("Train is passing through road A");
					try {
						Thread.sleep(1);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}

		public void takeRoadB() { // same order locking(not cyclick locking) to prevent deadlock situation(applicable in small application)
			synchronized (roadA) {
				System.out.println("RoadB is locked by thread"+ Thread.currentThread().getName());
				synchronized (roadB) {
					System.out.println("Train is passing through road B");
					try {
						Thread.sleep(1);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	public static void main(String[] args) {
		Intersection intersection = new Intersection();
		//Intersection intersection1 = new Intersection();
		Thread trainAThread = new Thread(new TrainA(intersection));
		Thread trainBThread = new Thread(new TrainB(intersection));
		
		trainAThread.start();
		trainBThread.start();
	}

}
