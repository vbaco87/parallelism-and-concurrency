package B_RiverCrossingImplicitLock;

public class RiverCrossingMonitor_B {

	/* LAUNCHER. DO NOT modify */
	public static void main(String[] args) {

		System.out.println("there we go");

		RiverCrossingMonitor_B monitor = new RiverCrossingMonitor_B();

		Boat boat = new Boat(monitor);
		Passenger[] monks = new Passenger[10];
		Passenger[] nuns = new Passenger[10];

		for (int i = 0; i < monks.length; i++) {
			monks[i] = new Passenger(i, Passenger.NATURE.MONK, monitor);
			nuns[i] = new Passenger(i, Passenger.NATURE.NUN, monitor);
			monks[i].start();
			nuns[i].start();
		}
		boat.start();

		try {
			Thread.sleep(20000);
		} catch (InterruptedException ie) {
		}

		for (int i = 0; i < monks.length; i++) {
			monks[i].stop();
			nuns[i].stop();
		}
		boat.stop();
	}

	/* End of Launcher */

	// code of monitor starts here...
	// declare attributes here

	private volatile int nuns = 0, monks = 0, alreadySang = 0;
	private volatile boolean departed = false;

	public synchronized void board(Passenger p) {
		/* COMPLETE */

		boolean proceed = false;
		while (!proceed) {
			if (monks + nuns != 4) {
				if (p.type == Passenger.NATURE.NUN) {
					if (!(monks == 3 || (monks == 1 && nuns == 2))) {
						nuns++;
						proceed = true;
					}
				} else if (p.type == Passenger.NATURE.MONK) {
					if (!(nuns == 3 || (nuns == 1 && monks == 2))) {
						monks++;
						proceed = true;
					}
				}
			}
			if (!proceed) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		notifyAll();

	}

	public synchronized void sing(Passenger p) {
		/* COMPLETE */
		while (!departed) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("	*** Row, row row your boat (" + p.type + " " + p.id + ")");
		alreadySang++;
		notifyAll();

	}

	public synchronized void depart() {
		/* COMPLETE */

		while (nuns + monks != 4) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Boat DEPARTS now with MONKS " + monks + " NUNS: " + nuns);
		departed = true;
		notifyAll();
	}

	public synchronized void waitAllHaveSung() {
		/* COMPLETE */
		while (alreadySang != 4) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Boat HAS RETURNED now");
		nuns = 0;
		monks = 0;
		alreadySang = 0;
		departed = false;
		notifyAll();
	}

	// add private methods if necessary

}

/* DO NOT MODIFY THESE CLASSES: Passenger and Boat */

class Passenger extends Thread {
	public enum NATURE {
		MONK, NUN
	};

	public int id;
	public NATURE type;
	private RiverCrossingMonitor_B monitor;

	public Passenger(int id, NATURE type, RiverCrossingMonitor_B monitor) {
		this.id = id;
		this.type = type;
		this.monitor = monitor;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(50 + 50 * (int) Math.random());
			} catch (InterruptedException ie) {
			}
			monitor.board(this);
			try {
				Thread.sleep(50 + 50 * (int) Math.random());
			} catch (InterruptedException ie) {
			}
			monitor.sing(this);
		}
	}
}

class Boat extends Thread {

	private RiverCrossingMonitor_B monitor;

	public Boat(RiverCrossingMonitor_B monitor) {
		this.monitor = monitor;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(50 + 50 * (int) Math.random());
			} catch (InterruptedException ie) {
			}
			monitor.depart();
			try {
				Thread.sleep(50 + 50 * (int) Math.random());
			} catch (InterruptedException ie) {
			}
			monitor.waitAllHaveSung();
		}
	}
}