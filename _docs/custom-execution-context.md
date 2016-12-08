---
# GENERATED - DO NOT EDIT
title: Custom Execution Context
category: Development
nav_order: 18
---
{% include get_url.liquid %}
Replacing the execution context of Arden2ByteCode means providing a class that serves as information gateway to the "outer environment" of a Medical Logic Module. Thus, via an execution context, you can integrate Arden2ByteCode into your data infrastructure.

Any execution context should be a subclass of the abstract class `arden.runtime.ExecutionContext`. Also a class `arden.runtime.BaseExecutionContext` exists, providing the ability to look up and call MLMs from the classpath. Cyclic or delayed calls are possible if an `arden.EventEngine` is provided to the BaseExecutionContext.

## Methods
The following methods of `arden.runtime.ExecutionContext` may be overridden:  

```java
public DatabaseQuery createQuery(MedicalLogicModule mlm, String mapping)
```
This method is called when a `READ` statement like `READ LAST 3 FROM {SELECT * FROM person}` in a MLM (Medical Logic Module) is executed.  
The string `mapping` contains whatever the braces of the read statement contain (`SELECT * FROM person` as of the example). These strings are specific to the environment the MLM is attached to. In the above example, SQL syntax is used, because Arden2ByteCode provides an execution context where the mapping is passed to a JDBC SQL driver specified by the user. You may use the mapping string for whatever information you need to pass from the MLM to the execution context.  
The MLMs variables can be accessed via the `mlm` parameter, e.g. `mlm.getValue("eventtime")`.  
<br>

```java
public ArdenValue getMessage(MedicalLogicModule mlm, String mapping)
```
This method is called when a message is assigned using the `MESSAGE` statement.  
Example: `msg := MESSAGE {pneumonia};`  
<br>

```java
public ArdenValue getMessageAs(MedicalLogicModule mlm, String mapping, ObjectType type)
```
This method is called when a message is assigned using the `MESSAGE AS` statement.  
Example:  
`Patient := OBJECT[name, date_of_birth];`  
`msg := MESSAGE AS Patient {MR-111-1111};`  
<br>

```java
public ArdenValue getDestination(MedicalLogicModule mlm, String mapping)
```
This method is called when a destination is assigned using the `DESTINATION` statement.  
Example: `dest := DESTINATION {email: email@example.com};`  
<br>

```java
public ArdenValue getDestinationAs(MedicalLogicModule mlm, String mapping, ObjectType type)
```
This method is called when a destination is assigned using the `DESTINATION AS` statement.  
Example:  
`Email := OBJECT[subject, text];`  
`dest := DESTINATION AS Email {email: email@example.com};`  
<br>

```java
public ArdenEvent getEvent(MedicalLogicModule mlm, String mapping)
```
This method is called when an event is assigned using the `EVENT` statement.  
For example: `patient_admission := EVENT {patient admission};`  
<br>

```java
public ArdenRunnable findInterface(MedicalLogicModule mlm, String mapping)
```
This method is called, when an interface is referenced using the `INTERFACE` statement.  
Example: `web_service := INTERFACE {my web service};`  
The returned `arden.runtime.ArdenRunnable` acts as a proxy for an external program or service.  
<br>

```java
public MedicalLogicModule findModule(String name, String institution)
```
This method is run, when another MLM is referenced in the data slot using the `MLM` statement.  
Example: `find_allergies := MLM 'find_allergies' FROM INSTITUTION "my_institution";`  
In the above example, `find_allergies` is the module name string and `my_institution` is the institution string.  
<br>

```java
public MedicalLogicModule[] findModules(ArdenEvent event)
```
This method is run before an event is called in the logic slot. It should return all MLMs that are directly (no delay) evoked by the event.
Example: `collected_results := CALL an_event WITH 1,2,3;`   
<br>

```java
public ArdenTime getCurrentTime()
```
This method is called, whenever `CURRENTTIME` is used in a MLM.  
<br>

```java
public void write(ArdenValue message, ArdenValue destination, double urgency)
```
This method is called when a message is written in the action slot using the `WRITE` statement.  The `urgency` is defined in the MLMs urgency slot.
Example: `WRITE "hello" AT dest;`  
Here, `message` would be `"hello"` and `destination` would be the destination, which was created previously created with a line like `dest := DESTINATION {email: email@example.com};`.  
<br>

```java
public void call(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delay, Trigger trigger, double urgency)
```
This method is called, when an MLM `CALL` statement is executed in the action slot. The `trigger` parameter can be used to calculate the called MLMs `EVENTTIME` and `TRIGGERTIME`.   
Example:  
`other_mlm := MLM 'other';`  
`CALL other_mlm WITH 2, TRUE, "xyz" DELAY 5 minutes;`  
<br>

```java
public void call(ArdenEvent event, ArdenValue delay, double urgency)
```
This method is called, when an event `CALL` statement is executed in the action slot.
Example:  
`my_event := EVENT {custom event};`  
`CALL my_event DELAY 1 week;`  

## Examples
The classes `arden.runtime.BaseExecutionContext`, `arden.runtime.StdIOExecutionContext` and `arden.runtime.JDBCExecutionContext` provide examples of how to implement these methods.

If you try to attach a MLM to a certain database or any other datasource, you can for example override the `createQuery()` method and pass the parameters to your database query parser.  
The `write()`,  `getMessage()` and `getDestination()` methods are intended to inform the user of MLM messages. They could be implemented to display information on a GUI or web interface.  
`findModule()` and `findInterface()` can serve as interface to your IT infrastructure meaning you can load programs written in other languages than Arden Syntax as well if you want that.

## Command line integration
To use Arden2ByteCode's command line interface for the new execution context, add it to the `arden.MainClass#createExecutionContext()` method.  
This allows running simple MLMs directly, as well as using the `arden.engine.EvokeEngine` and listening to events on a port. An argument string can be passed to it via the `--environment` option.
