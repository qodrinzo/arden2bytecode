package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class MaintenanceCategoryTest extends SpecificationTest {

	@Test
	public void testTitle() throws Exception {
		assertSlotIsRequired("title:");
		assertValidSlot("title:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
				+ "Quisque libero felis, bibendum at ullamcorper ac, " + "dictum eget eros.");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testMlmname() throws Exception {
		assertSlotIsRequired("mlmname:");
		assertValidSlot("mlmname:", "my_mlm123_X_");
		assertInvalidSlot("mlmname:", "my mlm");
		assertInvalidSlot("mlmname:", "1mlm");
		assertInvalidSlot("mlmname:", "");

		// more than 80 characters
		assertInvalidSlot("mlmname:", "Lorem_ipsum_dolor_sit_amet_consectetur_adipiscing_elit_"
				+ "Quisque_libero_felis_bibendum_at_ullamcorper_ac_dictum_eget_eros");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_1)
	public void testMlmnamePeriod() throws Exception {
		// period (.) is allowed in "mlmname:" slot since version 2.1
		assertValidSlot("mlmname:", "my_mlm.123_X_.");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2, max = ArdenVersion.V2, pedantic = true)
	public void testMlmnamePeriodInvalid() throws Exception {
		// no period allowed in version 2
		assertInvalidSlot("mlmname:", "my_mlm.123_X_.");
	}

	@Test
	@Compatibility(max = ArdenVersion.V1, pedantic = true)
	public void testMlmnameInvalid() throws Exception {
		// only valid since version 2
		String mlmname = createCodeBuilder().renameSlot("filename:", "mlmname:").toString();
		assertInvalid(mlmname);
	}

	@Test
	@Compatibility(max = ArdenVersion.V1)
	public void testFileName() throws Exception {
		/*
		 * The ArdenCodeBuilder template for version 1 already has the
		 * "filename:" slot and an empty "arden:" slot.
		 */
		assertValid(createCodeBuilder().toString());
	}

	@Test
	@Compatibility(min = ArdenVersion.V2, pedantic = true)
	public void testFileNameInvalid() throws Exception {
		// only valid in version 1
		String filename = createCodeBuilder().renameSlot("mlmname:", "filename:").toString();
		assertInvalid(filename);
	}

	@Test
	public void testNameCaseInsensitive() throws Exception {
		String mlm1 = createCodeBuilder()
				.setName("Mlm1")
				.addAction("RETURN 1;")
				.toString();
		String mlm2 = createCodeBuilder()
				.addMlm(mlm1)
				.addData("mlm1 := MLM 'mLM1';")
				.clearSlotContent("logic:")
				.addLogic("x := CALL mlm1;")
				.addLogic("CONCLUDE TRUE;")
				.addAction("RETURN x;")
				.toString();
		assertReturns(mlm2, "1");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2)
	public void testArdenVersion() throws Exception {
		// e.g. "version 2.10"
		String verionString = getSettings().targetVersion.toString().toLowerCase();
		String versionNumber = verionString.replace("version", "").trim();

		assertValidSlot("arden:", verionString);
		assertInvalidSlot("arden:", versionNumber);
		assertInvalidSlot("arden:", "xyz " + versionNumber);
		assertInvalidSlot("arden:", "version 1");
		assertValidSlot("arden:", "vErSiOn " + versionNumber);
	}

	@Test
	@Compatibility(max = ArdenVersion.V1)
	public void testArdenVersionMissing() throws Exception {
		// the ArdenCodeBuilder template for version 1 has no "arden:" slot.
		assertValid(createCodeBuilder().toString());
	}

	@Test
	@Compatibility(max = ArdenVersion.V1, pedantic = true)
	public void testArdenVersionInvalid() throws Exception {
		// insert arden slot
		String code = createCodeBuilder().toString();
		int versionIndex = code.indexOf("version:");
		String arden = code.substring(0, versionIndex)
				+ "arden: ;;" + System.lineSeparator()
				+ code.substring(versionIndex, code.length());

		String ardenV1 = new ArdenCodeBuilder(arden)
				.replaceSlotContent("arden:", "Version 1")
				.toString();
		assertInvalid(ardenV1);

		String ardenV1MlmName = new ArdenCodeBuilder(ardenV1)
				.renameSlot("filename:", "mlmname:")
				.replaceSlotContent("mlmname:", "test_mlm")
				.toString();
		assertInvalid(ardenV1MlmName);

		String ardenV2 = new ArdenCodeBuilder(arden)
				.replaceSlotContent("arden:", "Version 2")
				.toString();
		assertInvalid(ardenV2);

		String ardenV2MlmName = new ArdenCodeBuilder(ardenV2)
				.renameSlot("filename:", "mlmname:")
				.replaceSlotContent("mlmname:", "test_mlm")
				.toString();
		assertInvalid(ardenV2MlmName);
	}

	@Test
	public void testVersion() throws Exception {
		assertSlotIsRequired("version:");
		assertValidSlot("version:", "1.00");
		assertValidSlot("version:", "0.2.5-alpha");

		// more than 80 characters
		assertInvalidSlot("version:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
				+ "Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.");
	}

	@Test
	public void testInstitution() throws Exception {
		assertSlotIsRequired("institution:");
		assertValidSlot("institution:", "Peter L. Reichertz Institute for Medical Informatics");

		// more than 80 characters
		assertInvalidSlot("institution:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
				+ "Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.");
	}

	@Test
	public void testAuthor() throws Exception {
		assertSlotIsRequired("author:");
		assertValidSlot("author:", "John M. Doe, Jr., M.D. (john@doe.com); Jane Roe, Ph.D.");

		// more than 80 characters
		assertValidSlot("author:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
				+ "Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.");
	}

	@Test
	public void testSpecialist() throws Exception {
		assertSlotIsRequired("specialist:");
		assertValidSlot("specialist:", "John M. Doe, Jr., M.D. (john@doe.com); Jane Roe, Ph.D.");

		// more than 80 characters
		assertValidSlot("specialist:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
				+ "Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.");
	}

	@Test
	public void testDate() throws Exception {
		assertSlotIsRequired("date:");
		assertValidSlot("date:", "1900-05-01");
		assertValidSlot("date:", "1970-12-01T15:30:05");
		assertValidSlot("date:", "1800-01-01T00:00:00Z");
		assertValidSlot("date:", "1900-03-15T23:30:05.50Z");
		assertInvalidSlot("date:", "1900");
		assertInvalidSlot("date:", "NOW");
		assertInvalidSlot("date:", "");
	}

	@Test
	public void testValidation() throws Exception {
		assertSlotIsRequired("validation:");
		assertValidSlot("validation:", "production");
		assertValidSlot("validation:", "research");
		assertValidSlot("validation:", "testing");
		assertValidSlot("validation:", "expired");
		assertInvalidSlot("validation:", "none");
		assertInvalidSlot("validation:", "");
	}

}
