package shwethasp.com.api_call.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import shwethasp.com.api_call.R;
import shwethasp.com.api_call.httprequest.HTTP_POST;
import shwethasp.com.api_call.model.ModelClass;
import shwethasp.com.api_call.util.Connectivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mLoginEmailID, mLoginPassword, mDialogEditforgot_password_email;
    private TextView mSignUpText, mForgotPassword;
    private Button mLoginButon, mButtonDialogCancel, mButtonDialogSend;
    ProgressBar progressBar;
    Dialog mForgotPasswordDialog;
    String EmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //TODO:initialise the all ui
        initialiseUi();
        mLoginButon.setOnClickListener(this);
        mSignUpText.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
    }

    private void initialiseUi() {
        mLoginEmailID = (EditText) findViewById(R.id.login_user_email);
        mLoginPassword = (EditText) findViewById(R.id.login_user_password);
        mLoginButon = (Button) findViewById(R.id.login_butt);
        mSignUpText = (TextView) findViewById(R.id.login_signup_text);
        progressBar = (ProgressBar) findViewById(R.id.progresser_bar);
        mForgotPassword = (TextView) findViewById(R.id.login_forgot_text);
        forgotPwdDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_butt:
                //Check the username and password field is null or not
                if (!mLoginEmailID.getText().toString().trim().contentEquals("")) {
                    if (!mLoginPassword.getText().toString().trim().contentEquals("")) {
                        String EmailId = mLoginEmailID.getText().toString().trim();
                        String Password = mLoginPassword.getText().toString().trim();

                        //Call the login api
                        loginApiCall(EmailId, Password);


                    } else {
                        Toast.makeText(getApplicationContext(), "Email-id Should not be blank", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Username Should not be empty", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.login_signup_text:
                Intent signupActivity = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(signupActivity);
                finish();
                break;

            case R.id.login_forgot_text:
                //Show the forgot password dialog
                mForgotPasswordDialog.show();
                break;

            case R.id.dialog_cancel:
                mDialogEditforgot_password_email.setText("");
                if (mForgotPasswordDialog.isShowing())
                    mForgotPasswordDialog.dismiss();
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;

            case R.id.dialog_send:

                EmailID = mDialogEditforgot_password_email.getText().toString();
                if (mDialogEditforgot_password_email.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your Mobile Number", Toast.LENGTH_SHORT).show();
                }
                if (validate()) {
                    //Call the api here

                }


                break;

        }

    }

    /**
     * Calling login API
     * @param EmailId
     * @param Password
     */
    private void loginApiCall(String EmailId, String Password) {

        //Check the internet connectivity
        if (Connectivity.isConnected(getApplicationContext())) {
            try {
                JSONObject loginJson = new JSONObject();
                loginJson.put("email", EmailId);
                loginJson.put("password", Password);
                HTTP_POST http_post = new HTTP_POST() {


                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressBar.setVisibility(View.VISIBLE);


                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);

                        progressBar.setVisibility(View.GONE);
                        if (s != null && !s.isEmpty()) {
                            if (!s.contains("Unauthorized")) {
                                try {
                                    JSONObject resultJsonObject = new JSONObject(s);
                                    if (!resultJsonObject.get("access_token").toString().contentEquals("")) {
                                        String AccessToken = resultJsonObject.get("access_token").toString();
                                        String token = AccessToken.replaceAll("\"", "");
                                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (Exception e) {
                                    Log.i("error message", e.getMessage());
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Unauthorized user", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Some error occur, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                http_post.execute(ModelClass.LoginAPT, loginJson.toString());

            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection. Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }


    //Call the forgot password
    private void forgotPwdDialog() {
        mForgotPasswordDialog = new Dialog(this);
        mForgotPasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mForgotPasswordDialog.setContentView(R.layout.dialog_forgot_password);
        mForgotPasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mForgotPasswordDialog.setCancelable(false);

        mButtonDialogCancel = (Button) mForgotPasswordDialog.findViewById(R.id.dialog_cancel);
        mButtonDialogSend = (Button) mForgotPasswordDialog.findViewById(R.id.dialog_send);
        mDialogEditforgot_password_email = (EditText) mForgotPasswordDialog.findViewById(R.id.forgot_password_mobile);
        mButtonDialogCancel.setOnClickListener(this);
        mButtonDialogSend.setOnClickListener(this);

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
        if (!Validation.isValidEmail(EmailID)) {
            if (EmailID.length() <= 0) {
                Toast.makeText(getApplicationContext(), "Please enter your Email-ID", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please provide valid Email-ID", Toast.LENGTH_SHORT).show();
            }

            return false;
        }

        return true;
    }

    //To check the text
   /* private boolean textValidation() {

        if (!mLoginEmailID.getText().toString().trim().contentEquals("")) {
            if (!mLoginPassword.getText().toString().trim().contentEquals("")) {
                String EmailId = mLoginEmailID.getText().toString().trim();
                String Password = mLoginPassword.getText().toString().trim();


                return true;

            } else {
                Toast.makeText(getApplicationContext(), "Email-id Should not be blank", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Username Should not be empty", Toast.LENGTH_SHORT).show();

        }
        return true;

    }
*/
}