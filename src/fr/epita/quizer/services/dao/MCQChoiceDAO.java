package fr.epita.quizer.services.dao;

import fr.epita.quizer.datamodel.MCQAnswer;
import fr.epita.quizer.datamodel.MCQChoice;
import fr.epita.quizer.datamodel.Question;
import fr.epita.quizer.exceptions.CreationException;
import fr.epita.quizer.services.conf.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MCQChoiceDAO {

	private static final String INSERT = "INSERT INTO MCQCHOICE(CHOICE, QUESTIONID, VALID, QUIZID) VALUES (?,?,?,?)";
	private static final String READ_ALL = "select * from MCQCHOICE";
	private static final String UPDATE = "update MCQCHOICE set CHOICE = ? WHERE ID = ?;";
	private static final String FIND = "SELECT * FROM MCQCHOICE WHERE ID = ?;";
	private static final String DELETE = "delete from MCQCHOICE where id = ?;";
	private static final String SEARCH = "SELECT * FROM MCQCHOICE where QUESTIONID = ?;";
	QuestionDAO dao = new QuestionDAO();
	QuizDAO quizDAO = new QuizDAO();

	private Connection getConnection() throws SQLException {

		Configuration conf = Configuration.getInstance();
		Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"),
				conf.getConfValue("db.user"),
				conf.getConfValue("db.password"));
		String schema = connection.getSchema();
		if (!"PUBLIC".equals(schema)) {
			throw new RuntimeException("DB connection was not successful");
		}
		String createStatement = "CREATE TABLE IF NOT EXISTS MCQCHOICE(ID IDENTITY PRIMARY KEY, CHOICE VARCHAR(255), QUESTIONID INT REFERENCES QUESTION(ID), VALID INT, QUIZID INT REFERENCES QUIZ(ID));";

		PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
		preparedStatement.execute();

		return connection;

	}

	public void create(MCQChoice mcqChoice) throws CreationException {
		try (Connection connection = getConnection()) {

			String insertStatement = INSERT;

			PreparedStatement insert = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, mcqChoice.getChoice());
			insert.setInt(2, mcqChoice.getQuestion().getId());
			insert.setBoolean(3, mcqChoice.isValid());
			insert.setInt(4, mcqChoice.getQuiz().getId());
			insert.execute();

		} catch (SQLException sqle) {
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public List<MCQChoice> readALl() throws SQLException {
		String selectStatement = READ_ALL;

		Connection connection = getConnection();
		PreparedStatement select = connection.prepareStatement(selectStatement);
		ResultSet resultSet = select.executeQuery();

		List<MCQChoice> mcqChoices = new ArrayList<>();
		while (resultSet.next()) {
			int id = resultSet.getInt("ID");
			String choice = resultSet.getString("CHOICE");
			boolean valid = resultSet.getBoolean("VALID");
			MCQChoice mcqChoice = new MCQChoice(choice);
			mcqChoice.setId(id);
			mcqChoice.setChoice(choice);
			mcqChoice.setValid(valid);
			mcqChoices.add(mcqChoice);
			System.out.println("ID:" + mcqChoice.getId() + " Value:" + mcqChoice.getChoice() + " Validity:" + mcqChoice.isValid());
		}
		connection.close();
		return mcqChoices;
	}

	public void update(MCQChoice mcqChoice, Integer id) throws CreationException {
		try (Connection connection = getConnection()) {

			String updateStatement = UPDATE;

			PreparedStatement update = connection.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
			update.setString(1, mcqChoice.getChoice());
			update.setBoolean(2, mcqChoice.isValid());
			update.setInt(3, id);
			update.execute();
		} catch (SQLException sqle) {
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public MCQChoice find(Integer ident) throws CreationException {
		try (Connection connection = getConnection()){

			String findStatement = FIND;

			PreparedStatement find = connection.prepareStatement(findStatement);
			find.setInt(1, ident);
			ResultSet searchResult = find.executeQuery();
			if (searchResult.next()){
				int id = searchResult.getInt("ID");
				String choice = searchResult.getString("CHOICE");
				int questionId = searchResult.getInt("QUESTIONID");
				int quizId = searchResult.getInt("QUIZID");
				boolean valid = searchResult.getBoolean("VALID");
				MCQChoice mcqChoice = new MCQChoice(choice);
				mcqChoice.setId(id);
				mcqChoice.setQuestion(dao.find(questionId));
				mcqChoice.setQuiz(quizDAO.find(quizId));
				mcqChoice.setValid(valid);
				return mcqChoice;
			}

		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
		return new MCQChoice("");
	}

	public void delete(Integer id) throws CreationException {
		try (Connection connection = getConnection()) {

			String deleteStatement = DELETE;

			PreparedStatement delete = connection.prepareStatement(deleteStatement, Statement.RETURN_GENERATED_KEYS);
			delete.setInt(1, id);
			delete.execute();

		} catch (SQLException sqle) {
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public List<MCQChoice> search(Integer idQuestion) throws Exception {

		String searchStatement = SEARCH;

		Connection connection = getConnection();
		PreparedStatement search = connection.prepareStatement(searchStatement);
		search.setInt(1, idQuestion);
		ResultSet searchResult = search.executeQuery();

		List<MCQChoice> searchList = new ArrayList<>();
		while (searchResult.next()) {
			int id = searchResult.getInt("ID");
			String choice = searchResult.getString("CHOICE");
			int questionId = searchResult.getInt("QUESTIONID");
			int quizId = searchResult.getInt("QUIZID");
			boolean valid = searchResult.getBoolean("VALID");
			MCQChoice mcqChoice = new MCQChoice(choice);
			mcqChoice.setId(id);
			mcqChoice.setQuestion(dao.find(questionId));
			mcqChoice.setQuiz(quizDAO.find(quizId));
			mcqChoice.setValid(valid);
			searchList.add(mcqChoice);
			/*System.out.println("ID:" + mcqChoice.getId() + " Value:" + mcqChoice.getChoice() + " Valid:" + mcqChoice.isValid() +
					" QuestionID" + mcqChoice.getQuestion().getId() + " QuizID:" + mcqChoice.getQuiz().getId());*/
		}

		connection.close();
		return searchList;


	}
}