package com.lvc.syndichand;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoggingIn extends SyndicHandActivity {


    private static final String NAME_KEY = "NAME_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";

    private static final String CONFIRMED_EMAIL_KEY = "emailVerified";

    private EditText editTextName;
    private EditText editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loggin_in);

        editTextName = (EditText) findViewById(R.id.edit_text_login);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();
                loggingIn(name, password);
            }
        });

        findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString(NAME_KEY, name);
                bundle.putString(PASSWORD_KEY, password);
                goToNextScreen(SignUp.class, bundle);
            }
        });


        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser != null) {
            if(isEmailVerified(parseUser)) {
                goToNextScreen(OwnerList.class);
                finish();
            } else {
                showMessageToast(getString(R.string.confirm_your_email_to_login));
            }
        }
    }

    private void loggingIn(String userName, String password) {
        showProgressDialog();
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                dismissProgressDialog();
                if (e == null) {
                    treatAutenticationResponse(user);
                } else {
                    e.printStackTrace();
                    showMessageToast(getString(R.string.fail_to_login_check_your_connection_and_try_again));
                }

            }
        });
    }

    private void treatAutenticationResponse(ParseUser user) {
        if (user != null) {
            if (isEmailVerified(user)) {
                goToNextScreen(OwnerList.class);
            } else {
                showMessageToast(getString(R.string.valid_your_email_and_try_again));
                        /*
                        Isso aqui tem que ser substituído, devemos mostrar uma tela de instruções informando ao usuário que enviamos um e-mail para ele e é necessário
                        que ele o confirme
                         */
            }
        } else {
            showMessageToast(getString(R.string.user_not_found));
        }
    }

    private boolean isEmailVerified(ParseUser user) {
        boolean emailWasVerified = user.getBoolean(CONFIRMED_EMAIL_KEY);
        return emailWasVerified;
    }
}
