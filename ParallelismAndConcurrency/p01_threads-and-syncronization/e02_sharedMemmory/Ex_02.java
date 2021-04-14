package e02_sharedMemmory;


public class Ex_02 {

	// LAUNCHER
	public static void main(String[] args) {
		Synchronizer sync = new Synchronizer();
		Du du = new Du(sync);
		Bi bi = new Bi(sync);
		Duu duu = new Duu(sync);
		KitKat kitkat = new KitKat(sync);

		System.out.println("Let's go...\n");

		kitkat.start();
		duu.start();
		bi.start();
		du.start();

		Synchronizer.waitAwhile(5000);

		System.exit(0);
	}
}

class Synchronizer {

	// kit kat related...
	private volatile boolean canKitkat = false;
	private volatile boolean kitkatReady = false;

	/* COMPLETE */
	private volatile boolean canDu = true;
	private volatile boolean canBi = false;
	private volatile boolean canDuu = false;
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
		while (!canDu) {
			waitAwhile(10);
		}
		canDu = false;
	}

	public void duDone() {
		if (!uppercase && kitkatReady) {
			canKitkat = true;
		} else {
			canBi = true;
		}

	}

	public void letMeBi() {
		while (!canBi) {
			waitAwhile(10);
		}
		canBi = false;
	}

	public void biDone() {
		if (uppercase) {
			canDu = true;
		} else {
			canDuu = true;
		}
		uppercase = !uppercase;
	}

	public void letMeDuu() {
		while (!canDuu) {
			waitAwhile(10);
		}
		canDuu = false;
	}

	public void duuDone() {
		if (kitkatReady) {
			canKitkat = true;
		} else {
			canDu = true;
		}
	}

	public void letMeKitkat() {
		waitAwhile(500);
		kitkatReady = true;
		while (!canKitkat) {
			waitAwhile(10);
		}
		kitkatReady = false;
		canKitkat = false;
	}

	public void kitkatDone() {
		uppercase = true;
		canDu = true;
	}

	public boolean getUppercase() {
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
			sync.letMeKitkat();
			System.out.println("");
			System.out.print("\t\t KIT KAT:");
			for(int i=9; i>=0; i--) {
				System.out.print(" "+ i);
				Synchronizer.waitAwhile(200);
			}
			System.out.println("");
			sync.kitkatDone();
		}
	}

}

class Du extends Thread {

	private Synchronizer sync;

	public Du(Synchronizer sync) {
		this.sync = sync;
	}

	public void run() {
		while (true) {
			/* COMPLETE */
			sync.letMeDu();
			System.out.print("DU ");
			sync.duDone();
		}
	}
}

class Bi extends Thread {

	private Synchronizer sync;

	public Bi(Synchronizer sync) {
		this.sync = sync;
	}

	public void run() {
		while (true) {
			/* COMPLETE */
			sync.letMeBi();
			if (sync.getUppercase()) {
				System.out.print("BI ");
			} else {
				System.out.print("bi ");
			}

			sync.biDone();
		}
	}
}

class Duu extends Thread {

	private Synchronizer sync;

	public Duu(Synchronizer sync) {
		this.sync = sync;
	}

	public void run() {
		while (true) {
			/* COMPLETE */
			sync.letMeDuu();
			System.out.println("DUU! ");
			sync.duuDone();
		}
	}
}