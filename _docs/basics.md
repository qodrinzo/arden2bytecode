---
# GENERATED - DO NOT EDIT
title: Basics
category: Arden Syntax
nav_order: 12
---
{% include get_url.liquid %}
This wiki page covers some of the features of Arden Syntax. If you want to know more, you might be interested in a [paper](http://www.sciencedirect.com/science/article/pii/0010482594900027) introducing [Arden Syntax](http://en.wikipedia.org/wiki/Arden_syntax) written by George Hripcsak. The Arden Syntax ANSI standard is released by [HL7](http://www.hl7.org/special/Committees/arden/products.cfm).

## Sample Medical Logic Modules (MLM)

### Example 1
This MLM recieves some values via the `ARGUMENT` statement, calculates a value and returns it via the `RETURN` statement.

```arden
maintenance:
    title: GFR Calculator ;;
    mlmname: gfr_calculator;;
    arden: version 2.5;;
    version: 1.0;;
    institution: Peter L. Reichertz Institut;;
    author: Mike Klimek;;
    specialist: Mike Klimek;;
    date: 2016-07-02;;
    validation: testing;;

library:
    purpose: Calculate the estimated Glomerular Filtration Rate (GFR or eGFR);;
    explanation:
    	This MLM is used to calculated the GFR (in mL/min/1.73m^2) from a patients latest creatinine value,
    	age, race and sex. This allows to check for renal insufficiency, e.g. values < 30.
    	The calculation is based on the CKD-EPI referenced creatinine method.
	;;
    keywords: GFR; eGFR; Glomerular Filtration Rate; creatinine; kindey function; CKD-EPI;;
    citations:
    	1. SUPPORT Levey A. A New Equation to Estimate Glomerular Filtration Rate.
    	   Annals of Internal Medicine. 2009;150(9):604.
    ;;
    links: URL 'Calculate eGFR using the CKD-EPI formula', "https://www.qxmd.com/calculate/calculator_251/egfr-using-ckd-epi";;

knowledge:
    type: data_driven;;

    data:
    	(creatinine, age, race, sex) := ARGUMENT;
    ;;

    evoke: ;;

    logic:
    	IF sex = "M" THEN
    	    K := 0.9;
    	    A := -0.411;
    	    S := 1;
	    ELSE
	    	K := 0.7;
	    	A := -0.329;
	    	S := 1.018;
    	ENDIF;

    	IF race IS IN ("African-American", "Black") THEN
    		R := 1.159;
		ELSE
			R := 1;
    	ENDIF;

    	age_in_years := age / 1 YEAR;
    	GFR := 141 * MIN(creatinine/K, 1)**A * MAX(creatinine/K, 1)**(-1.209) * 0.993**age_in_years * S * R;

        CONCLUDE TRUE;
    ;;

    action:
    	RETURN GFR;
    ;;

end:
```

### Example 2
This MLM is activated by an event. It reads some values with the `READ` statement and uses the above MLM to calculate a result. It may then, if necessary, send a message with the `WRITE` statement.

```arden
maintenance:
	title: CT study order for patient with renal insufficiency;;
	mlmname: ct_contrast_renal_insufficiency_check;;
	arden: version 2.5;;
	version: 1.0;;
	institution: Peter L. Reichertz Institut;;
	author: Mike Klimek;;
	specialist: Mike Klimek;;
	date: 2016-07-01;;
	validation: testing;;

library:
	purpose:
		Send an alert when a physician orders a CT study with contrast in a patient with renal insufficiency
	;;
	explanation:
		When a physician orders a CT scan with contrast, this MLM calculates the estimated Glomerular
		Filtration Rate (eGFR) from the patients most recent serum creatinine.
		The incidence of contrast-induced nephropathy (CIN) in patients with eGFR less than 30 is greater
		than 10% in large cohorts. Therefore if the eGFR is less than 30 the system issues an alert to the
		physician to consider the possibility that his patient has renal insufficiency, and to use other
		contrast dyes.
		A notice is also issued, if there are no serum creatinine values more recent than 6 weeks old.
	;;
	keywords: renal insufficiency; CT; contrast; creatinine; eGFR; nephropathy; CIN;;
	links:
		'https://radiology.ucsf.edu/patient-care/patient-safety/contrast/iodinated/elevated-creatinine/';
	;;

knowledge:
	type: data_driven;;

	data:
		ct_contrast_order := EVENT {ct contrast order};
		gfr_calculator := MLM 'gfr_calculator';

		(birthdate, sex, race) := READ {birthdate, sex, race};
		creatinine := READ LATEST {Creatinine levels} WHERE THEY OCCURRED WITHIN THE PAST 6 WEEKS; // in mg/dL
	;;

	evoke: ct_contrast_order;;

	logic:
		IF creatinine IS NOT PRESENT THEN
			info := "No recent serum creatinine available. Consider patient's kidney function before ordering
					 contrast studies.";
			CONCLUDE TRUE;
		ENDIF;

		age := NOW - birthdate;
		gfr := CALL gfr_calculator WITH creatinine, age, race, sex; // in ml/min/1.73 m^2
		IF gfr < 30 THEN
			info := "Consider impaired kidney function when ordering contrast studies for this patient.

					 eGFR: " || gfr || " on: " || TIME OF creatinine;
			CONCLUDE TRUE;
		ENDIF;
	;;

	action:
		WRITE info;
	;;

end:
```

## Medical Logic Module (MLM) Structure
A Medical Logic Module consists of three parts or categories, namely _maintenance_, _library_ and _knowledge_.  
These categories are marked with their corresponding names and a colon.  
The categories make up the top level of the MLM structure as presented in the following (incomplete) MLM:

```arden
maintenance:
  /* maintenance slots appear here */

library:
  /* here goes the library metadata */

knowledge:
  /* actual program logic goes here */
```

### Maintenance category
The maintenance category covers general information used for developing the MLM.

The metadata consists of the following slots:

- `title`: A short title.
- `mlmname`: A name, that allows the MLM to be called by other MLMs. Only letters, digits and underscores are allowed (no spaces).
- `arden`: The used Arden Syntax version, e.g. 'Version 2.5'.
- `version`: The version of the MLM.
- `institution`: The institution. Allows distinguishing MLMs with the same `mlmname`.
- `author`: The author of the MLM.
- `specialist`: The developer or technical contact.
- `date`: The date of creation in [ISO 8601 format](https://en.wikipedia.org/wiki/ISO_8601).
- `validation`: Valid values are
  * `production` - MLM is production ready
  * `research` - MLM is for research purposes
  * `testing` - MLM is used for debugging and sharing
  * `expired` - MLM is no longer in use

All slots are required, but for some, like `institution`, `author` and `specialist` the content may be left blank.

### Library category
The library category contains additional metadata about the medical knowledge contained in the MLM. It allows to categorize the MLM and to put it in context of scientific research.

The slots are:

- `purpose`: Short descriptive text about the purpose of the MLM.
- `explanation`: A longer plain text explanation of what this MLM does.
- `keywords`: List of keywords, delimited by a semicolons.
- (optional) `citations`: A numbered list of citations in Vancouver style, that 'SUPPORT' or 'REFUTE' the knowledge in this MLM. See [example 1](#example-1).
- (optional) `links`: A list of relevant links. See [example 1](#example-1).

All of these fields may be left blank if the information is not available.

### Knowledge category
This is where the actual code goes.

The Knowledge category consists of these slots:

- `type`: This is always `data_driven`.
- `data`: In this slot, data may be read from a database or initialized from constants.
- (optional) `priority`: A number between 1 and 99 that decides the order of execution, when multiple MLMs triggered by the same event.
- `evoke`: This slot tells when the MLM should be triggered. See [Scheduling]({{ baseurl }}/docs/scheduling/) for more information.
- `logic`: This slots contains program logic needed to make a decision. It can either conclude to `FALSE` (no action is needed) or to `TRUE`.
- `action`: If the `logic` slot concludes `TRUE`, this slot is executed. Mostly, a message is printed out in this slot.
- (optional) `urgency`: A number or variable between 1 and 99, that tells the environment how important the actions of the `action` slot are.

### Data slot
In this slot, variables may be initialized from external data or constants.

All institution specific statements (statements with curly braces) belong in this slot. For example the following read statement

```arden
systolic_blood_pressure := READ LAST {systolic blood pressure measurements};
```

is supposed to read a list of blood pressure values and take the last one of these values as denoted by the `LAST` operator. The part in the curly braces can be any text, and is compiler- or institution specific.

This example

```arden
systolic_blood_pressure_list := READ {systolic blood pressure measurements};
```

reads the whole list.  

Another example would be:

```arden
(systolic_blood_pressure, pulse) := READ LAST {systolic blood pressure measurements, pulse measurements};
```

This example reads the last value of each of the two different data rows. As you can see, multiple values may be assigned at once.

You can also assign values to variables:

```arden
a_string := "a string";
a_number := 3.141;
a_time := 2015-11-27T00:00:00;
a_duration := 5 WEEKS;
a_boolean := FALSE;
a_list := ("another string", 5, FALSE, a_time);
```

Unset variables will return `NULL` when used.  
Arden Syntax is weakly typed, so a variable that holds a string can be assigned again to hold a number.  
Variable identifiers are not case sensitive. They have to begin with a letter and may contain digits and underscores in the subsequent characters.  

Objects can be defined in the data slot:

```arden
Patient := OBJECT [Name, DateOfBirth, Sex];
john := NEW Patient WITH "John Doe", 1970-01-01, "M";
john.Name := "John Roe";
```

There are a number of special objects, like events, MLMs or messages:

```arden
warning := MESSAGE {WARNING: Your food may be toxic};
email := DESTINATION {email: "email@address.com"};
an_event := EVENT {something happens};
another_mlm := MLM 'another_mlm' FROM INSTITUTION "my institution";
web_service := INTERFACE {https://my.webservice.com};
(arg1, arg2, arg3) := ARGUMENT;
t := CURRENTTIME;
```

Note that the parts in between curly braces are institution specific.

### Logic slot
In this slot, computations are done in order to make a decision.

If a `CONCLUDE TRUE` statement is executed during this section, the `action` slot is run immediately, skipping the rest of the logic slot. Accordingly, on `CONCLUDE FALSE` or when the end of the logic slot is reached, the `action` slot will not be executed.  
Complex expression are also possible, for example:

```arden
CONCLUDE systolic_blood_pressure >= systolic_pressure_threshold;
```

This is equivalent to the following code:

```arden
IF systolic_blood_pressure >= systolic_pressure_threshold THEN
	CONCLUDE TRUE;
ELSE
	CONCLUDE FALSE;
ENDIF;
```

Loops are also possible:

```arden
sum_to_100 := 0;
FOR i IN 1 SEQTO 100 DO
    sum_to_100 := sum_to_100 + i;
ENDDO;
```

Other MLMs can be called with multiple arguments:

```arden
calculated_value := CALL other_mlm WITH 123, an_argument, "a string";
```

### Action slot
The `action` slot contains the actions that need to be taken if the conditions of the `logic` slot are met.  

There are three possible kinds of actions:

- Return a value to a calling MLM using the `RETURN` statement.  
This is usually not combined with the other two kinds of actions.  
Multiple return values are possible:  

  ```arden
  RETURN a_variable, "a string", 5.34;
  ```

- Write a message using the `WRITE` statement:  

  ```arden
  WRITE "Hello World!";
  WRITE email AT email_destination;
  ```

  How this is handled is institution specific. Possible scenarios:
  * Printing a notification message on the screen
  * Sending an e-mail
  * Triggering an alarm
  * etc...

- Trigger an event or other MLMs without waiting for them to finish using the `CALL` statement.  
This can be done with a delay:

  ```arden
  CALL other_mlm WITH 123, an_argument, "a string";
  CALL other_mlm WITH 123, an_argument, "a string" DELAY 10 MINUTES;
  CALL an_event DELAY 10 MINUTES;
  ```
