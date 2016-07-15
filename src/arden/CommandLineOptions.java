// arden2bytecode
// Copyright (c) 2010, Daniel Grunwald
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification, are
// permitted provided that the following conditions are met:
//
// - Redistributions of source code must retain the above copyright notice, this list
//   of conditions and the following disclaimer.
//
// - Redistributions in binary form must reproduce the above copyright notice, this list
//   of conditions and the following disclaimer in the documentation and/or other materials
//   provided with the distribution.
//
// - Neither the name of the owner nor the names of its contributors may be used to
//   endorse or promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS &AS IS& AND ANY EXPRESS
// OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
// IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
// OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package arden;

import java.util.List;

import com.lexicalscope.jewel.cli.CommandLineInterface;
import com.lexicalscope.jewel.cli.Option;
import com.lexicalscope.jewel.cli.Unparsed;

@CommandLineInterface(application = "arden2bytecode")
public interface CommandLineOptions {
	
	//*** Mode (run, compile, engine) ***
	@Option(shortName = "r",
			description = "Run MLM file or already compiled MLM class file.")
	boolean getRun();

	@Option(shortName = "c",
			description = "Compile input file.")
	boolean getCompile();

	@Option(shortName = "e",
	        description = "Run event engine that waits for events or evoke triggers and \n"
	                + "\t  executes MLMs when they are scheduled.")
	boolean getEngine();

	
	//*** Output options ***
	@Option(shortName = "v",
			description = "Verbose mode.")
	boolean getVerbose();

	@Option(shortName = "n",
			description = "Don't print logo.")
	boolean getNologo();

	@Option(shortName = { "h", "?" },
			description = "Display help.",
			helpRequest = true)
	boolean getHelp();

	
	//*** Arguments for Compiler/Runtime ***
	@Option(longName = {"classpath", "cp"},
			description = "Additional classpath. \n"
					+ "\t  E.g. a database driver like \"mysql-connector-java-[version]-bin.jar\".")
	String getClasspath();
	boolean isClasspath();
	
	@Option(shortName = "o",
			description = "Output file name to compile .MLM file to. \n"
					+ "\t  You can also specify a directory in order to compile multiple MLMs.")
	String getOutput();
	boolean isOutput();

	@Option(shortName = "a",
			description = "Arguments to MLM if running an MLM.")
	List<String> getArguments();
	boolean isArguments();

	@Option(longName = {"environment", "env"},
			description = "Set arguments to execution environment if \n\t  running an MLM. \n"
					+ "\t  In case of using JDBC, this may be a connection URL e.g. \n"
					+ "\t   \"jdbc:mysql://host:port/database?options\".",
			defaultValue = "stdio")
	String getEnvironment();

	@Option(shortName = "d",
			description = "Class name of database driver to load \n"
					+ "\t  (e.g. \"com.mysql.jdbc.Driver\").")
	String getDbdriver();
	boolean isDbdriver();

	@Option(shortName = "p",
	        description = "Port on which to listen for events. \n" +
	            "\t  Will start a server if specified.")
	int getPort();
	boolean isPort();
	
	@Unparsed
	List<String> getFiles();
	boolean isFiles();
}
