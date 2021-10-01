package fr.epita.quizer.services.dao;

import fr.epita.quizer.datamodel.MCQAnswer;
import fr.epita.quizer.datamodel.MCQQuestion;
import fr.epita.quizer.datamodel.Question;
import fr.epita.quizer.exceptions.CreationException;
import fr.epita.quizer.services.conf.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MCQQuestionDAO {

	private static final String INSERT = "INSERT INTO MCQQUESTION(CHOICE, QUESTIONID, VALID, QUIZID) VALUES (?,?,?,?)";
	private static final String READ_ALL = "select * from MCQQUESTION";
	private static final String UPDATE = "update MCQQUESTION set CHOICE = ? WHERE ID = ?;";
	private static final String DELETE = "delete from MCQQUESTION where id = ?;";

	private Connection getConnection() throws SQLException {

		Configuration conf = Configuration.getInstance();
		Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"),
				conf.getConfValue("db.user"),
				conf.getConfValue("db.password"));
		String schema = connection.getSchema();
		if (!"PUBLIC".equals(schema)){
			throw new RuntimeException("DB connection was not successful");
		}
		String createStatement = "CREATE TABLE IF NOT EXISTS MCQQUESTION(ID IDENTITY PRIMARY KEY, CHOICE VARCHAR(255), QUESTIONID INT REFERENCES QUESTION(ID), VALID INT, QUIZID INT REFERENCES QUIZ(ID));";

		PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
		preparedStatement.execute();

		return connection;

	}

	public void create(MCQQuestion mcqQuestion) throws CreationException {
		try (Connection connection = getConnection()){

			String insertStatement = INSERT;

			PreparedStatement insert = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, mcqQuestion.getChoice());
			insert.setInt(3, mcqQuestion.getQuestion().getId());
			insert.setBoolean(4, mcqQuestion.isValid());
			insert.setInt(5, mcqQuestion.getQuiz().getId());
			insert.execute();

		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public List<MCQAnswer> readALl() throws SQLException{
		String selectStatement = READ_ALL;

		Connection connection = getConnection();
		PreparedStatement select = connection.prepareStatement(selectStatement);
		ResultSet resultSet = select.executeQuery();

		List<MCQAnswer> mcqAnswers = new ArrayList<>();
		while (resultSet.next()){
			int id = resultSet.getInt("ID");
			String choice = resultSet.getString("CHOICE");
			boolean valid = resultSet.getBoolean("VALID");
			MCQAnswer mcqAnswer = new MCQAnswer(choice);
			mcqAnswer.setId(id);
			mcqAnswer.setChoice(choice);
			mcqAnswer.setValid(valid);
			mcqAnswers.add(mcqAnswer);
			System.out.println(mcqAnswer.getId()+" "+mcqAnswer.getChoice()+" "+mcqAnswer.isValid());
		}
		connection.close();
		return mcqAnswers;
	}

	public void update(MCQAnswer mcqAnswer, Integer id) throws CreationException {
		try (Connection connection = getConnection()){

			String updateStatement = UPDATE;

			PreparedStatement update = connection.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
			update.setString(1, mcqAnswer.getChoice());
			update.setBoolean(2, mcqAnswer.isValid());
			update.setInt(3, id);
			update.execute();
		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public void delete(Integer id) throws CreationException {
		try (Connection connection = getConnection()){

			String deleteStatement = DELETE;

			PreparedStatement delete = connection.prepareStatement(deleteStatement, Statement.RETURN_GENERATED_KEYS);
			delete.setInt(1,  id);
			delete.execute();
		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

}
