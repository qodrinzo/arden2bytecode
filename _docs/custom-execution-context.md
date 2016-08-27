---
# GENERATED - DO NOT EDIT
title: Custom Execution Context
category: Development
nav_order: 18
---
{% include get_url.liquid %}
This document is intended to be read by people who are capable of Java programming.

Replacing the execution context of Arden2ByteCode means providing a class that serves as information gateway to the "outer world" of a Medical Logic Module. Thus, via an execution context, you can integrate Arden2ByteCode into your data infrastructure.

Any execution context should be a subclass of the abstract class `arden.runtime.ExecutionContext`. Also a class `arden.runtime.BaseExecutionContext` exists, providing a simple implementation of the call statement.

The following methods of `arden.runtime.ExecutionContext` may be overridden:

* `public DatabaseQuery createQuery(String mapping)`  
  This method is called when a read statement like 
  `read last 3 from {select * from person}` in a MLM 
  (Medical Logic Module) is executed.  
  The string `mapping` contains whatever the braces of 
  the read statement contain (`select * from person` 
  as of the example). 
  These strings are specific to the environment the 
  MLM is attached to. In the above example, I used 
  SQL syntax, because Arden2ByteCode provides an 
  execution context where the mapping is passed to a 
  JDBC SQL driver specified by the user.  
  You may use the mapping string for whatever 
  information you need to pass from the MLM to the
  execution context.
* `public ArdenValue getMessage(String mapping)`  
  This method is called when a message is assigned
  using the `MESSAGE` statement.  
  An example would be:  
  `msg := MESSAGE {pneumonia};`  
  In this example `pneumonia` would be the mapping
  string.
* `public void write(ArdenValue message, String destination)`  
  This method is called when a write statement is
  executed.  
  Example: `WRITE "hello" at dest;`  
  Here, `message` would be `"hello"` and `destination`
  would be the destination mapping previously created
  with a line like `dest := destination{mapping};`.
* `public ArdenRunnable findModule(String name, String institution)`  
  This method is run, when another MLM is referenced in
  a data assign phrase.  
  Example: `find_allergies := MLM {find_allergies from my_institution};`  
  In the above example, `find_allergies` is the module
  name string and `my_institution` is the institution 
  string.
* `public ArdenRunnable findInterface(String mapping)`  
  This method is called, when an interface statement
  with the specified mapping is executed.
* `public void callWithDelay(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delay)`  
  This method is executed, when a call statement with
  a delay is executed.  
  Example: `mlmx := MLM 'my_mlm' CALL mlmx DELAY 3 days;`
* `public ArdenTime getEventTime()`  
  This method is called, whenever `EVENTTIME` is called
  in a MLM.
* `public ArdenTime getTriggerTime()`  
  This method is called, whenever `TRIGGERTIME` is called
  in a MLM.
* `public ArdenTime getCurrentTime()`  
  This method is called, whenever `NOW` is stated in a
  MLM.

You may implement these methods as of your needs.  
The classes `arden.runtime.BaseExecutionContext` and 
`arden.runtime.StdIOExecutionContext` provide examples of
how to implement these methods.

If you e.g. try to attach a MLM to a certain database or
any other datasource, you can for example override the 
`createQuery()` method and pass the parameters to your
database query parser.

The `write()` and `getMessage()` methods are intended to
inform the user of MLM messages. They could be implemented
as to display information on a GUI or web interface.

`findModule()` and `callWithDelay()` are intended to load
other MLMs but they can also serve as interface to your
IT infrastructure meaning you can load programs written in
other languages than Arden Syntax as well if you want 
that.
