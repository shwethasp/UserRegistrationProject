package shwethasp.com.api_call.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import shwethasp.com.api_call.R;
import shwethasp.com.api_call.httprequest.HTTP_POST;
import shwethasp.com.api_call.model.ModelClass;
import shwethasp.com.api_call.util.Connectivity;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mSignupUserName, mSignupEmailId, mSignupUserPass;
    private Button mSignupButton;
    public static RegistrationActivity activity;
    String Username, Password, EmailId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.activity = activity;

        //TODO:initialise the all ui
        initialiseUi();
        mSignupButton.setOnClickListener(this);
    }

    private void initialiseUi() {
        mSignupUserName = (TextView) findViewById(R.id.signup_user_name);
        mSignupEmailId = (TextView) findViewById(R.id.signup_user_email);
        mSignupUserPass = (TextView) findViewById(R.id.signup_user_password);
        mSignupButton = (Button) findViewById(R.id.signup_button);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_button:
                if (!mSignupUserName.getText().toString().trim().contentEquals("")) {
                    if (!mSignupEmailId.getText().toString().trim().contentEquals("")) {
                        if (!mSignupUserPass.getText().toString().trim().contentEquals("")) {
                             Username = mSignupUserName.getText().toString().trim();
                             EmailId = mSignupEmailId.getText().toString().trim();
                             Password = mSignupUserPass.getText().toString().trim();

                            if (validate()) {
                                signUpUserMethod(Username, EmailId, Password);
                            }else {

                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Password should Not be blank", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Email-id Should not be blank", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Username Should not be empty", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    //call api to registration
    private void signUpUserMethod(String username, String emailid, String password) {

        /*
        * Check Internet connection
        * */

        if (Connectivity.isConnected(getApplicationContext())) {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", username);
                jsonObject.put("email", emailid);
                jsonObject.put("password", password);

                HTTP_POST http_post = new HTTP_POST() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();


                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);

                        if (s != null && !s.isEmpty()) {
                            try {
                                JSONObject resultJsonObject = new JSONObject(s);

                                if (resultJsonObject.getBoolean("result")) {

                                    Toast.makeText(getApplicationContext(), "SinUp sucess", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(getApplicationContext(), "Username already registered with this email id", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Log.i("error message", e.getMessage());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Some error occur", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                http_post.execute(ModelClass.SignUpAPI, jsonObject.toString());
            } catch (Exception e) {
                Log.e("Exception",e.getMessage());

            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection. Please check your internet", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean validate() {

       /* if (!Validation.isValidPhoneNumer(Mobilenumber)) {
            if (mEditMobile.getText().toString().length() <= 0) {
                Toast.makeText(getApplicationContext(), "Please enter your Mobile Number", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please provide 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
            }
            return false;
        }*/

        //Validate emailid
        if (!Validation.isValidEmail(EmailId)) {
            if (EmailId.length() <= 0) {
                Toast.makeText(getApplicationContext(), "Please enter your Email-ID", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please provide valid Email-ID", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
        if (!new Validation(getApplicationContext()).isValidPassword(Password)) {

            // Toast.makeText(getActivity(),"Password should be minimum 4 characters",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
