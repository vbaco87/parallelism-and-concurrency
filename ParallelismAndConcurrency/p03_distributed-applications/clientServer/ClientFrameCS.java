package clientServer;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.Question;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;

import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import java.awt.Color;

public class ClientFrameCS extends JFrame implements ActionListener {

	/*
	 * GUI Components HERE. Other attributes (connections, channels, ...) must be
	 * placed in the "// ----------------- My stuff starts here --------------"
	 * section around line 280...
	 */

	private JPanel contentPane;
	private JButton btnConnect;
	private JTextField name_textField;
	private JButton btnGeography;
	private JButton btnScience;
	private JLabel lblQuestion;
	private JRadioButton rdbtnAnswer1;
	private JRadioButton rdbtnAnswer2;
	private JRadioButton rdbtnAnswer3;
	private JRadioButton rdbtnAnswer4;
	private JButton btnCheck;
	private JButton btnArt;
	private JButton btnPlayNoMore;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JLabel lblCorrect;
	private JFrame connectionFrame = new JFrame("Connection");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrameCS frame = new ClientFrameCS();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientFrameCS() {
		initComponents();
	}

	private void initComponents() {
		setTitle("Trivial Solitaire C/S socket-based version (by E. Sesa)");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 712, 462);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		btnConnect = new JButton("CONNECT");
		btnConnect.addActionListener(this);

		name_textField = new JTextField();
		name_textField.setText("write your name here...");
		name_textField.setColumns(10);
		name_textField.addActionListener(this);

		btnGeography = new JButton("GEOGRAPHY");
		btnGeography.addActionListener(this);
		btnGeography.setEnabled(false);

		btnScience = new JButton("SCIENCE");
		btnScience.addActionListener(this);
		btnScience.setEnabled(false);

		lblQuestion = new JLabel("Question goes here");
		lblQuestion.setEnabled(false);
		lblQuestion.setFont(new Font("Tahoma", Font.BOLD, 16));

		rdbtnAnswer1 = new JRadioButton("answer 1");
		rdbtnAnswer1.addActionListener(this);
		rdbtnAnswer1.setEnabled(false);
		buttonGroup.add(rdbtnAnswer1);

		rdbtnAnswer2 = new JRadioButton("answer 2");
		rdbtnAnswer2.addActionListener(this);
		rdbtnAnswer2.setEnabled(false);
		buttonGroup.add(rdbtnAnswer2);

		rdbtnAnswer3 = new JRadioButton("answer 3");
		rdbtnAnswer3.addActionListener(this);
		rdbtnAnswer3.setEnabled(false);
		buttonGroup.add(rdbtnAnswer3);

		rdbtnAnswer4 = new JRadioButton("answer 4");
		rdbtnAnswer4.addActionListener(this);
		rdbtnAnswer4.setEnabled(false);
		buttonGroup.add(rdbtnAnswer4);

		btnCheck = new JButton("CHECK");
		btnCheck.addActionListener(this);
		btnCheck.setEnabled(false);

		btnArt = new JButton("ART");
		btnArt.setEnabled(false);
		btnArt.addActionListener(this);

		btnPlayNoMore = new JButton("PLAY NO MORE");
		btnPlayNoMore.addActionListener(this);
		btnPlayNoMore.setEnabled(false);

		lblCorrect = new JLabel("CORRECT!");
		lblCorrect.setForeground(Color.GREEN);
		lblCorrect.setVisible(false);
		lblCorrect.setFont(new Font("Tahoma", Font.BOLD, 16));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(rdbtnAnswer4)
								.addComponent(rdbtnAnswer1)
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(btnConnect)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(name_textField,
												GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(btnGeography).addGap(18)
										.addComponent(btnScience).addGap(18).addComponent(btnArt))
								.addComponent(lblQuestion).addComponent(btnCheck).addComponent(btnPlayNoMore)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(rdbtnAnswer3).addComponent(rdbtnAnswer2))
										.addGap(120).addComponent(lblCorrect)))
						.addContainerGap(371, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane
						.createParallelGroup(Alignment.BASELINE).addComponent(btnConnect).addComponent(name_textField,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(39)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnGeography)
						.addComponent(btnScience).addComponent(btnArt))
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(29).addComponent(lblQuestion).addGap(18)
								.addComponent(rdbtnAnswer1).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(rdbtnAnswer2).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(rdbtnAnswer3).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(rdbtnAnswer4).addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnCheck).addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
								.addComponent(btnPlayNoMore).addGap(26))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(109).addComponent(lblCorrect)
								.addContainerGap()))));
		contentPane.setLayout(gl_contentPane);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == rdbtnAnswer4) {
			rdbtnAnswer4actionPerformed(arg0);
		}
		if (arg0.getSource() == rdbtnAnswer3) {
			rdbtnAnswer3actionPerformed(arg0);
		}
		if (arg0.getSource() == rdbtnAnswer2) {
			rdbtnAnswer2actionPerformed(arg0);
		}
		if (arg0.getSource() == rdbtnAnswer1) {
			rdbtnAnswer1actionPerformed(arg0);
		}
		if (arg0.getSource() == btnCheck) {
			btnCheckactionPerformed(arg0);
		}
		if (arg0.getSource() == btnPlayNoMore) {
			btnPlayNoMoreactionPerformed(arg0);
		}
		if (arg0.getSource() == btnArt) {
			btnArtactionPerformed(arg0);
		}
		if (arg0.getSource() == btnScience) {
			btnScienceactionPerformed(arg0);
		}
		if (arg0.getSource() == btnGeography) {
			btnGeographyactionPerformed(arg0);
		}
		if (arg0.getSource() == btnConnect) {
			btnConnectactionPerformed(arg0);
		}
		if (arg0.getSource() == this.name_textField) {
			btnConnectactionPerformed(arg0);
		}

	}

	protected void btnConnectactionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		try {
			connect();
			sendRequest("HELLO " + name_textField.getText());

			this.btnConnect.setEnabled(false);
			this.btnGeography.setEnabled(true);
			this.btnArt.setEnabled(true);
			this.btnScience.setEnabled(true);
			this.btnPlayNoMore.setEnabled(true);
			this.lblCorrect.setVisible(true);

			JOptionPane.showMessageDialog(connectionFrame, "Server says: " + receiveReply());

		} catch (IOException ioException) {
			JOptionPane.showMessageDialog(connectionFrame, "CONNECTIONS ERROR, TRY AGAIN", "ERROR",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void btnGeographyactionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		try {

			sendRequest("NEXT " + "GEO");
			getQuestionsActions();
			displayQuestion(receiveReply());

			lastQuestionSubject = GEO;

		} catch (IOException ioException) {
		}

	}

	protected void btnScienceactionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		try {

			sendRequest("NEXT " + "SCIENCE");
			getQuestionsActions();
			displayQuestion(receiveReply());

			lastQuestionSubject = SCIENCE;
		} catch (IOException ioException) {
		}
	}

	protected void btnArtactionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		try {

			sendRequest("NEXT " + "ART");
			getQuestionsActions();
			displayQuestion(receiveReply());

			lastQuestionSubject = ART;
		} catch (IOException ioException) {
		}
	}

	protected void btnPlayNoMoreactionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		try {
			disconnect();
		} catch (Exception ex) {
		}
		System.exit(0);
	}

	protected void rdbtnAnswer1actionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		selectedAnswer(1);
		this.btnCheck.setEnabled(true);
	}

	protected void rdbtnAnswer2actionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		selectedAnswer(2);
		this.btnCheck.setEnabled(true);
	}

	protected void rdbtnAnswer3actionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		selectedAnswer(3);
		this.btnCheck.setEnabled(true);
	}

	protected void rdbtnAnswer4actionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		selectedAnswer(4);
		this.btnCheck.setEnabled(true);
	}

	protected void btnCheckactionPerformed(ActionEvent arg0) {
		/* COMPLETE */
		if (selectedAnswer == correctAnswer) {
			lblCorrect.setForeground(Color.GREEN);
			lblCorrect.setText(name_textField.getText() + " your answer is correct!");
		} else {
			lblCorrect.setForeground(Color.RED);
			lblCorrect.setText(name_textField.getText() + " your answer is incorrect!");
		}
		this.lblCorrect.setVisible(true);
		
		this.lblQuestion.setEnabled(false);
		this.btnCheck.setEnabled(false);
		enableQuestionSubjectBtn();

		Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
		while (enumeration.hasMoreElements()) {
			enumeration.nextElement().setEnabled(false);
		}

	}

	// ----------------- My stuff starts here --------------

	private BufferedReader inputChannel;
	private PrintWriter outputChannel;
	private Socket connection;

	private int correctAnswer;
	private int selectedAnswer;
	private Question question;

	private boolean artQuestions = true;
	private boolean geoQuestions = true;
	private boolean scienceQuestions = true;
	private int lastQuestionSubject = -1;

	private static final int ART = 0;
	private static final int GEO = 1;
	private static final int SCIENCE = 2;
	/* COMPLETE: add other necessary attributes */

	/*
	 * COMPLETE: add other auxiliary private methods: to request a question to the
	 * server, to display a question and its four answers...
	 */
	private boolean validateEndTypeQuestion(String questionToAsk) {

		if (questionToAsk.equalsIgnoreCase("NO MORE QUESTIONS OF THIS TYPE")) {
			return true;
		}
		return false;

	}

	private void displayQuestion(String questionToAsk) {
		// TODO Auto-generated method stub

		if (!validateEndTypeQuestion(questionToAsk)) {

			question = Question.fromString(questionToAsk);
			this.lblQuestion.setText(question.getTheQuestion());
			String[] answers = question.getAnswers();
			Enumeration<AbstractButton> enumeration = buttonGroup.getElements();

			for (int i = 0; enumeration.hasMoreElements(); i++) {
				enumeration.nextElement().setText(answers[i]);
			}

			correctAnswer = question.getCorrect();

		} else {
			JOptionPane.showMessageDialog(connectionFrame, "No more questions of this type", "Questions",
					JOptionPane.WARNING_MESSAGE);

			disableQuestionButton(lastQuestionSubject);
			enableQuestionSubjectBtn();

			Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
			while (enumeration.hasMoreElements()) {
				enumeration.nextElement().setEnabled(false);
			}
			buttonGroup.clearSelection();
			btnCheck.setEnabled(false);
			lblQuestion.setEnabled(false);
		}

	}

	private void disableQuestionButton(int lastQuestionSubject) {

		switch (lastQuestionSubject) {
		case ART:
			artQuestions = false;
			break;
		case GEO:
			geoQuestions = false;
			break;
		case SCIENCE:
			scienceQuestions = false;
			break;
		}

	}

	private void enableQuestionSubjectBtn() {
		if (artQuestions) {
			this.btnArt.setEnabled(true);
		}
		if (geoQuestions) {
			this.btnGeography.setEnabled(true);
		}
		if (scienceQuestions) {
			this.btnScience.setEnabled(true);
		}

	}

	private void getQuestionsActions() {

		this.lblQuestion.setEnabled(true);
		this.lblCorrect.setVisible(false);

		this.btnGeography.setEnabled(false);
		this.btnArt.setEnabled(false);
		this.btnScience.setEnabled(false);

		buttonGroup.clearSelection();
		Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
		while (enumeration.hasMoreElements()) {
			enumeration.nextElement().setEnabled(true);
		}

	}

	private void selectedAnswer(int i) {
		selectedAnswer = i;
	}

	private void connect() throws IOException {
		connection = new Socket("localhost", 4445);
		inputChannel = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		outputChannel = new PrintWriter(connection.getOutputStream(), true);
	}

	private void disconnect() throws IOException {
		sendRequest("STOP now");
		inputChannel.close();
		outputChannel.close();
		connection.close();
	}

	private String receiveReply() throws IOException {
		return inputChannel.readLine();
	}

	private void sendRequest(String request) throws IOException {
		outputChannel.println(request);
	}

}