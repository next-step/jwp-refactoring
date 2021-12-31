import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class DatabaseCleanup {

    @Autowired
    private DataSource dataSource;

    public void truncate() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement setChecks = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = ?");
             PreparedStatement getTables = connection.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema = SCHEMA()")) {
            try (ResultSet tablesRes = getTables.executeQuery()) {
                setChecks.setBoolean(1, false);
                setChecks.executeUpdate();
                while (tablesRes.next()) {
                    String table = tablesRes.getString(1);
                    try (PreparedStatement truncateTable = connection.prepareStatement("TRUNCATE TABLE " + table + " RESTART IDENTITY")) {
                        truncateTable.executeUpdate();
                    }
                }
            } finally {
                setChecks.setBoolean(1, true);
                setChecks.executeUpdate();
            }
        }
    }
}
