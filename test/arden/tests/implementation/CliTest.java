package arden.tests.implementation;

import org.junit.Assert;
import org.junit.Test;

import arden.MainClass;

/** tests for the command line interface (CLI) */
public class CliTest extends ImplementationTest {
	@Test
	public void testBasename() throws Exception {
		Assert.assertEquals("basename", MainClass.getFilenameBase("sdvnj/\\$%&../\\//./4e5\\v.s..df.v/basename.bfgb"));
		Assert.assertEquals("", MainClass.getFilenameBase("sdvnj/\\$%&../\\//./4e5\\v.s..df.v/..fg.bfgb\\"));
		Assert.assertEquals(".", MainClass.getFilenameBase(".."));
	}
}
