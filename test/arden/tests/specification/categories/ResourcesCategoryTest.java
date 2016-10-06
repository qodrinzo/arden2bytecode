package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class ResourcesCategoryTest extends SpecificationTest {

	@Test
	@Compatibility(min = ArdenVersion.V2_6, max = ArdenVersion.V2_8)
	public void testOptional() throws Exception {
		String mlmWithoutResources = createCodeBuilder()
				.removeResourceSlots()
				.toString()
				.replace("resources:", "");
		assertValid(mlmWithoutResources);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_9, pedantic = true)
	public void testRequired() throws Exception {
		// required since version 2.9
		String mlmWithoutResources = createCodeBuilder()
				.removeResourceSlots()
				.toString()
				.replace("resources:", "");
		assertInvalid(mlmWithoutResources);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testDefaultSlot() throws Exception {
		assertSlotIsRequired("default:");

		String defaultSlot = createCodeBuilder()
				.removeResourceSlots()
				.appendDefaultSlot("de")
				.appendLanguageSlot("de")
				.toString();
		assertValid(defaultSlot);

		String multipleDefaults = createCodeBuilder()
				.removeResourceSlots()
				.appendDefaultSlot("en")
				.appendDefaultSlot("de")
				.appendLanguageSlot("en")
				.appendLanguageSlot("de")
				.toString();
		assertInvalid(multipleDefaults);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testLanguageSlot() throws Exception {
		String multipleSlots = createCodeBuilder()
				.appendLanguageSlot("de")
				.appendLanguageSlot("es")
				.toString();
		assertValid(multipleSlots);

		String noSlots = createCodeBuilder()
				.removeResourceSlots()
				.appendDefaultSlot("en")
				.toString();
		assertInvalid(noSlots);

		String textConstants = createCodeBuilder()
				.addTextConstant("en", "msg", "A Message")
				.appendLanguageSlot("de")
				.addTextConstant("de", "msg", "Eine Nachricht")
				.toString();
		assertValid(textConstants);
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6)
	public void testRegionCode() throws Exception {
		String regionCode = createCodeBuilder()
				.removeResourceSlots()
				.appendDefaultSlot("en_US")
				.appendLanguageSlot("en_US")
				.toString();
		assertValid(regionCode);

		String multipleRegionCodes = createCodeBuilder()
				.removeResourceSlots()
				.appendDefaultSlot("en_US")
				.appendLanguageSlot("en_US")
				.appendLanguageSlot("en_GB")
				.toString();
		assertValid(multipleRegionCodes);

		String mixed = createCodeBuilder()
				.removeResourceSlots()
				.appendDefaultSlot("en_US")
				.appendLanguageSlot("en")
				.appendLanguageSlot("en_GB")
				.toString();
		assertInvalid(mixed);

		String firstRegionIsChosen = createCodeBuilder()
				.addData("msg := LOCALIZED 'msg' BY \"en\";")
				.addAction("RETURN msg;")
				.removeResourceSlots()
				.appendDefaultSlot("en")
				.appendLanguageSlot("en_GB")
				.addTextConstant("en_GB", "msg", "colour")
				.appendLanguageSlot("en_US")
				.addTextConstant("en_US", "msg", "color")
				.toString();
		assertReturns(firstRegionIsChosen, "\"colour\"");
	}

}
