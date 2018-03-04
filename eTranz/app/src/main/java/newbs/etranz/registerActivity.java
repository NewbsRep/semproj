package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        EditText passwordInput = (EditText) findViewById(R.id.etPassword);
//        EditText repeatPasswordInput = (EditText) findViewById(R.id.etPasswordRepeat);
//        InputFilter[] filterArray = new InputFilter[1];
//        filterArray[0] = new InputFilter.LengthFilter(5);
//        passwordInput.setFilters(filterArray);
//        repeatPasswordInput.setFilters(filterArray);

    }

    public void registerButtonPressed(View view){
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);
    }
}
