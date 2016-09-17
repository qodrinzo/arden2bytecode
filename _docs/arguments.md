---
# GENERATED - DO NOT EDIT
title: Arguments
category: Getting Started
nav_order: 6
---
{% include get_url.liquid %}
Medical Logic Modules (MLMs) can access their arguments via the special `ARGUMENT` identifier:

```arden
(id, name, birthdate) := ARGUMENT;
```

## Passing arguments
To pass arguments from the command line, use the **-a** when running MLMs, followed by one or multiple arguments, separated by spaces.
An argument must be a valid Arden Syntax constant, such as `"Jane Doe"`, `1 YEAR` or `1970-01-01` or a list of constants, such as `("x",TRUE,"y")`. Note that string constants must be in between double quotes, otherwise an error will occur.
Special characters like quotes, parenthesis and spaces inside constants must be properly escaped, depending on your command line interpreter:

- The **Windows Command Prompt** can escape spaces inside a constant by wrapping it in double quotes. Quotes in a string constant need to be escaped with backslashes:

  ```shell
  arden2bytecode -a (3.5,2) "5 WEEKS" 123 "\"Jane Doe\"" -r arguments.mlm
  ```

- **Bash** needs to escape quotes, parenthesis and spaces. This can be done with backslashes or by wrapping the constant in single quotes:

  ```bash
  ./arden2bytecode -a '(3.5,2)' '5 WEEKS' 123 '"Jane Doe"' -r arguments.mlm
  ```

Concerning how to use the **-a** flag, you may also look at the [command line options]({{ baseurl }}/docs/command-line-options/) wiki page.

## Lists and multiple arguments
Lists are handled different than multiple arguments, but likely to be mixed up:

For the following statement

```arden
(value1, value2, value3) := ARGUMENT;
```

and the following arguments

```bash
arden2bytecode -a (1,2,3) -r arguments.mlm
```

`value1` will contain the whole list and the other values will be `NULL`.

The correct command line options would be:

```bash
arden2bytecode -a 1 2 3 -r arguments.mlm
```
