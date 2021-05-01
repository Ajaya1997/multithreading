package multithreading.thread.readwritelock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Main {

	public static void main(String[] args) {
		PriceInventoryDatabase priInventoryDatabase = new PriceInventoryDatabase();
		Random random = new Random();
		
		for (int i = 0; i < 10000000; i++) {
			priInventoryDatabase.addItem(random.nextInt(1000));
		}
		
		Thread writer = new Thread(() ->{
			while(true) {
				priInventoryDatabase.addItem(random.nextInt(1000));
				priInventoryDatabase.remove(random.nextInt(1000));
				
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		writer.setDaemon(true);
		writer.start();
		
		int noOfReaderThreads = 8;
		List<Thread> readers = new ArrayList(); 
		// creating 8 threads
		for (int i = 0; i <noOfReaderThreads; i++) {
			
			Thread reader = new Thread(()->{
				int upperBoundPrice = random.nextInt(1000);
				int lowerBound = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
				priInventoryDatabase.getNumberofItemsInPriceRange(lowerBound, upperBoundPrice);
			});
			reader.setDaemon(true);
			readers.add(reader);
		}
		long startReadingTime = System.currentTimeMillis();
		
		for (Thread reader: readers) {
			reader.start();
		}
		
		for(Thread reader: readers) {
			try {
				reader.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}
		
		long endReadingTime = System.currentTimeMillis();
		
		System.out.printf("reading took %d ms", endReadingTime - startReadingTime);
		
	}
	
	public static class PriceInventoryDatabase {
		private TreeMap<Integer, Integer> items = new TreeMap();
		private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		Lock readLock = lock.readLock();
		Lock writeLock = lock.writeLock();
		public int getNumberofItemsInPriceRange(int lowerBound, int higherBound) {
			readLock.lock();
			try {
				Integer fromIndex = items.ceilingKey(lowerBound);
				Integer toIndex = items.ceilingKey(higherBound);
				if (fromIndex == null || toIndex == null) {
					return 0;
				}
				NavigableMap<Integer, Integer> subItems = items.subMap(fromIndex, true, toIndex, true);
				int sum = 0;
				for(Integer subItem : subItems.values()) {
					sum += subItem; 
				}
				
				return sum;
			} finally {
				readLock.unlock();
			}
		}

		public void addItem(int price) {
			writeLock.lock();
			try {
				Integer value = items.get(price);
				if (value == null) {
					items.put(price, 1);
				} else {
					items.computeIfPresent(price, (key, val) -> val + 1);
				}
			} finally {
				writeLock.unlock();
			}

		}

		public void remove(int price) {
			writeLock.lock();
			try {
				Integer item = items.get(price);
				
				if(item == null || item == 1){
					items.remove(price);
				}
				else {
					items.computeIfPresent(price, (key, val) -> val - 1);
				}
			} finally {
				writeLock.unlock();
			}
		}
	}
}
