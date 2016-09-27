package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class MaintenanceCategoryTest extends SpecificationTest {
	
	@Test
	public void testTitle() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("title:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("title:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);
	}

	@Test
	public void testMlmname() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("mlmname:").toString();
		assertInvalid(missingSlot);
		
		String allowedCharacters = createCodeBuilder().replaceSlotContent("mlmname:", "my_mlm.123_X_.").toString();
		assertValid(allowedCharacters);
		
		String invalidCharacter1 = createCodeBuilder().replaceSlotContent("mlmname:", "my mlm").toString();
		assertInvalid(invalidCharacter1);
		
		String invalidCharacter2 = createCodeBuilder().replaceSlotContent("mlmname:", "1mlm").toString();
		assertInvalid(invalidCharacter2);
		
		String invalidLength = createCodeBuilder().replaceSlotContent("mlmname:", "Lorem_ipsum_dolor_sit_amet_consectetur_adipiscing_elit_Quisque_libero_felis_bibendum_at_ullamcorper_ac_dictum_eget_eros").toString();
		assertInvalid(invalidLength);
		
		String empty = createCodeBuilder().replaceSlotContent("mlmname:", "").toString();
		assertInvalid(empty);
	}
	
	@Test
	@Compatibility(max=ArdenVersion.V1)
	public void testFileName() throws Exception {
		String alternative = createCodeBuilder()
				.removeSlot("arden:") // v1
				.renameSlot("mlmname:", "filename:")
				.replaceSlotContent("filename:", "my_mlm.mlm")
				.toString();
		assertValid(alternative);
		
	}

	@Test
	public void testArdenSyntaxVersion() throws Exception {
		String v25 = createCodeBuilder().replaceSlotContent("arden:", "Version 2.5").toString();
		assertValid(v25);
		
		String invalid = createCodeBuilder().replaceSlotContent("arden:", "x 2.5").toString();
		assertInvalid(invalid);
	}

	@Test
	@Compatibility(max = ArdenVersion.V1)
	public void testArdenSyntaxVersionV1() throws Exception {
		// no arden version slot in v1
		String v1 = createCodeBuilder().removeSlot("arden:").toString();
		assertValid(v1);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2, max = ArdenVersion.V2)
	public void testArdenSyntaxVersionV2() throws Exception {
		String v2 = createCodeBuilder().replaceSlotContent("arden:", "Version 2").toString();
		assertValid(v2);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_1, max = ArdenVersion.V2_1)
	public void testArdenSyntaxVersionV21() throws Exception {
		String v21 = createCodeBuilder().replaceSlotContent("arden:", "Version 2.1").toString();
		assertValid(v21);
	}

	@Test
	public void testVersion() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("version:").toString();
		assertInvalid(missingSlot);
		
		String version = createCodeBuilder().replaceSlotContent("version:", "1.00").toString();
		assertValid(version);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("version:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertInvalid(moreThan80Characters);
	}

	@Test
	public void testInstitution() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("institution:").toString();
		assertInvalid(missingSlot);
		
		String institution = createCodeBuilder().replaceSlotContent("institution:", "Lorem ipsum dolor sit amet").toString();
		assertValid(institution);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("institution:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertInvalid(moreThan80Characters);
	}

	@Test
	public void testAuthor() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("author:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("author:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);
	}

	@Test
	public void testSpecialist() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("specialist:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("specialist:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);
	}

	@Test
	public void testDate() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("date:").toString();
		assertInvalid(missingSlot);

		String date = createCodeBuilder().replaceSlotContent("date:", "1800-01-01").toString();
		assertValid(date);
		
		String datetime = createCodeBuilder().replaceSlotContent("date:", "1800-01-01T00:00:00").toString();
		assertValid(datetime);
		
		String datetimeZone = createCodeBuilder().replaceSlotContent("date:", "1800-01-01T00:00:00Z").toString();
		assertValid(datetimeZone);
		
		String invalid = createCodeBuilder().replaceSlotContent("date:", "1800").toString();
		assertInvalid(invalid);
	}

	@Test
	public void testValidation() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("validation:").toString();
		assertInvalid(missingSlot);
		
		String production = createCodeBuilder().replaceSlotContent("validation:", "production").toString();
		assertValid(production);
		
		String research = createCodeBuilder().replaceSlotContent("validation:", "research").toString();
		assertValid(research);
		
		String testing = createCodeBuilder().replaceSlotContent("validation:", "testing").toString();
		assertValid(testing);
		
		String expired = createCodeBuilder().replaceSlotContent("validation:", "expired").toString();
		assertValid(expired);
		
		String invalid = createCodeBuilder().replaceSlotContent("validation:", "none").toString();
		assertInvalid(invalid);
	}

}
