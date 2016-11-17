package shwethasp.com.api_call.activity;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by anshikas on 29-06-2016.
 */
public class Validation {

    private static int PASSWORD_LENGTH = 4;
    private static int USERNAME_LENGTH = 4;

    private static Context con;

    public Validation(Context con) {
        this.con = con;
    }

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
   /* public static boolean isValidPassword(Context context,String pass) {
        if (pass != null) {
            if (pass.length() == 0) {
                Toast.makeText(context, "Enter the Password",Toast.LENGTH_SHORT).show();
            } else if (pass.length() >= PASSWORD_LENGTH) {
                return true;
            } else {
                Toast.makeText(context,"Password should be minimum of " + PASSWORD_LENGTH+ " Characters.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "Enter the Password",Toast.LENGTH_SHORT).show();
        }

        return false;
    }*/

    //TODO:Anshika valid password <code></code>

    public static boolean isValidPassword(String pass) {
        if (pass != null) {
            if (pass.length() == 0) {
                Toast.makeText(con, "Enter the Password",Toast.LENGTH_SHORT).show();
            } else if (pass.length() >= PASSWORD_LENGTH) {
                return true;
            } else {
                Toast.makeText(con,"Password should be minimum of " + PASSWORD_LENGTH+ " Characters.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(con, "Enter the Password",Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    // validating phone number
    public static boolean isValidPhoneNumer(String phNum) {

        return (phNum != null & phNum.length() == 10) ? true : false;
    }

    // validating username
    public static boolean isValidUserName(String userName) {
        if (userName != null && userName.length() >= USERNAME_LENGTH) {
            return true;
        }
        return false;
    }

}
