package scaladbtest.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
/**
 * Tagging a class with this annotation will cause data to be populated and deleted for each @Test method.
 * By default it will look in src/test/resources/package/of/test/class/TestClass for .dbt files.
 * 
 * @author efenderbosch
 *
 */
public @interface ScalaDbTest {

	/** defaults to <method_name_of_test_being_run>.dbt */
	String script() default "";
	String directory() default "src/test/resources";
}
