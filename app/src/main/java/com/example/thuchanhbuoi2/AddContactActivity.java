package com.example.thuchanhbuoi2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtPhone;
    private Spinner spGroup;
    private RadioGroup rgGender;
    private CheckBox cbFavorite;
    private EditText edtNote;
    private Button btnSave;

    private ArrayList<String> existingPhones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle("Thêm liên hệ");
            bar.setDisplayHomeAsUpEnabled(true);
        }

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        spGroup = findViewById(R.id.spGroup);
        rgGender = findViewById(R.id.rgGender);
        cbFavorite = findViewById(R.id.cbFavorite);
        edtNote = findViewById(R.id.edtNote);
        btnSave = findViewById(R.id.btnSave);

        ArrayList<String> phones = getIntent().getStringArrayListExtra(Contact.EXTRA_PHONES);
        if (phones != null) existingPhones = phones;

        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(
                this, R.array.groups, android.R.layout.simple_spinner_item);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGroup.setAdapter(groupAdapter);
        spGroup.setSelection(1); // mặc định "Bạn bè" như bản phác thảo

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (name.isEmpty()) {
            edtName.setError("Họ và tên không được để trống");
            edtName.requestFocus();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            edtPhone.setError("Số điện thoại gồm 10 chữ số và bắt đầu bằng 0");
            edtPhone.requestFocus();
            return;
        }

        if (existingPhones.contains(phone)) {
            edtPhone.setError("Số điện thoại đã tồn tại trong danh bạ");
            edtPhone.requestFocus();
            return;
        }

        String group = spGroup.getSelectedItem().toString();

        String gender;
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbFemale) {
            gender = "Nữ";
        } else if (checkedId == R.id.rbOther) {
            gender = "Khác";
        } else {
            gender = "Nam";
        }

        String note = edtNote.getText().toString().trim();
        Contact contact = new Contact(name, phone, group, gender, cbFavorite.isChecked(), note);

        Intent result = new Intent();
        result.putExtra(Contact.EXTRA_CONTACT, contact);
        setResult(RESULT_OK, result);
        Toast.makeText(this, "Đã lưu liên hệ", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
