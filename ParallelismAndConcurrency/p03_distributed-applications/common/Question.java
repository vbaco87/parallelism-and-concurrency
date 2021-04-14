package common;

import java.util.*;
import java.io.*;

public class Question implements Serializable {
	
	
	private String type; // The type of the question: GEO, ...
	private String theQuestion; // text of the question
	private String [] answers = new String[4]; // the four possible answers
	private int correct; // index of the correct answer
	
	public Question(String type, String theQuestion, String [] answers, int correct) {
		this.type = type;
		this.theQuestion = theQuestion;
		this.answers = answers;
		this.correct = correct;
	}
	
	
	public String getType() {return this.type;}
	public String getTheQuestion () {return this.theQuestion;}
	public String [] getAnswers () {return this.answers;}
	public int getCorrect() {return this.correct;}
	
	// generates a String representation of a question
	public  String toString ()  {
		String result = this.type+":"+this.theQuestion+":";
		for(String s : answers) {
			result += s+":";
		}
		return result+correct;
	}
	
	// makes a question out of a String (inverse of toString)
	public static Question fromString (String s) {
		String [] contents = s.split(":");
		String [] answers = new String[4];
		for (int i=2; i<=5; i++) {
			answers[i-2]=contents[i];
		}
		return new Question(contents[0], contents[1], 
				            answers, 
				            Integer.parseInt(contents[6]));
	}
	
	// loads questions from a file and returns them in a list
	public static List<Question> fromFile (File f) throws IOException {
		LinkedList<Question> result = new LinkedList<Question>();
		BufferedReader bur = new BufferedReader(new FileReader(f));
		String line = bur.readLine();
		while (line!=null) {
			result.add(fromString(line));
			line = bur.readLine();
		}
		bur.close();
		return result;
	}

}
