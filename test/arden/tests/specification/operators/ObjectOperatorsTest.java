package arden.tests.specification.operators;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class ObjectOperatorsTest extends SpecificationTest {

	private String createData() {
		return createCodeBuilder()
				.addData("Name := OBJECT [FirstName, LastName];")
				.addData("johnName := NEW Name WITH \"John\", \"Lennon\";")
				.addData("paulName := NEW Name WITH \"Paul\", \"McCartney\";")
				.addData("namelist := (johnName, paulName);")
				.addData("Person := OBJECT [FullName, Birthdate];")
				.addData("john := NEW Person WITH johnName, 1940-10-09T00:00:00;")
				.toString();
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testDot() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "namelist.FirstName", "(\"John\",\"Paul\")");
		assertEvaluatesToWithData(data, "namelist.LastName", "(\"Lennon\",\"McCartney\")");
		assertEvaluatesToWithData(data, "namelist[1].FirstName", "\"John\"");
		assertEvaluatesToWithData(data, "namelist[1].Height", "NULL");
		assertEvaluatesToWithData(data, "namelist.Height", "(NULL,NULL)");
		assertEvaluatesToWithData(data, "LENGTH johnName.FirstName + LENGTH paulName.FirstName", "8");
		assertEvaluatesToWithData(data, "john.FullName.FirstName", "\"John\"");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testClone() throws Exception {
		String data = createData();

		String list = new ArdenCodeBuilder(data)
				.addData("namelist2 := CLONE (1, 2, namelist);")
				.addAction("RETURN namelist2[3].FirstName;")
				.toString();
		assertReturns(list, "\"John\"");

		String deepcopy = new ArdenCodeBuilder(data)
				.addData("john2 := CLONE OF john;")
				.addAction("john2.FullName.FirstName := \"John2\";")
				.addAction("RETURN john.FullName.FirstName <> john2.FullName.FirstName;")
				.toString();
		assertReturns(deepcopy, "TRUE");

		assertEvaluatesTo("CLONE OF 1990-03-15T15:00:00", "1990-03-15T15:00:00");
		assertEvaluatesTo("CLONE NULL", "NULL");

		String preserveTime = new ArdenCodeBuilder(data)
				.addData("ringoFirstName := \"Ringo\"; TIME ringoFirstName := 1950-07-07T00:00:00;")
				.addData("ringoName := NEW Name WITH ringoFirstName, \"Starr\";")
				.addData("ringoName2 := CLONE ringoName;")
				.addAction("RETURN TIME ringoName2.FirstName;")
				.toString();
		assertReturns(preserveTime, "1950-07-07T00:00:00");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testExtract() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "EXTRACT ATTRIBUTE NAMES john", "(\"FullName\",\"Birthdate\")");
		assertEvaluatesToWithData(data, "EXTRACT ATTRIBUTE NAMES 5", "NULL");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_5)
	public void testAttribute() throws Exception {
		String data = createData();
		assertEvaluatesToWithData(data, "ATTRIBUTE \"LastName\" FROM paulName", "\"McCartney\"");
		assertEvaluatesToWithData(data, "ATTRIBUTE \"x\" FROM paulName", "NULL");
		assertEvaluatesTo("ATTRIBUTE \"LastName\" FROM NULL", "NULL");
		assertEvaluatesTo("ATTRIBUTE \"LastName\" FROM 5", "NULL");
	}
}
