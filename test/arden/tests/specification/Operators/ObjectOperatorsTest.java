package arden.tests.specification.Operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class ObjectOperatorsTest extends SpecificationTest {
	
	private static final String DATA = new ArdenCodeBuilder()
			.addData("Name := OBJECT [FirstName, LastName];")
			.addData("johnName := NEW Name WITH \"John\", \"Lennon\";")
			.addData("paulName := NEW Name WITH \"Paul\", \"McCartney\";")
			.addData("namelist := (johnName, paulName);")
			.addData("Person := OBJECT [FullName, Birthdate];")
			.addData("john := NEW Person WITH johnName, 1940-10-09T00:00:00;")
			.toString();
	
	@Test
	public void testDot() throws Exception {
		assertEvaluatesToWithData(DATA, "namelist.FirstName","(\"John\",\"Paul\")");
		assertEvaluatesToWithData(DATA, "namelist.LastName","(\"Lennon\",\"McCartney\")");
		assertEvaluatesToWithData(DATA, "namelist[1].FirstName","\"John\"");
		assertEvaluatesToWithData(DATA, "namelist[1].Height","NULL");
		assertEvaluatesToWithData(DATA, "namelist.Height","(NULL,NULL)");
		assertEvaluatesToWithData(DATA, "LENGTH johnName.FirstName + LENGTH paulName.FirstName","8");
		assertEvaluatesToWithData(DATA, "john.FullName.FirstName","\"John\"");
	}
	
	@Test
	public void testClone() throws Exception {
		String list = new ArdenCodeBuilder(DATA)
				.addData("namelist2 := CLONE (1, 2, namelist);")
				.addAction("RETURN namelist2[3].FirstName;")
				.toString();
		assertReturns(list, "\"John\"");
		
		String deepcopy = new ArdenCodeBuilder(DATA)
				.addData("john2 := CLONE OF john;")
				.addAction("john2.FullName.FirstName := \"John2\";")
				.addAction("RETURN john.FullName.FirstName <> john2.FullName.FirstName;")
				.toString();
		assertReturns(deepcopy, "TRUE");
		
		assertEvaluatesTo("CLONE OF 1990-03-15T15:00:00","1990-03-15T15:00:00");
		assertEvaluatesTo("CLONE NULL","NULL");
		
		String preserveTime = new ArdenCodeBuilder(DATA)
				.addData("ringoFirstName := \"Ringo\"; TIME ringoFirstName := 1950-07-07T00:00:00;")
				.addData("ringoName := NEW Name WITH ringoFirstName, \"Starr\";")
				.addData("ringoName2 := CLONE ringoName;")
				.addAction("RETURN TIME ringoName2.FirstName;")
				.toString();
		assertReturns(preserveTime, "1950-07-07T00:00:00");
	}
	
	@Test
	public void testExtract() throws Exception {
		assertEvaluatesToWithData(DATA, "EXTRACT ATTRIBUTE NAMES john","(\"FullName\",\"Birthdate\")");
		assertEvaluatesToWithData(DATA, "EXTRACT ATTRIBUTE NAMES 5","NULL");
	}
	
	@Test
	public void testAttribute() throws Exception {
		assertEvaluatesToWithData(DATA, "ATTRIBUTE \"LastName\" FROM paulName","\"McCartney\"");
		assertEvaluatesToWithData(DATA, "ATTRIBUTE \"x\" FROM paulName","NULL");
		assertEvaluatesTo("ATTRIBUTE \"LastName\" FROM NULL","NULL");
		assertEvaluatesTo("ATTRIBUTE \"LastName\" FROM 5","NULL");
	}
}
