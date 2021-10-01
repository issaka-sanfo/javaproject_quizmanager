import java.sql.*;

public class H2ConnectionTests {

    public static void main(String[] args) throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:h2:" + "./DB/testdb", "root", "root");
        String schema = connection.getSchema();
        if (!"PUBLIC".equals(schema)) {
            throw new Exception("COnnection was not successful");
        }

        System.out.println(schema);

        String createStatement = "CREATE TABLE IF NOT EXISTS QUESTION(ID IDENTITY PRIMARY KEY, TITLE VARCHAR(255), DIFFICULTY INT);";

        PreparedStatement preparedStatement = connection.prepareStatement(createStatement);
        boolean isExecuted = preparedStatement.execute();

        String insertStatement = "INSERT INTO QUESTION(TITLE, DIFFICULTY) VALUES(?,?)";
        PreparedStatement insert = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        insert.setString(1, "What is a JavaDoc?");
        insert.setInt(2, 2);
        insert.execute();

        ResultSet generatedKeys = insert.getGeneratedKeys();
        generatedKeys.next();
        int generatedId = generatedKeys.getInt(1);
        System.out.println(generatedId);
        String selectStatement = "SELECT ID, TITLE, DIFFICULTY FROM QUESTION";
        PreparedStatement select = connection.prepareStatement(selectStatement);
        ResultSet resultSet = select.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt("ID");
            int difficulty = resultSet.getInt("DIFFICULTY");
            String title = resultSet.getString("TITLE");
            System.out.println(id+" "+difficulty+" "+title);
        }
    }
}
