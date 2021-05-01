package multithreading.thread.semaphore;

import java.util.concurrent.Semaphore;
class Shared{
	public static int count = 0;
}
class MyThread extends Thread{
	Semaphore semaphore = null;
	String name;
	public MyThread(Semaphore semaphore, String name) {
		super(name);
		this.semaphore = semaphore;
		this.name = name;
	}
	
	@Override
	public void run() {
		if(currentThread().getName().equals("A")) {
			System.out.println("Thrad"+name+"Starded");
			
			try {
				System.out.println(name + " waiting for permit");
				semaphore.acquire();
				System.out.println(name + " acquired the resource");
				
				for (int i = 0; i < 5; i++) {
					Shared.count++;
					System.out.println(name + ": " + Shared.count);
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(name + " releases the permit.");
			semaphore.release();
			
		}
		
		else {
			System.out.println("Starting " + name);
			try {
				System.out.println(name + " is waiting for a permit.");
				semaphore.acquire();
				System.out.println(name + " gets a permit.");
				
				 for(int i=0; i < 5; i++)
	                {
	                    Shared.count--;
	                    System.out.println(name + ": " + Shared.count);
	                    Thread.sleep(10);
	                }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 System.out.println(name + " releases the permit.");
             semaphore.release();
		}
	}
}

public class Test {

	public static void main(String[] args) throws InterruptedException {
		Semaphore semaphore = new Semaphore(1);
		
		MyThread mt1 = new MyThread(semaphore, "A");
        MyThread mt2 = new MyThread(semaphore, "B");
        mt1.start();
        mt2.start();
        
        mt1.join();
        mt2.join();
        
        System.out.println("count: " + Shared.count);
	}

}
