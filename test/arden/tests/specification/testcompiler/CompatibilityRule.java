package arden.tests.specification.testcompiler;

import static org.junit.Assume.assumeTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * JUnit rule to check all tests for the <code>@Compatibility</code> annotation
 * and skip backward compatibility tests for compilers which do not support the
 * given version. <br>
 * Usage:
 * 
 * <pre>
 * &#064;Rule
 * public CompatibilityRule rule = new CompatibilityRule();
 * 
 * &#064;Test
 * &#064;Compatibility(ArdenVersion.V1)
 * public void test() {
 * }
 * </pre>
 */
public class CompatibilityRule implements MethodRule {

	private TestCompiler compiler;

	public CompatibilityRule(TestCompiler compiler) {
		this.compiler = compiler;
	}

	@Override
	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		Statement result = base;

		Compatibility annotation = method.getAnnotation(Compatibility.class);
		if (annotation != null) {
			ArdenVersion version = annotation.value();
			if (!compiler.isVersionSupported(version)) {
				String message = "Compiler doesn't support backward compatibility tests for version: "
						+ version.toString();
				result = new IgnoreStatement(message);
			}
		}

		return result;
	}

	/**
	 * Use this to specify the required Arden Syntax version to run a test
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD })
	public @interface Compatibility {
		ArdenVersion value();
	}

	private static class IgnoreStatement extends Statement {
		private final String message;

		private IgnoreStatement(String message) {
			this.message = message;
		}

		@Override
		public void evaluate() {
			// skip test
			assumeTrue(message, false);
		}
	}

}
