# Arden Syntax Test Suite
This directory contains a **compiler independent** test suite to test the standard conformance of Arden Syntax compilers up to Arden Syntax **version 2.10**.

Nearly every statement made in the Arden Syntax language specifications is backed by a test. This should lead to a very high test coverage.


## Running
- **Using [Eclipse](https://eclipse.org/)**  
Right-click on [SpecificationTestSuite.java](SpecificationTestSuite.java) and select *Run As* &rArr; *JUnit Test*. A new view should open and show a list of all successful, failed or skipped tests.
- **Using [Apache Ant](http://ant.apache.org/)**  
Using the command line go, to the project root and type `ant test`. A report, which can be opened in a browser, will be generated into the [report](../../../../report) directory.


## Writing tests
The [ArdenCodeBuilder](testcompiler/ArdenCodeBuilder.java) can be used to easily create code and keep tests short. It works by adding code to a [template](testcompiler/Template.mlm), which is automatically chosen depending on the supported Arden Syntax version.  
For custom asserts the base test class ([SpecificationTest](testcompiler/SpecificationTest.java)) should be used.

Example:
```java
String mlm1 = createCodeBuilder()
	.replaceSlotContent("mlmname:", "mlm1")
	.addData("Patient := OBJECT [Name, DateOfBirth, Id];")
	.toString();
String include = createCodeBuilder()
	.addMlm(mlm1)
	.addData("othermlm := MLM 'mlm1';")
	.addData("INCLUDE othermlm;")
	.addData("p := NEW Patient;")
	.addData("p.Id := 5;")
	.addAction("RETURN p.Id;")
	.toString();
assertReturns(include, "5");
```

Tests, which require backward compatibility, can be flagged via an [annotation](testcompiler/CompatibilityRule.java).  
The annotation's min and max field describe when the tested feature was introduced to the language or removed/deprecated.
Only compilers supporting versions between these two values will run the test.  

Example:
```java
@Test
@Compatibility(min = ArdenVersion.V2_6, max = ArdenVersion.V2_8, pedantic = true)
public void testResourcesCategoryIsOptional() {…}
```

## Testing your compiler
To test a different Arden Syntax compiler, the [TestCompiler](testcompiler/TestCompiler.java) interface has to be implemented and put as the compiler for the base test class ([SpecificationTest](testcompiler/SpecificationTest.java)) which all tests extend.  

It must configure its supported Arden Syntax version(s) by providing a [TestCompilerSettings](testcompiler/TestCompilerSettings.java) object.  
To test mapping statements (`READ`, `INTERFACE`, etc.) it should provide a [TestCompilerMappings](testcompiler/TestCompilerMappings.java) object with valid mappings.  
The output has to be filtered to match the requirements of [TestCompilerResult](testcompiler/TestCompilerResult.java).   
When an error is encountered a [TestCompilerCompiletimeException](testcompiler/TestCompilerCompiletimeException.java) or a [TestCompilerRuntimeException](testcompiler/TestCompilerRuntimeException.java) should be thrown.

For more information read the detailed [Javadoc](https://en.wikipedia.org/wiki/Javadoc) comments for the various files or have a look at the implementation for Arden2ByteCode in the [impl](testcompiler/impl) directory.
