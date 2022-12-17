package kitchenpos.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleanUtils implements InitializingBean {

	final String TABLE_NAME = "TABLE_NAME";

	@Autowired
	DataSource dataSource;

	final List<String> tableNames = new ArrayList<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			tableNames.addAll(getTables(connection));
		}
	}

	public void cleanUp() {
		try (Connection connection = dataSource.getConnection()) {
			execute(connection, "SET REFERENTIAL_INTEGRITY FALSE");
			tableNames.stream().filter(this::isNotFlywayTable)
				.forEach(tableName -> execute(connection, "TRUNCATE TABLE " + tableName ));
			execute(connection,"SET REFERENTIAL_INTEGRITY TRUE");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isNotFlywayTable(String tableName) {
		return !tableName.startsWith("FLYWAY") && !tableName.startsWith("flyway");
	}

	private static void execute(Connection connection, String sql) {
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<String> getTables(Connection connection) throws SQLException {
		List<String> tableNames = new ArrayList<>();

		try (ResultSet resultSet = connection.getMetaData()
			.getTables(null, "PUBLIC", "%", null)) {
			while (resultSet.next()) {
				tableNames.add(resultSet.getString(TABLE_NAME));
			}
		}
		return tableNames;
	}
}

