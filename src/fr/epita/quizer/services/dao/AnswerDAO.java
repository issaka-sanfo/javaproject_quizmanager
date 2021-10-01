package fr.epita.quizer.services.dao;

import fr.epita.quizer.datamodel.Answer;
import fr.epita.quizer.datamodel.Question;
import fr.epita.quizer.exceptions.CreationException;
import fr.epita.quizer.services.conf.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO {

	private static final String INSERT = "INSERT INTO ANSWER(TEXT) VALUES (?)";
	private static final String READ_ALL = "select * from ANSWER";
	private static final String UPDATE = "update ANSWER set TEXT = ? WHERE ID = ?;";
	private static final String DELETE = "delete from ANSWER where id = ?;";

	private Connection getConnection() throws SQLException {

		Configuration conf = Configuration.getInstance();
		Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"),
				conf.getConfValue("db.user"),
				conf.getConfValue("db.password"));
		String schema = connection.getSchema();
		if (!"PUBLIC".equals(schema)){
			throw new RuntimeException("DB connection was not successful");
		}
		String createStatement = "CREATE TABLE IF NOT EXISTS ANSWER(ID IDENTITY PRIMARY KEY, TEXT VARCHAR(255));";

		PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
		preparedStatement.execute();

		return connection;

	}

	public void create(Answer answer) throws CreationException {
		try (Connection connection = getConnection()){

			String insertStatement = INSERT;

			PreparedStatement insert = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
			insert.setString(1, answer.getText());
			insert.execute();

		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public List<Answer> readALl() throws SQLException{
		String selectStatement = READ_ALL;

		Connection connection = getConnection();
		PreparedStatement select = connection.prepareStatement(selectStatement);
		ResultSet resultSet = select.executeQuery();

		List<Answer> answers = new ArrayList<>();
		while (resultSet.next()){
			int id = resultSet.getInt("ID");
			String text = resultSet.getString("TEXT");
			Answer answer = new Answer(text);
			answer.setId(id);
			answer.setText(text);
			answers.add(answer);
			System.out.println(answer.getId()+" "+answer.getText());
		}
		connection.close();
		return answers;
	}

	public void update(Answer answer, Integer id) throws CreationException {
		try (Connection connection = getConnection()){

			String updateStatement = UPDATE;

			PreparedStatement update = connection.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
			update.setString(1, answer.getText());
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
