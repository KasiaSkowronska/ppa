import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Test;

import javax.xml.transform.Result;
import java.sql.*;

/**
 * Created by kosss on 16.03.2017.
 */
public class DatabaseTest {

    @Test
    public void testConnection() throws SQLException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            try (Connection c = DriverManager.getConnection(
                    "jdbc:hsqldb:file:testdb",
                    "SA",
                    "")) {
                DatabaseMetaData md = c.getMetaData();
                md.getTables(null, null, null, null);
            }
        } catch (SQLException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testQueries() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        try (Connection c = DriverManager.getConnection(
                "jdbc:hsqldb:file:testdb",
                "SA",
                "")) {// where is this "testdb" file located in our system?
            c.setAutoCommit(false);
            try {

                DatabaseMetaData md = c.getMetaData();
                try (ResultSet tf = md.getTables(null, null, "TESTTABLE", null)) {
                    if (!tf.next()) {
                        final PreparedStatement ps = c.prepareStatement("CREATE TABLE TESTTABLE " +
                                "(ID INT IDENTITY PRIMARY KEY, SOMENUMBER INT, SOMESTRING VARCHAR(255))");
                        ps.execute();
                    }
                }
            /*final PreparedStatement del = c.prepareStatement("DELETE FROM TESTTABLE");
            del.execute();*/
                final PreparedStatement insert = c.prepareStatement("INSERT INTO TESTTABLE " +
                        "(ID, SOMENUMBER, SOMESTRING) VALUES (NULL, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < 10; i++) {
                    insert.clearParameters();
                    insert.setInt(1, i);
                    insert.setString(2, "number" + i);
                    insert.execute();
                    try (ResultSet keys = insert.getGeneratedKeys()) {
                        keys.next();
                        System.out.println("Created entry: " + keys.getInt(1));
                    }
                }
                final PreparedStatement update = c.prepareStatement("UPDATE TESTTABLE SET SOMESTRING=?" +
                        " WHERE SOMENUMBER=?");
                update.setString(1, "pomidor");
                update.setInt(2, 7);
                update.execute();
                final Statement retrieve = c.createStatement();
                try (ResultSet rs = retrieve.executeQuery("SELECT * FROM TESTTABLE")) {
                    while (rs.next()) {
                        int id = rs.getInt("ID");
                        int num = rs.getInt("SOMENUMBER");
                        String str = rs.getString("SOMESTRING");
                        System.out.println("Entry " + id + ": number " + num + ", string " + str);
                    }
                }
                final PreparedStatement rps = c.prepareStatement("SELECT * FROM TESTTABLE WHERE SOMENUMBER > ? AND SOMENUMBER < ?");
                rps.setInt(1, 5);
                rps.setInt(2, 8);
                try (ResultSet rs = rps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("ID");
                        int num = rs.getInt("SOMENUMBER");
                        String str = rs.getString("SOMESTRING");
                        System.out.println("Select with where: entry " + id + ": number " + num + ", string " + str);
                    }
                }
            } finally {
                c.rollback();
            }
        }
    }

    @Test
    public void testDatabase() throws ClassNotFoundException {
        // all my knowledge from databased used here
        Class.forName("org.hsqldb.jdbc.JDBCDriver"); // set correct database
        try(Connection connection = DriverManager.getConnection(  // make connection to it
                "jdbc:hsqldb:file:newdb", "admin", "")) {
            connection.setAutoCommit(false); // something to deal with transactions
            try{
                DatabaseMetaData metaData = connection.getMetaData();
                try(ResultSet resultSet = metaData.getTables(null, null, "NEWTABLE2", null)) {
                    if (!resultSet.next()){ // if there is no table "NEWTABLE"
                        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE NEWTABLE2 " +
                        "(ID INT IDENTITY PRIMARY KEY, QUESTIONID INT, ANSWERCODE VARCHAR(255))");
                        // create table with: ID, some number and some string.
                        preparedStatement.execute(); // do it now!
                    }
                    final PreparedStatement somethingToInsert = connection.prepareStatement(
                            "INSERT INTO NEWTABLE2(ID, QUESTIONID, ANSWERCODE) VALUES (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    somethingToInsert.clearParameters();
                    somethingToInsert.setInt(1, 2); // e.g. question ID
                    somethingToInsert.setString(2, "C"); // e.g. answer code
                    // above: we refer to next ?'s by typing their indexes (first '?' have index '1')
                    somethingToInsert.execute(); // insert them into NEWTABLE

                    somethingToInsert.clearParameters();
                    somethingToInsert.setInt(1, 5); // e.g. question ID
                    somethingToInsert.setString(2, "A"); // e.g. answer code
                    somethingToInsert.execute();
//                    try (ResultSet keys = somethingToInsert.getGeneratedKeys()){
//                        keys.next(); // returns boolean "isNext"
//                        System.out.println("Created entry id: " + keys.getInt(1));
//                    }
                    // above should print when event is save to database

                    // data were added to database. Now we want to see them. I'd like to ask does everything is OK.
                    // for that reason we'll made another query
                    // we don't need to update data, so I'll skip this lines from testQueries(), going at once to
                    // retrievieng data.
                    // below should be packed into funtion of button presenting data.
                    final Statement retrieveAnswers = connection.createStatement();
                    try (ResultSet everything = retrieveAnswers.executeQuery("SELECT * FROM NEWTABLE2")){
                        while (everything.next()){
                            int dbId = everything.getInt("ID");
                            int questionId = everything.getInt("questionId");
                            String answerCode = everything.getString("answerCode");
                            System.out.println("Database entry number: " + dbId);
                            System.out.println("Question id:  " + questionId);
                            System.out.println("Answer: " + answerCode);
                            System.out.println("-------------------------");
                        }
                    }
                    // we would like to see all data; in case of filtrate them we need to SELECT correct ones, not "*".
                    // example of how to do it is at the end of testQueries();
                }
            } finally {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
