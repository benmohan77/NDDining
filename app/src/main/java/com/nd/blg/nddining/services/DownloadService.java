package com.nd.blg.nddining.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.nd.blg.nddining.activites.MainActivity.convertStreamToString;

/**
 * Created by Ben on 8/15/2017.
 */

public class DownloadService extends IntentService {
    public DownloadService(){
        super("DownloadService");
    }
    public static final int UPDATE_COMPLETE = 8344;
    public static final int UPDATE_FAIL = 8345;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        ResultReceiver reciever = (ResultReceiver) intent.getParcelableExtra("reciever");
        try{
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();

            connection.connect();

            int fileLength = connection.getContentLength();

            InputStream input = new BufferedInputStream(connection.getInputStream());
            String ip = convertStreamToString(input);

            URL ipURL = new URL(ip + "/today_NDSU");
            URLConnection ipConnection = ipURL.openConnection();




            InputStream ipInput = new BufferedInputStream(ipConnection.getInputStream());
            OutputStream outputStream = openFileOutput("today.txt", Context.MODE_PRIVATE);

            byte data[] = new byte[1024];
            int count;
            while((count = ipInput.read(data)) != -1){
                data.toString();
                outputStream.write(data, 0, count);
            }


            URL allURL = new URL(ip + "/all_NDSU");
            URLConnection allConnection = allURL.openConnection();
            InputStream allInput = new BufferedInputStream(allConnection.getInputStream());
            OutputStream allOutput = openFileOutput("all.txt", Context.MODE_PRIVATE);

            byte allData[] = new byte[1024];
            int allCount;
            while((allCount = allInput.read(allData)) != -1){
                allData.toString();
                allOutput.write(allData, 0, allCount);
            }

            Bundle resultData = new Bundle();
            reciever.send(UPDATE_COMPLETE, resultData);



            outputStream.flush();
            outputStream.close();
            input.close();
            ipInput.close();

        } catch (MalformedURLException e) {
            reciever.send(UPDATE_FAIL, null);
        } catch (IOException e) {
            reciever.send(UPDATE_FAIL, null);
        }
    }





}
