# Arden Syntax Test Suite
This directory contains a **compiler independent** test suite to test the standard conformance of Arden Syntax compilers.

Most of the statements made in the Arden Syntax language specification (v2.5) are backed by a test. This should lead to a very high test coverage.


## Running
- **Using [Eclipse](https://eclipse.org/)**  
Right-click on [SpecificationTestSuite.java](SpecificationTestSuite.java) and select *Run As* &rArr; *JUnit Test*. A new view should open and show a list of all successful, failed or skipped tests.
- **Using [Apache Ant](http://ant.apache.org/)**
Using the command line go, to the project root and type `ant test`. A report, which can be opened in a browser, will be generated into the [report](../../../../report) directory.


## Writing tests
The [ArdenCodeBuilder](testcompiler/ArdenCodeBuilder.java) can be used to easily create code and keep tests short. It works by adding code to a [template](testcompiler/Template.mlm).  
For custom asserts the base test class ([SpecificationTest](testcompiler/SpecificationTest.java)) should be used.

Example:
```java
String mlm1 = new ArdenCodeBuilder()
	.replaceSlotContent("mlmname:", "mlm1")
	.addData("Patient := OBJECT [Name, DateOfBirth, Id];")
	.toString();
String include = new ArdenCodeBuilder()
	.addMlm(mlm1)
	.addData("othermlm := MLM 'mlm1';")
	.addData("INCLUDE othermlm;")
	.addData("p := NEW Patient;")
	.addData("p.Id := 5;")
	.addAction("RETURN p.Id;")
	.toString();
assertReturns(include, "5");
```


## Testing your compiler
To test a different Arden Syntax compiler, the [TestCompiler](testcompiler/TestCompiler.java) interface has to be implemented and put as the compiler for the base test class ([SpecificationTest](testcompiler/SpecificationTest.java)) which all tests extend.  
To test mapping statements (`READ`, `INTERFACE`, etc.) it should provide a [TestCompilerMappings](testcompiler/TestCompilerMappings.java) object with valid mappings.  
The output has to be filtered to match the requirements of [TestCompilerResult](testcompiler/TestCompilerResult.java).  
When an error is encountered a [TestCompilerCompiletimeException](testcompiler/TestCompilerCompiletimeException.java) or a [TestCompilerRuntimeException](testcompiler/TestCompilerRuntimeException.java) should be thrown.

For more information read the detailed [Javadoc](https://en.wikipedia.org/wiki/Javadoc) comments for the various files or have a look at the implementation for Arden2ByteCode in the [impl](testcompiler/impl) directory.
