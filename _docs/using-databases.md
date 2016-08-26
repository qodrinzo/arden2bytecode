---
# GENERATED - DO NOT EDIT
title: Using Databases
category: Getting Started
nav_order: 14
---
{% include get_url.liquid %}
Arden2ByteCode supports usage of JDBC drivers in order to connect to external data sources. If properly set up, SQL statements can be issued in read statements.  
Let's have a look at an example:

	maintenance:
	
	  title:  SQL example;;
	  mlmname:  sql_example;;
	  arden:  Version 2.1;;  version:  1.70;;
	  institution:  arden2bytecode authors;;
	  author:  Hannes Flicka;;
	  specialist:  ;;
	  date:  2011-09-08;;
	  validation:  testing;;
	
	library:
	
	  purpose:  demonstration of sql usage in read statements;;
	  explanation:  ;;
	  keywords:  read statement, sql;;
	  citations:  ;;
	
	knowledge:
	
	  type:  data-driven;;
	
	  data:
		 stdout_dest := destination
		  {STDOUT};
		 temperature := read last 3 from {select temperature from person where person.name='A' order by timestamp asc};
		 temperature_average := average temperature;
	     ;;
	
	  evoke:
	      null_event;; 
	      
	  logic:
	      conclude true;;
	
	  action:
	     write "The last 3 measured temperature values were: " || temperature at stdout_dest;
	     write "The average temperature of the last 3 values is: " || temperature_average
	     at stdout_dest;;
	
	  urgency:  50;;
	
	end:

### Explanation

The above MLM reads the last 3 temperature values for person 'A' from the database and computes their average.  
It is assumed that the database is created as follows:

	create table person (name string, temperature real, timestamp string)

### Example output

The database contains the following data values:

	drop table if exists person;
	create table person (name string, temperature real, timestamp string);
	insert into person values ('A', 101.2, datetime('now', '-5 day'));
	insert into person values ('A', 100.5, datetime('now', '-4 day'));
	insert into person values ('A', 100.6, datetime('now', '-3 day'));
	insert into person values ('A', 101.7, datetime('now', '-2 day'));
	insert into person values ('A', 101.2, datetime('now', '-1 day'));
		 
	insert into person values ('B', 100.2, datetime('now', '-5 day'));
	insert into person values ('B', 99.5, datetime('now', '-4 day'));
	insert into person values ('B', 99.6, datetime('now', '-3 day'));
	insert into person values ('B', 99.2, datetime('now', '-2 day'));
	insert into person values ('B', 99.7, datetime('now', '-1 day'));

Arden2ByteCode was called with the following command:

	> arden2bytecode -n -p ..\sqlite-jdbc-3.7.2.jar -d org.sqlite.JDBC -e jdbc:sqlite:person.sqlite -r sql-example.mlm

Thus the output according to the data is:

	The last 3 measured temperature values were: (100.6,101.7,101.2)
	The average temperature of the last 3 values is: 101.16666666666667
	There was no return value.

## Now, how does it work?

As you can see, you can use plain SQL in read statements. However, it is important that the JDBC driver is loaded by issuing the right command line.

Arden2ByteCode features the following options to be used for setting up the MLM environment:

* **-p**, **--classpath**  
  An additional classpath from where the database driver may be loaded.  
  For [SQLite](http://www.sqlite.org/) as used in the above example, I set this to `<path>/sqlite-jdbc-3.7.2.jar`.
  I downloaded the JDBC driver binaries from https://github.com/xerial/sqlite-jdbc/releases.
* **-d**, **--dbdriver**  
  The class name of the database driver.  
  Again, for SQLite, I used the JDBC driver class name `org.sqlite.JDBC`.
* **-e**, **--environment**  
  The environment string used in the Arden2ByteCode runtime environment.  JDBC connection strings imply that a JDBC environment is set up.  
  In the above example, I set this to: `jdbc:sqlite:person.sqlite` for the SQLite Driver to be used on the database file `person.sqlite`.

Having all these values set up correctly, the read statements are forwarded to the JDBC driver and the returned results are converted to according Arden Syntax data structures.

You can also assign two values in one statement as specified in Arden Syntax:  
The statement `(name, room_number) := read {select name, room_number from room_plan};` would read two lists with the columns of the returned data set.
