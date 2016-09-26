package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenCodeBuilder;
import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class KnowledgeCategoryTest extends SpecificationTest {
	
	@Test
	public void testType() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("type:").toString();
		assertInvalid(missingSlot);
		
		String type = new ArdenCodeBuilder().replaceSlotContent("type:", "data_driven").toString();
		assertValid(type);
		
		String invalid = new ArdenCodeBuilder().replaceSlotContent("type:", "experience").toString();
		assertInvalid(invalid);
	}
	
	@Test
	@Compatibility(max=ArdenVersion.V1)
	public void testTypeDash() throws Exception {
		String typeDash = new ArdenCodeBuilder()
				.removeSlot("arden:") // v1
				.replaceSlotContent("type:", "data-driven").toString();
		assertValid(typeDash);
	}
	
	@Test
	public void testData() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("data:").toString();
		assertInvalid(missingSlot);
		
		String empty = new ArdenCodeBuilder().clearSlotContent("data:").toString();
		assertValid(empty);
	}

	@Test
	public void testPriority() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("priority:").toString();
		assertValid(missingSlot);

		String priority = new ArdenCodeBuilder().replaceSlotContent("priority:", "50").toString();
		assertValid(priority);
		
		String invalid = new ArdenCodeBuilder().replaceSlotContent("priority:", "high").toString();
		assertInvalid(invalid);
		
		String invalidRange1 = new ArdenCodeBuilder().replaceSlotContent("priority:", "0").toString();
		assertInvalid(invalidRange1);
		
		String invalidRange2 = new ArdenCodeBuilder().replaceSlotContent("priority:", "-50").toString();
		assertInvalid(invalidRange2);
		
		String invalidRange3 = new ArdenCodeBuilder().replaceSlotContent("priority:", "100").toString();
		assertInvalid(invalidRange3);
	}

	@Test
	public void testEvoke() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("evoke:").toString();
		assertInvalid(missingSlot);

		String empty = new ArdenCodeBuilder().clearSlotContent("evoke:").toString();
		assertValid(empty);
	}

	@Test
	public void testLogic() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("logic:").toString();
		assertInvalid(missingSlot);

		String empty = new ArdenCodeBuilder().clearSlotContent("logic:").toString();
		assertValid(empty);
	}

	@Test
	public void testAction() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("action:").toString();
		assertInvalid(missingSlot);

		String empty = new ArdenCodeBuilder().clearSlotContent("action:").toString();
		assertValid(empty);
	}

	@Test
	public void testUrgency() throws Exception {
		String missingSlot = new ArdenCodeBuilder().removeSlot("urgency:").toString();
		assertValid(missingSlot);
		
		String priority = new ArdenCodeBuilder().replaceSlotContent("urgency:", "50").toString();
		assertValid(priority);
		
		String variable = new ArdenCodeBuilder()
				.replaceSlotContent("data:", "urg := 10")
				.replaceSlotContent("urgency:", "urg").toString();
		assertValid(variable);
		
		String invalidRange1 = new ArdenCodeBuilder().replaceSlotContent("urgency:", "0").toString();
		assertInvalid(invalidRange1);
		
		String invalidRange2 = new ArdenCodeBuilder().replaceSlotContent("urgency:", "-50").toString();
		assertInvalid(invalidRange2);
		
		String invalidRange3 = new ArdenCodeBuilder().replaceSlotContent("urgency:", "100").toString();
		assertInvalid(invalidRange3);

	}
}
