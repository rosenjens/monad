package se.uu.csproject.monadclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import se.uu.csproject.monadclient.serverinteractions.ClientAuthentication;
import se.uu.csproject.monadclient.R;
import se.uu.csproject.monadclient.serverinteractions.UpdateProfileTask;


public class ProfileActivity extends MenuedActivity {

    Toolbar toolbar;
    Button submitButton;
    Button passwordButton;
    private EditText usernameField;
    private EditText emailField;
    private EditText phoneField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.actionToolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);


        //set the profile fields from the profile stored in ClientAuthentication
        usernameField = (EditText)findViewById(R.id.textView_profile_user);
        usernameField.setText(ClientAuthentication.getUsername());

        phoneField = (EditText)findViewById(R.id.textView_profile_phone);
        phoneField.setText(ClientAuthentication.getPhone());

        emailField = (EditText)findViewById(R.id.textView_profile_email);
        emailField.setText(ClientAuthentication.getEmail());

        submitButton = (Button) findViewById(R.id.button_updateprofile);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileInfo(v);
            }
        });

        passwordButton = (Button) findViewById(R.id.button_changepassword);
        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(v);
            }
        });
    }

    public void changePassword(View v){
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("RESET", false); //to determine how to display the ResetPasswordActivity
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_profile || super.onOptionsItemSelected(item);
    }

    public void editProfileInfo(View v){
        String userEntered = usernameField.getText().toString();
        String emailEntered = emailField.getText().toString();
        String phoneEntered = phoneField.getText().toString();

        UpdateProfileTask task = new UpdateProfileTask();

        String response = null;
        try {
            response = task.execute(ClientAuthentication.getClientId(), userEntered, emailEntered, phoneEntered).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(response.startsWith("Success (1)")){
            Toast.makeText(getApplicationContext(), getString(R.string.java_profile_updatesuccess), Toast.LENGTH_LONG).show();
            usernameField.setText(ClientAuthentication.getUsername());
            emailField.setText(ClientAuthentication.getEmail());
            phoneField.setText(ClientAuthentication.getPhone());
        }
        else{
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        }
    }
}