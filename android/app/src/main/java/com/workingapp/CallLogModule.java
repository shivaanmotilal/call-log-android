package com.workingapp;

import android.widget.Toast;

//to create react bridge
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableNativeMap;       //Native bridge component like WritableNativeMap and 
import com.facebook.react.bridge.WritableNativeArray;     //WritableNativeArray are used instead of JSONObject and JSONArray
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.bridge.Arguments;


//android tools imports
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;   //import cursor to parse call logs
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.os.Bundle;
import android.text.Html;  //import html to  make *online query to content provider
import android.util.Log;
import android.view.View;
import android.webkit.WebView;  //Commented out to use Facebook webview instead
import android.webkit.WebViewClient;
import android.widget.Toast;

import android.widget.ImageView;
import android.view.Gravity;   //import gravity to centre layout
import android.view.ViewGroup;
import android.view.LayoutInflater;


import java.util.Map;
import java.net.*;      //import uri - a Uniform Resource Identifier (URI) is a string of characters used to identify a resource. Such identification enables interaction with representations of the resource over a network
import java.util.Date;  //deprecated
import java.util.Calendar; //use this instead
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.widget.TextView;
import java.util.HashMap;
import java.net.URLEncoder; 

import org.json.*;  //JSON tools to create JSON String object

public class CallLogModule extends ReactContextBaseJavaModule implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String REACT_CLASS = "CallLogAndroid";
    private static final String E_LAYOUT_ERROR = "E_LAYOUT_ERROR";
    ReactApplicationContext reactContext;
    private static HashMap<String, String> rowDataCall;
    private static final String TAG = "CallLog";
    private static final int URL_LOADER = 1;

    private static TextView callLogsTextView;

    private static String[] strBuffer = new String[1];
    private String str;

    //Create instance of writable array
    public static WritableArray callList;

    /**Constructor */
    public CallLogModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext= reactContext;
    }

    public Activity getActivity() {
        return this.getCurrentActivity();
    }

    /**Loads all calls */
    @Override
    @ReactMethod
    public Loader onCreateLoader(int loaderID, Bundle args) {

    Log.d(TAG, "onCreateLoader() >> loaderID : " + loaderID);
    
    switch (loaderID) {
        case URL_LOADER:
        // Returns a new CursorLoader
        return new CursorLoader(
        reactContext,   // Parent activity context
        CallLog.Calls.CONTENT_URI,        // Table to query
        null,     // Projection to return
        null,            // No selection clause
        null,            // No selection arguments
        null             // Default sort order
        );

        default:
        return null;
        }
    }

    /**On completing load, return/output formatted string*/
    @Override
    @ReactMethod
    public void onLoadFinished(Loader loader, Cursor managedCursor) {
        Log.d(TAG, "onLoadFinished()");
 
        StringBuilder sb = new StringBuilder();
 
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
 
        sb.append("<h4>Call Log Details <h4>");
        sb.append("\n");
        sb.append("\n");
 
        sb.append("<table>");
 
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime= null;
            if( (!callDate.trim().equals("") ) && (callDate != null)) {    
                callDayTime = new Date(Long.valueOf(callDate));  //if value of long is null
            }
            String callDuration = managedCursor.getString(duration);
            String dir = null;
 
            int callTypeCode = Integer.parseInt(callType);
            switch (callTypeCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing";
                    break;
 
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming";
                    break;
 
                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }
 
            sb.append("<tr>")
                    .append("<td>Phone Number: </td>")
                    .append("<td><strong>")
                    .append(phNumber)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Call Type:</td>")
                    .append("<td><strong>")
                    .append(dir)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Date & Time:</td>")
                    .append("<td><strong>")
                    .append(callDayTime)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Call Duration (Seconds):</td>")
                    .append("<td><strong>")
                    .append(callDuration)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<br/>");
        }
        sb.append("</table>");
 
        managedCursor.close();
 
        callLogsTextView.setText(Html.fromHtml(sb.toString()),TextView.BufferType.SPANNABLE);
    }

    @Override
    @ReactMethod
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
        // do nothing
    }

    /**Method to obtain name of the class */
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    /**Method that actually gets call logs directly**/
    @ReactMethod
    public void returnLogs(int tag, int ancestorTag, Promise promise) {
        try{

        StringBuffer sb = new StringBuffer();
        Uri contacts = CallLog.Calls.CONTENT_URI;
        Cursor managedCursor = (reactContext.getCurrentActivity()).getContentResolver().query(contacts, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        sb.append("Call Log Details:");  //tried to print out with react syntax contrary to code commented below
        sb.append("\n");
        sb.append("\n");

        String dura="";
        String num= "";
        String ty="";
        String dt="";

        while (managedCursor.moveToNext()) {
           dura = managedCursor.getString(duration);
           num = managedCursor.getString(number);
           ty = managedCursor.getString(type);
           dt = managedCursor.getString(date);
           sb.append("Duration: "+dura+" seconds\n");
           sb.append("Number: "+num+"\n");
           String dir=null;
            int dirCode=Integer.parseInt(ty);
            switch(dirCode)
            {
            case CallLog.Calls.OUTGOING_TYPE:
                dir="OUTGOING";
                break;
            case CallLog.Calls.INCOMING_TYPE:
                dir="INCOMING";
                break;
            case CallLog.Calls.MISSED_TYPE:
                dir="MISSED";
                break;

            }

           sb.append("Type: "+dir+"\n");

           //Date format
            Date dte= new Date(Long.valueOf(dt));
            sb.append("Date: "+dte.toString()+"\n");
           sb.append("\n");
           sb.append("\n");
        }
        sb.append("Done!");
        managedCursor.close();
        String clLog=sb.toString();
        promise.resolve(clLog);

        }

        catch (IllegalViewOperationException e) { promise.reject(E_LAYOUT_ERROR, e); }
    }

    /**Method that actually gets call logs directly**/
    @ReactMethod
    public void alert(int tag, int ancestorTag, Promise promise) {


        WritableArray call_list = new WritableNativeArray(); //initialise native array
        try{

        StringBuffer sb = new StringBuffer();
        Uri contacts = CallLog.Calls.CONTENT_URI;
        Cursor managedCursor = (reactContext.getCurrentActivity()).getContentResolver().query(contacts, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        //sb.append("Call Details :");

        sb.append("Call Log Details:");  //tried to print out with react syntax contrary to code commented below
        sb.append("\n");
        sb.append("\n");
        
        //initialise string details
        String dura="";
        String num= "";
        String ty="";
        String dt="";

        while (managedCursor.moveToNext()) {
           dura = managedCursor.getString(duration);
           num = managedCursor.getString(number);
           ty = managedCursor.getString(type);
           dt = managedCursor.getString(date);
           String dir=null;
            int dirCode=Integer.parseInt(ty);
            switch(dirCode)
            {
            case CallLog.Calls.OUTGOING_TYPE:
                dir="OUTGOING";
                break;
            case CallLog.Calls.INCOMING_TYPE:
                dir="INCOMING";
                break;
            case CallLog.Calls.MISSED_TYPE:
                dir="MISSED";
                break;

            }

            //Date format
            Date dte= new Date(Long.valueOf(dt)); 
            Calendar c= Calendar.getInstance();
            sb.append("Date: "+dte.toString()+"\n");

            try{
                
                WritableMap map = new WritableNativeMap();
                map.putString("duration",dura);
                map.putString("number",num);
                map.putString("type",dir);

                
                //CONVERT TO USABLE DATE FORMAT
                SimpleDateFormat format1 = new SimpleDateFormat(
                    "MM/dd/yyyy");                                  //"01/04/2014"  used to pass in to moment class in react
                SimpleDateFormat format2 = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");                      //"2016-01-04 10:34:23" used this earlier to pass into react date class
                    
                
                c.setTime(dte);
                //format1.format(dte);
                map.putString("date",dte.toString());
                map.putInt("year",c.get(Calendar.YEAR));
                map.putInt("month", c.get(Calendar.MONTH));
                map.putInt("day",c.get(Calendar.DAY_OF_MONTH));
                // map.putString("dte",dte.getTime()+"");
                // map.putString("input",format2.format(dte));

                call_list.pushMap(map);
            }

            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        sb.append("Done!");

        callList= call_list;


        managedCursor.close();
        String clLog=sb.toString();
        promise.resolve(callList);

        }

        catch (IllegalViewOperationException e) { promise.reject(E_LAYOUT_ERROR, e); }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
