package com.artpellets.custom_adapters.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * This class checks for device's network connectivity and network speed
 * Wifi network speed is calculated by downloading a file and calculating network speed
 * 
 */
public class InternetConnectivity{

    @SuppressWarnings("unused")
    private static final String TAG = InternetConnectivity.class.getSimpleName();

    /**
     * Check if there is any internet connection
     * @param context - For getting network info
     * @return boolean
     */
    public static boolean isConnectedToAnyNetwork(Context context){
        NetworkInfo info = InternetConnectivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check for Wifi network connection
     * @param context - For getting network info
     * @return return
     */
    public static boolean isConnectedToWifiNetwork(Context context){
        NetworkInfo info = InternetConnectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check for mobile network connection
     * @param context - For getting network info
     * @return boolean
     */
    public static boolean isConnectedToMobileNetwork(Context context){
        NetworkInfo info = InternetConnectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connection
     * @param context - For getting network info
     * @return boolean
     */
    public static boolean isConnectionFast(Context context){
        NetworkInfo info = InternetConnectivity.getNetworkInfo(context);
        return (info != null && info.isConnected() && InternetConnectivity.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast with specific type and subType
     * @param type - networkType
     * @param subType - networkSubType
     * @return boolean
     */
    public static boolean isConnectionFast(int type, int subType){
        if (type==ConnectivityManager.TYPE_WIFI){

            // Calculate Wifi network speed - (Wifi is not always fast)
            return calculateNetworkSpeedByDownloadingFile();

        } else if(type==ConnectivityManager.TYPE_MOBILE){

            switch(subType){

                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_EDGE:    return false;   // ~ 50-100     -Kbps   slow-----
                case TelephonyManager.NETWORK_TYPE_CDMA:    return false;   // ~ 14-64      -Kbps   slow-----
                case TelephonyManager.NETWORK_TYPE_EVDO_0:  return true;    // ~ 400-1000   -Kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:  return true;    // ~ 600-1400   -Kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:    return false;   // ~ 100        -Kbps   slow-----
                case TelephonyManager.NETWORK_TYPE_HSDPA:   return true;    // ~ 2000-1400  -Kbps
                case TelephonyManager.NETWORK_TYPE_HSPA:    return true;    // ~ 700-1700   -Kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:   return true;    // ~ 1000-2300  -Kbps
                case TelephonyManager.NETWORK_TYPE_UMTS:    return true;    // ~ 400-7000   -Kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD:   return true;    // ~ 1000-2000  -Kbps   // API level 11
                case TelephonyManager.NETWORK_TYPE_EVDO_B:  return true;    // ~ 5000       -Kbps   // API level 9
                case TelephonyManager.NETWORK_TYPE_HSPAP:   return true;    // ~ 10000-20000-Kbps   // API level 13
                case TelephonyManager.NETWORK_TYPE_IDEN:    return false;   // ~ 25         -Kbps   // API level 8
                case TelephonyManager.NETWORK_TYPE_LTE:     return true;    // ~ 10000+     -Kbps   // API level 11
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:                 // ~ Unknown
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Get network info
     * @param context - For getting connectivity service
     * @return NetworkInfo
     */
    private static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
    }

    /**
     * Calculate network speed by downloading file
     * @return networkState
     */
    private static boolean calculateNetworkSpeedByDownloadingFile(){

        long startTime;
        final long[] endTime = new long[1];
        final long[] fileSize = new long[1];
        OkHttpClient client = new OkHttpClient();
        final boolean[] isFastNetwork = {false};
        

        // Create and build request
        Request request = new Request.Builder()
                // Url of image to be downloaded
                .url(NetworkUtils.NetworkUrls.backendAndroidFolder + "testImage.JPG")
                .build();

        // Get start time
        startTime = System.currentTimeMillis();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @SuppressWarnings("unused")
            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {

                    // Switch error codes
                    switch (response.code()) {
                        case 401:   throw new IOException("Unauthorized " + response.code());
                        case 404:   throw new IOException("Not found " + response.code());
                        case 408:   throw new IOException("Request Timeout " + response.code());
                        case 444:   throw new IOException("Connection Closed Without Response " + response.code());
                        case 500:   throw new IOException("Internal Server Error " + response.code());
                        default:    throw new IOException("Unexpected code " + response.code() + " "  + response);
                    }

                }

                // Get headers from response
                Headers responseHeaders = response.headers();

                // Loop through headers
                /*for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }*/

                try (InputStream input = response.body().byteStream()) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];

                    while (input.read(buffer) != -1) {
                        bos.write(buffer);
                    }

                    // byte[] docBuffer = bos.toByteArray();
                    fileSize[0] = bos.size();
                }

                // Write to end time
                endTime[0] = System.currentTimeMillis();

                // Calculate download time by subtracting endTime from startTime
                double timeTakenMills = Math.floor(endTime[0] - startTime);  // time taken in milliseconds
                double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
                final int kilobytesPerSec = (int) Math.round(1024 / timeTakenSecs);
                
                //Bandwidth in Kbps
                //POOR -      Bandwidth under 100 Kbps.
                //MODERATE    Bandwidth between 150 and 550 Kbps.
                //GOOD        Bandwidth over 2000 Kbps.
                //EXCELLENT   Bandwidth over 2000 Kbps.
                //UNKNOWN     Connection quality cannot be found.
                if (kilobytesPerSec > 100){

                    // Update connection state
                    isFastNetwork[0] = true;
                }

                // get the download speed by dividing the file size by time taken to download
                double speed = fileSize[0] / timeTakenMills;

                // DEBUG Logs
                /*Log.d(TAG, "Time taken in secs: " + timeTakenSecs);
                Log.d(TAG, "Bytes per sec: " + kilobytePerSec * 1000);
                Log.d(TAG, "Kilobytes per sec: " + kilobytePerSec);
                Log.d(TAG, "Download Speed: " + speed);
                Log.d(TAG, "File size: " + fileSize[0]);*/
            }
        });

        // Return connection state
        return isFastNetwork[0];
    }

}
