package com.example.thuchanhbuoi2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailActivity extends AppCompatActivity {

    private Contact contact;
    private int position;
    private CheckBox cbFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle("Chi tiết liên hệ");
            bar.setDisplayHomeAsUpEnabled(true);
        }

        Contact c = (Contact) getIntent().getSerializableExtra(Contact.EXTRA_CONTACT);
        position = getIntent().getIntExtra(Contact.EXTRA_POSITION, -1);
        if (c == null || position < 0) {
            finish();
            return;
        }
        contact = c;

        TextView tvAvatar = findViewById(R.id.tvAvatar);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvGroup = findViewById(R.id.tvGroup);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvNote = findViewById(R.id.tvNote);
        cbFavorite = findViewById(R.id.cbFavorite);
        Button btnCall = findViewById(R.id.btnCall);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        ContactAdapter.applyAvatar(tvAvatar, contact);
        tvName.setText(contact.getName());
        tvPhone.setText(contact.getFormattedPhone());
        tvGroup.setText(contact.getGroup());
        tvGender.setText(contact.getGender());
        String note = contact.getNote();
        tvNote.setText(note == null || note.isEmpty() ? "(Không có ghi chú)" : note);
        cbFavorite.setChecked(contact.isFavorite());

        // Đổi trạng thái yêu thích: trả kết quả ngay để dù thoát bằng cách nào
        // (nút Quay lại, mũi tên trên thanh tiêu đề, nút Back hệ thống) danh sách vẫn được cập nhật.
        cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contact.setFavorite(isChecked);
            Intent result = new Intent();
            result.putExtra(Contact.EXTRA_ACTION, Contact.ACTION_UPDATE);
            result.putExtra(Contact.EXTRA_POSITION, position);
            result.putExtra(Contact.EXTRA_CONTACT, contact);
            setResult(RESULT_OK, result);
        });

        // Intent ngầm định ACTION_DIAL
        btnCall.setOnClickListener(v -> {
            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhone()));
            try {
                startActivity(dial);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Thiết bị không hỗ trợ quay số", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Xóa liên hệ")
                .setMessage("Bạn có chắc muốn xóa \"" + contact.getName() + "\"?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, which) -> {
                    Intent result = new Intent();
                    result.putExtra(Contact.EXTRA_ACTION, Contact.ACTION_DELETE);
                    result.putExtra(Contact.EXTRA_POSITION, position);
                    setResult(RESULT_OK, result);
                    finish();
                })
                .show());
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
