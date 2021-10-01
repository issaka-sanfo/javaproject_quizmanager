package fr.epita.quizer.services.dao;

import fr.epita.quizer.datamodel.Question;
import fr.epita.quizer.exceptions.CreationException;
import fr.epita.quizer.services.conf.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

	private static final String INSERT = "INSERT INTO QUESTION(TITLE, DIFFICULTY, TOPICS) VALUES (?,?,?)";
	private static final String READ_ALL = "select * from QUESTION";
	private static final String UPDATE = "update QUESTION set TITLE = ?, DIFFICULTY= ? WHERE ID = ?;";
	private static final String FIND = "SELECT * FROM QUESTION WHERE ID = ?;";
	private static final String DELETE = "delete from QUESTION where id = ?;";
	private static final String SEARCH = "SELECT ID, TITLE, DIFFICULTY, TOPICS FROM QUESTION where TOPICS = ?;";

	private Connection getConnection() throws SQLException {

		Configuration conf = Configuration.getInstance();
		Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"),
				conf.getConfValue("db.user"),
				conf.getConfValue("db.password"));
		String schema = connection.getSchema();
		if (!"PUBLIC".equals(schema)){
			throw new RuntimeException("DB connection was not successful");
		}
		String createStatement = "CREATE TABLE IF NOT EXISTS QUESTION(ID IDENTITY PRIMARY KEY, TITLE VARCHAR(255), DIFFICULTY INT, TOPICS VARCHAR(510));";

		PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
		preparedStatement.execute();

		return connection;

	}

	public void create(Question question) throws CreationException {
		try (Connection connection = getConnection()){

			String insertStatement = INSERT;

			PreparedStatement insert = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, question.getQuestion());
			insert.setInt(2, question.getDifficulty());
			insert.setString(3, question.getTopics());
			insert.execute();

		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public List<Question> readALl() throws SQLException{
		String selectStatement = READ_ALL;

		Connection connection = getConnection();
		PreparedStatement select = connection.prepareStatement(selectStatement);
		ResultSet resultSet = select.executeQuery();

		List<Question> questions = new ArrayList<>();
		while (resultSet.next()){
			int id = resultSet.getInt("ID");
			int difficulty = resultSet.getInt("DIFFICULTY");
			String title = resultSet.getString("TITLE");
			String topic = resultSet.getString("TOPICS");
			Question question = new Question(title);
			question.setId(id);
			question.setDifficulty(difficulty);
			question.setTopics(topic);
			questions.add(question);

		}
		connection.close();
		return questions;
	}

	public void update(Question question, Integer id) throws CreationException {
		try (Connection connection = getConnection()){

			String updateStatement = UPDATE;

			PreparedStatement update = connection.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
			update.setString(1, question.getQuestion());
			update.setInt(2, question.getDifficulty());
			update.setInt(3, id);
			update.execute();
		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public Question find(Integer ident) throws CreationException {
		try (Connection connection = getConnection()){

			String findStatement = FIND;

			PreparedStatement find = connection.prepareStatement(findStatement);
			find.setInt(1, ident);
			ResultSet searchResult = find.executeQuery();
			if (searchResult.next()){
				int id = searchResult.getInt("ID");
				int difficulty = searchResult.getInt("DIFFICULTY");
				String title = searchResult.getString("TITLE");
				String setTopics = searchResult.getString("TOPICS");
				Question questionItem = new Question(title);
				questionItem.setId(id);
				questionItem.setDifficulty(difficulty);
				questionItem.setTopics(setTopics);
				return questionItem;
			}

		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
		return new Question("");
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

	public List<Question> search(String topics) throws Exception {

		String searchStatement = SEARCH;

		Connection connection = getConnection();
		PreparedStatement search = connection.prepareStatement(searchStatement);
		search.setString(1, topics);
		ResultSet searchResult = search.executeQuery();

		List<Question> searchList = new ArrayList<>();
		while (searchResult.next()) {
			int id = searchResult.getInt("ID");
			int difficulty = searchResult.getInt("DIFFICULTY");
			String title = searchResult.getString("TITLE");
			String setTopics = searchResult.getString("TOPICS");
			Question questionItem = new Question(topics);
			questionItem.setId(id);
			questionItem.setDifficulty(difficulty);
			questionItem.setQuestion(title);
			questionItem.setTopics(setTopics);
			searchList.add(questionItem);
			System.out.println(questionItem.getId() + " " + questionItem.getQuestion() + " " + questionItem.getDifficulty() + " " + questionItem.getTopics());
		}

		connection.close();
		return searchList;

	}

}
