package scaladbtest.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ScalaDbTest {

	String script() default "";
	String directory() default "src/test/resources";
	boolean skip() default false;
	String url() default "jdbc:postgresql:test";
}
