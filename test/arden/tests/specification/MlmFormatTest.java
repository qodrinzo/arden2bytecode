package arden.tests.specification;

import org.junit.Ignore;
import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class MlmFormatTest extends SpecificationTest {

	@Test
	@Compatibility(min=ArdenVersion.V2)
	public void testFileFormat() throws Exception {
		String otherMlm = createCodeBuilder().replaceSlotContent("mlmname:", "other_mlm").toString();
		String multipleMlms = createCodeBuilder().addMlm(otherMlm).toString();
		assertValid(multipleMlms);
		
		String missingEnd = createCodeBuilder().renameSlot("end:", "").toString();
		assertInvalid(missingEnd);
	}

	@Test
	@Ignore
	public void testCategories() throws Exception {
		// TODO error on wrong order
	}

	@Test
	public void testSlots() throws Exception {
		String duplicateSlot = createCodeBuilder()
				.renameSlot("citations:", "links:")
				.toString();
		assertInvalid(duplicateSlot);
		
		String wrongOrder = createCodeBuilder()
				.renameSlot("specialist:", "temp:")
				.renameSlot("author:", "specialist:")
				.renameSlot("temp:", "author:")
				.toString();
		assertInvalid(wrongOrder);
		
		String doubleSemicolonInSlot = createCodeBuilder().replaceSlotContent("institution:", "abc;;xyz").toString();
		assertInvalid(doubleSemicolonInSlot);
		
		// double semicolon allowed in string constants
		assertValidStatement("x := \";;\";");
		
		String doubleSemicolonInMapping = createCodeBuilder().addData("x := READ {;;};").toString();
		assertValid(doubleSemicolonInMapping);
	}

	@Test
	public void testSlotBodyTypes() throws Exception {
		String arbitrayTextInTextualSlot = createCodeBuilder().replaceSlotContent("title:", "the mlm at time of now & = + - * ; abs if else >= mlmname mlmname:").toString();
		assertValid(arbitrayTextInTextualSlot);
		
		String emptyTextualSlots = createCodeBuilder()
				.replaceSlotContent("title:", "")
				.replaceSlotContent("version:", "")
				.replaceSlotContent("institution:", "")
				.replaceSlotContent("author:", "")
				.replaceSlotContent("specialist:", "")
				.replaceSlotContent("purpose:", "")
				.replaceSlotContent("explanation:", "")
				.replaceSlotContent("keywords:", "")
				.replaceSlotContent("citations:", "")
				.replaceSlotContent("links:", "")
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
