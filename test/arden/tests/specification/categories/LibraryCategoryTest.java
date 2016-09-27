package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class LibraryCategoryTest extends SpecificationTest {
	
	@Test
	public void testPurpose() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("purpose:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("purpose:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);
	}

	@Test
	public void testExplanation() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("explanation:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("explanation:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);

	}

	@Test
	public void testKeywords() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("keywords:").toString();
		assertInvalid(missingSlot);
		
		String moreThan80Characters = createCodeBuilder()
				.replaceSlotContent("keywords:",
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Quisque libero felis, bibendum at ullamcorper ac, "
						+ "dictum eget eros.")
				.toString();
		assertValid(moreThan80Characters);

	}

	@Test
	public void testCitations() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("citations:").toString();
		assertValid(missingSlot);

		String structuredFormat = createCodeBuilder()
				.replaceSlotContent("citations:", "1. SUPPORT lorem ipsum.\n "
						+ "2. REFUTE dolor sit amet. "
						+ "3. consectetur adipiscing elit")
				.toString();
		assertValid(structuredFormat);
	}
	
	@Test
	@Compatibility(max=ArdenVersion.V1)
	public void testArbitrarySlotText() throws Exception {
		String arbitraryCitationsText = createCodeBuilder()
				.removeSlot("arden:") // v1
				.replaceSlotContent("citations:", "Lorem ipsum, dolor sit amet; "
						+ "consectetur adipiscing elit; "
						+ "Quisque libero felis, bibendum at ullamcorper ac; "
						+ "dictum eget eros")
				.toString();
		assertValid(arbitraryCitationsText);
		
		String arbitraryLinksText = createCodeBuilder()
				.removeSlot("arden:") // v1
				.replaceSlotContent("links:", "Lorem ipsum, dolor sit amet; "
						+ "consectetur adipiscing elit; "
						+ "Quisque libero felis, bibendum at ullamcorper ac; "
						+ "dictum eget eros")
				.toString();
		assertValid(arbitraryLinksText);
	}

	@Test
	public void testLinks() throws Exception {
		String missingSlot = createCodeBuilder().removeSlot("links:").toString();
		assertValid(missingSlot);
		
		String structuredFormat = createCodeBuilder()
				.replaceSlotContent("links:", "'http://www.nlm.nih.gov/'; "
						+ "OTHER_LINK 'lorem.ipsum'; "
						+ "EXE_LINK \"exe file\", 'file://f.exe'")
				.toString();
		assertValid(structuredFormat);
	}

}
