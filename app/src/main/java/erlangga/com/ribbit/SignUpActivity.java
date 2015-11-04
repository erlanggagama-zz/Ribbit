package erlangga.com.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SignUpActivity extends ActionBarActivity {

    @InjectView(R.id.usernameField) EditText mUsername;
    @InjectView(R.id.passwordField) EditText mPassword;
    @InjectView(R.id.emailField) EditText mEmail;
    @InjectView(R.id.signUpProgressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);
        setProgressBar();
    }

    @OnClick(R.id.signUpButton) public void onClick() {

        //showing progress bar

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        username = username.trim();
        password = password.trim();
        email = email.trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {

            showDialog(getString(R.string.error_message));

        } else {

            //showing the progress bar
            setProgressBar();
            setSupportProgressBarIndeterminateVisibility(true);

            //Create a new user
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    //hide the progress bar
                    setProgressBar();
                    setSupportProgressBarIndeterminateVisibility(false);

                    if (e == null) {

                        //Go to main activity
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        showDialog(e.getMessage());
                    }
                }
            });
        }
    }

    private void setProgressBar() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(getString(R.string.error_message_title))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
