package arden.tests.specification;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class MlmFormatTest extends SpecificationTest {

	// TODO test character set (ASCII/UNICODE, linebreak)?

	@Test
	public void testFileFormat() throws Exception {
		String otherMlm = createCodeBuilder()
				.setName("other_mlm")
				.toString();
		String multipleMlms = createCodeBuilder()
				.addMlm(otherMlm)
				.toString();
		assertValid(multipleMlms);

		String missingEnd = createCodeBuilder()
				.toString()
				.replace("end:", "");
		assertInvalid(missingEnd);
	}

	@Test
	public void testCategories() throws Exception {
		String spaceBeforeColon = createCodeBuilder().renameSlot("maintenance:", "maintenance :").toString();
		assertInvalid(spaceBeforeColon);

		// error on wrong category order
		String wrongOrder = createCodeBuilder().toString();
		int maintenanceIndex = wrongOrder.indexOf("maintenance:");
		int libraryIndex = wrongOrder.indexOf("library:");
		int knowledgeIndex = wrongOrder.indexOf("knowledge:");
		String maintenanceCode = wrongOrder.substring(maintenanceIndex, libraryIndex);
		String libraryCode = wrongOrder.substring(libraryIndex, knowledgeIndex);
		wrongOrder = wrongOrder
				.replace(maintenanceCode, "$TEMP")
				.replace(libraryCode, maintenanceCode)
				.replace("$TEMP", libraryCode);
		assertInvalid(wrongOrder);
	}

	@Test
	public void testSlots() throws Exception {
		String spaceBeforeColon = createCodeBuilder().renameSlot("version:", "version :").toString();
		assertInvalid(spaceBeforeColon);

		String duplicateSlot = createCodeBuilder().renameSlot("citations:", "links:").toString();
		assertInvalid(duplicateSlot);

		String wrongOrder = createCodeBuilder()
				.renameSlot("specialist:", "temp:")
				.renameSlot("author:", "specialist:")
				.renameSlot("temp:", "author:")
				.toString();
		assertInvalid(wrongOrder);

		// double semicolon not allowed in slot
		assertInvalidSlot("institution:", "abc;;xyz");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testDoubleSemicolon() throws Exception {
		// double semicolon in string constants
		assertValidStatement("x := \";;\";");

		// double semicolon in mapping
		assertValidSlot("data:", "x := EVENT {;;};");

		// double semicolon in comment
		assertValidSlot("data:", "//;;" + System.lineSeparator());
	}

	@Test
	@Compatibility(max = ArdenVersion.V1, pedantic = true)
	public void testDoubleSemicolonInvalid() throws Exception {
		// double semicolon are not allowed in string constants in version 1
		assertInvalidStatement("x := \";;\";");
	}

	@Test
	public void testSlotBodyTypes() throws Exception {
		// arbitrary text in textual slots
		assertValidSlot("title:", "the mlm at time of now & = + - * ; abs if else >= mlmname mlmname:");

		String emptyTextualSlots = createCodeBuilder()
				.clearSlotContent("title:")
				.clearSlotContent("version:")
				.clearSlotContent("institution:")
				.clearSlotContent("author:")
				.clearSlotContent("specialist:")
				.clearSlotContent("purpose:")
				.clearSlotContent("explanation:")
				.clearSlotContent("keywords:")
				.clearSlotContent("citations:")
				.clearSlotContent("links:")
				.toString();
		assertValid(emptyTextualSlots);
	}

	@Test
	public void testCaseInsensitivity() throws Exception {
		String mixedCases = createCodeBuilder()
				.renameSlot("end:", "eNd:")
				.renameSlot("library:", "LIBraRy:")
				.renameSlot("author:", "aUTHOr:")
				.toString();
		assertValid(mixedCases);
	}

}
