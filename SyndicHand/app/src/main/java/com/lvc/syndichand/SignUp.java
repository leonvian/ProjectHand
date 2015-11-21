package com.lvc.syndichand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lvc.syndichand.model.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends SyndicHandActivity {

    private EditText editTextUserName;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        editTextUserName = (EditText) findViewById(R.id.edit_text_user_name);
        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isObrigatoryFieldsOk(editTextEmail, editTextName, editTextUserName, editTextPassword)) {
                    String userName = editTextUserName.getText().toString();
                    String name = editTextName.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();

                    //createUserSyndic(String name, String userName, String password, String email, String condominium, String unityId, boolean unityAdmin) {
                    ParseUser user = User.createUserSyndic(name, userName, password, email, "", "", true);
                    signUp(user);
                }
            }
        });

        goToOwnersListIfHasSyndicEntriedAlready();
    }

    private void goToOwnersListIfHasSyndicEntriedAlready() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null) {
            Intent intent = new Intent(this, OwnerList.class);
            startActivity(intent);
        }
    }

    private void signUp(ParseUser user) {
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    goToNextScreen(LoggingIn.class);
                } else {
                    e.printStackTrace();
                    showMessageToast(getString(R.string.fail_to_entry_user));
                }
            }
        });
    }


}
