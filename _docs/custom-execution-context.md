---
# GENERATED - DO NOT EDIT
title: Custom Execution Context
category: Development
nav_order: 18
---
{% include get_url.liquid %}
Replacing the execution context of Arden2ByteCode means providing a class that serves as information gateway to the "outer environment" of a Medical Logic Module. Thus, via an execution context, you can integrate Arden2ByteCode into your data infrastructure.

Any execution context should be a subclass of the abstract class `arden.runtime.ExecutionContext`. Also a class `arden.runtime.BaseExecutionContext` exists, providing the ability to look up and call MLMs from the classpath. Cyclic or delayed calls are possible if an `arden.EventEngine` is provided to the BaseExecutionContext,

## Methods
The following methods of `arden.runtime.ExecutionContext` may be overridden:

```java
public DatabaseQuery createQuery(String mapping)
```
This method is called when a `READ` statement like `READ LAST 3 FROM {SELECT * FROM person}` in a MLM (Medical Logic Module) is executed.  
The string `mapping` contains whatever the braces of the read statement contain (`SELECT * FROM person` as of the example). These strings are specific to the environment the MLM is attached to. In the above example, SQL syntax is used, because Arden2ByteCode provides an execution context where the mapping is passed to a JDBC SQL driver specified by the user. You may use the mapping string for whatever information you need to pass from the MLM to the execution context.
<br><br>

```java
public ArdenValue getMessage(String mapping)
```
This method is called when a message is assigned using the `MESSAGE` statement.  
For example: `msg := MESSAGE {pneumonia};`
<br><br>

```java
public EvokeEvent getEvent(String mapping)
```
This method is called when a event is assigned using the `EVENT` statement.  
For example: `patient_admission := EVENT {patient admission};`  
Usually a `arden.runtime.events.MappedEvokeEvent` is returned.
<br><br>

```java
public void write(ArdenValue message, String destination)
```
This method is called when a message is written in the action slot using the `WRITE` statement.  
Example: `WRITE "hello" AT dest;`  
Here, `message` would be `"hello"` and `destination` would be the destination mapping previously created with a line like `dest := DESTINATION {mapping};`.
<br><br>

```java
public ArdenRunnable findModule(String name, String institution)
```
This method is run, when another MLM is referenced in the data slot using the `MLM` statement.  
Example: `find_allergies := MLM 'find_allergies' FROM INSTITUTION "my_institution";`  
In the above example, `find_allergies` is the module name string and `my_institution` is the institution string.
<br><br>

```java
public ArdenRunnable findInterface(String mapping)
```
This method is called, when an interface is referenced using the `INTERFACE` statement.  
Example: `web_service := INTERFACE {my web service}`  
The returned `arden.runtime.ArdenRunnable` acts as a proxy for an external program or service.
<br><br>

```java
public void callWithDelay(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delay)
```
This method is called, when a `CALL` statement is executed in the action slot.  
Example: `mlmx := MLM 'my_mlm' CALL mlmx DELAY 3 days;`
<br><br>

```java
public ArdenTime getEventTime()
```
This method is called, whenever `EVENTTIME` is used in a MLM.
<br><br>

```java
public ArdenTime getTriggerTime()
```
This method is called, whenever `TRIGGERTIME` is used in a MLM.
<br><br>

```java
public ArdenTime getCurrentTime()
```
This method is called, whenever `CURRENTTIME` is stated in a MLM.

## Examples
The classes `arden.runtime.BaseExecutionContext`, `arden.runtime.StdIOExecutionContext` and `arden.runtime.JDBCExecutionContext` provide examples of how to implement these methods.

If you try to attach a MLM to a certain database or any other datasource, you can for example override the `createQuery()` method and pass the parameters to your database query parser.  
The `write()` and `getMessage()` methods are intended to inform the user of MLM messages. They could be implemented to display information on a GUI or web interface.  
`findModule()` and `findInterface()` can serve as interface to your IT infrastructure meaning you can load programs written in other languages than Arden Syntax as well if you want that.

## Command line integration
To use Arden2ByteCode's command line interface for the new execution context, add it to the `arden.MainClass#createExecutionContext()` method.  
This allows running simple MLMs directly, as well as using the `arden.EventEngine` and listening to events an a port. An argument string can be passed to it via the `--environment` option.
