package scaladbtest.test;

import java.io.File;
import java.sql.DriverManager;

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

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				ScalaDbTest annotation = description.getAnnotation(ScalaDbTest.class);
				if (annotation == null) {
					annotation = description.getTestClass().getAnnotation(ScalaDbTest.class);
				}
				if (annotation == null) {
					base.evaluate();
					return;
				}
				String script = annotation.script();
				if ("".equals(script)) {
					script = description.getMethodName();
				}
				String baseDir = annotation.directory() + File.separator + description.getClassName().replace('.', File.separatorChar);
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