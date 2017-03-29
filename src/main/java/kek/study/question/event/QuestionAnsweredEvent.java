package kek.study.question.event;

import kek.study.question.IQuestion;
import kek.study.question.Answer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


public class QuestionAnsweredEvent {

	protected IQuestion question;
	protected Answer answer;

	public QuestionAnsweredEvent(IQuestion question, Answer answer) {
		this.question = question;
		this.answer = answer;
	}

	public IQuestion getQuestion() {
		return question;
	}

	public void setQuestion(IQuestion question) {
		this.question = question;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public void saveToFile() {

		try (FileWriter fw = new FileWriter("answers.answ", true);
			 BufferedWriter bw = new BufferedWriter(fw);
			 PrintWriter out = new PrintWriter(bw)) {
			out.println("Id: " + question.getId() + " answer: " + answer.getAnswer());
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
	}

	public void saveToDatabase() throws ClassNotFoundException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver"); // set correct database
		try (Connection connection = DriverManager.getConnection(  // make connection to it
				"jdbc:hsqldb:file:newdb", "admin", "")) {
			connection.setAutoCommit(false); // something to deal with transactions, dunno exactly what it is
			try {
				DatabaseMetaData metaData = connection.getMetaData();
				try (ResultSet resultSet = metaData.getTables(null, null, "ANSWERS_TABLE", null)) {
					if (!resultSet.next()) { // if there is no table "ANSWERS_TABLE"
						PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE ANSWERS_TABLE " +
								"(ID INT IDENTITY PRIMARY KEY, QUESTIONID INT, ANSWERCODE VARCHAR(255))");
						preparedStatement.execute(); // create it now!
					}
					final PreparedStatement somethingToInsert = connection.prepareStatement(
							"INSERT INTO ANSWERS_TABLE(ID, QUESTIONID, ANSWERCODE) VALUES (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
					somethingToInsert.clearParameters();
					somethingToInsert.setInt(1, Integer.parseInt(question.getId())); // question ID
					somethingToInsert.setString(2, answer.getAnswer()); // answer code
					somethingToInsert.execute(); // save them to our database
					try (ResultSet keys = somethingToInsert.getGeneratedKeys()) {
						keys.next(); // returns boolean "isNext"
						System.out.println("Created entry id: " + keys.getInt(1));
						// it informs that entry with that id was created
					}
					// now presenting data is here, after each event save something to database, but should
					// be moved to corresponding button action event.
					// IT WORKS HERE, BUT: this code should works in presentData() in MainController.
//					try {
//						final Statement retrieveAnswers = connection.createStatement();
//						try (ResultSet everything = retrieveAnswers.executeQuery("SELECT * FROM ANSWERS_TABLE")) {
//							while (everything.next()) {
//								int dbId = everything.getInt("ID");
//								int questionId = everything.getInt("QUESTIONID");
//								String answerCode = everything.getString("ANSWERCODE");
//								System.out.println("Database entry number: " + dbId);
//								System.out.println("Question id:  " + questionId);
//								System.out.println("Answer: " + answerCode);
//								System.out.println("-------------------------");
//							}
//						}
//					} catch (SQLException e) {
//						e.printStackTrace();
					}
				} finally {
					connection.rollback();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

}