package arden.tests.specification.testcompiler;

public class TestCompilerRuntimeException extends TestCompilerException {
	private static final long serialVersionUID = -1047470305689855882L;

	public TestCompilerRuntimeException(String message) {
		super(message);
	}

	public TestCompilerRuntimeException(Throwable cause) {
		super(cause);
	}

	public TestCompilerRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
