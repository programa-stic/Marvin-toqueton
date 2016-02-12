# Marvin Toquetón #

Herramienta de testing automático de GUI desarrollado para Marvin-dynamic-analyzer.

Version 0.1

El framework es utilizado para dos propósitos:
* Hookear elementos de la UI para interactuar con ellos sin conocer el layout de las Views.
* Modificar el acceso a identificadores específicos del dispositivo, contactos, información de la wifi e ubicación para permitir analizar si esos identificadores son transmitidos por la red o almacenados de forma insegura en el dispositivo.  

## Implementación ##

Toquetón instrumenta las siguientes Views para interactuar con la aplicación:

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

Cuando una View es creada, un mensaje es enviado a un Handler que corre en el main thread de la aplicación y es responsable de interactuar con ella periódicamente. Un retraso aleatorio para interactuar con la View es ajustado cada vez dependiendo de la view y su visibilidad.

Además, para extender la interacción con la aplicación, el fuzzer comienza una nueva actividad (no necesariamente exportada) continuamente cada cierto tiempo.

Los métodos instrumentados para acceder a la información privada del dispositivo está basada el el proyecto [ASA](https://github.com/c0d1ngb4d/ASA/). Estos métodos hookeados devuelven la información leída desde un archivo denominado 'privacy.json' ubicado en la memoria externa del dispositivo.

## Requerimientos ##

* Dispositivo rooteado con Android 4.3 o menor
* Cydia Substrate

## Créditos ##
* Joaquín Rinaudo ([@xeroxnir](www.twitter.com/xeroxnir))
* Juan Heguiabehere ([@jheguia](www.twitter.com/jheguia))

## Contacto ##
* Mandar un correo a stic en el dominio fundacionsadosky.org.ar
