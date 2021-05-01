package multithreading.thread.lock.reetrantlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main{
	
	
	public static class PriceContainer {
		private Lock lockObject = new ReentrantLock();
		
		private double bitcoinprice;
		private double etherPrice;
		private double litecoinPrice;
		private double bitcoinCashPrice;
		private double ripplePrice;
		
		public double getBitcoinprice() {
			return bitcoinprice;
		}
		public void setBitcoinprice(double bitcoinprice) {
			this.bitcoinprice = bitcoinprice;
		}
		public double getEtherPrice() {
			return etherPrice;
		}
		public void setEtherPrice(double etherPrice) {
			this.etherPrice = etherPrice;
		}
		public double getLitecoinPrice() {
			return litecoinPrice;
		}
		public void setLitecoinPrice(double litecoinPrice) {
			this.litecoinPrice = litecoinPrice;
		}
		public double getBitcoinCashPrice() {
			return bitcoinCashPrice;
		}
		public void setBitcoinCashPrice(double bitcoinCashPrice) {
			this.bitcoinCashPrice = bitcoinCashPrice;
		}
		public double getRipplePrice() {
			return ripplePrice;
		}
		public void setRipplePrice(double ripplePrice) {
			this.ripplePrice = ripplePrice;
		}
		public Lock getLockObject() {
			return lockObject;
		}
	}

	public static class PriceUpdater extends Thread{
		private PriceContainer priceContainer;
		private Random random = new Random();
		
		public PriceUpdater(PriceContainer priceContainer) {
			this.priceContainer = priceContainer;
		}
		
		@Override
		public void run() {
			while (true) {
				priceContainer.getLockObject().lock();
				
				try {
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					priceContainer.setBitcoinCashPrice(20000);
					priceContainer.setBitcoinprice(1000);
					priceContainer.setEtherPrice(500);
					priceContainer.setLitecoinPrice(500);
					priceContainer.setRipplePrice(random.nextDouble());
					
				} finally {
					priceContainer.getLockObject().unlock();
				}
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		// not completed because of fx

	}

}
