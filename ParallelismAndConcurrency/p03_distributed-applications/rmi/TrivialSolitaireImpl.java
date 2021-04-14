package rmi;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import common.*;

public class TrivialSolitaireImpl extends UnicastRemoteObject implements TrivialSolitaire {

	public static void main(String[] args) throws Exception {
		// launcher
		Registry registry = LocateRegistry.createRegistry(1998);
		registry.bind("TrivialSolitaire", new TrivialSolitaireImpl());
		System.out.println("Trivial solitaire service running (registry in port 1998)...");
	}

	// ---------------------------------------------------------------------

	private static List<Question> art, geo, science; // shouldn't modify these lists...

	/* COMPLETE 1a add other necessary attributes */
	int numServices = 0;
	Map<Integer, ClientInfo> runningServices = new HashMap<Integer, ClientInfo>();
	// static initializer (initializes the lists)
	static {
		art = new LinkedList<Question>();
		geo = new LinkedList<Question>();
		science = new LinkedList<Question>();
		try {
			List<Question> questions = Question.fromFile(new File("Questions.txt"));
			for (Question question : questions) {
				switch (question.getType()) {
				case "GEO":
					geo.add(question);
					break;
				case "ART":
					art.add(question);
					break;
				case "SCIENCE":
					science.add(question);
					break;
				}
			}
		} catch (IOException ioex) {
			System.err.println("static initialization failed!!!");
			System.err.println(ioex);
			System.exit(0);
		}
	}

	public TrivialSolitaireImpl() throws RemoteException {
		/* COMPLETE if needed 1b: Constructor ... */
	}

	/* COMPLETE 2: implement interface and other helper methods */
	@Override
	public int Hello() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("received");
		numServices++;
		runningServices.put(numServices,
				new ClientInfo(numServices, new ArrayList<>(geo), new ArrayList<>(art), new ArrayList<>(science)));
		return numServices;
	}

	@Override
	public Question next(int id, String type) throws RemoteException {
		// TODO Auto-generated method stub

		return runningServices.get(id).getRandomQuestion(type);
	}

	@Override
	public void stop(int id) throws RemoteException {
		// TODO Auto-generated method stub
		if (runningServices.remove(runningServices.get(id)) == null) {
			throw new RemoteException("THE ID DOESN'T EXIST");
		}
	}

}

// consider using instances of this class to store relevant information regarding a particular client
// (like the questions that have not been been sent to it yet...)
class ClientInfo {
	/* COMPLETE */
	private int id;
	private List<Question> geoQuestions;
	private List<Question> artQuestions;
	private List<Question> scienceQuestions;

	public ClientInfo(int id, List<Question> geoQuestions, List<Question> artQuestions,
			List<Question> scienceQuestions) {
		super();
		this.id = id;
		this.geoQuestions = geoQuestions;
		this.artQuestions = artQuestions;
		this.scienceQuestions = scienceQuestions;
	}

	public Question getRandomQuestion(String type) {
		// TODO Auto-generated method stub
		int ranQuestion;

		if (type.equalsIgnoreCase("GEO")) {
			if (geoQuestions.isEmpty()) {
				return null;
			}
			ranQuestion = (int) (Math.random() * geoQuestions.size());
			return geoQuestions.remove(ranQuestion);
		} else if (type.equalsIgnoreCase("ART")) {
			if (artQuestions.isEmpty()) {
				return null;
			}
			ranQuestion = (int) (Math.random() * artQuestions.size());
			return artQuestions.remove(ranQuestion);
		}

		else if (type.equalsIgnoreCase("SCIENCE")) {
			if (scienceQuestions.isEmpty()) {
				return null;
			}
			ranQuestion = (int) (Math.random() * scienceQuestions.size());
			return scienceQuestions.remove(ranQuestion);
		}

		return null;

	}

}
