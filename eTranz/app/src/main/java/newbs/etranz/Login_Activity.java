package newbs.etranz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void loginButtonPressed(View view){
        Intent intent = new Intent(this, HomeScreen_Activity.class);
        startActivity(intent);
    }

    public void registerButtonPressed(View view){
        startActivity(new Intent(this, registerActivity.class));
    }
}
