package fr.epita.quizer.services.dao;

import fr.epita.quizer.datamodel.Question;
import fr.epita.quizer.datamodel.Student;
import fr.epita.quizer.exceptions.CreationException;
import fr.epita.quizer.services.conf.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

	private static final String INSERT = "INSERT INTO STUDENT(ID, STUDENTNAME) VALUES (?,?)";
	private static final String READ_ALL = "select * from STUDENT";
	private static final String UPDATE = "update STUDENT set STUDENTNAME = ? WHERE ID = ?;";
	private static final String DELETE = "delete from STUDENT where id = ?;";
	private static final String FIND = "SELECT ID, STUDENTNAME FROM STUDENT where ID = ?;";

	private Connection getConnection() throws SQLException {

		Configuration conf = Configuration.getInstance();
		Connection connection = DriverManager.getConnection(conf.getConfValue("db.url"),
				conf.getConfValue("db.user"),
				conf.getConfValue("db.password"));
		String schema = connection.getSchema();
		if (!"PUBLIC".equals(schema)){
			throw new RuntimeException("DB connection was not successful");
		}
		String createStatement = "CREATE TABLE IF NOT EXISTS STUDENT(ID VARCHAR(255) PRIMARY KEY, STUDENTNAME VARCHAR(255));";

		PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
		preparedStatement.execute();

		return connection;

	}

	public void create(Student student) throws CreationException {
		try (Connection connection = getConnection()){

			String insertStatement = INSERT;

			PreparedStatement insert = connection.prepareStatement(insertStatement);
			insert.setString(1, student.getId());
			insert.setString(2, student.getName());
			insert.execute();

		}catch (SQLException sqle){
			CreationException creationException = new CreationException();
			creationException.initCause(sqle);
			throw creationException;
		}
	}

	public List<Student> readALl() throws SQLException{
		String selectStatement = READ_ALL;

		Connection connection = getConnection();
		PreparedStatement select = connection.prepareStatement(selectStatement);
		ResultSet resultSet = select.executeQuery();

		List<Student> students = new ArrayList<>();
		while (resultSet.next()){
			String id = resultSet.getString("ID");
			String name = resultSet.getString("STUDENTNAME");
			Student student = new Student(name);
			student.setId(id);
			students.add(student);
			System.out.println(student.getId()+" "+student.getName());
		}
		connection.close();
		return students;
	}

	public void update(Student student, Integer id) throws CreationException {
		try (Connection connection = getConnection()){

			String updateStatement = UPDATE;

			PreparedStatement update = connection.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
			update.setString(1, student.getName());
			update.setInt(2, id);
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

	public List<Student> search(String id) throws Exception {

		String searchStatement = FIND;

		Connection connection = getConnection();
		PreparedStatement search = connection.prepareStatement(searchStatement);
		search.setString(1, id);
		ResultSet searchResult = search.executeQuery();

		List<Student> searchList = new ArrayList<>();
		while (searchResult.next()) {
			String name = searchResult.getString("STUDENTNAME");
			Student student = new Student(id);
			student.setId(id);
			student.setName(name);
			searchList.add(student);
			System.out.println(student.getId() + " " + student.getName());
		}

		connection.close();
		return searchList;

	}

}
