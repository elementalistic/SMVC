#SMVC
Starmade Vote Checker

Java based tool for handling vote rewards for starmade-servers.com

This is the first Java program I have created, so it is probably full of bad methods of doing things.

#Config
File: SMVC_Settings.json
```
servername:			Unused Currently (String)
serverhost:			IP or Hostname of StarMade server (String)
serverport:			Port of StarMade server (Integer, Default 4242) 
serverkey:			API Key from starmade-servers.com  (String)
votereward:			Number of credits to award players for voting  (Integer)
passthrough:		Prefixed to API urls. (passthrough.php) (String)
adminpassword:		Super Admin password from StarMade Server Config (String) 
throttle:			Delay between validating each vote (Miliseconds)
revertonfailclaim:	Take credits back if API claim failed ("true" or "false")
messages:			Various Messages for events
	start:			Sent before any votes are checked
	vote:			Sent after claim has been verified. Accepts %player for playername
```
