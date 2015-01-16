# javaschtasks.batchjob
A simple javafx gui to parse windows scheduler task, edit arguments and submit new XML to remote windows machines.  


Quick Start:

-run JobSumitGui click set folder and browse to sampleFiles
-close program and refresh eclipse, click on jobsubmit.properties and edit the default to your environment. 

Syntax we're following: 
schtasks /create /tn TaskName /tr TaskRun  [/s computer [/u [domain\]user /p password]] [/ru {[Domain\]User | "System"} [/rp Password]] 


Here is the doc for schtasks command:
http://www.microsoft.com/resources/documentation/windows/xp/all/proddocs/en-us/schtasks.mspx?mfr=true

Make sure you use Jave 8u20 or higher or the args wont be editable because of this error:

java.lang.RuntimeException: ControlsFX Error: ControlsFX 8.0.6_20 requires at least Java 8u20
	at impl.org.controlsfx.version.VersionChecker.doVersionCheck(VersionChecker.java:83)
	
Once you get the system running you can export task from a windows scheduler point javaschtask to your exported tasks. 

