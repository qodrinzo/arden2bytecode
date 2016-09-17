---
# GENERATED - DO NOT EDIT
title: Using Databases
category: Getting Started
nav_order: 14
---
{% include get_url.liquid %}
Arden2ByteCode supports usage of JDBC drivers in order to connect to external data sources. If properly set up, SQL statements can be issued in `READ` statements.

## Example  
Let's have a look at an example:

```arden
maintenance:
	title: SQL Example;;
	mlmname: sql_example;;
	arden: Version 2.5;;
	version: 1.80;;
	institution: arden2bytecode authors;;
	author: Hannes Flicka;;
	specialist: ;;
	date: 2011-09-08;;
	validation: testing;;

library:
	purpose: Demonstration of SQL usage in READ statements;;
	explanation: ;;
	keywords: READ statement, SQL;;

knowledge:
	type:  data-driven;;

	data:
		stdout_dest := destination {STDOUT};
		temperature := READ LAST 3 FROM {SELECT temperature FROM person WHERE person.name='Alice' ORDER BY timestamp ASC};
	;;

	evoke: ;;

	logic:
		temperature_average := AVERAGE temperature;
		CONCLUDE TRUE
	;;

	action:
		WRITE "The last 3 measured temperature values were: " || temperature AT stdout_dest;
		WRITE "The average temperature of the last 3 values is: " || temperature_average AT stdout_dest;
	;;

end:
```

The above MLM reads the last 3 temperature values for person 'Alice' from the database and computes their average.  

Given a database SQLite 3 database `person.sqlite` that contains the following table

```sql
CREATE TABLE person (name TEXT, temperature REAL, timestamp TEXT);
```

and the following data values

```sql
INSERT INTO person VALUES ('Alice', 101.2, DATETIME('now', '-5 day'));
INSERT INTO person VALUES ('Alice', 100.5, DATETIME('now', '-4 day'));
INSERT INTO person VALUES ('Alice', 100.6, DATETIME('now', '-3 day'));
INSERT INTO person VALUES ('Alice', 101.7, DATETIME('now', '-2 day'));
INSERT INTO person VALUES ('Alice', 101.2, DATETIME('now', '-1 day'));
INSERT INTO person VALUES ('Bob',   100.2, DATETIME('now', '-5 day'));
INSERT INTO person VALUES ('Bob',   99.5,  DATETIME('now', '-4 day'));
INSERT INTO person VALUES ('Bob',   99.6,  DATETIME('now', '-3 day'));
INSERT INTO person VALUES ('Bob',   99.2,  DATETIME('now', '-2 day'));
INSERT INTO person VALUES ('Bob',   99.7,  DATETIME('now', '-1 day'));
```

when Arden2ByteCode is called with the following command

```bash
arden2bytecode -n --cp sqlite-jdbc-3.8.11.2.jar --db org.sqlite.JDBC --env jdbc:sqlite:person.sqlite -r sql-example.mlm
```

then the output according to the data is:

	The last 3 measured temperature values were: (100.6,101.7,101.2)
	The average temperature of the last 3 values is: 101.16666666666667
	There was no return value.

## How does it work?
As you can see, you can use plain SQL in `READ` statements. However, it is important that the JDBC driver is loaded by issuing the right command line.

Arden2ByteCode features the following options to be used for setting up the execution environment:

- **--cp** _classpath_, **--classpath** _classpath_  
An additional classpath from where the database driver may be loaded.  
For [SQLite-JDBC](https://github.com/xerial/sqlite-jdbc/releases): `sqlite-jdbc-3.8.11.2.jar`  
For [MySQL Connector/J](http://dev.mysql.com/downloads/connector/j/): `mysql-connector-java-5.1.39-bin.jar`

- **--db** _classname_, **--dbdriver** _classname_  
The class name of the database driver.  
For SQLite: `org.sqlite.JDBC`.  
For MySQL: `com.mysql.jdbc.Driver`.

- **--env** _text_, **--environment** _text_  
The database connection string for the JDBC execution environment.  
In the above example, this is set to `jdbc:sqlite:person.sqlite` for the SQLite Driver to be used on the database file `person.sqlite`.  
For MySQL this will look similar to `jdbc:mysql://host:port/database?options`. Read [Connector/J docs](https://dev.mysql.com/doc/connector-j/5.1/en/) for more information.

Having all these values set up correctly, the `READ` statements are forwarded to the JDBC driver and the returned results are converted to according Arden Syntax data structures.

You can also assign two values in one statement as specified in Arden Syntax:  
The statement

```arden
(names, room_numbers) := read {select name, room_number from room_plan};
```

would read two lists with the columns of the returned data set.
