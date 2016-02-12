# Marvin Toquetón #

Automated GUI testing utility developed for Marvin-dynamic-analyzer. 

Version 0.1

The framework is used for two purposes:

* Hook UI elements to interact with them without knowing the view layout.
* Modify access to device specific identifiers, contacts, wifi and locations informations to be able to 
analyze if those identifiers are transmitted to the network or stored insecurely in the device

## Implementation description ##

Toquetón instruments the following Views for interacting with the application:

* EditText
* Checkbox
* RadioGroup
* ToggleButton
* Spinner
* DatePicker
* TimePicker
* Button
* TextView
* Menu
* AdapterView
* SearchView
* ViewGroup
* View

When a View is created, a message is send to a Handler running in the main thread of the application that is responsible of interacting with it periodically. A random delay for interacting with it is set depending on the type of view and it's visibility status. 

Also, in order to extend the interaction with the application, the fuzzer starts a new random activity (non necessarily exported) repeatedly.

Instrumentation for methods that access to private information identifiers and resources is based in [ASA](https://github.com/c0d1ngb4d/ASA/) project. The methods hooked and the information return by hooks is read from a file stored in external storage named 'privacy.json'. 

## Requirements ##

* Android rooted device with 4.3 or lower 
* Cydia Substrate

