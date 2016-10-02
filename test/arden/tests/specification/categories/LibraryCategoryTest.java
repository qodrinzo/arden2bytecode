package arden.tests.specification.categories;

import org.junit.Test;

import arden.tests.specification.testcompiler.ArdenVersion;
import arden.tests.specification.testcompiler.CompatibilityRule.Compatibility;
import arden.tests.specification.testcompiler.SpecificationTest;

public class LibraryCategoryTest extends SpecificationTest {

	@Test
	public void testPurpose() throws Exception {
		assertSlotIsRequired("purpose:");

		// more than 80 characters
		assertValidSlot("purpose:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
				+ "Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.");
	}

	@Test
	public void testExplanation() throws Exception {
		assertSlotIsRequired("explanation:");

		// more than 80 characters
		assertValidSlot("explanation:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
				+ "Quisque libero felis, bibendum at ullamcorper ac, dictum eget eros.");
	}

	@Test
	public void testKeywords() throws Exception {
		assertSlotIsRequired("keywords:");

		// more than 80 characters
		assertValidSlot("keywords:", "Lorem ipsum dolor sit amet, consectetur adipiscing elit; "
				+ "Quisque libero felis; bibendum at ullamcorper ac; dictum eget eros;");
	}

	@Test
	public void testCitations() throws Exception {
		assertSlotIsOptional("citations:");

		/*
		 * The specification BNF uses semicolon delimiters and double quotes,
		 * but the examples do not. Weird. The example format would not be
		 * correctly parsable as citations may contain a number followed by a
		 * dot, which would start the next citation as linebreaks (whitespace)
		 * are ignored.
		 */
		// valid in free-form text and structured format
		String structuredFormat = createCodeBuilder()
				.appendSlotContent("citations:", "1. SUPPORT Gietzelt M, Goltz U, Grunwald D, Lochau M, Marschollek M, Song B and Wolf K-H. Arden2ByteCode: A one-pass Arden Syntax compiler for service-oriented decision support systems based on the OSGi platform. Comput Methods Programs Biomed. 2012;106(2):114-25.")
				.appendSlotContent("citations:", System.lineSeparator())
				.appendSlotContent("citations:", "2. REFUTE Wolf K-H, Klimek M. A Conformance Test Suite for Arden Syntax Compilers and Interpreters. Studies in health technology and informatics. 2016;228:379.")
				.appendSlotContent("citations:", System.lineSeparator())
				.appendSlotContent("citations:", "3. Levey A. A New Equation to Estimate Glomerular Filtration Rate. Annals of Internal Medicine. 2009;150(9):604.")
				.appendSlotContent("citations:", System.lineSeparator())
				.toString();
		assertValid(structuredFormat);
	}

	@Test
	@Compatibility(max = ArdenVersion.V1)
	public void testCitationsTextualFormat() throws Exception {
		// free-form text
		assertValidSlot("citations:", "Wolf K-H, Klimek M. A Conformance Test Suite for Arden Syntax Compilers and Interpreters. Studies in health technology and informatics. 2016;228:379.");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2, pedantic = true)
	public void testCitationsStructuredFormat() throws Exception {
		assertInvalidSlot("citations:", "Lorem Ipsum");
		assertInvalidSlot("citations:", "Wolf K-H, Klimek M. A Conformance Test Suite for Arden Syntax Compilers and Interpreters. Studies in health technology and informatics. 2016;228:379.");
	}

	@Test
	public void testLinks() throws Exception {
		assertSlotIsOptional("links:");
	}

	@Test
	@Compatibility(max = ArdenVersion.V1)
	public void testLinksTextualFormat() throws Exception {
		// free-form text
		assertValidSlot("links:", "https://www.ncbi.nlm.nih.gov/pubmed");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2, max = ArdenVersion.V2_5, pedantic = true)
	public void testLinksStructuredFormatTerms() throws Exception {
		/*
		 * The specification BNF (version 2 to version 2.9) uses URL_LINK and
		 * MESH_LINK, while the examples use just URL and MESH. Odd. In version
		 * 2.10 both use the long URL_LINK form, so this is probably the right
		 * format.
		 */
		String structuredFormat = createCodeBuilder()
				.appendSlotContent("links:", "URL_LINK \"PubMed\", 'https://www.ncbi.nlm.nih.gov/pubmed'; ")
				.appendSlotContent("links:", "MESH_LINK \"mesh\", 'lorem and ipsum'; ")
				.appendSlotContent("links:", "OTHER_LINK \"doi\", '10.1000/xyz123'; ")
				.toString();
		assertValid(structuredFormat);

		// only link text required
		assertValidSlot("links:", "'https://www.ncbi.nlm.nih.gov/pubmed'");
		assertValidSlot("links:", "OTHER_LINK 'lorem.ipsum'");

		assertInvalidSlot("links:", "https://www.ncbi.nlm.nih.gov/pubmed");
		assertInvalidSlot("links:", "XYZ 'https://www.ncbi.nlm.nih.gov/pubmed'");
	}

	@Test
	@Compatibility(min = ArdenVersion.V2_6, pedantic = true)
	public void testLinksStructuredFormatStrings() throws Exception {
		String structuredFormat = createCodeBuilder()
				.appendSlotContent("links:", "URL_LINK 'PubMed', \"https://www.ncbi.nlm.nih.gov/pubmed\"; ")
				.appendSlotContent("links:", "MESH_LINK 'mesh', \"lorem and ipsum\"; ")
				.appendSlotContent("links:", "OTHER_LINK 'doi', \"10.1000/xyz123\"; ")
				.toString();
		assertValid(structuredFormat);

		// only link text required
		assertValidSlot("links:", "\"https://www.ncbi.nlm.nih.gov/pubmed\"");
		assertValidSlot("links:", "OTHER_LINK \"lorem.ipsum\"");

		assertInvalidSlot("links:", "https://www.ncbi.nlm.nih.gov/pubmed");
		assertInvalidSlot("links:", "XYZ \"https://www.ncbi.nlm.nih.gov/pubmed\"");
	}

}
