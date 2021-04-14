package e05_CompareAndSet;

import java.util.concurrent.atomic.*;



public class Ex_05 {

	// LAUNCHER
	public static void main(String[] args) {
		int INSTANCES = 10;

		Synchronizer sync = new Synchronizer();
		KitKat kitkat = new KitKat(sync);
		Du[] dus = new Du[INSTANCES];
		Bi[] bis = new Bi[INSTANCES];
		Duu[] duus = new Duu[INSTANCES];

		for (int i = 0; i < INSTANCES; i++) {
			dus[i] = new Du(sync, i);
			bis[i] = new Bi(sync, i);
			duus[i] = new Duu(sync, i);
		}

		System.out.println("Let's go...\n");

		kitkat.start();
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
	public static final int WRITING = 5;
	public static final int CAN_DU = 1;
	public static final int CAN_BI = 2;
	public static final int CAN_DUU = 3;
	public static final int CAN_KITKAT = 4;

	private volatile boolean uppercase = true;
	private volatile boolean kitkatReady = false;

	private AtomicInteger canOperate = new AtomicInteger(CAN_DU);

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
		while (!canOperate.compareAndSet(CAN_DU, WRITING)) {
			waitAwhile(10);
		}

	}

	public void duDone() {
		if (!uppercase && kitkatReady) {
			canOperate.set(CAN_KITKAT);
		} else {
			canOperate.set(CAN_BI);
		}

	}

	public void letMeBi() {
		while (!canOperate.compareAndSet(CAN_BI, WRITING)) {
			waitAwhile(10);
		}
	}

	public void biDone() {
		uppercase = !uppercase;
		if (!uppercase) {
			canOperate.set(CAN_DU);
		} else {
			canOperate.set(CAN_DUU);
		}

	}

	public void letMeDuu() {
		while (!canOperate.compareAndSet(CAN_DUU, WRITING)) {
			waitAwhile(10);
		}
	}

	public void duuDone() {
		if (kitkatReady) {
			canOperate.set(CAN_KITKAT);
		} else {
			canOperate.set(CAN_DU);
		}
	}

	public void letMeKitkat() {
		while (!canOperate.compareAndSet(CAN_KITKAT, WRITING)) {
			waitAwhile(10);
		}
		kitkatReady = false;

	}

	public void setKitkatReady(boolean state) {
		kitkatReady = state;
	}

	public void kitkatDone() {
		uppercase = true;
		canOperate.set(CAN_DU);
	}

	public boolean getUppercase() {
		// TODO Auto-generated method stub
		return uppercase;
	}

}

class KitKat extends Thread {

	private Synchronizer sync;

	public KitKat(Synchronizer sync) {
		this.sync = sync;
	}

	public void run() {
		while (true) {
			/* COMPLETE */
			Synchronizer.waitAwhile(500);
			sync.setKitkatReady(true);

			sync.letMeKitkat();
			System.out.println();
			System.out.print("\t\t KIT KAT:");
			for (int i = 9; i >= 0; i--) {
				System.out.print(" " + i);
				Synchronizer.waitAwhile(200);

			}
			System.out.println();
			sync.kitkatDone();

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
