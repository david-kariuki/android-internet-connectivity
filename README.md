# JAVA Android Internet Connectivity
This class checks for internet connection and speed on your android phone.
It checks for both mobile and wifi connections.

For mobile network speeds, the `network subtypes` below are checked

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

<br/>For WiFi and VPN speed calculation:

    1. A file is downloaded from the internet.
    2. Get download start time.
    3. Calculate download time by subtracting download endTime from download startTime.
		double timeTakenMills = Math.floor(endTime[0] - startTime);  // time taken in milliseconds.
          	double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds.
          	final int kilobytesPerSec = (int) Math.round(1024 / timeTakenSecs);  
		
    4. Get the download speed by dividing the file size by time taken to download
          double speed = fileSize[0] / timeTakenMills;


<br/>**Manifest**
<br/>The following permissions will be required. Add them to your `AndroidManifest.xml` file.
```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```

<br/>**build.gradle**
<br/>Add `okhttp` library to your `dependencies` in your `build.gradle` file.
```
dependencies {
    implementation 'com.squareup.okhttp:okhttp-urlconnection:2.0.0'
}
```

<br/>

# Examples in code:<br/>

Check connectivity to either WiFi or mobile connection.
```
// Check connectivity to either WiFi or mobile connection
if (InternetConnectivity.isConnectedToAnyNetwork(mContext)){
    // Run process requiring WiFi or mobile connection
    doSomething();

    // Check if connection is fast
    if (InternetConnectivity.isConnectionFast(mContext)){
	// Run heavy request requiring strong network connection
	doSomething();
    }
}
```

Check connectivity to mobile connection.
```
// Check connectivity to mobile connection
if (InternetConnectivity.isConnectedToMobileNetwork(mContext)){
    // Run process on mobile network
    doSomething();

    // Check if connection is fast
    if (InternetConnectivity.isConnectionFast(mContext)){
	// Run heavy request requiring strong network connection
	doSomething();
    }

    // Check mobile connection speed with subtype
    if (InternetConnectivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_UNKNOWN)){
	// Throw UNKNOWN NETWORK exception
	doSomething();
    }
}
```

Check connectivity to WiFi connection.
```
// Check connectivity to WiFi connection
if (InternetConnectivity.isConnectedToWifiNetwork(mContext)){
    // Run process on WiFi connection - e.g. Heavy downloads, updates etc
    doSomething();

    // Check if connection is fast
    if (InternetConnectivity.isConnectionFast(mContext)){
	// Run heavy request requiring strong network connection
	doSomething();
    }


    // Check if connection is fast and return network information
    // Returns HashMap<String, String> with details (timeTakenMillis, timeTakenSecs, linkSpeedBps, linkSpeedKbps, linkSpeedMbps, testFileDownloadSpeed, testFileSize)
    String timeTakenMillis, timeTakenSecs, linkSpeedBps, linkSpeedKbps, linkSpeedMbps, testFileDownloadSpeed, testFileSize;
    boolean isFastNetwork;
    connectionSpeedInfo = InternetConnectivity.getWiFiConnectionSpeedInfo(mContext);
    //noinspection unused
    timeTakenMillis = connectionSpeedInfo.get("timeTakenMillis");
    timeTakenSecs = connectionSpeedInfo.get("timeTakenSecs");
    linkSpeedBps = connectionSpeedInfo.get("linkSpeedBps");
    linkSpeedKbps = connectionSpeedInfo.get("linkSpeedKbps");
    linkSpeedMbps = connectionSpeedInfo.get("linkSpeedMbps");
    testFileDownloadSpeed = connectionSpeedInfo.get("testFileDownloadSpeed");
    testFileSize = connectionSpeedInfo.get("testFileSize");
    isFastNetwork = Boolean.parseBoolean(connectionSpeedInfo.get("isFastNetwork"));
} 
```

Check connectivity to VPN connection.
```
// Check connectivity to VPN connection
if (InternetConnectivity.isConnectedToVPNNetwork(mContext)){
    // Run process on WiFi connection - e.g. Heavy downloads, updates etc
    doSomething();

    // Check if connection is fast
    if (InternetConnectivity.isConnectionFast(mContext)){
	// Run heavy request requiring strong network connection
	doSomething();
    }

    // Check if connection is fast and return network information
    // Returns HashMap<String, String> with details (timeTakenMillis, timeTakenSecs, linkSpeedBps, linkSpeedKbps, linkSpeedMbps, testFileDownloadSpeed, testFileSize)
    String timeTakenMillis, timeTakenSecs, linkSpeedBps, linkSpeedKbps, linkSpeedMbps, testFileDownloadSpeed, testFileSize;
    boolean isFastNetwork;
    connectionSpeedInfo = InternetConnectivity.getVPNConnectionSpeedInfo(mContext);
    timeTakenMillis = connectionSpeedInfo.get("timeTakenMillis");
    timeTakenSecs = connectionSpeedInfo.get("timeTakenSecs");
    linkSpeedBps = connectionSpeedInfo.get("linkSpeedBps");
    linkSpeedKbps = connectionSpeedInfo.get("linkSpeedKbps");
    linkSpeedMbps = connectionSpeedInfo.get("linkSpeedMbps");
    testFileDownloadSpeed = connectionSpeedInfo.get("testFileDownloadSpeed");
    testFileSize = connectionSpeedInfo.get("testFileSize");
    isFastNetwork = Boolean.parseBoolean(connectionSpeedInfo.get("isFastNetwork"));
}
```
<br/>

# Android Application Example.

An android application with code examples named `InternetConnectivityExample.zip` has been included in this repository.
It contains: 
1. `InternetConnectivity.class`
2. `Example code implementations`
3. A basic `network_security_config.xml` file - could be used to configure network traffic like disabling `clearTextTraffic` to domains and subdomains.
