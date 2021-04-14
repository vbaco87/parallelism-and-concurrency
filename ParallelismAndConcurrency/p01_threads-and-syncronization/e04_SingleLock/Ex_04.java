package e04_SingleLock;

import java.util.concurrent.locks.*;

public class Ex_04 {

	// LAUNCHER
	public static void main(String[] args) {
		int INSTANCES = 10;

		Synchronizer sync = new Synchronizer(INSTANCES);
		Du[] dus = new Du[INSTANCES];
		Bi[] bis = new Bi[INSTANCES];
		Duu[] duus = new Duu[INSTANCES];

		for (int i = 0; i < INSTANCES; i++) {
			dus[i] = new Du(sync, i);
			bis[i] = new Bi(sync, i);
			duus[i] = new Duu(sync, i);
		}

		System.out.println("Let's go...\n");

		for (int i = 0; i < INSTANCES; i++) {
			duus[i].start();
			bis[i].start();
			dus[i].start();
		}

		Synchronizer.waitAwhile(5000);

		System.exit(0);
	}
}

class Synchronizer {

	// the synchronizer must know the number of running instances...
	public static final int CAN_DU = 1;
	public static final int CAN_BI = 2;
	public static final int CAN_DUU = 3;
	public volatile int state = CAN_DU;

	private volatile int instances;// for duu;
	public ReentrantLock lock = new ReentrantLock();
	private volatile int threadDuu = 0;
	private volatile int threadDuAndBi = -1;
	private volatile boolean uppercase = true;

	public Synchronizer(int iNSTANCES2) {
		// TODO Auto-generated constructor stub
		instances = iNSTANCES2;
	}

	public int getInstances() {
		return instances;
	}

	public int getthreadDuu() {
		return threadDuu;
	}

	public void letMeDu(int id) {
		lock.lock();
		while (state != CAN_DU) {
			lock.unlock();
			Thread.yield();
			lock.lock();
		}
		threadDuAndBi = id;
	}

	public void duDone(int id) {
		state = CAN_BI;
		lock.unlock();
	}

	public void letMeBi(int id) {

		while (true) {
			lock.lock();
			while (state != CAN_BI) {
				lock.unlock();
				Thread.yield();
				lock.lock();
			}
			if(!this.uppercase) {
				if (id != threadDuAndBi) {
					lock.unlock();
					Thread.yield();
				} else {
					return;
				}
			}else {
				return;
			}
		}
	}

	public void biDone() {

		this.uppercase = !this.uppercase;
		if (!uppercase) {
			state = CAN_DUU;
		} else {
			state = CAN_DU;
		}
		lock.unlock();
	}

	public void letMeDuu(int id) {

		while (true) {
			lock.lock();
			while (state != CAN_DUU) {
				lock.unlock();
				Thread.yield();
				lock.lock();
			}
			if (id != threadDuu) {
				lock.unlock();
				Thread.yield();
			} else {
				return;
			}
		}

	}

	public void duuDone() {
		state = CAN_DU;
		if (threadDuu == this.instances-1) {
			threadDuu = 0;
		} else {
			threadDuu++;
		}
		lock.unlock();

	}

	public boolean upper() {
		return this.uppercase;
	}

	/* COMPLETE */

	// convenience method. Spares you the burden of the try-catch blocks that
	// surround
	// the invocations of sleep. Use where appropiate but favour Thread.yield to
	// avoid pure busy waiting
	protected static void waitAwhile(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ie) {
		}
	}
}

class Du extends Thread {

	private Synchronizer sync;
	private int id;

	public Du(Synchronizer sync, int id) {
		this.sync = sync;
		this.id = id;
	}

	public void run() {
		while (true) {
			/* COMPLETE */
			sync.letMeDu(id);
			System.out.print(" DU(" + id + ")");
			sync.duDone(id);
		}
	}
}

class Bi extends Thread {

	private Synchronizer sync;
	private int id;

	public Bi(Synchronizer sync, int id) {
		this.sync = sync;
		this.id = id;
	}

	public void run() {
		while (true) {
			/* COMPLETE */
			sync.letMeBi(id);
			if (!sync.upper()) {
				System.out.print(" BI(" + id + ")");
			} else {
				System.out.print(" bi(" + id + ")");
			}
			sync.biDone();
		}
	}
}

class Duu extends Thread {

	private Synchronizer sync;
	private int id;

	public Duu(Synchronizer sync, int id) {
		this.sync = sync;
		this.id = id;
	}

	public void run() {
		while (true) {
			/* COMPLETE */
			sync.letMeDuu(id);
			System.out.println(" DUU!(" + id + ")");
			sync.duuDone();
		}
	}
}