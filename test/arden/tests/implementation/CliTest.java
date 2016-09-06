package arden.tests.implementation;

import static org.junit.Assert.assertTrue;

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

	@Test
	public void testFormatHelpMessage() throws Exception {
		String testString = String.format("Usage: arden2bytecode [options] ARGUMENTS...%n" + 
				"	[--arguments -a value...] : Arguments to MLM if running an MLM. Arguments must be Arden Syntax constants (Strings in quotes). Multiple arguments are separated by spaces.%n" + 
				"	[--classpath --cp value] : Additional classpath. E.g. a database driver like \"mysql-connector-java-[version]-bin.jar\".%n" + 
				"	[--compile -c] : Compile input file.%n" + 
				"	[--dbdriver -d value] : Class name of database driver to load (e.g. \"com.mysql.jdbc.Driver\").%n" + 
				"	[--engine -e] : Run event engine that waits for events or evoke triggers and executes MLMs when they are scheduled.%n" + 
				"	[--environment --env value] : Set arguments to execution environment if running an MLM. In case of using JDBC, this may be a connection URL e.g. \"jdbc:mysql://host:port/database?options\".%n" + 
				"	[--help -h -?] : Display help.%n" + 
				"	[--nologo -n] : Don't print logo.%n" + 
				"	[--output -o value] : Output file name to compile .MLM file to. You can also specify a directory in order to compile multiple MLMs.%n" + 
				"	[--port -p value] : Port on which to listen for events. Will start a server if specified.%n" + 
				"	[--run -r] : Run MLM file or already compiled MLM class file.%n" + 
				"	[--verbose -v] : Verbose mode.");
		
		String newline = System.getProperty("line.separator");
		String[] foldedLines = MainClass.formatHelpMessage(testString).split(newline);
		assertTrue(foldedLines.length >= 13);
		
		int max = 0;
		for (String line : foldedLines) {
			max = line.length() > max? line.length() : max;
			assertTrue(line.length()<=80);
		}
		
		// not too short
		assertTrue(max >= MainClass.MAX_LINE_LENGTH * 0.8);
	}
}
