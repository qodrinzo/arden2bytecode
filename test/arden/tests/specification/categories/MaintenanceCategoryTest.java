package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class MaintenanceCategoryTest extends SpecificationTest {
	
	@Test
	public void testTitle() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("title:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = new ArdenCodeBuilder()
				.replaceSlotContent("title:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);
	}

	@Test
	public void testMlmname() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("mlmname:").toString();
		assertInvalid(missingSlot);
		
		String allowedCharacters = new ArdenCodeBuilder().replaceSlotContent("mlmname:", "my_mlm.123_X_.").toString();
		assertValid(allowedCharacters);
		
		String invalidCharacter1 = new ArdenCodeBuilder().replaceSlotContent("mlmname:", "my mlm").toString();
		assertInvalid(invalidCharacter1);
		
		String invalidCharacter2 = new ArdenCodeBuilder().replaceSlotContent("mlmname:", "1mlm").toString();
		assertInvalid(invalidCharacter2);
		
		String invalidLength = new ArdenCodeBuilder().replaceSlotContent("mlmname:", "Lorem_ipsum_dolor_sit_amet_consectetur_adipiscing_elit_Quisque_libero_felis_bibendum_at_ullamcorper_ac_dictum_eget_eros").toString();
		assertInvalid(invalidLength);
		
		String empty = new ArdenCodeBuilder().replaceSlotContent("mlmname:", "").toString();
		assertInvalid(empty);
	}
	
	@Test
	@Compatibility(ArdenVersion.V1)
	public void testFileName() throws Exception {
		String alternative = new ArdenCodeBuilder()
				.removeSlot("arden:") // v1
				.renameSlot("mlmname:", "filename:")
				.replaceSlotContent("filename:", "my_mlm.mlm")
				.toString();
		assertValid(alternative);
		
	}

	@Test
	public void testArdenSyntaxVersion() throws Exception {
		String v25 = new ArdenCodeBuilder().replaceSlotContent("arden:", "Version 2.5").toString();
		assertValid(v25);
		
		String invalid = new ArdenCodeBuilder().replaceSlotContent("arden:", "x 2.5").toString();
		assertInvalid(invalid);
	}
	
	@Test
	@Compatibility(ArdenVersion.V1)
	public void testArdenSyntaxVersionV1() throws Exception {
		// no arden version slot in v1
		String v1 = new ArdenCodeBuilder().removeSlot("arden:").toString();
		assertValid(v1);
	}
	
	@Test
	@Compatibility(ArdenVersion.V2)
	public void testArdenSyntaxVersionV2() throws Exception {
		String v2 = new ArdenCodeBuilder().replaceSlotContent("arden:", "Version 2").toString();
		assertValid(v2);
	}
	
	@Test
	@Compatibility(ArdenVersion.V2_1)
	public void testArdenSyntaxVersionV21() throws Exception {
		String v21 = new ArdenCodeBuilder().replaceSlotContent("arden:", "Version 2.1").toString();
		assertValid(v21);
	}

	@Test
	public void testVersion() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("version:").toString();
		assertInvalid(missingSlot);
		
		String version = new ArdenCodeBuilder().replaceSlotContent("version:", "1.00").toString();
		assertValid(version);
		
		String moreThan80Characters = new ArdenCodeBuilder()
				.replaceSlotContent("version:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertInvalid(moreThan80Characters);
	}

	@Test
	public void testInstitution() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("institution:").toString();
		assertInvalid(missingSlot);
		
		String institution = new ArdenCodeBuilder().replaceSlotContent("institution:", "Lorem ipsum dolor sit amet").toString();
		assertValid(institution);
		
		String moreThan80Characters = new ArdenCodeBuilder()
				.replaceSlotContent("institution:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertInvalid(moreThan80Characters);
	}

	@Test
	public void testAuthor() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("author:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = new ArdenCodeBuilder()
				.replaceSlotContent("author:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);
	}

	@Test
	public void testSpecialist() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("specialist:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = new ArdenCodeBuilder()
				.replaceSlotContent("specialist:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);
	}

	@Test
	public void testDate() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("date:").toString();
		assertInvalid(missingSlot);

		String date = new ArdenCodeBuilder().replaceSlotContent("date:", "1800-01-01").toString();
		assertValid(date);
		
		String datetime = new ArdenCodeBuilder().replaceSlotContent("date:", "1800-01-01T00:00:00").toString();
		assertValid(datetime);
		
		String datetimeZone = new ArdenCodeBuilder().replaceSlotContent("date:", "1800-01-01T00:00:00Z").toString();
		assertValid(datetimeZone);
		
		String invalid = new ArdenCodeBuilder().replaceSlotContent("date:", "1800").toString();
		assertInvalid(invalid);
	}

	@Test
	public void testValidation() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("validation:").toString();
		assertInvalid(missingSlot);
		
		String production = new ArdenCodeBuilder().replaceSlotContent("validation:", "production").toString();
		assertValid(production);
		
		String research = new ArdenCodeBuilder().replaceSlotContent("validation:", "research").toString();
		assertValid(research);
		
		String testing = new ArdenCodeBuilder().replaceSlotContent("validation:", "testing").toString();
		assertValid(testing);
		
		String expired = new ArdenCodeBuilder().replaceSlotContent("validation:", "expired").toString();
		assertValid(expired);
		
		String invalid = new ArdenCodeBuilder().replaceSlotContent("validation:", "none").toString();
		assertInvalid(invalid);
	}
}
