As of September 2012, it is possible to declare events in the evoke slot of a MLM.

An example would be:

    //...
    data: ...;;
    evoke: every 5 seconds for 99 years starting 2012-01-01;;
    logic: conclude true;;
    action: ...;;
    //...

Using the above evoke slot, the MLM is executed every 5 seconds.  
The syntax of the evoke slot is documented in the Arden Syntax standard.

For the evoke slot, you may as well use:

* Fixed dates or timestamps in ISO format  
  `evoke: 1992-01-03T14:23:17.0;;`
* Event variables declared in the data slot  
  `evoke: penicillin_storage;; // must be declared in data slot`
* A blank evoke statement  
  `evoke: ;;`
* A combination of duration, "AFTER TIME OF" and an event the time of which is known
  `evoke: 3 days after time of penicillin_storage;;`
* A periodic schedule as above

You can start Arden2ByteCode with the `--daemon` option instead of `-r` or `-c` to start a daemon that invokes the MLM on the desired schedule.  
If this does not suit your needs, you can write an own wrapper that accesses the MLMs evoke slot via `getEvoke()` in the `MedicalLogicModule` interface.  
Also you can override the ExecutionContexts `getEvent()` method to provide own events.