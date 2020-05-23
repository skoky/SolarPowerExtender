# SolarPowerExtender v2

SolarPower java based utility used to collect and display statistical data from Solar Panels and Inverters. 
This project modifies the utility to also send the collected data to external entity (ex: website).

Originally motivated by https://github.com/aviadoffer/SolarPowerExtender The `v2` is simplified and works with 
SolarPower 1.14SP7 (latest as of now)

This version does not send data to http server as this needs lot of changes to SolarPower app, but rather simply
publishes data using UDP socket that is part of JVM 1.7. This JVM is embedded in SolarPower app 1.14SP7.

How do I use this:
------------------

You will need to
- Modify the `P18ComUSBProcessor` and/or `P17ComUSBProcessor` (see source using Intellij idea or different Java code decompiler) 
and add this code:

```
// on global class level to initialize UDP socket
Exporter e = new Exporter();

// on the end of method pollQuery() to publish data over UDP
this.e.exportData(workInfo);
```

- copy all libs from SolarPower installation to `libs` directory here. This is required for extended classes compilation.
- Compile those  two classes plus one in `skoky` package and inject it to current version of SolarPower.jar. Make sure 
proper package path. The compilation must be done with proper java version - as of now JVM 1.7.+ - not newer) 
Make sure SolarPower is stopped while injecting. Backup original jar files in advance.
- Start SolarPower. If it does not startup, you likely compiled it with wrong java version. If the code fails
in the new code, see error files in temp directory, look for files `sp?-*.txt`
- listen on the same machine for data on UDP port 5555 using command 

```bash
   nc -ulp 5555
```

The extender should publish data in text form like this `SPV1;getWorkMode:Grid-tie with backup;getBatteryVoltage:47.6;power:10;at:1590093010655` where

- `SPV1` is protocol version 1
- `getWorkMode:Grid-tie with backup` is inverter working mode
- `getBatteryVoltage:47.6` is battery voltage 47.6V
- `power:10` is solar panel input power 10W
- `at:1590093010655` data were generated at this millis since epoch

Fill free to change the format or fields generated in the `skoky.Exporter` class.


