import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by kosss on 16.03.2017.
 */
public class DatabaseTest {
    @Test
    public void testConnection() throws SQLException {
        try {
            Class.forName("org.hsqldb.jbdc.JBFCDriver");
            try (Connection c = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "")){
            // c - zmienna typu autoclosable
            DatabaseMetaData md = c.getMetaData();
            md.getTables(null, null, null, null);
            } // jak wykona się ten blok to wykonywana jest metoda dla autoclosable.
            // to się dzieje i wtedy, kiedy wykona się poprawnie i wtedy kiedy będą wyjątki
            // wszystkie zasoby (implementujące autoclosable) opakowujemy w taki blok.
            // W Javie są strumienie; Takie bytowe.
            // lewa strona implementacjące autoclosable
            // Baza danych i pliki: try with resources zawsze.
        } catch (SQLException e){
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
