package com.lvc.syndichand;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by administrator on 9/25/15.
 */
public class SyndicHandActivity extends AppCompatActivity {

    private ProgressDialog alertDialog = null;

    protected void prepareActionBarToBack(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_left);
        actionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean isObrigatoryFieldsOk(EditText... fields) {
        boolean result = true;

        for (EditText editText : fields) {
            String value = editText.getText().toString();

            if (value.length() == 0) {
                editText.setError(getString(R.string.this_field_is_obrigatory));
                result = false;
            }
        }

        return result;
    }

    protected void showProgressDialog() {
        if (alertDialog == null)
            alertDialog = new ProgressDialog(this);

        if(!alertDialog.isShowing()) {
            alertDialog.setMessage(getResources().getString(R.string.loader));
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        alertDialog.dismiss();
    }


    protected void showMessageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    protected void goToNextScreen(View animationTarget, Class<? extends Activity> target) {
       String transitionName = getString(R.string.transition_view);
        Intent intent = new Intent(this,target);

            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            animationTarget,
                            transitionName
                    );
            ActivityCompat.startActivity(this, intent, options.toBundle());

    }

    protected void goToNextScreen(Class<? extends Activity> target, int requestCode) {
        Intent intent = new Intent(this,target);
        startActivityForResult(intent,requestCode);
    }

    protected void goToNextScreen(Class<? extends Activity> target) {
        Intent intent = new Intent(this,target);
        startActivity(intent);
    }

    protected void goToNextScreen(Class<? extends Activity> target, Bundle bundle) {
        Intent intent = new Intent(this,target);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
