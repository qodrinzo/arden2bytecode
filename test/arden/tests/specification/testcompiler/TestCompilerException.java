package arden.tests.specification.testcompiler;

public abstract class TestCompilerException extends Exception {
	private static final long serialVersionUID = 4191840017090634081L;

	public TestCompilerException(String message) {
		super(message);
	}

	public TestCompilerException(Throwable cause) {
		super(cause);
	}

	public TestCompilerException(String message, Throwable cause) {
		super(message, cause);
	}

}
