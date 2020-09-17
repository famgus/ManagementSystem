package com.ec.managementsystem.moduleView.login;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.MainActivity;
import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.Vendedores;
import com.ec.managementsystem.clases.request.LoginRequest;
import com.ec.managementsystem.clases.responses.LoginResponse;
import com.ec.managementsystem.interfaces.IDelegateLoginTaskControl;
import com.ec.managementsystem.task.LoginTaskController;

public class LoginActivity extends BaseActivity implements IDelegateLoginTaskControl {

    private LoginViewModel loginViewModel;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    new Thread() {
                        public void run() {
                            try {
                                sleep(10000);
                                loadingProgressBar.setVisibility(View.GONE);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    updateUiWithUser(loginResult.getSuccess());
                    startActivity(LoginActivity.this, MainActivity.class, true, null);

                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                //loginViewModel.login(usernameEditText.getText().toString(),
                //      passwordEditText.getText().toString());
                LoginRequest request = new LoginRequest();
                request.setUserName(usernameEditText.getText().toString());
                request.setPassword(passwordEditText.getText().toString());
                request.setDate(System.currentTimeMillis());
                LoginTaskController loginTaskController = new LoginTaskController();
                loginTaskController.setListener(LoginActivity.this);
                loginTaskController.execute(request);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogin(LoginResponse response) {
        loadingProgressBar.setVisibility(View.GONE);
        if (response != null) {
            if (response.getVendedoresList() != null && response.getVendedoresList().size() >= 1) {
                Vendedores vendedores = response.getVendedoresList().get(0);
                startActivity(LoginActivity.this, MainActivity.class, true, null);
                setResult(Activity.RESULT_OK);
                //Complete and destroy login activity once successful
                finish();
            } else {
                showLoginFailed(response.getMessage());
            }
        } else {
            showLoginFailed(getString(R.string.text_error_authentication));
        }
    }
}
