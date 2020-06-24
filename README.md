# Android-Internet-connectivity-class-java
This class checks for internet connection and speed on your android phone.
It checks for both mobile and wifi connections.

For mobile network speeds, the network subtypes below are checked

	1.  NETWORK_TYPE_1xRTT:
	2.  NETWORK_TYPE_EDGE:    return false;   // ~ 50-100     -Kbps   
	3.  NETWORK_TYPE_CDMA:    return false;   // ~ 14-64      -Kbps   
	4.  NETWORK_TYPE_EVDO_0:  return true;    // ~ 400-1000   -Kbps
	5.  NETWORK_TYPE_EVDO_A:  return true;    // ~ 600-1400   -Kbps
	6.  NETWORK_TYPE_GPRS:    return false;   // ~ 100        -Kbps   
	7.  NETWORK_TYPE_HSDPA:   return true;    // ~ 2000-1400  -Kbps
	8.  NETWORK_TYPE_HSPA:    return true;    // ~ 700-1700   -Kbps
	9.  NETWORK_TYPE_HSUPA:   return true;    // ~ 1000-2300  -Kbps
	10. NETWORK_TYPE_UMTS:    return true;    // ~ 400-7000   -Kbps
	11. NETWORK_TYPE_EHRPD:   return true;    // ~ 1000-2000  -Kbps   // API level 11
	12. NETWORK_TYPE_EVDO_B:  return true;    // ~ 5000       -Kbps   // API level 9
	13. NETWORK_TYPE_HSPAP:   return true;    // ~ 10000-20000-Kbps   // API level 13
	14. NETWORK_TYPE_IDEN:    return false;   // ~ 25         -Kbps   // API level 8
	15. NETWORK_TYPE_LTE:     return true;    // ~ 10000+     -Kbps   // API level 11
	16. NETWORK_TYPE_UNKNOWN:                 // ~ Unknown

<br/>For wifi speed calculation:

    1. A file is downloaded from the internet.
    2. Get download start time.
    3. Calculate download time by subtracting download endTime from download startTime.
		double timeTakenMills = Math.floor(endTime[0] - startTime);  // time taken in milliseconds.
          	double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds.
          	final int kilobytesPerSec = (int) Math.round(1024 / timeTakenSecs);  
		
    4. Get the download speed by dividing the file size by time taken to download
          double speed = fileSize[0] / timeTakenMills;


<br/>The following permissions will be required. Add them to your `AndroidManifest.xml` file.

	1. <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	2. <uses-permission android:name="android.permission.INTERNET" />
