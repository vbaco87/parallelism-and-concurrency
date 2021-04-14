package e03_Semaphores;

import java.util.concurrent.*;

public class Ex_03 {

	// LAUNCHER
	public static void main(String[] args) {
		int INSTANCES = 10;

		Synchronizer sync = new Synchronizer();
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
	/* COMPLETE */
	private Semaphore canDu = new Semaphore(1);
	private Semaphore canBi = new Semaphore(0);
	private Semaphore canDuu = new Semaphore(0);

	private volatile boolean uppercase = true;

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

	public void letMeDu() {
		try {
			canDu.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void duDone() {
		canBi.release();
	}

	public void letMeBi() {
		try {
			canBi.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void biDone() {
		uppercase = !uppercase;
		if (!uppercase) {
			canDu.release();
		} else {
			canDuu.release();
		}
		
	}

	public void letMeDuu() {
		try {
			canDuu.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void duuDone() {
		canDu.release();
	}

	public boolean getUppercase() {
		return uppercase;
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
			sync.letMeDu();
			System.out.print("DU(" + id + ") ");
			sync.duDone();
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
			sync.letMeBi();

			if (sync.getUppercase()) {
				System.out.print("BI(" + id + ") ");
			} else {
				System.out.print("bi(" + id + ") ");
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
			sync.letMeDuu();
			System.out.println("DUU!(" + id + ") ");
			sync.duuDone();
		}
	}
}
