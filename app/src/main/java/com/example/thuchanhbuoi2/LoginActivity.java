package com.example.thuchanhbuoi2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Tài khoản cố định (hard-code)
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvError;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        tvError.setVisibility(View.GONE);

        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();

        // 1. Không cho đăng nhập khi bỏ trống
        if (username.isEmpty() || password.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập và mật khẩu");
            return;
        }

        // 2. So sánh với tài khoản cố định
        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Nút Back không quay lại màn hình đăng nhập
        } else {
            showError("Sai tên đăng nhập hoặc mật khẩu");
        }
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}
