---
# GENERATED - DO NOT EDIT
title: Scheduling
category: Getting Started
nav_order: 7
---
{% include get_url.liquid %}
It is possible to declare trigger statements in the evoke slot of a MLM.

An example would be:

```arden
…
data: an_event := EVENT{an event};;
evoke: an_event;;
logic: CONCLUDE TRUE;;
action: WRITE CURRENTTIME;;
…
```

This MLM would write the current time, when the event happens.

For the evoke slot, you may use:

- A blank evoke statement that implies the MLM can only be called:  
`evoke: ;;`

- Fixed dates or datetimes in ISO format:  
`evoke: 1992-01-03T14:23:17.0;;`

- Event variables declared in the data slot:  
`evoke: an_event;;`  
`evoke: an_event OR another_event;;`  
`evoke: ANY OF(an_event, another_event);;`  

- Delayed triggers:  
`evoke: 1 YEAR AFTER 2000-01-01;;`  
`evoke: 15 SECONDS AFTER TIME OF an_event;;`

- Cyclic triggers:  
`evoke: EVERY 5 SECONDS FOR 2 WEEKS STARTING 2016-07-16T16:21:00;;`  
`evoke: EVERY 5 SECONDS FOR 2 WEEKS STARTING TIME OF an_event;;`  
`evoke: EVERY 5 SECONDS FOR 2 WEEKS STARTING TIME OF an_event UNTIL a_number > 5;;`
 
- Various combinations:  
`evoke: EVERY 5 SECONDS FOR 1 MINUTE STARTING 10 SECONDS AFTER TIME OF meh;;`  
`evoke: EVERY 5 SECONDS FOR 2 WEEKS STARTING TIME OF (an_event OR another_event);;`  

## Starting the Evoke Engine
You can start Arden2ByteCode with the **-e** or **--engine** option instead of **-r** or **-c**. This will start an engine that invokes the MLM on the desired schedule. It will listen for events indefinitely or until closed with `ctrl+c`.

The engine can be used together with the **--port** _number_ option. This will start a server, that listens for event mapping strings on the given port.  
To send an event to the server on port 9701 type (in Linux with Bash):

```bash
echo "Patient admission" > /dev/tcp/127.0.0.1/9701
```

Or connect via `telnet`:

```bash
telnet 127.0.0.1 9701
```
