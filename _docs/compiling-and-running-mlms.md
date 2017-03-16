---
# GENERATED - DO NOT EDIT
title: Compiling and Running MLMs
category: Development
nav_order: 19
---
{% include get_url.liquid %}
Compiling:

```java
MedicalLogicModule mlm = new Compiler().compileMlm(new FileReader("./my_mlm.mlm"));
```

Compiling with debug info (e.g. display current line in MLM in stacktrace when an exception occurs):

```java
String mlmPath = "./my_mlm.mlm";
Compiler compiler = new Compiler();
compiler.enableDebugging(mlmPath);
MedicalLogicModule mlm = compiler.compileMlm(new FileReader(mlmPath));
```

Running:

```java
ExecutionContext context = new ExecutionContext() {
    public void write(ArdenValue message, ArdenValue destination, double urgency) {
        System.out.println(message.toString());
    }
};
ArdenValue[] arguments = new ArdenValue[] {};
Trigger trigger = new CallTrigger();  // allows setting the eventtime, evoking event, etc.
ArdenValue[] returnValues = mlm.run(context, arguments, trigger);
```
