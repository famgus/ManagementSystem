package com.ec.managementsystem.moduleView.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.MainActivity;
import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.Vendedores;
import com.ec.managementsystem.clases.request.LoginRequest;
import com.ec.managementsystem.clases.responses.LoginResponse;
import com.ec.managementsystem.interfaces.IDelegateLoginTaskControl;
import com.ec.managementsystem.task.LoginTaskController;
import com.ec.managementsystem.util.MySingleton;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FinalLogginActivity extends BaseActivity implements IDelegateLoginTaskControl {

    private Button btnlogin;
    private ProgressBar progressBar;

    private String user1, user2, user3;
    private String pass1, pass2, pass3;
    private EditText etuser, etpass;
    User u1, u2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_loggin);
        getSupportActionBar().hide();

        btnlogin = (Button) findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_login);
        etuser = findViewById(R.id.etUser);
        etpass = findViewById(R.id.etPass);

        //Temporal......
        u1 = new User("juanp13", "123", 0);
        u2 = new User("test", "test", 0);

    }

    public void Login(View view) {
        progressBar.setVisibility(View.VISIBLE);
        LoginRequest request = new LoginRequest();
        request.setUserName(etuser.getText().toString());
        request.setPassword(etpass.getText().toString());

        Date startDate = new Date(System.currentTimeMillis());
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        startCalendar.add(GregorianCalendar.MONTH, -1);
        startCalendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
        startCalendar.set(GregorianCalendar.MINUTE, 0);
        startCalendar.set(GregorianCalendar.SECOND, 0);

        request.setDate(startCalendar.getTimeInMillis());
        LoginTaskController loginTaskController = new LoginTaskController();
        loginTaskController.setListener(FinalLogginActivity.this);
        loginTaskController.execute(request);

    }

    private void goToActivity() {

        new Thread() {
            public void run() {
                try {
                    synchronized (this) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        });
                        //progressBar.setVisibility(View.VISIBLE);
                        sleep(2000);
                        //progressbar.setVisibility(View.GONE);
                        startActivity(FinalLogginActivity.this, MainActivity.class, true, null);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onLogin(LoginResponse response) {
        progressBar.setVisibility(View.GONE);
        if (response != null) {
            if (response.getVendedoresList() != null && response.getVendedoresList().size() >= 1) {
                Vendedores vendedores = response.getVendedoresList().get(0);
                startActivity(FinalLogginActivity.this, MainActivity.class, true, null);
                setResult(Activity.RESULT_OK);
                User user = new User(etuser.getText().toString(), etpass.getText().toString(), vendedores.getTipousuario());
                MySingleton.SaveUser(user);
                finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Usuario y/o Contraseña incorrecta", Toast.LENGTH_LONG).show();
        }
    }
}
