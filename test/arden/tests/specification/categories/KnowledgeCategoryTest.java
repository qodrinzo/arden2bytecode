package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class KnowledgeCategoryTest extends SpecificationTest {
	
	@Test
	public void testType() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("type:").toString();
		assertInvalid(missingSlot);
		
		String type = createCodeBuilder().replaceSlotContent("type:", "data_driven").toString();
		assertValid(type);
		
		String invalid = createCodeBuilder().replaceSlotContent("type:", "experience").toString();
		assertInvalid(invalid);
	}
	
	@Test
	@Compatibility(max=ArdenVersion.V1)
	public void testTypeDash() throws Exception {
		String typeDash = createCodeBuilder()
				.removeSlot("arden:") // v1
				.replaceSlotContent("type:", "data-driven").toString();
		assertValid(typeDash);
	}
	
	@Test
	public void testData() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("data:").toString();
		assertInvalid(missingSlot);
		
		String empty = createCodeBuilder().clearSlotContent("data:").toString();
		assertValid(empty);
	}

	@Test
	public void testPriority() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("priority:").toString();
		assertValid(missingSlot);

		String priority = createCodeBuilder().replaceSlotContent("priority:", "50").toString();
		assertValid(priority);
		
		String invalid = createCodeBuilder().replaceSlotContent("priority:", "high").toString();
		assertInvalid(invalid);
		
		String invalidRange1 = createCodeBuilder().replaceSlotContent("priority:", "0").toString();
		assertInvalid(invalidRange1);
		
		String invalidRange2 = createCodeBuilder().replaceSlotContent("priority:", "-50").toString();
		assertInvalid(invalidRange2);
		
		String invalidRange3 = createCodeBuilder().replaceSlotContent("priority:", "100").toString();
		assertInvalid(invalidRange3);
	}

	@Test
	public void testEvoke() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("evoke:").toString();
		assertInvalid(missingSlot);

		String empty = createCodeBuilder().clearSlotContent("evoke:").toString();
		assertValid(empty);
	}

	@Test
	public void testLogic() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("logic:").toString();
		assertInvalid(missingSlot);

		String empty = createCodeBuilder().clearSlotContent("logic:").toString();
		assertValid(empty);
	}

	@Test
	public void testAction() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("action:").toString();
		assertInvalid(missingSlot);

		String empty = createCodeBuilder().clearSlotContent("action:").toString();
		assertValid(empty);
	}

	@Test
	public void testUrgency() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("urgency:").toString();
		assertValid(missingSlot);
		
		String priority = createCodeBuilder().replaceSlotContent("urgency:", "50").toString();
		assertValid(priority);
		
		String variable = createCodeBuilder()
				.replaceSlotContent("data:", "urg := 10")
				.replaceSlotContent("urgency:", "urg").toString();
		assertValid(variable);
		
		String invalidRange1 = createCodeBuilder().replaceSlotContent("urgency:", "0").toString();
		assertInvalid(invalidRange1);
		
		String invalidRange2 = createCodeBuilder().replaceSlotContent("urgency:", "-50").toString();
		assertInvalid(invalidRange2);
		
		String invalidRange3 = createCodeBuilder().replaceSlotContent("urgency:", "100").toString();
		assertInvalid(invalidRange3);

	}
}
