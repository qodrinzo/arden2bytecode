package arden.tests.specification;

import org.junit.Ignore;
import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.SpecificationTest;

public class MlmFormatTest extends SpecificationTest {

	@Test
	public void testFileFormat() throws Exception {
		String otherMlm = new ArdenCodeBuilder().replaceSlotContent("mlmname:", "other_mlm").toString();
		String multipleMlms = new ArdenCodeBuilder().addMlm(otherMlm).toString();
		assertValid(multipleMlms);
		
		String missingEnd = new ArdenCodeBuilder().renameSlot("end:", "").toString();
		assertInvalid(missingEnd);
	}

	@Test
	@Ignore
	public void testCategories() throws Exception {
		// TODO error on wrong order
	}

	@Test
	public void testSlots() throws Exception {
		String duplicateSlot = new ArdenCodeBuilder()
				.renameSlot("citations:", "links:")
				.toString();
		assertInvalid(duplicateSlot);
		
		String wrongOrder = new ArdenCodeBuilder()
				.renameSlot("specialist:", "temp:")
				.renameSlot("author:", "specialist:")
				.renameSlot("temp:", "author:")
				.toString();
		assertInvalid(wrongOrder);
		
		String doubleSemicolonInSlot = new ArdenCodeBuilder().replaceSlotContent("institution:", "abc;;xyz").toString();
		assertInvalid(doubleSemicolonInSlot);
		
		// double semicolon allowed in string constants
		assertValidStatement("x := \";;\";");
		
		String doubleSemicolonInMapping = new ArdenCodeBuilder().addData("x := READ {;;};").toString();
		assertValid(doubleSemicolonInMapping);
	}

	@Test
	public void testSlotBodyTypes() throws Exception {
		String arbitrayTextInTextualSlot = new ArdenCodeBuilder().replaceSlotContent("title:", "the mlm at time of now & = + - * ; abs if else >= mlmname mlmname:").toString();
		assertValid(arbitrayTextInTextualSlot);
		
		String emptyTextualSlots = new ArdenCodeBuilder()
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
		String mixedCases = new ArdenCodeBuilder()
				.renameSlot("end:", "eNd:")
				.renameSlot("library:", "LIBraRy:")
				.renameSlot("author:", "aUTHOr:")
				.toString();
		assertValid(mixedCases);
	}

}
