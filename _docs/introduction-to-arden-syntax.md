---
# GENERATED - DO NOT EDIT
title: Introduction to Arden Syntax
---
{% include get_url %}
This wiki page covers some of the features of Arden Syntax. If you want to know more, you might be interested in a [paper](http://www.sciencedirect.com/science/article/pii/0010482594900027) introducing [Arden Syntax](http://en.wikipedia.org/wiki/Arden_syntax) written by George Hripcsak.

## Sample Medical Logic Module (MLM)

	maintenance:
	  title: Blood pressure check;;
	  mlmname: blood_pressure;;
	  arden: Version 2.1;;  version: 1.02;;
	  institution: Technical University Brunswick;;
	  author: Hannes Flicka (h.flicka@tu-bs.de);;
	  specialist: ;;
	  date: 2011-09-20;;
	  validation: testing;;

	library:
	  purpose: check if the blood pressure is within limits;;
	  explanation: This MLM is an example for reading data and writing a message;;
	  keywords: blood pressure; categorization;;
	  citations: ;;

	knowledge:
	  type: data-driven;;

	  data:
		/* read the blood pressure */
		systolic_blood_pressure := read last
		  {systolic blood pressure}; /* the value in braces is specific to your runtime environment */
		/* If the height is larger than height_threshold, output a message */
		systolic_pressure_threshold := 140; 
		stdout_dest := destination
		  {stdout};
	  ;;

	  evoke: null_event;;

	  logic:
		if (systolic_blood_pressure is not number) then
			conclude false;
		endif;
		if (systolic_blood_pressure >= systolic_pressure_threshold) then
			conclude true;
		else
			conclude false;
		endif;
	  ;;

	  action:
		write "Your blood pressure is too high"
		  at stdout_dest;
	  ;;

	end:


## Medical Logic Module (MLM) Structure

A Medical Logic Module consists of three parts or categories, namely _maintenance_, _library_ and _knowledge_.  
These categories are marked with their corresponding names and a colon.  
The categories make up the top level of the MLM structure as presented in the following (incomplete) MLM:

	maintenance:
	  /* maintenance slots appear here */
	
	library:
	  /* here goes the library metadata */
	
	knowledge:
	  /* actual program logic goes here */

### Maintenance category

The maintenance category contains general metadata about the MLM such as author, date of creation, title, etc.

In the sample MLM, the metadata consists of `title`, `mlmname`, `arden` version requirement, `institution`, `author`, `specialist`, `date` and `validation` slots. Many of these slots are self-explanatory.

For `mlmname` only letters, digits and underscores are allowed (no spaces).  
For `validation` the valid values are:

* `production` - MLM is production ready
* `research` - MLM is for research purposes
* `testing` - MLM is used for debugging and sharing
* `expired` - MLM is no longer in use

Slots that are not required may be left blank. These are `institution`, `author` and `specialist`.

### Library category

The library category contains metadata that allows categorize the MLM and to put it in context of scientific research.

The slots are `purpose`, `explanation`, `keywords`, `citations` and, optionally, `links`. All of these fields may be left blank if the information is not available.

### Knowledge category

This is where the actual code goes.  
The Knowledge category consists of the slots:

* `type` - This is always `data-driven`
* `data` - In this slot, data may be read from a database or initialized from constants
* `evoke` - This slot tells when the MLM should be triggered. At the moment this is not implemented in Arden2ByteCode
* `logic` - This slots contains program logic needed to make a decision. It can either conclude to `false` or to `true`.
* `action` - If the `logic` slot concludes `true`, this slot is executed. Mostly, a message is printed out in this slot, like, in the above example "Your blood pressure is too high".

### Data slot

In this slot, variables may be initialized from external data.  
Variables don't have to be declared. You can just set them with the `:=` operator. But note that you should set variables before reading from them.  
Variable specifiers are not case sensitive. They have to begin with a letter and may contain digits and underscores in the subsequent characters.

In the introductory example, the data slot contains the following read statement:

	systolic_blood_pressure := read last
	  {systolic blood pressure};

It is supposed to read a list of blood pressure values and take the last one of these values as denoted by the `last` operator.  
Another example would be: 

	(systolic_blood_pressure, pulse) := read last 3 from
	  {systolic blood pressure, pulse};

This example reads the last three values of each of the two different data rows pulse and systolic blood pressure. As you can see, multiple values may be assigned at once.

### Logic slot

In this slot, computations are done in order to make a decision.  
If a `conclude true;` statement is executed during this section, the `action` slot is run in order to inform the user about the results of the MLM. Accordingly, on `conclude false;`, the `action` slot will not be executed.

In the sample MLM `blood_pressure` it is being checked if the systolic blood pressure exceeds the common threshold:

	if (systolic_blood_pressure >= systolic_pressure_threshold) then
	  conclude true;
	else
	  conclude false;
	endif;

### Action slot

The `action` slot contains the actions that need to be taken if the conditions of the `logic` slot are met.  
This could be:

* Printing a notification message on the screen
* Sending an e-mail
* Triggering an alarm
* etc...

In the introductory example, a message is printed on STDOUT.

	action:
		write "Your blood pressure is too high"
		  at stdout_dest;
	;;

The destination `stdout_dest` has been previously declared in the `data` slot. It could be an e-mail destination as well.
