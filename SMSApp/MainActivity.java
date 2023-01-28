package com.example.textmessageproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView receivedPhoneNum, receivedMsgBody, sendPhoneNum, sendMsgBody;
    int currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receivedPhoneNum = findViewById(R.id.id_receivedTextPhoneNumber);
        receivedMsgBody = findViewById(R.id.id_receivedTextBody);
        sendPhoneNum = findViewById(R.id.id_sendTextPhoneNumTextView);
        sendMsgBody = findViewById(R.id.id_sendTextBodyTextView);

        currentState = 1;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 1);
        }
        else
        {
            Log.d("TAG_1", "Permissions Granted");
        }

        ReceiveText receiveText = new ReceiveText();
        registerReceiver(receiveText, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public class ReceiveText extends BroadcastReceiver
    {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle receivedData = intent.getExtras();
            Object[] pdus = (Object[]) receivedData.get("pdus");
            SmsMessage[] storeSmsMessages = new SmsMessage[pdus.length];
            String displayMessage = "";
            String displayPhoneNum = "";
            for(int i = 0; i < pdus.length; i++)
            {
                byte[] element = (byte[]) pdus[i];
                storeSmsMessages[i] = SmsMessage.createFromPdu(element, receivedData.getString("format"));
                Log.d("TAG_1", storeSmsMessages[i].getDisplayMessageBody());
                Log.d("TAG_1", storeSmsMessages[i].getDisplayOriginatingAddress());
                displayMessage = storeSmsMessages[i].getDisplayMessageBody();
                displayPhoneNum = storeSmsMessages[i].getDisplayOriginatingAddress();
                if(i > 0)
                {
                    displayMessage += " " + storeSmsMessages[i].getDisplayMessageBody();
                }
            }
            receivedPhoneNum.setText("From:\n\n" + displayPhoneNum);
            receivedMsgBody.setText("Message:\n\n" + displayMessage);
            Log.d("TAG_1", displayMessage);
            Log.d("TAG_1", "" + pdus.length);
            Toast messageReceivedToast = Toast.makeText(getApplicationContext(), "Message Received!", Toast.LENGTH_SHORT);
            messageReceivedToast.show();
            Log.d("TAG_1", "OnReceive Triggered");
            sendText(displayPhoneNum, displayMessage);
        }
    }

    public void sendText(String phNum, String message)
    {
        String messageToSend = "";
        String[] caseOneResponses = {"Hi", "Hello", "What's up", "Hey", "Yo"};
        String[] caseTwoResponses = {"Listen, I don't think this is going to work out.", "I don't think we're in a healthy place right now.", "Yeah, so, I think it might be time to take our own paths.", "I think we need to take some time apart."};
        String[] caseThreeResponses = {"I'm not happy in this relationship anymore", "I feel you are ruining this relationship", "Your antics won't cut it anymore, I'm done", "You've been manipulating me all this time... I'm done"};
        String[] caseFourResponses = {"It's over, goodbye", "Please don't contact me anymore, goodbye", "If you contact this number again, I will block you", "You can't fix this now, I'm leaving you", "Your fake sympathy won't change anything, it's over"};
        String[] nonsenseResponses = {"What?", "?", "I don't understand", "Idk what that means", "Come again?"};

        Log.d("TAG_1", "Current State: " + currentState);

        if(currentState == 1)
        {
            if(message.equalsIgnoreCase("hi") || message.equalsIgnoreCase("hello") || message.equalsIgnoreCase("what's up") || message.equalsIgnoreCase("yo") || message.equalsIgnoreCase("hey"))
            {
                int chooseRandCaseOneNormal = (int)(Math.random()*caseOneResponses.length);
                messageToSend = caseOneResponses[chooseRandCaseOneNormal];
                currentState++;
                Log.d("TAG_1", "Transferring to state 2...");
            }
            else
            {
                int chooseRandCaseOneNonsense = (int)(Math.random()*nonsenseResponses.length);
                messageToSend = nonsenseResponses[chooseRandCaseOneNonsense];
            }
        }else if(currentState == 2)
        {
            if(message.equalsIgnoreCase("How's everything going?") || message.equalsIgnoreCase("You wanted to talk?") || message.equalsIgnoreCase("What's going on?") || message.equalsIgnoreCase("You said you wanted to talk?") || message.equalsIgnoreCase("You alright?"))
            {
                int chooseRandCaseTwoNormal = (int)(Math.random()*caseTwoResponses.length);
                messageToSend = caseTwoResponses[chooseRandCaseTwoNormal];
                currentState++;
                Log.d("TAG_1", "Transferring to state 3...");
            }
            else
            {
                int chooseRandCaseTwoNonsense = (int)(Math.random()*nonsenseResponses.length);
                messageToSend = nonsenseResponses[chooseRandCaseTwoNonsense];
            }
        }else if(currentState == 3)
        {
            if(message.equalsIgnoreCase("What do you mean?") || message.equalsIgnoreCase("Why?") || message.equalsIgnoreCase("I thought everything was fine!") || message.equalsIgnoreCase("Are you sure about this?") || message.equalsIgnoreCase("It's been so long, why now?"))
            {
                int chooseRandCaseThreeNormal = (int)(Math.random()*caseThreeResponses.length);
                messageToSend = caseThreeResponses[chooseRandCaseThreeNormal];
                currentState++;
                Log.d("TAG_1", "Transferring to state 4...");
            }
            else
            {
                int chooseRandCaseThreeNonsense = (int)(Math.random()*nonsenseResponses.length);
                messageToSend = nonsenseResponses[chooseRandCaseThreeNonsense];
            }
        }else if(currentState == 4)
        {
            if(message.equalsIgnoreCase("Give me a chance to make it up!") || message.equalsIgnoreCase("Come on, we can fix this!") || message.equalsIgnoreCase("Please don't leave me!") || message.equalsIgnoreCase("You can't do this!") || message.equalsIgnoreCase("Don't worry, it'll be alright!"))
            {
                int chooseRandCaseFourNormal = (int)(Math.random()*caseFourResponses.length);
                messageToSend = caseFourResponses[chooseRandCaseFourNormal];
                currentState++;
                Log.d("TAG_1", "All states completed");
            }
            else
            {
                int chooseRandCaseFourNonsense = (int)(Math.random()*nonsenseResponses.length);
                messageToSend = nonsenseResponses[chooseRandCaseFourNonsense];
            }
        }else
        {
            messageToSend = "The bot has finished its messages";
        }

        sendMsgBody.setText(messageToSend);
        String finalMessageToSend = messageToSend;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SmsManager sendText = SmsManager.getDefault();
                sendPhoneNum.setText("To:\n\n" + phNum);
                sendText.sendTextMessage(phNum, null, finalMessageToSend, null, null);
            }
        }, 1500);
    }
}