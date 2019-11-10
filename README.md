# Android-Internet-connectivity-class
This class checks for internet connection and speed on your android phone.
It checks for both mobile and wifi connections.

For mobile network speeds, the network subtypes below are checked
	NETWORK_TYPE_1xRTT:
	NETWORK_TYPE_EDGE:    return false;   // ~ 50-100     -Kbps   
	NETWORK_TYPE_CDMA:    return false;   // ~ 14-64      -Kbps   
	NETWORK_TYPE_EVDO_0:  return true;    // ~ 400-1000   -Kbps
	NETWORK_TYPE_EVDO_A:  return true;    // ~ 600-1400   -Kbps
	NETWORK_TYPE_GPRS:    return false;   // ~ 100        -Kbps   
	NETWORK_TYPE_HSDPA:   return true;    // ~ 2000-1400  -Kbps
	NETWORK_TYPE_HSPA:    return true;    // ~ 700-1700   -Kbps
	NETWORK_TYPE_HSUPA:   return true;    // ~ 1000-2300  -Kbps
	NETWORK_TYPE_UMTS:    return true;    // ~ 400-7000   -Kbps
	NETWORK_TYPE_EHRPD:   return true;    // ~ 1000-2000  -Kbps   // API level 11
	NETWORK_TYPE_EVDO_B:  return true;    // ~ 5000       -Kbps   // API level 9
	NETWORK_TYPE_HSPAP:   return true;    // ~ 10000-20000-Kbps   // API level 13
	NETWORK_TYPE_IDEN:    return false;   // ~ 25         -Kbps   // API level 8
	NETWORK_TYPE_LTE:     return true;    // ~ 10000+     -Kbps   // API level 11
	NETWORK_TYPE_UNKNOWN:                 // ~ Unknown

For wifi speed calculation:

    1. A file is downloaded from the internet.
    2. Get download start time
    3. Calculate download time by subtracting download endTime from download startTime
			double timeTakenMills = Math.floor(endTime[0] - startTime);  // time taken in milliseconds
          	double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
          	final int kilobytesPerSec = (int) Math.round(1024 / timeTakenSecs);  	
    4. Get the download speed by dividing the file size by time taken to download
          double speed = fileSize[0] / timeTakenMills;
