package scaladbtest.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ScalaDbTestRule implements TestRule {
	
	private DataSource dataSource;

	public ScalaDbTestRule(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Statement apply(final Statement base, final Description description) {

		SkipScalaDbTest skip = description.getAnnotation(SkipScalaDbTest.class);
		if (skip != null) {
			return base;
		}

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
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

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				SkipScalaDbTest skip = description.getAnnotation(SkipScalaDbTest.class);
				if (skip != null) {
					base.evaluate();
					return;
				}
				String script = description.getMethodName();
				String baseDir = "src/test/resources/" + description.getClassName().replace('.', File.separatorChar);
				ScalaDbTester scalaDbTester = new ScalaDbTester(dataSource, baseDir);
				if (new File(baseDir, "common.dbt").canRead()) {
					scalaDbTester.onBefore("common.dbt");
				}
				scalaDbTester.onBefore(script + ".dbt");
				try {
					base.evaluate();
				} finally {
					scalaDbTester.onAfter();
				}
			}
		};
	}
}
