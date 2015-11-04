package erlangga.com.ribbit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class LoginActivity extends ActionBarActivity {

    @InjectView(R.id.usernameField) EditText mUsername;
    @InjectView(R.id.passwordField) EditText mPassword;
    @InjectView(R.id.loginProgressBar)ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        setProgressBar();
    }

    @OnClick(R.id.forgotPasswordText) void forgotPass() {
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(getString(R.string.forgot_password_dialog_box_title))
                .setView(input)
                .setPositiveButton(getString(R.string.reset_password_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String email = input.getText().toString();
                                resetPassword(email);
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void resetPassword(String email) {
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.successfully_reset_password),
                            Toast.LENGTH_LONG).show();
                } else {
                    showDialog(e.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.signUpText) void textListener(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.loginButton) void login() {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty()) {
            showDialog(getString(R.string.error_login_message));
        } else {

            //showing the progress bar
            setProgressBar();
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    //hide the progress bar
                    setProgressBar();
                    if (parseUser != null) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(getString(R.string.error_message_title))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
