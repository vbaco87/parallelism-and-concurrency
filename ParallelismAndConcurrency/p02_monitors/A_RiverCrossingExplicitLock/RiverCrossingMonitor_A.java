package A_RiverCrossingExplicitLock;

import java.util.concurrent.locks.*;

import A_RiverCrossingExplicitLock.Passenger.NATURE;

public class RiverCrossingMonitor_A {

	/* LAUNCHER. DO NOT modify */
	public static void main(String[] args) {

		System.out.println("there we go");

		RiverCrossingMonitor_A monitor = new RiverCrossingMonitor_A();

		Boat boat = new Boat(monitor);
		Passenger[] monks = new Passenger[10];
		Passenger[] nuns = new Passenger[10];

		for (int i = 0; i < monks.length; i++) {
			monks[i] = new Passenger(i, Passenger.NATURE.MONK, monitor);
			nuns[i] = new Passenger(i, Passenger.NATURE.NUN, monitor);
		}
		for (int i = 0; i < monks.length; i++) {
			nuns[i].start();
			monks[i].start();
		}
		boat.start();

		try {
			Thread.sleep(200000);
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
	private ReentrantLock lock = new ReentrantLock();
	private Condition canBoard = lock.newCondition();
	private Condition canSing = lock.newCondition();
	private Condition allHaveSung = lock.newCondition();
	private Condition canDepart = lock.newCondition();
	private volatile int monks = 0;
	private volatile int nuns = 0;
	private volatile int sung = 0;
	private volatile boolean departed = false, allSung = false;
	
	public void board (Passenger p) {
		/* COMPLETE */
		lock.lock();
		if(monks+nuns==0) canBoard.signal();
		if(p.type == Passenger.NATURE.MONK) {
			while((monks+nuns)==4 ||  nuns==3 || (monks==2 && nuns==1)) {
				canBoard.signal();
				canBoard.awaitUninterruptibly();
			}
			monks++;
		}else {
			while((monks+nuns)==4 || monks==3 || (nuns==2 && monks==1)) {
				canBoard.signal();
				canBoard.awaitUninterruptibly();
			}
			nuns++;
		}
		if(monks+nuns==4) canDepart.signal();
		lock.unlock();
	}
	
	public void sing (Passenger p) {
		/* COMPLETE */
		lock.lock();
		if(!departed) {
			canBoard.signal();
			canSing.awaitUninterruptibly();
		}
		System.out.println("	*** Row, row row your boat ("+ p.type+ " "+p.id+")");
		sung++;
		if(sung==4) {
			sung = 0;
			allHaveSung.signal();
			allSung = true;
			departed=false;
		}else {
			canSing.signal();
		}
		lock.unlock();
	}
	
	
	public void depart () {
		/* COMPLETE */
		lock.lock();
		while(monks+nuns < 4) {
			canDepart.awaitUninterruptibly();
		}
		System.out.println("Boat DEPARTS now with MONKS: "+monks+" NUNS: "+nuns);
		canSing.signal();
		departed=true;
		lock.unlock();
	}
	
	public void waitAllHaveSung () {
		/* COMPLETE */
		lock.lock();
		if(!allSung) {
			allHaveSung.awaitUninterruptibly();
		}
		allSung = false;
		monks = 0;
		nuns = 0;
		canBoard.signal();
		System.out.println("Boat HAS RETURNED now");
		lock.unlock();
	}
	// add private methods if necessary

}

/* DO NOT MODIFY THESE CLASSES: Passenger and Boat */

class Passenger extends Thread {
	public static enum NATURE {
		MONK, NUN
	};

	public int id;
	public NATURE type;
	private RiverCrossingMonitor_A monitor;

	public Passenger(int id, NATURE type, RiverCrossingMonitor_A monitor) {
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

	private RiverCrossingMonitor_A monitor;

	public Boat(RiverCrossingMonitor_A monitor) {
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