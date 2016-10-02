package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class KnowledgeCategoryTest extends SpecificationTest {

	@Test
	public void testType() throws Exception {
		assertSlotIsRequired("type:");

		/*
		 * The ArdenCodeBuilder template is automatically set to the correct
		 * data-driven or data_driven type.
		 */
		assertValid(createCodeBuilder().toString());

		assertInvalidSlot("type:", "experience");
	}

	@Test
	@Compatibility(max = ArdenVersion.V1, pedantic = true)
	public void testTypeUnderscoreInvalid() throws Exception {
		// only valid since version 2
		assertInvalidSlot("type:", "data_driven");
	}

	@Test
	public void testData() throws Exception {
		assertSlotIsRequired("data:");
		assertValidSlot("data:", ""); // empty slot
	}

	@Test
	public void testPriority() throws Exception {
		assertSlotIsOptional("priority:");
		assertValidSlot("priority:", "50");
		assertValidSlot("priority:", "1");
		assertValidSlot("priority:", "99");
		assertValidSlot("priority:", "3.141");
		assertInvalidSlot("priority:", "0");
		assertInvalidSlot("priority:", "-50");
		assertInvalidSlot("priority:", "100");
		assertInvalidSlot("priority:", "");
		assertInvalidSlot("priority:", "high");
	}

	@Test
	public void testEvoke() throws Exception {
		assertSlotIsRequired("evoke:");
		assertValidSlot("evoke:", ""); // empty slot
	}

	@Test
	public void testLogic() throws Exception {
		assertSlotIsRequired("logic:");
		assertValidSlot("logic:", ""); // empty slot
	}

	@Test
	public void testAction() throws Exception {
		assertSlotIsRequired("action:");
		assertValidSlot("action:", ""); // empty slot
	}

	@Test
	public void testUrgency() throws Exception {
		assertSlotIsOptional("urgency:");
		assertValidSlot("urgency:", "50");
		assertValidSlot("urgency:", "1");
		assertValidSlot("urgency:", "99");
		assertInvalidSlot("urgency:", "0");
		assertInvalidSlot("urgency:", "-50");
		assertInvalidSlot("urgency:", "100");
		assertInvalidSlot("urgency:", "");

		String variable = createCodeBuilder()
				.addData("urg := 10")
				.replaceSlotContent("urgency:", "urg")
				.toString();
		assertValid(variable);
	}
}
