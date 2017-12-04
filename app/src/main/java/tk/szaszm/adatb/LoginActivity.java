package tk.szaszm.adatb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends Activity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private CheckBox mRememberCredentials;
    private View mProgressView;
    private View mLoginFormView;
    private FecskeSession fecske;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fecske = FecskeSession.getInstance();
        if(fecske.getToken() != null) {
            switchToMainActivity();
        }

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRememberCredentials = (CheckBox) findViewById(R.id.remember_credentials);
        Toolbar toolbar = findViewById(R.id.loginToolbar);
        setActionBar(toolbar);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Pair<String, String> loginData = loadLoginData();
        mUsernameView.setText(loginData.first);
        mPasswordView.setText(loginData.second);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private void saveLoginData(String username, String password)
    {
        SharedPreferences sharedPreferences = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private Pair<String, String> loadLoginData()
    {
        SharedPreferences sharedPreferences = this.getPreferences(MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        return new Pair<>(username, password);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void switchToMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                fecske.tryAuth(mUsername, mPassword);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if(mRememberCredentials.isChecked()) {
                    saveLoginData(mUsername, mPassword);
                }
                //finish();
                switchToMainActivity();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

