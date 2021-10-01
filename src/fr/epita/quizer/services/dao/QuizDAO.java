package fr.epita.quizer.services.dao;

import fr.epita.quizer.datamodel.Question;
import fr.epita.quizer.datamodel.Quiz;
import fr.epita.quizer.exceptions.CreationException;
import fr.epita.quizer.services.conf.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizDAO {

	private static final String INSERT = "INSERT INTO QUIZ(TITLE) VALUES (?)";
	private static final String READ_ALL = "select * from QUIZ";
	private static final String UPDATE = "update QUIZ set TITLE = ? WHERE ID = ?;";
	private static final String FIND = "SELECT * FROM QUIZ WHERE ID = ?;";
	private static final String DELETE = "delete from QUIZ where id = ?;";

	private Connection getConnection() throws SQLException {

		Configuration conf = Configuration.getInstance();
		Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"),
				conf.getConfValue("db.user"),
				conf.getConfValue("db.password"));
		String schema = connection.getSchema();
		if (!"PUBLIC".equals(schema)){
			throw new RuntimeException("DB connection was not successful");
		}
		String createStatement = "CREATE TABLE IF NOT EXISTS QUIZ(ID IDENTITY PRIMARY KEY, TITLE VARCHAR(255));";

		PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
		preparedStatement.execute();

		return connection;

	}

	public int create(Quiz quiz) throws CreationException {
		try (Connection connection = getConnection()){

			String insertStatement = INSERT;

			PreparedStatement insert = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, quiz.getTitle());
			insert.execute();
			ResultSet generatedKeys = insert.getGeneratedKeys();
			generatedKeys.next();
			int geneId = generatedKeys.getInt(1);
			return geneId;
		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public List<Quiz> readALl() throws SQLException{
		String selectStatement = READ_ALL;

		Connection connection = getConnection();
		PreparedStatement select = connection.prepareStatement(selectStatement);
		ResultSet resultSet = select.executeQuery();

		List<Quiz> quizzes = new ArrayList<>();
		while (resultSet.next()){
			int id = resultSet.getInt("ID");
			String title = resultSet.getString("TITLE");
			Quiz quiz = new Quiz(title);
			quiz.setId(id);
			quiz.setTitle(title);
			quizzes.add(quiz);
			System.out.println(quiz.getId()+" "+quiz.getTitle());
		}
		connection.close();
		return quizzes;
	}

	public void update(Quiz quiz, Integer id) throws CreationException {
		try (Connection connection = getConnection()){

			String updateStatement = UPDATE;

			PreparedStatement update = connection.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
			update.setString(1, quiz.getTitle());
			update.setInt(2, quiz.getId());
			update.execute();
		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public Quiz find(Integer ident) throws CreationException {
		try (Connection connection = getConnection()){

			String findStatement = FIND;

			PreparedStatement find = connection.prepareStatement(findStatement);
			find.setInt(1, ident);
			ResultSet searchResult = find.executeQuery();
			if (searchResult.next()){
				int id = searchResult.getInt("ID");
				String title = searchResult.getString("TITLE");
				Quiz quiz = new Quiz(title);
				quiz.setId(id);
				return quiz;
			}

		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}

		return this.find(this.create(new Quiz("Quiz 2021"+new Date())));
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
