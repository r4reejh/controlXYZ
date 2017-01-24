package com.soft.tm.controlxyz;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextView t1, txtSpeechInput;
    EditText ed1;
    Button b1, b2;
    int count = 1, count1 = 1;
    Typeface face;
    int flag = 0, flag1 = 0;
    private SensorManager sensorManager;
    private Sensor acc;
    float vibrateThreshold;
    ImageButton btnSpeak;
    Vibrator v;
    long store;
    long on = 0, off = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        t1 = (TextView) findViewById(R.id.textview1);
        //t2=(TextView)findViewById(R.id.textView2);
        //b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        ed1 = (EditText) findViewById(R.id.editText1);

        face = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        t1.setTypeface(face);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener((SensorEventListener) this, acc, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = acc.getMaximumRange() / 2;
        } else {
            t1.setText("Acc not found");
        }
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void prompt(View v){
        promptSpeechInput();
    }
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, acc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.values[2] < 4 && event.values[1] < 7) {
            if (flag1 == 0 && b2.getText().toString() == "ON") {
                v.vibrate(20);
                flag1 = 1;
                t1.setText("ON");
                t1.setBackgroundColor(Color.parseColor("#2AD60D"));
                sendTask sendTask1 = new sendTask();
                sendTask1.execute("http://" + ed1.getText().toString() + ":3000/1");
            } else if (flag1 == 1 && b2.getText().toString() == "ON") {
                v.vibrate(20);
                flag1 = 0;
                t1.setText("OFF");
                t1.setBackgroundColor(Color.parseColor("#FFC00B"));
                sendTask sendTask1 = new sendTask();
                sendTask1.execute("http://" + ed1.getText().toString() + ":3000/2");
            }
        }
        String x = String.valueOf(event.values[0]);
        String y = String.valueOf(event.values[1]);
        String z = String.valueOf(event.values[2]);
        //if(flag==1)
        //t2.setText(x+"\n"+y+"\n"+z);
    }

    /*public void change(View view){
        if(count1%2!=0) {
            b1.setText("STOP");
            flag=1;
        }
        else {
            b1.setText("START");
            flag=0;
        }
        count1++;
    }*/

    public void change1(View view) {
        if (count % 2 != 0) {
            b2.setText("ON");
        } else {
            b2.setText("OFF");
        }
        count++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.soft.tm.controlxyz/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.soft.tm.controlxyz/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class sendTask extends AsyncTask<String, String, String> {
        private final String USER_AGENT = "Mozilla/5.0";

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String url = stringUrl;

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                //add request header
                con.setRequestProperty("User-Agent", USER_AGENT);
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //t1.setText(result);

        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String xinput=result.get(0).toString();
                    sendTask sendTask1=new sendTask();
                    if(xinput.matches("(?i).*on.*")){
                        sendTask1.execute("http://" + ed1.getText().toString() + ":3000/1");
                        t1.setText("ON");
                        t1.setBackgroundColor(Color.parseColor("#2AD60D"));
                    }
                    else
                    if(xinput.matches("(?i).*off.*")){
                        sendTask1.execute("http://" + ed1.getText().toString() + ":3000/2");
                        t1.setText("OFF");
                        t1.setBackgroundColor(Color.parseColor("#FFC00B"));
                    }
                }
                break;
            }

        }
    }
}
