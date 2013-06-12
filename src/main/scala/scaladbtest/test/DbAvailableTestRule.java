package scaladbtest.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DbAvailableTestRule implements TestRule {

	private DataSource dataSource;

	public DbAvailableTestRule(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Statement apply(Statement base, Description description) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			return base;
		} catch (SQLException e) {
			System.out.println("Warning: skipping " + description + " due to " + e.getMessage());
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					// no-op
				}
			};
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException ignore) {
					// ignore
				}
			}
		}
	}
}
